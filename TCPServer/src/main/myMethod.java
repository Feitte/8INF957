package main;

import java.util.ArrayList;

public class myMethod {


    static public ArrayList<Integer>[] arrayDivider(Integer[] tab,int num) {

        int max;
        int min;



        ArrayList<Integer>[] dividedArray = new ArrayList[num];

        for(int i = 0; i< num; i++){
            dividedArray[i] = new ArrayList<Integer>();
        }


        max = tab[0];
        min = tab[0];

        for (int val : tab) {
            max = Integer.max(val, max);
            min = Integer.min(val, min);
        }

        int truc = (max - min) / num;

        for (int val : tab) {
            if (val == min) {
                dividedArray[0].add(val);
            } else {
                if (val == max) {
                    dividedArray[num - 1].add(val);
                } else {
                    dividedArray[(int)(val - min) / truc].add(val);
                }
            }
        }
        return  dividedArray;
    }


}
