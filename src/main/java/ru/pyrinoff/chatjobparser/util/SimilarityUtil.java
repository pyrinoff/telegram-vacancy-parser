package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface SimilarityUtil {

    static float getSimilarityPercent(@Nullable String first, @Nullable String second) {
        if(Objects.equals(first, second)) return 100;
        if(first == null) throw new NullPointerException("First string is null");
        if(second == null) throw new NullPointerException("Second string is null");
        first = first.toLowerCase();
        second = second.toLowerCase();
        return (float)(similar(first, second)*200)/(first.length()+second.length());
    }

    private static int similar(String first, String second)  {
        int p, q, l, sum;
        int pos1=0;
        int pos2=0;
        int max=0;
        char[] arr1 = first.toCharArray();
        char[] arr2 = second.toCharArray();
        int firstLength = arr1.length;
        int secondLength = arr2.length;

        for (p = 0; p < firstLength; p++) {
            for (q = 0; q < secondLength; q++) {
                for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p+l] == arr2[q+l]); l++);
                if (l > max) {
                    max = l;
                    pos1 = p;
                    pos2 = q;
                }

            }
        }
        sum = max;
        if (sum > 0) {
            if (pos1 > 0 && pos2 > 0) {
                sum += similar(first.substring(0, pos1>firstLength ? firstLength : pos1), second.substring(0, pos2>secondLength ? secondLength : pos2));
            }

            if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
                sum += similar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
            }
        }
        return sum;
    }

}
