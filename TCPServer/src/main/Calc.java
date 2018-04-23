package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Calc {

    public List<Object> MainMethod(List<Object> list, int type) {
        return list;
    }

    public List<Object> FinalMethod(List<Object> list, int type) {
        return list;
    }

    public ArrayList<Object>[] arrayDivider(Object[] tab, int num, int type) {
        ArrayList<Object>[] dividedArray = new ArrayList[num];
        int length = tab.length;
        for (int i = 0; i < num; i++) {
            dividedArray[i] = new ArrayList<Object>();
        }
        for (int i = 0; i < num; i++) {
            for (int j = i * length / num; j < ((i + 1) * length / num) && j< length; j++) {
                dividedArray[i].add(tab[j]);
            }
        }
        return dividedArray;
    }
}
