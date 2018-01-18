package com.project.blackspider.musician.view.CustomRecycler.utility;

/**
 * Created by Rujel on 09-10-2017.
 */

public class StringMatcher {
    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null)
            return false;
        if (keyword.length() > value.length())
            return false;

        int i = 0, j = 0;
        do {
            if (keyword.charAt(j) == value.charAt(i)) {
                i++;
                j++;
            } else if (j > 0)
                break;
            else
                i++;
        } while (i < value.length() && j < keyword.length());

        return (j == keyword.length())? true : false;
    }
}