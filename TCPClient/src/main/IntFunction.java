package main;

import java.util.ArrayList;

public class IntFunction extends TypeFunction{

    @Override
    public void StringToList(String str, ArrayList<Object> integerArrayList) {
        String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
        for (String string : arrayString) {
            try {
                integerArrayList.add(Integer.parseInt(string));
            } catch (Exception e) {
                System.out.println("Not integer");
            }

        }
    }
}
