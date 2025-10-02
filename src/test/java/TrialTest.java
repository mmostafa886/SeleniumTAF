import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TrialTest {

    public static void findDuplicateCharacters(String str) {
        if (str == null || str.isEmpty()) {
            System.out.println("Input string is empty.");
            return;
        }

        Map<Character, Integer> charCountMap = new HashMap<>();
        char[] strArray = str.toCharArray();

        for (char c : strArray) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        System.out.println("Duplicate characters:");
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    //======================================================================================

    public static List<Integer> findDuplicates(List<Integer> integers, int numberOfDuplicates) {

        if (integers == null || integers.isEmpty() || numberOfDuplicates < 1){
            System.out.println("Integers list is empty or there is no duplicates");
            return null;
        }

        Map<Integer, Integer> countMap = new HashMap<>();
        for (Integer num : integers) {
            if (num != null) {
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
            }
        }

        List<Integer> duplicates = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == numberOfDuplicates) {
                duplicates.add(entry.getKey());
            }
        }

        System.out.println("Duplicates: " + duplicates.toString());
        return duplicates;
    }


    public static void main(String[] args) {
        findDuplicateCharacters("programmingroio");
        findDuplicates(asList(-1, 1, null, 3, 2, 5, 6, -1, null, 3, 6), 2);
    }
}
