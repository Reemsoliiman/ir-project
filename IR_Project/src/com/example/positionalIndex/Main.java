package com.example.positionalIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException {

        String filePath = "mapReduceOutput.txt";
        Map<String, Map<Integer, List<Integer>>> outPut = positionalIndexParser.parseFile(filePath);
        printTable("Positional Index", outPut);

        int totalDocuments = 10;

        // Calculate Term Frequency (TF)
        Map<String, Map<Integer, Integer>> TF = TermFrequency.calculateTF(outPut, totalDocuments);
        printTable("Term Frequency (TF)", TF);

        // Calculate Weight TF
        Map<String, Map<Integer, Number>> weightTF = TermFrequency.calculateWeightTF(TF, totalDocuments);
        printTable("Weighted Term Frequency (TF Weight)", weightTF);

        // Calculate Document Frequency (DF)
        Map<String, Integer> DF = documentFrequency.calculateDF(outPut);
        printTable("Document Frequency (DF)", DF);

        // Calculate Inverse Document Frequency (IDF)
        Map<String, Number> IDF = documentFrequency.calculateIDF(DF, totalDocuments);
        printTable("Inverse Document Frequency (IDF)", IDF);

        // Calculate TF-IDF
        Map<String, Map<Integer, Number>> TFIDF = TF_IDF.calcTF_IDF(weightTF, IDF);
        printTable("TF-IDF", TFIDF);

        // Calculate Document Lengths
        Map<Integer, Number> docLengths = TF_IDF.calculateDocLengths(TFIDF);
        printTable("Document Lengths", docLengths);

        // Normalize TF-IDF
        Map<String, Map<Integer, Number>> normalizedTFIDF = TF_IDF.normalizeTFIDF(TFIDF, docLengths);
        printTable("Normalized TF-IDF", normalizedTFIDF);

//        List<String> query1 = new ArrayList<>();
//        query1.add("to tread");
//        List<String> query2 = new ArrayList<>();
//        query2.add("fear");
//
//        Map<String, List<Integer>> queryPositionalIndex1 = QueryProcessor.getQueryPositionalIndex(outPut , query1);
//        System.out.println("queryPositionalIndex1 =>" + queryPositionalIndex1);
//
//        Map<String, List<Integer>> queryPositionalIndex2 = QueryProcessor.getQueryPositionalIndex(outPut , query2);
//        System.out.println("queryPositionalIndex2 =>" + queryPositionalIndex2);
//
//        Map<String, List<Integer>> queryPositionalIndex = new TreeMap<>();
//        queryPositionalIndex.putAll(queryPositionalIndex1);
//        queryPositionalIndex.putAll(queryPositionalIndex2);
//
//        List<String> query = new ArrayList<>();
//        query.addAll(query1);
//        query.add("AND NOT");
//        query.addAll(query2);
//
//        List<Integer> result = QueryProcessor.logicalOperatorResult(queryPositionalIndex , query);
//        System.out.println("logicalOperatorResult ==> " + result);
    }


    // Enhanced table printer
    private static <K, V> void printTable(String title, Map<K, V> data) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(" " + title + " ");
        System.out.println("═".repeat(50));
        System.out.printf("%-20s | %-50s\n", "Key", "Value");
        System.out.println("─".repeat(80));

        for (Map.Entry<K, V> entry : data.entrySet()) {
            System.out.printf("%-20s | %-50s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("═".repeat(80));
    }
}
