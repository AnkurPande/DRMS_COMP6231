package client;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Ankurp
 *
 */
public class AdminClient {
	 
	private Scanner scan;
	
	private FileWriter myWriter;
	private File clientLogFolder;
	private String instituteName =null;
	private static AdminClient admin;
	private String userName , password, bookName,numOfDays;
	private Client client;
	private WebResource resource;

	
	/**
	 * @param institution
	 * @param orb
	 * @throws IOException
	 * Initialize the servers.
	 */
	public AdminClient(String institution) throws Exception{
		
		scan = new Scanner(System.in);
		
		try {
		  
			client = Client.create();
        	
      } catch (Exception e) {
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
        		case 1:	admin.getNonReturners(true);
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
	public boolean getNonReturners(boolean test) throws IOException{
		
		System.out.println("Enter Username: ");
		this.userName =scan.next();
        System.out.println("Enter Password: ");
        this.password =scan.next();
        System.out.println("Enter No Of Days: ");
        this.numOfDays = scan.next();
        
        resource = client.resource("http://" + getServer(instituteName) + "/get-non-returners/"+ this.userName +"-"+ this.password + "-" + this.instituteName + "-"+ this.numOfDays);
        ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
        
     
        if(response.getStatus() == 200){
        	
        	System.out.println(response.getEntity(String.class));
       
        	String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
            		+ "Non Returners : Success";
            setLogger(logInfo, this.userName);
            if (test) admin.showMenu();       
            return true;
        	
        }
        else{
        	
        	String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
            		+ "Non Returners : Failed";
            setLogger(logInfo, this.userName);
        }
        
        admin.showMenu();
        return false;
	}
	
	/**
	 * @throws IOException
	 * This method is used to set duration for the books in the library.
	 */
	public boolean setDuration() throws Exception{
		System.out.println("Enter Username: ");
		this.userName =scan.next();
        System.out.println("Enter Book Name: ");
        this.bookName =scan.next();
        System.out.println("Enter No Of Days: ");
        this.numOfDays = scan.next();
        
        resource = client.resource("http://" + getServer(instituteName) + "/set-duration/"+ userName +"-"+ bookName + "-" + Integer.parseInt(numOfDays));
        ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
        
     
        if(response.getStatus() == 200){
        	System.out.println("Duration set successfully!!!");
            String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                              + " Duration set successfully for : " + this.bookName + " on server: "
                              + this.instituteName;
            setLogger(logInfo, this.userName);
            
            return true;
        }
        else {
        	System.out.println("Duration set unsuccessfull!!!");        	
        }
        admin.showMenu();
        return false;
	}
	
	/**
	 * @param institute
	 * @return library server instance
	 */
	public String getServer(String edu){

		String s = edu;
		String server;
		if (s.equals("Concordia"))
			server = "localhost:8080/RestTestApp/library/library-service-concordia";
		else if (s.equals("McGill"))
			server = "localhost:8080/RestTestApp/library/library-service-mcgill";
		else if (s.equals("UdeM"))
			server = "localhost:8080/RestTestApp/library/library-service-udem";
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
    
        while (!valid) {
        	try{
        		choice = scan.nextInt();
        		switch (choice) {
        		case 1:	admin  = new AdminClient("Concordia");
                       	valid = true; break;
                case 2: admin  = new AdminClient("McGill");
                       	valid = true; break;
                case 3:	admin  = new AdminClient("UdeM");
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
