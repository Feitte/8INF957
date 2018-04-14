package main;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateImpl extends java.rmi.server.UnicastRemoteObject implements Calculate {

    protected CalculateImpl() throws RemoteException {
        super();
    }

    @Override
    public int add(String a, String b) throws RemoteException {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public int[] sort(int[] tab) throws RemoteException {
        Arrays.sort(tab);
        return tab;
    }

    public ArrayList<Integer>[] arrayDivider(int[] tab) {

        int hashMapLength = 1;
        int max;
        int min;

        if (tab.length > 100) {
            hashMapLength = tab.length / 100;
        }

        ArrayList<Integer>[] dividedArray = new ArrayList[hashMapLength];
        max = tab[0];
        min = tab[0];

        for (int val : tab) {
            max = Integer.max(val, max);
            min = Integer.min(val, min);
        }

        int truc = (max - min) / hashMapLength;

        for (int val : tab) {
            if (val == min) {
                dividedArray[0].add(val);
            } else {
                if (val == max) {
                    dividedArray[hashMapLength - 1].add(val);
                } else {
                    dividedArray[(val - min) / truc].add(val);
                }
            }
        }
        return  dividedArray;
    }
}
