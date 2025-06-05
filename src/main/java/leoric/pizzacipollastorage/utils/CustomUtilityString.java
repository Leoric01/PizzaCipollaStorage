package leoric.pizzacipollastorage.utils;

import java.text.Normalizer;

public class CustomUtilityString {
    public static String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}