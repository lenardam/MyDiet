package com.lenardam.mydiet.utils;

public class Utils {
    public static String doubleToStringFormat(double valueToFormat)
    {
        String formattedValue;
        if (valueToFormat % 1 == 0) {
            formattedValue = String.format("%.0f", valueToFormat);
        }
        else {
            formattedValue = String.format("%.2f", valueToFormat);
        }
        return formattedValue;
    }
}
