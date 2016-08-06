package com.example.roman.calculator.fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roman.calculator.calculations.Factorial;
import com.example.roman.calculator.R;

public class FactorialFragment extends Fragment {


    public FactorialFragment() {
        // Required empty public constructor
    }

    public static FactorialFragment newInstance() {
        FactorialFragment fragment = new FactorialFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_factorial, container, false);
        final TextView tvResult = (TextView) rootView.findViewById(R.id.tab_factorial_result);
        final EditText editText = (EditText) rootView.findViewById(R.id.tab_factorial_input);
        rootView.findViewById(R.id.btn_calculate_factorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Factorial factorial = new Factorial();
                String strNumber = editText.getText().toString();
                if (strNumber.isEmpty()) {
                    tvResult.setText("Enter the number.");
                } else {
                    int number = 0;
                    try {
                        number = Integer.parseInt(strNumber);
                    } catch (NumberFormatException e) {
                        Log.e("Error", e.getMessage());
                    }
                    try {
                        tvResult.setText("sum = " + factorial.getSumOfFactorial(number));
                    }catch (Exception e){
                        tvResult.setText("Enter a number less than.");
                    }

                }
            }
        });
        return rootView;
    }
}
