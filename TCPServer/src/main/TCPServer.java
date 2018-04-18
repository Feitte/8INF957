package main;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TCPServer {

    static boolean continu;

    public static void main(String args[]) throws Exception {
       ServerSocket serverSoc;

        String class_name;
        String method;

        int portNumber;
        continu = true;
        if (args.length == 2) {
            portNumber = Integer.parseInt(args[1]);
            serverSoc = new ServerSocket(portNumber,100, InetAddress.getByName(args[0]));

        } else {
            System.out.print("Invalid parameters ");
            return;
        }

        System.out.println("\nTCP Server Started on Port Number: " + portNumber);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n\n[ DISPLAY ]");
        System.out.println("Enter Class and Method names:");
        class_name = br.readLine();
        method = br.readLine();


        Integer[] tab = {1, 0, 2, 3, 1, 4, 6, 5, 4, 2, 3, 4, 5, 6};

        List<TCPServerThread> tcpServerThreadList = new ArrayList<TCPServerThread>();
        ArrayList<ArrayList<Integer>> integerList = new ArrayList<ArrayList<Integer>>();
        List<Socket> socketList = new ArrayList<>();

        GoThread goThread = new GoThread(br);
        SocketThread socketThread = new SocketThread(socketList, serverSoc);
        while (continu) {

            if (!socketList.isEmpty()) {
                ArrayList<Integer> tmp = new ArrayList<>();
                integerList.add(tmp);
                Socket clientSoc = socketList.get(0);
                TCPServerThread serverThread = new TCPServerThread(clientSoc, class_name, method, tab, goThread, tmp);
                socketList.remove(clientSoc);
                tcpServerThreadList.add(serverThread);

            }
            if (!goThread.isAlive()) {
                continu = false;
                socketThread.interrupt();
            }
        }
        for (TCPServerThread serverThread : tcpServerThreadList) {
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        ArrayList<Integer> finalList = new ArrayList<>();
        for(List<Integer> l : integerList){
            finalList.addAll(l);
        }
        System.out.println(finalList);

    }
}

class SocketThread extends Thread {
    ServerSocket serverSocket;
    List<Socket> socketList;

    String class_name;
    String method;

    SocketThread(List<Socket> socketList, ServerSocket serverSoc) {
        this.serverSocket = serverSoc;
        this.socketList = socketList;
        start();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Waiting for Connection ...");
            try {
                Socket clientSoc = serverSocket.accept();
                socketList.add(clientSoc);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class GoThread extends Thread {
    BufferedReader br;
    boolean continu = true;

    GoThread(BufferedReader br) {
        this.br = br;
        start();
    }

    @Override
    public void run() {
        while (continu) {
            try {
                if (br.readLine().equals("go")) {
                    continu = false;
                }
            } catch (IOException e) {
                System.out.println(e);
            }

        }
    }
}

class TCPServerThread extends Thread {
    static int numClient;
    int idClient;

    Integer[] tab;
    ArrayList<Integer> list;
    ArrayList<Integer> integerArrayList;

    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    String class_name;
    String method;

    Thread th;


    TCPServerThread(Socket soc, String class_name, String method, Integer[] tab, Thread th, ArrayList<Integer> integerArrayList) {
        try {
            ClientSoc = soc;

            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader((new InputStreamReader(System.in)));

            this.class_name = class_name;
            this.method = method;
            this.tab = tab;
            this.th = th;
            this.integerArrayList = integerArrayList;


            System.out.println("\nTCP Client Connected ...");
            this.idClient = numClient;
            numClient++;
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {

        System.out.println(numClient);

        File calc = new File("src\\main\\Calc.java");

        try {
            dout.writeUTF(this.class_name);
            dout.writeUTF(this.method);

            ByteStream.toStream(dout, calc);

            if (th.isAlive()) {
                System.out.println("alive");
                try {
                    th.join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                dout.writeUTF("go");

                list = myMethod.arrayDivider(tab, numClient)[idClient];
                dout.writeUTF(list.toString());
                dout.writeUTF("go");

                String str = din.readUTF();
                String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
                for (String string : arrayString) {
                    try {
                        integerArrayList.add(Integer.parseInt(string));
                    } catch (Exception e) {
                        System.out.println("Not integer");
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            numClient--;
            System.out.println(this.idClient);
            //e.printStackTrace();
        }

    }
}

