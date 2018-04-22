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


public class TCPServer{

    static boolean continu;

    public static void main(String args[]) throws Exception {

        ServerSocket serverSoc = null;
        String fin = "in.txt";
        String fout = "out.txt";
        int type ;
        TypeFunction tf;

        String class_name;
        String method;

        int portNumber;
        continu = true;

        if (args.length == 4) {
            portNumber = Integer.parseInt(args[1]);
            serverSoc = new ServerSocket(portNumber, 100, InetAddress.getByName(args[0]));
            fin = args[3];
            type = Integer.parseInt(args[2]);

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
            tf = TypeManager.getTypeFunction(type);
            Object[] tab = tf.StringToArray(str);

            int[] cpt = {0};
            List<TCPServerThread> tcpServerThreadList = new ArrayList<TCPServerThread>();
            ArrayList<ArrayList<Object>> objectList = new ArrayList<ArrayList<Object>>();
            List<Socket> socketList = new ArrayList<>();

            GoThread goThread = new GoThread(br);
            SocketThread socketThread = new SocketThread(socketList, serverSoc);

            while (continu) {

                if (!socketList.isEmpty()) {

                    ArrayList<Object> tmp = new ArrayList<>();
                    objectList.add(tmp);

                    Socket clientSoc = socketList.get(0);
                    TCPServerThread serverThread = new TCPServerThread(clientSoc, class_name, method, tab, goThread, tmp,type,tf);
                    socketList.remove(clientSoc);
                    tcpServerThreadList.add(serverThread);

                }
                if (!goThread.isAlive()) {
                    continu = false;
                    socketThread.interrupt();
                }
            }

            WaitForServerThread(tcpServerThreadList);
            List<Integer> finalList = FinalList(objectList);
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


    private static void toFile(List<?> finalList, String fout) {
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
    private static <T> List FinalList(ArrayList<ArrayList<T>> list) {
        ArrayList<T> finalList = new ArrayList<T>();
        for (List<T> l : list) {
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
    private int type;
    private TypeFunction tf;

    private Object[] tab;
    private ArrayList<Object> list;
    private ArrayList<Object> integerArrayList;

    private Socket ClientSoc;
    private DataInputStream din;
    private DataOutputStream dout;
    private BufferedReader br;

    private String class_name;
    private String method;

    private Thread th;


    TCPServerThread(Socket soc, String class_name, String method, Object[] tab, Thread th, ArrayList<Object> integerArrayList, int type, TypeFunction tf) {
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
            this.type = type;
            this.tf = tf;


            System.out.println("\nTCP Client Connected ...");
            this.idClient = numClient;
            numClient++;
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* public void stringToListInteger(String str, ArrayList<Integer> integerArrayList) {
        String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
        for (String string : arrayString) {
            try {
                integerArrayList.add(Integer.parseInt(string));
            } catch (Exception e) {
                System.out.println("Not integer");
            }

        }
    }*/

    @Override
    public void run() {

        System.out.println(numClient);

        File calc = new File("src\\main\\Calc.java");

        try {
            dout.writeUTF(this.class_name);
            dout.writeUTF(this.method);
            dout.writeUTF(Integer.toString(this.type));

            ByteStream.toStream(dout, calc);

            if (th.isAlive()) {

                try {
                    th.join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                dout.writeUTF("go");
                list = myMethod.arrayDivider(tab, numClient,type)[idClient];
                System.out.println(list.toString());
                dout.writeUTF(list.toString());


                String str = din.readUTF();
                tf.StringToList(str, integerArrayList);
            }

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            //numClient--;
            System.out.println(this.idClient);
            list = myMethod.arrayDivider(tab, numClient,type)[idClient];
            Calc c = new Calc();
            //integerArrayList.addAll(c.sort(list));

            //e.printStackTrace();
        }

    }
}

