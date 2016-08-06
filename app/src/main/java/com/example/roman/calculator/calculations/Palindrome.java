package com.example.roman.calculator.calculations;

public class Palindrome {

    public int getMaxPalindrome() {
        final int MAX_NUMBER = 999;
        final int MIN_NUMBER = 100;
        int maxPalindrom = 0;
        for (int i = MAX_NUMBER; i >= MIN_NUMBER; i--) {
            if (i * i < maxPalindrom) {
                break;
            }
            for (int j = MAX_NUMBER; j >= MIN_NUMBER; j--) {
                int result = j * i;
                if (isPalindrome(result)) {
                    if (maxPalindrom < result) {
                        maxPalindrom = result;
                    } else {
                        break;
                    }
                }
            }
        }
        return maxPalindrom;
    }

    public boolean isPalindrome(int number) {
        String str = String.valueOf(number);
        return str.equals(new StringBuilder(str).reverse().toString());
    }

}
