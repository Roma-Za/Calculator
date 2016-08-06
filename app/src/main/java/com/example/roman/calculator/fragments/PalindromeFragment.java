package com.example.roman.calculator.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roman.calculator.calculations.Palindrome;
import com.example.roman.calculator.R;

public class PalindromeFragment extends Fragment {

    public PalindromeFragment() {
    }

    public static PalindromeFragment newInstance() {
        PalindromeFragment fragment = new PalindromeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_palindrome, container, false);
        final TextView tvResult = (TextView) rootView.findViewById(R.id.tab_palindrome_result);
        final EditText editText = (EditText) rootView.findViewById(R.id.tab_palindrome_input);
        rootView.findViewById(R.id.btn_calculate_palindrome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Palindrome palindrome = new Palindrome();

                String strNumber = editText.getText().toString();
                if (strNumber.isEmpty()) {
                    int p = palindrome.getMaxPalindrome();
                    tvResult.setText("Max palindrome: " + p);
                } else {
                    int number = 0;
                    try {
                        number = Integer.parseInt(strNumber);
                    } catch (NumberFormatException e) {
                        Log.e("Error", e.getMessage());
                    }

                    if (palindrome.isPalindrome(number)) {
                        tvResult.setText("Palindrome");
                    } else {
                        tvResult.setText("Not palindrome");
                    }

                }
            }
        });
        return rootView;
    }
}