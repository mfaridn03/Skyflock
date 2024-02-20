package dev.farid.skyflock.utils;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    /**
     * Removes unicode from a given input
     *
     * @param input Input string
     * @return The input string stripped of all unicode characters
     */
    public static String removeUnicode(String input) {
        return input.replaceAll("[^\\u0000-\\u007F]", "");
    }

    /**
     * Gets the matching object from the lines based on the provided regular expression and optional type.
     *
     * @param regex Regex pattern to match against the lines
     * @param list The list of strings to search for the matching pattern.
     * @param type The optional type of the matching object. 'int' returns an Integer, 'float' returns Float, otherwise returns String
     * @return The matching object based on the provided regular expression and type. If no match is found or the list is null, returns null.
     */
    public static Object getMatchFromLines(String regex, List<String> list, @Nullable String type, @Nullable Integer group) {
        if (list == null)
            return null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");

        for (String line : list) {
            matcher.reset(line);

            if (!matcher.find()) {
                continue;
            }

            String matchGroup = matcher.group(group == null ? 1 : group);

            if (type == null)
                return matchGroup;

            switch (type) {
                case "int":
                    return Integer.parseInt(matchGroup);
                case "float":
                    return Float.parseFloat(matchGroup);
                default:
                    return matchGroup;
            }
        }

        return null;
    }

    /**
     * Convert roman numeral string to int. Only 1-5
     *
     * @param romanNumeral roman numeral string
     * @return integer numeral
     */
    public static Integer romanToInt(String romanNumeral) {
        switch (romanNumeral) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            default:
                return null;
        }
    }

    /**
     * Converts integer to roman numeral string
     *
     * @param num integer input
     * @return roman numeral string
     */
    public static String intToRoman(int num) {
        switch (num) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return null;
        }
    }
}
