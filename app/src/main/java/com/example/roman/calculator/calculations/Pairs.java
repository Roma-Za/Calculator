package com.example.roman.calculator.calculations;

public class Pairs {
    private int[][] arr;

    public Pairs(int [] arrNam){
        arr = new int[arrNam.length/2][2];
        for (int i = 0; i<arrNam.length; i+=2){
            arr[i/2][0] = arrNam[i];
            arr[i/2][1] = arrNam[i+1];
        }
    }

    public String getSubList(){

        int startIdx = 0;
        int lengthList = 1;
        int startIdxTemp = -1;
        int lengthListTemp = 1;
        boolean isStarted = false;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1][0] < arr[i][0] && arr[i - 1][1] > arr[i][1]) {
                if (!isStarted) {
                    startIdxTemp = i - 1;
                }
                lengthListTemp++;
                isStarted = true;
            } else {
                if (startIdxTemp != -1 && lengthListTemp > lengthList) {
                    startIdx = startIdxTemp;
                    lengthList = lengthListTemp;
                }
                startIdxTemp = -1;
                lengthListTemp = 1;
                isStarted = false;
            }
        }
      return getStrSubArr(startIdx, lengthList, arr);
    }

    private String getStrSubArr(int startIdx, int lengthList, int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIdx, j = 0; j < lengthList; i++, j++) {
            sb.append(arr[i][0]);
            sb.append(" ");
            sb.append(arr[i][1]);
            sb.append("  ");
        }
        return sb.toString();
    }
}
