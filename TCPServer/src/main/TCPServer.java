package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class TCPServer {

    public static void main(String args[]) throws Exception {
        Socket clientSoc;
        ServerSocket serverSoc;

        String class_name;
        String method;

        int portNumber;

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
            serverSoc = new ServerSocket(portNumber);

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

        while (true) {
            System.out.println("Waiting for Connection ...");
            clientSoc = serverSoc.accept();
            TCPServerThread serverThread = new TCPServerThread(clientSoc, class_name, method, tab);
        }
    }
}


class TCPServerThread extends Thread {
    static int numClient;
    int idClient;

    Integer[] tab;

    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    String class_name;
    String method;


    TCPServerThread(Socket soc, String class_name, String method, Integer[] tab) {
        try {
            ClientSoc = soc;

            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader((new InputStreamReader(System.in)));

            this.class_name = class_name;
            this.method = method;
            this.tab = tab;


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

        ArrayList<Integer> list = new ArrayList<Integer>();


        System.out.println(numClient);

        File calc = new File("src\\main\\Calc.java");

        try {
            dout.writeUTF(this.class_name);
            dout.writeUTF(this.method);

            ByteStream.toStream(dout, calc);

            if (br.readLine().equals("go")) {
                dout.writeUTF("go");

                list = myMethod.arrayDivider(tab, numClient)[idClient];
                dout.writeUTF(list.toString());
                dout.writeUTF("go");
                System.out.println(din.readUTF());

            }

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            numClient--;
            System.out.println(this.idClient);
            //e.printStackTrace();
        }

    }
}

