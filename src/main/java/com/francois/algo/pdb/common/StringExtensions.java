package com.francois.algo.pdb.common;

public class StringExtensions {
    public static final String NewLineSeparator = System.getProperty("line.separator");

    public static final String getValueOrDefault(String value, String defaultValue) {
        return isNullOrEmpty(value) ? defaultValue : value;
    }

    public static boolean isNullOrEmpty(String str){
        return (str == null || str.trim().isEmpty());
    }
}
