package main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

public class TCPClientDemo {

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            TCPClient client = new TCPClient(args[0], Integer.parseInt(args[1]));
            client.display();

        } else {
            System.out.print("Invalid parameters ");
            return;
        }
    }
}


class TCPClient {
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
    File calc = new File("src\\main\\Calc.java");
    File calc_class = new File("out\\production\\Client\\main\\Calc.class");

    TCPClient(String hostName, int port) {
        try {
            ClientSoc = new Socket(hostName, port);
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Create a new process to compile source file
    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
        System.out.println("Compilation exitValue() : " + pro.exitValue());
    }

    public void display() throws Exception {
        String msgFromServer;
        String a;
        String b;
        String method;
        String class_name;
        String msg;
        Integer choice;
        String list;


        Class Calc;
        Method serverMethod;
        Object calc_class;
        Object output = null;

        while (true) {


            try {
                class_name = din.readUTF();
                System.out.println(class_name);

                method = din.readUTF();
                System.out.println(method);

                // Create a new source file if it doesn't exist
                File calc = new File("src\\main\\" + class_name + ".java");

                try {
                    boolean isNewFile = calc.createNewFile();
                    if (isNewFile) {
                        System.out.println("A source file was created");
                    } else {
                        System.out.println("Source file already exist, no need to create a new one");
                    }
                    // Copy data received from client to this new file
                    ByteStream.toFile(din, calc);

                    // Compilation Step
                    System.out.println("**********");
                    runProcess("javac -cp src src/main/" + class_name + ".java -d out/production/Master");
                    System.out.println("**********");

                    // Reflexion Step
                    Calc = Class.forName("main." + class_name);
                    calc_class = Calc.newInstance();

                    serverMethod = calc_class.getClass().getMethod(method, String.class, String.class);
                    //output = add.invoke(calc_class, a, b);

                } catch (IOException e) {
                    System.out.println("Exception Occurred:");
                    e.printStackTrace();
                }

                list = din.readUTF();
                System.out.println(list);

                ArrayList<Integer> liste = new ArrayList<>();
                String[] arrayString = list.substring(1, list.length() - 1).split("\\s*,\\s*");
                System.out.println(arrayString[0]);

                // liste = Arrays.asList(list,',');

                // Read parameters from client
              /*  msg = din.readUTF();
                class_name = din.readUTF();
                method = din.readUTF();
                a = din.readUTF();
                b = din.readUTF();
                System.out.println("Client wants to " + method + "(" + a + "," + b + ")");


                // début copier coller
                System.out.println("----> SourceColl Mod");

                // Create a new source file if it doesn't exist
                File calc = new File("src\\main\\" + class_name + ".java");
                try {
                    boolean isNewFile = calc.createNewFile();
                    if (isNewFile) {
                        System.out.println("A source file was created");
                    } else {
                        System.out.println("Source file already exist, no need to create a new one");
                    }
                    // Copy data received from client to this new file
                    ByteStream.toFile(din, calc);

                    // Compilation Step
                    System.out.println("**********");
                    runProcess("javac -cp src src/main/" + class_name + ".java -d out/production/Master");
                    System.out.println("**********");

                    // Reflexion Step
                    Calc = Class.forName("main." + class_name);
                    calc_class = Calc.newInstance();

                    add = calc_class.getClass().getMethod(method, String.class, String.class);
                    output = add.invoke(calc_class, a, b);


                } catch (IOException e) {
                    System.out.println("Exception Occurred:");
                    e.printStackTrace();
                }

                //Out: result + current time of server
                System.out.println("Result is : " + output);

                //dout.writeUTF(output.toString() + "  [" + (new Date().toString()) + "]");


                // fin copier coller
*/
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}

/*
                System.out.println("\n\n[ DISPLAY ]");
                System.out.println("Enter Class and Method names (press 'q' to exit):");
                class_name = br.readLine();
                if (class_name.equals("q")) {
                    break;
                }
                method = br.readLine();

                System.out.println("Enter two arguments for this :");
                a = br.readLine();
                b = br.readLine();

                System.out.println("1. Send via SourceColl");
                System.out.println("2. Send via ByteColl");
                System.out.println("3. Send via RMI");
                System.out.println("4. Exit");
                System.out.print("\nEnter Choice :");

                //Send parameters to server...
                choice = Integer.parseInt(br.readLine());
                dout.writeUTF(choice.toString());
                dout.writeUTF(class_name);
                dout.writeUTF(method);
                dout.writeUTF(a);
                dout.writeUTF(b);

                switch (choice) {
                    case 1:
                        System.out.println("[Client] SourceColl Mod");

                        //Send main.Calc to server...
                        ByteStream.toStream(dout, calc);

                        // Wait for the response from server...
                        msgFromServer = din.readUTF();
                        System.out.print("[Server] Result: " + msgFromServer + "\n");
                        break;
                    case 2:
                        System.out.println("[Client] ByteColl Mod");

                        //Send the message to server...
                        //ByteStream.toStream(dout, calc_class);

                        // Wait for the response from server...
                        msgFromServer = din.readUTF();
                        System.out.print("[Server] Result: " + msgFromServer + "\n");
                        break;
                    case 3:
                        System.out.println("[Client] RMI Mod");

                        // RMI Step : invoke method 'add' from server
                        Calculate c = (Calculate) java.rmi.Naming.lookup("rmi://localhost:10000/MaCalc");
                        Integer out = c.add(a, b);

                        // Wait for the response from server...
                        msgFromServer = din.readUTF();
                        System.out.print("[Server] Result: " + out + "  " + msgFromServer + "\n");
                        break;
                    case 4:
                        dout.close();
                        System.exit(1);
                    default:
                        break;
                }*/
