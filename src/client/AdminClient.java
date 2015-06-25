package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.omg.CORBA.ORB;
import corbaLibrary.CorbaLibraryServer;
import corbaLibrary.CorbaLibraryServerHelper;

/**
 * @author Ankurp
 *
 */
public class AdminClient {
	 
	private Scanner scan;
	private CorbaLibraryServer server;
	private FileWriter myWriter;
	private File clientLogFolder;
	private org.omg.CORBA.Object corbaObject;
	private String instituteName =null;
	private static AdminClient admin;
	private String userName , password, bookName,numOfDays;
	
	/**
	 * @param institution
	 * @param orb
	 * @throws IOException
	 * Initialize the servers.
	 */
	public AdminClient(String institution, ORB orb) throws IOException{
		scan = new Scanner(System.in);
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
	
	/**
	 * Shows the options available for user.
	 */
	public void showMenu(){
		boolean valid = false;
	    int choice = 0;
		
	    System.out.println("Choose an option:");
        System.out.println();
        System.out.println("1- Get Non Returners.");
        System.out.println("2- Set Duration.");
        System.out.println("3- Exit");
           
        while (!valid){
        	try {
        		choice = scan.nextInt();
        		switch (choice) {
        		case 1:	admin.getNonReturners();
        				valid = true;	break;
        		case 2:	admin.setDuration();
        				valid = true;   break;
                case 3: System.out.println("Thank you for visiting the online Library!!");
                		System.out.println("See you soon!!");
                		System.in.close();
                		System.exit(0);
                		valid = true;	break;	
                default: System.out.println("please choose options 1, 2 or 3 only!!");
                         valid = false;
        		}
        	}
        	catch (Exception e) {
        		System.out.println("Invalid input!!! Please enter an integer");
        	}
       }
	}
	
	/**
	 * @throws IOException
	 * The list of non returner students is displayed .
	 */
	public void getNonReturners() throws IOException{
		String result;
		System.out.println("Enter Username: ");
		this.userName =scan.next();
        System.out.println("Enter Password: ");
        this.password =scan.next();
        System.out.println("Enter No Of Days: ");
        this.numOfDays = scan.next();
        
        
        result =server.getNonRetuners(this.userName, this.password, this.instituteName, this.numOfDays);
        
        System.out.println(result);
        String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                		+ "Non Returners : " +result;
        setLogger(logInfo, this.userName);
        admin.showMenu();
	}
	
	/**
	 * @throws IOException
	 * This method is used to set duration for the books in the library.
	 */
	public void setDuration() throws IOException{
		System.out.println("Enter Username: ");
		this.userName =scan.next();
        System.out.println("Enter Book Name: ");
        this.bookName =scan.next();
        System.out.println("Enter No Of Days: ");
        this.numOfDays = scan.next();
        
        if(server.setDuration(userName, bookName,Integer.parseInt(numOfDays))){
        	System.out.println("Duration set successfully!!!");
            String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                              + " Duration set successfully for : " + this.bookName + " on server: "
                              + this.instituteName;
            setLogger(logInfo, this.userName);
        }
        else {
        	System.out.println("Duration set unsuccessfull!!!");        	
        }
        admin.showMenu();
	}
	
	/**
	 * @param institute
	 * @return library server instance
	 */
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
	
	/**
	 * @param logInfo
	 * @param fileName
	 * @throws IOException
	 */
	public void setLogger(String logInfo, String fileName) throws IOException{
		File adminClientFile = new File("clientLog",fileName); 
	   	 if(!adminClientFile.exists()) adminClientFile.createNewFile();
	   	 myWriter = new FileWriter(adminClientFile,true);
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
        @SuppressWarnings("resource")
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
		
        admin = null;
        ORB orb = ORB.init(args,null);
    
        while (!valid) {
        	try{
        		choice = scan.nextInt();
        		switch (choice) {
        		case 1:	admin  = new AdminClient("Concordia",orb);
                       	valid = true; break;
                case 2: admin  = new AdminClient("McGill",orb);
                       	valid = true; break;
                case 3:	admin  = new AdminClient("UdeM",orb);
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
