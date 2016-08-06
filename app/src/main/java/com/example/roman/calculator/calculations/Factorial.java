package com.example.roman.calculator.calculations;

public class Factorial {
    public int getSumOfFactorial(int n) {
        final int BASE = 100000;
        int[] dig = new int[100];
        dig[0] = 1;
        int first = 0, last = 0;

        for (int i = 2; i <= n; i++) {
            int carry = 0;
            for (int x = first; x <= last; x++) {
                carry = dig[x] * i + carry;
                dig[x] = carry % BASE;
                if (x == first && (carry % BASE) == 0) {
                    first++;
                }
                carry /= BASE;
            }
            if (carry != 0) {
                dig[++last] = carry;
            }
        }
        int sum = 0;
        for (int i = first; i <= last; i++) {
            sum += getSum(dig[i]);
        }
        return sum;
    }

    private int getSum(Integer number) {
        int sum = 0;
        char[] arr = number.toString().toCharArray();
        for (int i = 0; i < arr.length; i++) {
            sum += Character.getNumericValue(arr[i]);
        }
        return sum;
    }
}
