package main;

public interface Calculate extends java.rmi.Remote {

    int add(String a, String b) throws java.rmi.RemoteException;
    int[] sort(int[] tab) throws java.rmi.RemoteException;
}
