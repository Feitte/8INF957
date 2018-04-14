package main;

import java.rmi.RemoteException;
import java.util.Arrays;

public class Calc {
    public int add(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public int[] sort(int[] tab) throws RemoteException {
        Arrays.sort(tab);
        return tab;
    }
}
