package main;

import java.util.ArrayList;

public class myMethod {


   /* static public ArrayList<Object>[] arrayDivider(Object[] tab,int num, int type) {
        ArrayList<Object>[] dividedArray = new ArrayList[num];
        switch (type) {
            case 1: //int
                dividedArray = arrayDividerInt(tab, num);
            break;
            case 2: //float
                dividedArray = arrayDividerFLoat(tab,num);
                break;
        }
        return dividedArray;
    }


        private static ArrayList<Object>[] arrayDividerInt (Object[]tab,int num){


            int max;
            int min;
            ArrayList<Object>[] dividedArray = new ArrayList[num];
            for (int i = 0; i < num; i++) {
                dividedArray[i] = new ArrayList<Object>();
            }
            if (num != 1) {

                max = (int)tab[0];
                min = (int)tab[0];

                for (Object val : tab) {
                    max = Integer.max((int)val, max);
                    min = Integer.min((int)val, min);
                }

                float truc = (max - min) / (num - 1);

                for (Object val : tab) {
                    if ((int)val == min) {
                        dividedArray[0].add(val);
                    } else {
                        if ((int)val == max) {
                            dividedArray[num - 1].add(val);
                        } else {
                            float indice = (((int)val - min) / truc);
                            dividedArray[(int) indice].add(val);
                        }
                    }
                }
            } else {
                for (Object val : tab) {
                    dividedArray[0].add(val);
                }
            }

            return dividedArray;
        }



    static private ArrayList<Object>[] arrayDividerFLoat(Object[] tab,int num) {

        Float max;
        Float min;
        ArrayList<Object>[] dividedArray = new ArrayList[num];
        for(int i = 0; i< num; i++){
            dividedArray[i] = new ArrayList<Object>();
        }
        if (num != 1 ){

            max = (float)tab[0];
            min = (float)tab[0];

            for (Object val : tab) {
                max = Float.max((float)val, max);
                min = Float.min((float)val, min);
            }

            float truc = (max - min) / (num - 1);

            for (Object val : tab) {
                if ((float)val == min) {
                    dividedArray[0].add(val);
                } else {
                    if ((float)val == max) {
                        dividedArray[num - 1].add(val);
                    } else {
                        float indice = ((float)val - min) / truc;
                        dividedArray[(int)indice].add(val);
                    }
                }
            }
        }else{
            for (Object val : tab) {
                dividedArray[0].add(val);
            }
        }

        return  dividedArray;
    }
*/
}
