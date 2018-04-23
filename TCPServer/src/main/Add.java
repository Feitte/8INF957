package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Add extends Calc {
    public List<Object> MainMethod(List<Object> list, int type) {
        List<Object> liste = new ArrayList<Object>();
        switch (type) {
            case 1: //int
                liste = addInt(list);
                break;
            case 2: //float
                liste = addFloat(list);
                break;
        }
        return liste;
    }

    private List<Object> addInt(List<Object> list){
        int sum = 0;
        for (Object o : list) {
            sum += (int) o;
        }
        List<Object> liste = new ArrayList<>();
        liste.add(sum);
        return liste;
    }
    private List<Object> addFloat(List<Object> list){
        float sum = 0;
        for (Object o : list) {
            sum += (float) o;
        }
        List<Object> liste = new ArrayList<>();
        liste.add(sum);
        return liste;
    }

    public List<Object> FinalMethod (List<Object> list, int type){
        return MainMethod(list,type);
    }

}
