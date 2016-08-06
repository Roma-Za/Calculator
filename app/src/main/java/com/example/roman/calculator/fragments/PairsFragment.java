package com.example.roman.calculator.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roman.calculator.calculations.Pairs;
import com.example.roman.calculator.R;

public class PairsFragment extends Fragment {


    public PairsFragment() {
        // Required empty public constructor
    }

    public static PairsFragment newInstance() {
        PairsFragment fragment = new PairsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_pairs, container, false);
        final TextView tvResult = (TextView) rootView.findViewById(R.id.tab_pairs_result);
        final EditText editText = (EditText) rootView.findViewById(R.id.tab_pairs_input);
        rootView.findViewById(R.id.btn_calculate_pairs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNumber = editText.getText().toString();
                if(validate(strNumber)){

                    Pairs pairs = new Pairs(getArr(strNumber));
                    tvResult.setText(pairs.getSubList());
                }else{
                    tvResult.setText("Enter the correct pairs.");
                }
            }
        });
        return rootView;
    }

    private int[] getArr(String strNumber) {
        String [] strArr = strNumber.split(" ");
        int [] intArr = new int [strArr.length];
        for (int i = 0; i<strArr.length; i++){
            try {
                intArr[i] = Integer.parseInt(strArr[i]);
            } catch (NumberFormatException e) {
                Log.e("Error", e.getMessage());
            }
        }

        return intArr;
    }

    private boolean validate(String strNumber) {
        if(strNumber.isEmpty())return false;
        String [] strArr = strNumber.split(" ");
        if((strArr.length % 2)==0)return true;
        else return false;
    }

}
