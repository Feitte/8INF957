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
        if (num != 1 ){

            max = tab[0];
            min = tab[0];

            for (int val : tab) {
                max = Integer.max(val, max);
                min = Integer.min(val, min);
            }

            float truc = (max - min) / (num - 1);

            for (int val : tab) {
                if (val == min) {
                    dividedArray[0].add(val);
                } else {
                    if (val == max) {
                        dividedArray[num - 1].add(val);
                    } else {
                        float indice = (val - min) / truc;
                        dividedArray[(int)indice].add(val);
                    }
                }
            }
        }else{
            for (int val : tab) {
                dividedArray[0].add(val);
            }
        }

        return  dividedArray;
    }


}
