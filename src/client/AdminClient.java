package client;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private String AdminClientName;
	private String AdminPasword;
	private String EDU_Institution;
	private int NumberofDay;
	
	
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
	 */
	
	public AdminClient(String EDU) {
		EDU_Institution = EDU;
	}

	public static String FindServer(String EDU){
		String s = EDU;
		String server;
		if(s.equals("Concordia"))
			server = "2020/Concordia-server";
		else if(s.equals("Vanier"))
			server = "2021/Vanier-server";
		else if(s.equals("Dawson"))
			server = "2022/Dawson-server";
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
			result = libraryserver.getNonRetuners("admin","admin");
			
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
	
	
	public void getNonRetuners(Scanner scan)
	{
		String input = null;
		String[] s=null;
		String result= null;
		boolean valid= false;
		System.out.println("Please Enter Admin Username And valid Password and Educational Institution ");
		
		while(!valid)
		{
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
			    				LibraryServerInterface libraryserver= (LibraryServerInterface)Naming.lookup("rmi://localhost:"+server);
			    				result = libraryserver.getNonRetuners(s[0], s[1]);
			    				log("Admin_"+server.substring(5)+".log",result+"\r\n" +new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			    				System.out.println(result); 
			    				valid = true;
			    				break;
			    			}
			}catch(Exception e)
			{
					
			}
		}
	}
	
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
	
	public void run()
	{
		Test_GetStudentStatus();
	}
	
	
	
	
	
	
}
