package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calc {

    public List<Integer> sort(List<Integer> list) {
        Integer[] tab = new Integer[list.size()];
        tab = list.toArray(tab);
        Arrays.sort(tab);

        //Transforme le tableaux en liste
        ArrayList<Integer> liste = new ArrayList<Integer>();
        for (Integer integ : tab) {
            liste.add(integ);
        }
        return liste;
    }

}
