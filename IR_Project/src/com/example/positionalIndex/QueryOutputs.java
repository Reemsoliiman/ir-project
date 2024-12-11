package com.example.positionalIndex;

import java.util.*;

public class QueryOutputs {

    public static List<String> removeKeyWords (List<String> query){
        List<String> queryResult = new ArrayList<>();
        for(String term : query){
            if(!term.equals("AND") && !term.equals("OR") && !term.equals("AND NOT") && !term.equals("OR NOT")){
                if(term.trim().contains(" ")){
                    queryResult.addAll(List.of(term.split(" ")));
                }else{
                    queryResult.add(term);
                }
            }
        }
        return queryResult;
    }

    public static Map<String, Integer> computeQueryTF (List<String> query){
        Map<String, Integer> queryTF = new TreeMap<>();
        List<String> resultQuery = removeKeyWords(query);

         for(String term : resultQuery){
             int count = 0;

             for(int i = 0 ; i < resultQuery.size() ; i++){
                 if(resultQuery.get(i).equals(term)){
                     count++;
                 }
             }

             queryTF.put(term , count);
         }
         return queryTF;
    }

    public static Map<String, Number> computeQueryWeightTF (Map<String, Integer> queryTF){
        Map<String, Number> queryWeightTF = new TreeMap<>();

        for(String term : queryTF.keySet()){
            double weightTFValue = 1 + Math.log10(queryTF.get(term));
            double roundedValue = Math.round(weightTFValue * 100000.0) / 100000.0;

            if (roundedValue == Math.floor(roundedValue)) {
                queryWeightTF.put(term, (int) roundedValue);
            } else {
                queryWeightTF.put(term, roundedValue);
            }
        }
        return queryWeightTF;
    }
    public static Map<String, Number> retrieveQueryIDF(List<String> query, Map<String, Number> idfMap) {
        Map<String, Number> queryIDF = new TreeMap<>();
        List<String> resultQuery = removeKeyWords(query);

        for (String term : resultQuery) {
            if (idfMap.containsKey(term)) {
                queryIDF.put(term, idfMap.get(term));
            } else {
                queryIDF.put(term, 0);
            }
        }
        return queryIDF;
    }

    public static Map<String, Number> computeQueryTF_IDF(Map<String, Number> queryWeightTF, Map<String, Number> queryIDF) {
        Map<String, Number> queryTF_IDF = new TreeMap<>();

        for (String term : queryWeightTF.keySet()) {
            double tf = queryWeightTF.get(term).doubleValue();
            double idf = queryIDF.getOrDefault(term, 0).doubleValue();
            double tf_idf = tf * idf;
            double roundedValue = Math.round(tf_idf * 100000.0) / 100000.0;

            if (roundedValue == Math.floor(roundedValue)) {
                queryTF_IDF.put(term, (int) roundedValue);
            } else {
                queryTF_IDF.put(term, roundedValue);
            }
        }
        return queryTF_IDF;
    }
    public static double computeQueryLength(Map<String, Number> queryTF_IDF) {
        double sum = 0.0;
        for (Map.Entry<String, Number> entry : queryTF_IDF.entrySet()) {
            double weight = entry.getValue().doubleValue();
            sum += weight * weight;
        }
        double length = Math.sqrt(sum);
        return Math.round(length * 100000.0) / 100000.0;
    }
    public static Map<String, Number> computeNormalizedQueryTF_IDF(Map<String, Number> queryTF_IDF) {
        Map<String, Number> normalizedQueryTF_IDF = new TreeMap<>();
        double queryLength = computeQueryLength(queryTF_IDF);

        for (String term : queryTF_IDF.keySet()) {
            double tf_idf = queryTF_IDF.get(term).doubleValue();
            double normalizedValue = queryLength > 0 ? tf_idf / queryLength : 0;
            normalizedValue = Math.round(normalizedValue * 100000.0) / 100000.0;
            normalizedQueryTF_IDF.put(term, normalizedValue);
        }

        return normalizedQueryTF_IDF;
    }

}
