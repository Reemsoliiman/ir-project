package com.example.positionalIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

//---------------------------------------------testing--------------------------------------------------------------------//
        String queryInput = "fools fear in";

        List<String> parsedQuery = QueryProcessor.queryParser(queryInput);
        System.out.println("Parsed Query: " + parsedQuery);

        List<String> resultQuery = QueryOutputs.removeKeyWords(parsedQuery);
        System.out.println("Query: " + resultQuery);

        Map<String, Integer> queryTF = QueryOutputs.computeQueryTF(resultQuery);
        System.out.println("Query TF: " + queryTF);

        Map<String, Number> queryWeightTF = QueryOutputs.computeQueryWeightTF(queryTF);
        System.out.println("Query W TF: " + queryWeightTF);

        Map<String, Number> queryIDF = QueryOutputs.retrieveQueryIDF(resultQuery, IDF);
        System.out.println("Query IDF: " + queryIDF);

        Map<String, Number> queryTF_IDF = QueryOutputs.computeQueryTF_IDF(queryWeightTF, queryIDF);
        System.out.println("Query TF * IDF: " + queryTF_IDF);

        double queryLength = QueryOutputs.computeQueryLength(queryTF_IDF);
        System.out.println("Query Length: " + queryLength);


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
