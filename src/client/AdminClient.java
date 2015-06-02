package client;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.rmi.*;
import server.LibraryServerInterface;

/**
 * <h1>Admin Client</h1>
 * <p>
 * 		This File is use for Admin to compare the Actual Admin from database 
 * 		and its seem like fecade of the Library System.
 * </p>
 * 
 * @version 1.0.0.0
 * @author Mitul
 */
public class AdminClient {
	
	
	private String EDU_Institution;
	
	
	/**
	 * getter function for getting the Education Institution
	 * @return 
	 */
	public String getEducationalInstitution()
	{
		return EDU_Institution;
		
		
	}
	
	/**
	 * Setter of the Educational Institution variable 
	 * @param EDU
	 * 		Contain the data of Education institution
	 */
	public void setEducationalInstitution(String EDU) {
		EDU_Institution= EDU;
	}
	
	
	//--------------------------------------------------------------------------
	/**
	 * Default constructor 
	 */
	public AdminClient(){}
	
	
	/**
	 * <h1>Parameter Constructor</h1> 
	 * @param EDU
	 * 		String Variable for Education Institution
	 */
	public AdminClient(String EDU) {
		EDU_Institution = EDU;
	}

	/**
	 * This Library System have 3 server 
	 * 		1.Concordia
	 * 		2.Vanier
	 * 		3.McGill 
	 * 		and this function help to find out the particular server name..	
	 * @param EDU
	 * @return
	 */
	public static String FindServer(String EDU){
		String s = EDU;
		String server;
		if(s.equals("Concordia"))
			server = "4444/Concordia";
		else if(s.equals("McGill"))
			server = "4446/McGill";
		else if(s.equals("UdeM"))
			server = "4448/UdeM";
		else
		{
			server = "server is not found";
		}
		return server;
	}
	
	
	/**
	 * <h1></h1>
	 * <p>This Function is for show the Result or
	 * update of the All Student status it display with all the data </p>
	 */
	public void Test_GetStudentStatus()
	{
		String result;
		try
		{
			String server = FindServer(this.getEducationalInstitution());
			LibraryServerInterface libraryserver= (LibraryServerInterface)Naming.lookup("rmi://localhost:"+server);
			result = libraryserver.getNonRetuners("admin","admin","Concordia","5");
			
			/*For Display the result*/
			if(result!=null)
			{
				System.out.println(result);
			}else
			{
				System.out.println("Result is Null!!!");
			}
		}catch(Exception e)
		{
			e.getMessage();
		}
		
		
	}
	
	
	/**
	 * <p>
	 * 		This function lists all the students� First Name, Last Name and Phone Number who have not returned
			the books with NumDays past their loan date in all the three educational institutions. The
			admin File is updated with this information. The admin can then pass that information
			to student to remind them about the book loan.
	 * </p>
	 * @param scan
	 */
	public void getNonRetuners(Scanner scan)
	{
		String input = null;
		String[] s=null;
		String result= null;
		boolean valid= false;
		System.out.println("Please Enter Admin Username And valid Password and Educational Institution and Number of days");
		
		while(!valid)
		{
			try{
				input = scan.nextLine();
			    s = input.split("\\s");
			    String server = FindServer(s[2]);
			    	if(server.equals("server is not found")) {
			    		System.out.println("server do not exsit!");
			    		continue;
			    	}else {
			    		LibraryServerInterface libraryserver= (LibraryServerInterface)Naming.lookup("rmi://132.205.45.212:"+server);		
			    		result = libraryserver.getNonRetuners(s[0], s[1], s[2], s[3]);
			    		
			    		log("Admin_"+server.substring(5)+".log",new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "Get Nonreturners: " + result+"\r\n");
			    		System.out.println(result); 
			    		valid = true;			
			    	}
			}catch(Exception e)
			{
					
			}
		}
	}
	
	/**
	 * This function save the log in file
	 * 
	 * @param filename
	 * 		  New File Name
	 * @param stream
	 * 		  information that want to write in new File
	 */
	public void log(String filename,String stream) {
			try{
					File f = new File(filename);
						if(!f.exists()) 
							f.createNewFile();
						    FileWriter fw = new FileWriter(f,true);
						    fw.write(stream);
						    fw.write("\r\n");
						    fw.flush();
						    fw.close();
				}catch(IOException e)
				{		
					e.getMessage();
				}
	}
	
