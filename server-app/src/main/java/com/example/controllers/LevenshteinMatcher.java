package com.example.controllers;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

public class LevenshteinMatcher {
    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(
                                    dp[i - 1][j],
                                    dp[i][j - 1]
                            )
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private static String normalize(String str) {
        str = str.toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{M}", "");
        return str;
    }

    public static List<String> matchParallel(List<String> list, String target, int maxDistance) {
        String normalizedTarget = normalize(target);

        return list.parallelStream()
                .filter(s -> {
                    String normalizedWord = normalize(s);

                    if (normalizedWord.startsWith(normalizedTarget)) {
                        return true;
                    }
                    int distance = levenshteinDistance(normalizedTarget, normalizedWord);
                    int extraChars = Math.max(0, normalizedWord.length() - normalizedTarget.length());

                    return distance + extraChars <= maxDistance;
                })
                .collect(Collectors.toList());
    }


}
