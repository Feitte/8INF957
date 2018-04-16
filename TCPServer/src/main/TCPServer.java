package main;

import java.lang.reflect.Method;
import java.net.*;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.util.List;


public class TCPServer
{

	public static void main(String args[]) throws Exception
    {
		Socket clientSoc;
		ServerSocket serverSoc;

		String class_name;
		String method;

	   	int portNumber;
	   	
		if (args.length == 1)
		{	
			portNumber = Integer.parseInt(args[0]);
			serverSoc = new ServerSocket(portNumber);
							
		}
		else
		{
			System.out.print("Invalid parameters ");
			return;
		} 
		
		System.out.println("\nTCP Server Started on Port Number: " + portNumber);

		// RMI Configuration
        try {
            // Set codebase
            File f1= new File ("bin");
            String codeBase=f1.getAbsoluteFile().toURI().toURL().toString();
            System.setProperty("java.rmi.server.codebase", codeBase);

            // Set a port and bind
            LocateRegistry.createRegistry(10000);
            CalculateImpl c = new CalculateImpl();
            java.rmi.Naming.rebind("rmi://localhost:10000/MaCalc", c);
            System.out.println("RMI configuration done");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\n\n[ DISPLAY ]");
            System.out.println("Enter Class and Method names:");
            class_name = br.readLine();
            method = br.readLine();

            Integer[] tab = {0,1,2,3,4,5,6} ;




            while(true)
            {
                System.out.println("Waiting for Connection ...");
                clientSoc = serverSoc.accept();
                TCPServerThread serverThread = new TCPServerThread(clientSoc, class_name,method,tab);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}


class TCPServerThread extends Thread
{
    static int numClient;
    int idClient;

    Integer[] tab;

    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    String class_name;
    String method;

    TCPServerThread(Socket soc, String class_name, String method, Integer[] tab)
    {
        try
        {
            ClientSoc = soc;
            
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader((new InputStreamReader(System.in)));
            this.class_name = class_name;
            this.method=method;
            this.tab = tab;


            System.out.println("\nTCP Client Connected ...");
            this.idClient = numClient;
            numClient++;
            start();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
        
        
    @Override
    public void run()
    {

        ArrayList<Integer> list = new ArrayList<Integer>();
    	Class Calc;
        Object calc_class;
        Object output = null;
        Method add;
        String msg;
        String a;
        String b;
        String method;
        String class_name;
        String msgFromSlave ;


        System.out.println(numClient);



        //début Copier Coller

        File calc = new File("src\\main\\Calc.java");

        try {


            dout.writeUTF(this.class_name);
            dout.writeUTF(this.method);

            ByteStream.toStream(dout, calc);

            // Wait for the response from server...
            //msgFromSlave = din.readUTF();
            //System.out.print("[Server] Result: " + msgFromSlave + "\n");

            if (br.readLine().equals("go")) {
                list = myMethod.arrayDivider(tab, numClient)[idClient];
                for(Integer i : list){
                    System.out.println(i);
                }
                dout.writeUTF(list.toString());
                dout.writeUTF("go");
            }

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            numClient--;
            System.out.println(this.idClient);
            //e.printStackTrace();
        }








        System.out.println("[Client] SourceColl Mod");

        //Send main.Calc to server...


        //fin copier coller




/*

        while(true)
        {
            try
            {
                // Read parameters from client
                msg = din.readUTF();
                int mod=Integer.parseInt(msg);
                class_name = din.readUTF();
                method = din.readUTF();
                a = din.readUTF();
                b = din.readUTF();
                System.out.println("Client wants to " + method + "(" + a + "," + b + ")");

                switch (mod) {
                    case 1:
                        System.out.println("----> SourceColl Mod");

                        // Create a new source file if it doesn't exist
                        //File calc = new File("src\\main\\" + class_name + ".java");
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

                        dout.writeUTF(list.toString());
                        //dout.writeUTF(output.toString() + "  [" + (new Date().toString()) + "]");

                        break;
                    case 2:
                        System.out.println("----> ByteColl Mod");

                        // Create a new bytecode file if it doesn't exist
                        File calc_comp = new File("out\\production\\Master\\main\\" + class_name + ".class");
                        try {
                            boolean isNewFile = calc_comp.createNewFile();
                            if (isNewFile) {
                                System.out.println("A bytecode file was created");
                            } else {
                                System.out.println("Bytecode file already exist, no need to create a new one");
                            }
                            // Copy data received from client to this new file
                            ByteStream.toFile(din, calc_comp);

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
                        dout.writeUTF(output.toString() + "  [" + (new Date().toString()) + "]");
                        break;
                    case 3:
                        System.out.println("----> RMI Mod");

                        //Out: current time of server
                        dout.writeUTF("[" + (new Date().toString()) + "]");
                        break;
                }
            }

        }
        */
    }


    // Create a new process to compile source file
    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
        System.out.println("Compilation exitValue() : " + pro.exitValue());
    }
}

