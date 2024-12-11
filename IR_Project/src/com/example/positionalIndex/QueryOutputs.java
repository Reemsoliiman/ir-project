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

         for(String term : query){
             int count = 0;

             for(int i = 0 ; i < query.size() ; i++){
                 if(query.get(i).equals(term)){
                     count++;
                 }
             }

             queryTF.put(term , count);
         }
         return queryTF;
    }
    public static Map<String, Number> retrieveQueryIDF(List<String> query, Map<String, Number> idfMap) {
        Map<String, Number> queryIDF = new TreeMap<>();

        for (String term : query) {
            if (idfMap.containsKey(term)) {
                queryIDF.put(term, idfMap.get(term));
            } else {
                queryIDF.put(term, 0);
            }
        }
        return queryIDF;
    }



}
