package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calc {

    public List<Object> sort(List<Object> list, int type) {
        List<Object> liste = new ArrayList<Object>();
        switch (type) {
            case 1: //int
                liste = sortInt(list);
                break;
            case 2: //float
                liste = sortFloat(list);
                break;
        }
        return liste;
    }

    public List<Object> sortInt(List<Object> list){
        Integer[] tab = new Integer[list.size()];
        tab = list.toArray(tab);
        Arrays.sort(tab);

        //Transforme le tableaux en liste
        ArrayList<Object> liste = new ArrayList<Object>();
        for (Integer integ : tab) {
            liste.add(integ);
        }
        return liste;
    }


    public List<Object> sortFloat(List<Object> list) {
        Float[] tab = new Float[list.size()];
        tab = list.toArray(tab);
        Arrays.sort(tab);

        //Transforme le tableaux en liste
        ArrayList<Object> liste = new ArrayList<Object>();
        for (Float fl : tab) {
            liste.add(fl);
        }
        return liste;
    }

}
