package com.example.positionalIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

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
//        String queryInput = "fools fear in";
//
//        List<String> parsedQuery = QueryProcessor.queryParser(queryInput);
//        System.out.println("Parsed Query: " + parsedQuery);
//
//        List<String> resultQuery = QueryOutputs.removeKeyWords(parsedQuery);
//        System.out.println("Query: " + resultQuery);
//
//        Map<String, Integer> queryTF = QueryOutputs.computeQueryTF(resultQuery);
//        System.out.println("QueryTF: " + queryTF);
//
//        Map<String, Number> queryWeightTF = QueryOutputs.computeQueryWeightTF(queryTF);
//        System.out.println("QueryTF: " + queryWeightTF);
//
//        Map<String, Number> queryIDF = QueryOutputs.retrieveQueryIDF(resultQuery, IDF);
//        System.out.println("Query IDF: " + queryIDF);
//        ==========================================================================================

        int choice = 0;
        do{
            System.out.println("1- Write Query");
            System.out.println("2- Exit");
            System.out.print("Enter your choice: ");
            if (input.hasNextInt()) {
                choice = input.nextInt();

                if (choice == 1) {
                    StringBuilder query = new StringBuilder();

                    System.out.print("Enter query: ");
                    String query1 = input.next();
                    query.append(query1).append(" ");

                    System.out.print("Enter operator: ");
                    String op = input.next();
                    query.append(op).append(" ");

                    System.out.print("Enter query: ");
                    String query2 = input.next();
                    query.append(query2);

                    while (true) {
                        System.out.print("Do you want to add another operator and query? (y/n): ");
                        String continueChoice = input.next();

                        if (continueChoice.equalsIgnoreCase("n")) {
                            break;
                        } else if (continueChoice.equalsIgnoreCase("y")) {
                            System.out.print("Enter operator: ");
                            String nextOp = input.next();
                            query.append(" ").append(nextOp);

                            System.out.print("Enter query: ");
                            String nextQuery = input.next();
                            query.append(" ").append(nextQuery);
                        } else {
                            System.out.println("Invalid choice. Please type 'y' or 'n'.");
                        }
                    }

                    String finalQuery = query.toString();

                    List<String> parsedQuery = QueryProcessor.queryParser(finalQuery);
                    System.out.println("Query ==> " + parsedQuery);

                } else if (choice == 2) {
                    System.out.println("Exiting...");
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    choice = 0;
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                input.next();
            }
        }while (choice != 2);
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
