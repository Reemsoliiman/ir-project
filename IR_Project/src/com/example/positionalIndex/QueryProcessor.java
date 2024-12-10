package com.example.positionalIndex;

import java.util.ArrayList;
import java.util.List;

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
}