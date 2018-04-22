package main;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class TCPServer {

    static boolean continu;

    public static void main(String args[]) throws Exception {

        ServerSocket serverSoc = null;
        String fin = "in.txt";
        String fout = "out.txt";

        String class_name;
        String method;

        int portNumber;
        continu = true;

        if (args.length == 2) {
            portNumber = Integer.parseInt(args[1]);
            serverSoc = new ServerSocket(portNumber, 100, InetAddress.getByName(args[0]));


        } else {
            System.out.print("Invalid parameters ");
            return;
        }


        if (serverSoc != null) {

            try {
                System.out.println("new");
                Files.deleteIfExists(Paths.get(fout));
                Files.createFile(Paths.get(fout));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\nTCP Server Started on Port Number: " + portNumber);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\n\n[ DISPLAY ]");
            System.out.println("Enter Class and Method names:");
            class_name = br.readLine();
            method = br.readLine();

            String str = ReadFileIn(fin);
            Integer[] tab = StringToArray(str);

            int[] cpt = {0};
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
                    TCPServerThread serverThread = new TCPServerThread(clientSoc, class_name, method, tab, goThread, tmp, cpt);
                    socketList.remove(clientSoc);

                    tcpServerThreadList.add(serverThread);

                }
                if (!goThread.isAlive()) {
                    continu = false;
                    socketThread.interrupt();
                }
            }

            WaitForServerThread(tcpServerThreadList);
            List<Integer> finalList = FinalList(integerList);
            System.out.println(finalList);
            toFile(finalList, fout);
            br.close();


        }
    }

    private static String ReadFileIn(String fin) {
        String str = "";
        try {
            BufferedReader brFile = new BufferedReader(new FileReader(fin));
            try {
                str = brFile.readLine();
            } finally {
                brFile.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return str;
    }

    private static Integer[] StringToArray(String str) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
        for (String string : arrayString) {
            try {
                list.add(Integer.parseInt(string));
            } catch (Exception e) {
                System.out.println("Not integer");
            }
        }
        Integer[] tab = new Integer[list.size()];
        list.toArray(tab);
        return tab;
    }

    private static void toFile(List<Integer> finalList, String fout) {
        print("[", fout);
        for (int i = 0; i < finalList.size(); i++) {
            if (i == finalList.size() - 1) {
                print(finalList.get(i).toString(), fout);
            } else {
                print(finalList.get(i).toString() + " ,", fout);
            }
        }
        print("]", fout);
    }

    private static void print(String string, String filename) {
        try {
            Files.write(Paths.get(filename), (string + " ").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Attend que tu les threads de serveur soient terminées
    private static void WaitForServerThread(List<TCPServerThread> list) {
        for (TCPServerThread serverThread : list) {
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    //Passe d'une liste de liste d'entier à une liste d'entier
    private static List<Integer> FinalList(ArrayList<ArrayList<Integer>> list) {
        ArrayList<Integer> finalList = new ArrayList<>();
        for (List<Integer> l : list) {
            finalList.addAll(l);
        }
        return finalList;
    }
}

//Thread permettant de detecter de nouvelle connection
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

//Thread servant de repère pour le "go" de chaque thread de serveur
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

//Thread de serveur permettant l'envoi et la récéption de donnée avec le client
class TCPServerThread extends Thread {
    private static int numClient;
    private int idClient;

    private Integer[] tab;
    private ArrayList<Integer> list;
    private ArrayList<Integer> integerArrayList;

    private Socket ClientSoc;
    private DataInputStream din;
    private DataOutputStream dout;
    private BufferedReader br;

    private String class_name;
    private String method;

    private Thread th;


    TCPServerThread(Socket soc, String class_name, String method, Integer[] tab, Thread th, ArrayList<Integer> integerArrayList, int[] cpt) {
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

    public void stringToListInteger(String str, ArrayList<Integer> integerArrayList) {
        String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
        for (String string : arrayString) {
            try {
                integerArrayList.add(Integer.parseInt(string));
            } catch (Exception e) {
                System.out.println("Not integer");
            }

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

                try {
                    th.join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                dout.writeUTF("go");
                list = myMethod.arrayDivider(tab, numClient)[idClient];
                dout.writeUTF(list.toString());


                String str = din.readUTF();
                stringToListInteger(str, integerArrayList);
            }

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            //numClient--;
            System.out.println(this.idClient);
            list = myMethod.arrayDivider(tab, numClient)[idClient];
            Calc c = new Calc();
            integerArrayList.addAll(c.sort(list));

            //e.printStackTrace();
        }

    }
}

