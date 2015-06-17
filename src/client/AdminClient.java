package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Scanner;

import models.Student;

import org.omg.CORBA.ORB;

import corbaLibrary.CorbaLibraryServer;
import corbaLibrary.CorbaLibraryServerHelper;

/**
 * @author Ankurp
 *
 */
public class AdminClient {
	 
	private Scanner scan;
	private Student student;
	private CorbaLibraryServer server;
	private FileWriter myWriter;
	private File clientLogFolder;
	private ORB orb;
	private BufferedReader reader;
	private org.omg.CORBA.Object corbaObject;
	private String instituteName =null;
	private static AdminClient admin =null;
	
	public AdminClient(String institution, ORB orb) throws IOException{
		scan = new Scanner(System.in);
		this.orb = orb;
    
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(getServer(institution)));
        	String ior = reader.readLine();
        	reader.close();
        	corbaObject = orb.string_to_object(ior);
        	server = CorbaLibraryServerHelper.narrow(corbaObject);
      	   } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	  
        	this.instituteName = institution;
        	clientLogFolder = new File("clientLog");
        	if(!clientLogFolder.exists()) clientLogFolder.mkdir();  
	}
	
	public void showMenu(){
		
	}
	public void getNonreturners(){
		
	}
	public void setDuration(){
		
	}
	public String getServer(String institute){

		String instituteName = institute;
		String server;
		if (instituteName.equals("Concordia"))
			server = "ior/ior_Concordia.txt";
		else if (instituteName.equals("McGill"))
			server = "ior/ior_McGill.txt";
		else if (instituteName.equals("UdeM"))
			server = "ior/ior_UdeM.txt";
		else{
			server = "server is not found";
		}
		return server;
	}
	
	public void setlogger(String logInfo, String fileName) throws IOException{
		File clientFile = new File("clientLog",fileName); 
	   	 if(!clientFile.exists()) clientFile.createNewFile();
	   	 myWriter = new FileWriter(clientFile,true);
	   	 myWriter.write(logInfo+"\n");
	   	 myWriter.flush();
	   	 myWriter.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int choice = 0;
        boolean valid = false;
        Scanner scan = new Scanner(System.in);
	  		
        System.out.println("WELCOME TO ONLINE LIBRARY SYSTEM");
        System.out.println();
        System.out.println("Select your instution:");
        System.out.println();
        System.out.println("1- Concordia");
        System.out.println("2- McGill");
        System.out.println("3- UdeM");
        System.out.println("4- Exit");
        System.out.println("\n Enter Your Choice: " );
		
        AdminClient admin = null;
        ORB orb = ORB.init(args,null);
    
        while (!valid) {
        	try{
        		choice = scan.nextInt();
        	switch (choice) {
        		case 1:	admin  = new AdminClient("Concordia",orb);
                       		valid = true; break;
                	case 2: admin  = new AdminClient("McGill",orb);
                       		valid = true; break;
                	case 3:	admin  = new AdminClient("Udem",orb);
                       		valid = true; break;
                	case 4: System.out.println("Exited"); 
				System.exit(1); break;       
                	default:System.out.println("please choose options 1, 2 or 3 only!!");
                       		valid = false;
                }
             }
             catch (Exception e) {
                System.out.println("Invalid input!!! Please enter an integer");
                valid = false;
             }
        }
        admin.showMenu();
	}

}
