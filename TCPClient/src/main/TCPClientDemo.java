package main;

import java.io.*;
import java.net.Socket;

public class TCPClientDemo 
{

	public static void main(String[] args) throws Exception
	{
		if (args.length == 2)
		{	
			TCPClient client = new TCPClient(args[0], Integer.parseInt(args[1]));
			client.display();
			
		}
		else
		{
			System.out.print("Invalid parameters ");
			return;
		}
	}
}


class TCPClient
{
    Socket ClientSoc;
   
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
    File calc = new File("src\\main\\Calc.java");
    File calc_class = new File("out\\production\\TCPClient\\main\\Calc.class");

    TCPClient(String hostName, int port)
    {
        try
        {
            ClientSoc = new Socket(hostName,port);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
	}
    
    
    public void display() throws Exception
    {
    	String msgFromServer;
    	String a;
    	String b;
    	String method;
    	String class_name;
    	Integer choice;
    	
        while(true)
        {
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
            choice=Integer.parseInt(br.readLine());
            dout.writeUTF(choice.toString());
            dout.writeUTF(class_name);
            dout.writeUTF(method);
            dout.writeUTF(a);
            dout.writeUTF(b);

            switch (choice) {
                case 1:
                    System.out.println("[Client] SourceColl Mod");

                    //Send Calc to server...
                    ByteStream.toStream(dout, calc);

                    // Wait for the response from server...
                    msgFromServer=din.readUTF();
                    System.out.print("[Server] Result: " + msgFromServer + "\n");
                    break;
                case 2:
                    System.out.println("[Client] ByteColl Mod");

                    //Send the message to server...
                    ByteStream.toStream(dout, calc_class);

                    // Wait for the response from server...
                    msgFromServer=din.readUTF();
                    System.out.print("[Server] Result: " + msgFromServer + "\n");
                    break;
                case 3:
                    System.out.println("[Client] RMI Mod");

                    // RMI Step : invoke method 'add' from server
                    Calculate c = (Calculate) java.rmi.Naming.lookup("rmi://localhost:10000/MaCalc");
                    Integer out = c.add(a,b);

                    // Wait for the response from server...
                    msgFromServer=din.readUTF();
                    System.out.print("[Server] Result: " + out + "  " + msgFromServer + "\n");
                    break;
                case 4:
                    dout.close();
                    System.exit(1);
                default:
                    break;
            }
        }
    }
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    
}

