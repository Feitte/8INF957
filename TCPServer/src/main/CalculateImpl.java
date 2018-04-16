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




}
