package main;

import java.util.ArrayList;

public abstract class TypeFunction {
     abstract void StringToList(String str, ArrayList<Object> arrayList);

    public Object[] StringToArray(String str) {
        ArrayList<Object> list = new ArrayList<Object>();
        StringToList(str,list);
        Object[] tab = new Object[list.size()];
        list.toArray(tab);
        return tab;
    }
}
