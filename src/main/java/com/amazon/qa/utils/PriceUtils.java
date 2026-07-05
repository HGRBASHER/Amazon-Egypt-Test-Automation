package com.amazon.qa.utils;

public class PriceUtils {
    public static double parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) return 0.0;
        String cleanPrice = priceText.replaceAll("[^0-9.]", "");
        try {
            return Double.parseDouble(cleanPrice);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
