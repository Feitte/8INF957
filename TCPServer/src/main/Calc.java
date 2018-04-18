package main;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calc {
    public int add(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public List<Integer> sort(List<Integer> list) {
        Integer[] tab = new Integer[list.size()];
        tab =  list.toArray(tab);
        Arrays.sort(tab);
        ArrayList<Integer> liste = new ArrayList<Integer>();
        for(Integer integ:tab){
            liste.add(integ);
        }
        return liste;
    }

}
