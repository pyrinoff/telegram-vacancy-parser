package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface RegexUtils {

    /**
     *
     * @param sourceString - text to search in
     * @param patternString - regex pattern
     * @return Map like [0 => ["Group1", "Group2"]], where 0 - match #0
     */
    static @Nullable Map<Integer, List<String>> getMatches(String sourceString, String patternString) {
        Map<Integer, List<String>> resultMatches = new HashMap<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(sourceString);
        int matchIndex = 0;
        while(matcher.find()) {
            @NotNull final List<String> oneMatchOfPattern = new ArrayList<>();
            for(int i=0;i<=matcher.groupCount();i++) {
                oneMatchOfPattern.add(matcher.group(i)); //помещаем в один лист обьекты с одного совпадения
            }
            resultMatches.put(matchIndex, oneMatchOfPattern); //помещаем совпадение в мапу
            matchIndex++;
        }
        if(resultMatches.size()==0) return null;
        return resultMatches;
    }
    /**
     *
     * @param sourceString - text to search in
     * @param patternString - regex pattern
     * @return Map like [0 => ["Group1ForMatch1", "Group2ForMatch2"]], where 0 - group #0
     */
    static @Nullable Map<Integer, List<String>> getMatchesOld(String sourceString, String patternString) {
        Map<Integer, List<String>> resultMatches = new HashMap<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(sourceString);
        while(matcher.find()) {
            for(int i=0;i<=matcher.groupCount();i++) {
                if(!resultMatches.containsKey(i)) resultMatches.put(i, new ArrayList<>());
                resultMatches.get(i).add(matcher.group(i));
            }
        }
        if(resultMatches.size()==0) return null;
        return resultMatches;
    }

    static @Nullable List<String> getFirstFound(String contents, String regex) {
        List<List<String>> founds = getFound(contents, regex, 1);
        if(founds.size()<1) return null;
        return founds.get(0);
    }

    static @Nullable List<List<String>> getFound(String contents, String regex, @Nullable Integer limit) {
        if (isEmpty(regex) || isEmpty(contents)) {
            return null;
        }
        List<List<String>> results = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex, Pattern.UNICODE_CASE).matcher(contents);

        int currentCounter = 0;
        while (matcher.find()) {
            List <String> oneMatchResults = new ArrayList<>();
            for(int i=0;i<=matcher.groupCount(); i++) {
                if(i==0) continue; //dont add summary parsed string
                oneMatchResults.add(matcher.group(i));
            }
            results.add(oneMatchResults);
            if(limit!=null && ++currentCounter>=limit) break;
        }
        return results;
    }

    static boolean isEmpty(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }

    static boolean hasAtLeastOneMatch(String contents, String regex) {
        List<List<String>> founds = getFound(contents, regex, 1);
        return founds != null && founds.size() >= 1;
    }

}
