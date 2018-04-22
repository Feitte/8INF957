package main;

public class TypeManager {
    public static TypeFunction getTypeFunction(int i){
        TypeFunction tf = null;
        switch (i){
            case 1 : //int
                tf = new IntFunction();
                break;
            case 2 : // float
                tf = new FloatFunction();
                break;
        }
        return tf;
    }
}