	/**
	 * <p> This Function is useful for see the book information about who 
	 * took that book and plus remaining day left for borrow the books
	 * </p>
	 * 
	 * @param scan
	 */
	private void setDuration(Scanner scan) {
		// TODO Auto-generated method stub
		boolean valid = false;
		String input = null;
		String[] s = null;
		boolean result = false;
		System.out.println("This is Debug Tools");
		System.out.print("Please input Username Bookname EducationalInstitution and num_of_days");
		while(!valid){
			try{
			input = scan.nextLine();
		    s = input.split("\\s");
		    String server = FindServer(s[2]);
		    if(server.equals("server is not found"))
				{
					System.out.println("server do not exsit!");
					continue;
				}
		    else{
					LibraryServerInterface libraryserver= (LibraryServerInterface)Naming.lookup("rmi://132.205.45.212:"+server);
					result = libraryserver.setDuration(s[0], s[1], Integer.parseInt(s[3]));
					if(result==true)
						System.out.println("Set Duration succesfiul");
					else
						System.out.println("User name not exist");
					valid = true;
					break;
				}
			 }catch(Exception e){
				 System.out.println("error input!please try again!");
				 valid = false;
			 }
		}
	}
	
	
	/**
	 * Console Display Function 
	 */
	public static void DisplayMenuList()
	{
		System.out.println("=============================");
		System.out.println("Administrator Client:");
		System.out.println("=============================");
		System.out.println("Please select an Option......");
		System.out.println("1. Get Non Retuners ");
		System.out.println("2. Debug tools Set Duration");
		System.out.println("4. Exit");
	}
	
	
	//This is mendatory if need then we will make it together 
	/*
	public static void main(String[] args)
	{
		try
		{
			int userChoice = 0;
			AdminClient adminclient = new AdminClient();
			System.setSecurityManager(new RMISecurityManager());
			Scanner scan = new Scanner(System.in);
			Scanner scan2= new Scanner(System.in);
			DisplayMenuList();
			while(true)
			{
				Boolean Valid = false;
				while(!Valid)
				{
					try{
						userChoice=scan.nextInt();
						Valid=true;
					}catch(Exception e)
					{
						System.out.println("Invalid Input, please enter an Integer");
						Valid=false;
						scan.nextLine();
					}
				}
			}
			

			switch(userChoice)
			{
			case 1:
				adminclient.getNonRetuners(scan2);
				DisplayMenuList();
				break;
			case 2:
				adminclient.setDuration(scan2);
				DisplayMenuList();
				break;
			case 3:
				AdminClient admin_concordia = new AdminClient("Concordia");
				AdminClient admin_vanier = new AdminClient("Vanier");
				AdminClient admin_McGill = new AdminClient("McGill");
				admin_concordia.start();
				admin_vanier.start();
				admin_McGill.start();
				break;
			case 4:
				System.out.println("Have a nice day");
				System.in.close();
				System.exit(0);
			default:
				System.out.println("input is 1-4");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
	}

	
	*/
	
	
	/**
	 * void main Function
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			AdminClient adminclient = new AdminClient();
			System.setSecurityManager(new RMISecurityManager());		
			int userChoice=0;
			Scanner scan =  new Scanner(System.in);
			Scanner scan2 = new Scanner(System.in);
			DisplayMenuList();
		while(true)
		{
			Boolean valid = false;
				
			while(!valid)
			{
				try{
						userChoice=scan.nextInt();
						valid=true;
					}
					catch(Exception e)
					{
						System.out.println("Invalid Input, please enter an Integer");
						valid=false;
						scan.nextLine();
					}
			}
			
			switch(userChoice)
			{
			case 1:
				adminclient.getNonRetuners(scan2);
				DisplayMenuList();
				break;
			case 2:
				adminclient.setDuration(scan2);
				DisplayMenuList();
				break;
			case 3:
				AdminClient admin_concordia = new AdminClient("concordia");
				AdminClient admin_vanier = new AdminClient("vanier");
				AdminClient admin_McGill = new AdminClient("McGill");
				admin_concordia.Start();
				admin_vanier.Start();
				admin_McGill.Start();
				break;
			case 4:
				System.out.println("Have a nice day");
				System.in.close();
				System.exit(0);
			default:
				System.out.println("input is 1-4");
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	/**
	 * This function start the student function 
	 * 
	 * @link AdminClient.Test_GetStudentStatus()
	 */
	public void Start()
	{
		Test_GetStudentStatus();
	}
	
	
	
	
	
	
}
