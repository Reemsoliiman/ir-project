package com.example.positionalIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class QueryProcessor {
    public static List<String> queryParser(String query) {
        query = query.trim();
        List<String> result = new ArrayList<>();
        String[] logicalOperators = {"AND NOT", "OR NOT", "AND", "OR"};

        String[] words = query.split("\\s+");
        StringBuilder currentSegment = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            boolean isLogicalOperator = false;

            if (i + 1 < words.length) {
                String twoWordOperator = words[i] + " " + words[i + 1];
                for (String operator : logicalOperators) {
                    if (operator.equalsIgnoreCase(twoWordOperator)) {
                        isLogicalOperator = true;
                        if (!currentSegment.isEmpty()) {
                            result.add(currentSegment.toString().trim());
                            currentSegment.setLength(0);
                        }
                        result.add(twoWordOperator);
                        i++;
                        break;
                    }
                }
            }

            if (!isLogicalOperator) {
                for (String operator : logicalOperators) {
                    if (words[i].equalsIgnoreCase(operator)) {
                        isLogicalOperator = true;
                        if (!currentSegment.isEmpty()) {
                            result.add(currentSegment.toString().trim());
                            currentSegment.setLength(0);
                        }
                        result.add(words[i]);
                        break;
                    }
                }
            }

            if (!isLogicalOperator) {
                if (!currentSegment.isEmpty()) {
                    currentSegment.append(" ");
                }
                currentSegment.append(words[i]);
            }
        }

        if (!currentSegment.isEmpty()) {
            result.add(currentSegment.toString().trim());
        }

        return result;
    }
    public static Map<String, List<Integer>> getQueryPositionalIndex(Map<String, Map<Integer, List<Integer>>> positionalIndex, List<String> input) {
        Map<String, List<Integer>> queryPositionalIndex = new TreeMap<>();
        String index = input.get(0).trim();
        if (index.contains(" ")) {
            String[] terms = index.split(" ");
            Map<Integer, List<Integer>> firstTermDocs = positionalIndex.get(terms[0]);
            if (firstTermDocs != null) {
                List<Integer> validDocs = new ArrayList<>();

                for (Map.Entry<Integer, List<Integer>> firstTermDoc : firstTermDocs.entrySet()) {
                    int docID = firstTermDoc.getKey();
                    List<Integer> firstTermPositions = firstTermDoc.getValue();

                    boolean validPhrase = true;
                    List<Integer> currentPositions = new ArrayList<>(firstTermPositions);

                    for (int i = 1; i < terms.length; i++) {
                        Map<Integer, List<Integer>> termDocs = positionalIndex.get(terms[i]);

                        if (termDocs != null && termDocs.containsKey(docID)) {
                            List<Integer> termPositions = termDocs.get(docID);

                            List<Integer> adjustedPositions = new ArrayList<>();
                            for (Integer position : currentPositions) {
                                for (Integer termPosition : termPositions) {
                                    if (termPosition == position + 1) {
                                        adjustedPositions.add(termPosition);
                                    }
                                }
                            }
                            if (adjustedPositions.isEmpty()) {
                                validPhrase = false;
                                break;
                            }
                            currentPositions = adjustedPositions;
                        } else {
                            validPhrase = false;
                            break;
                        }
                    }
                    if (validPhrase) {
                        validDocs.add(docID);
                    }
                }
                if (!validDocs.isEmpty()) {
                    queryPositionalIndex.put(index, validDocs);
                }
            }
        } else {
            Map<Integer, List<Integer>> termDocs = positionalIndex.get(index);
            if (termDocs != null) {
                queryPositionalIndex.put(index, new ArrayList<>(termDocs.keySet()));
            }
        }

        return queryPositionalIndex;
    }


}