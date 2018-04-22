package main;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    //File calc = new File("src\\main\\Calc.java");
    //File calc_class = new File("out\\production\\Client\\main\\Calc.class");

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

    /*private void StringToListInteger(String str, List<Integer> list ){
        String[] arrayString = str.substring(1, str.length() - 1).split("\\s*,\\s*");
        for (String string : arrayString){
            try{
                list.add(Integer.parseInt(string));
            }catch (Exception e){
                System.out.println("Not integer");
            }

        }
    }*/

    public void display() throws Exception {

        String method;
        String class_name;
        String string;
        int type;

        Class Calc;
        Method serverMethod;
        Object calc_class;
        Object output = null;
        boolean continu = true;

        while (continu) {


            try {
                class_name = din.readUTF();
                System.out.println(class_name);

                method = din.readUTF();
                System.out.println(method);

                type = Integer.parseInt(din.readUTF());
                System.out.println(type);

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
                    serverMethod = calc_class.getClass().getMethod(method,List.class,int.class);
                    //go
                    System.out.println(din.readUTF());
                    //liste
                    string = din.readUTF();
                    System.out.println(string);

                    ArrayList<Object> list = new ArrayList<>();
                    TypeFunction tf = TypeManager.getTypeFunction(type);
                    tf.StringToList(string,list);

                    output = serverMethod.invoke(calc_class,list,type);



                } catch (IOException e) {
                    System.out.println("Exception Occurred:");
                    e.printStackTrace();
                }

                //Out: result + current time of server
                System.out.println("Result is : " + (output).toString());

                dout.writeUTF(output.toString());
                continu = false;

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}

