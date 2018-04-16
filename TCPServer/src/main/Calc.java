package main;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class Calc {
    public int add(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public Integer[] sort(List<Integer> list) {
        Integer[] tab = new Integer[list.size()];
        tab =  list.toArray(tab);

        Arrays.sort(tab);
        return tab;
    }

}
