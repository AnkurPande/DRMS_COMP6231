package client;

import models.*;
import server.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.net.MalformedURLException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.RMISecurityManager;
//import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

import corbaLibrary.CorbaLibraryServerHelper;
import corbaLibrary.CorbaLibraryServerOperations;

/**
 * <h1>Student Client</h1>
 * <p>
 * 		This class represents the student view of the library system.
 * </p>
 * 
 * @version 1.0.0.0
 * @author Eric Watat Lowe
 */

public class StudentClient 
{
   private Scanner scan;
   private Student student;
   private CorbaLibraryServerOperations server;
   private FileWriter myWriter;
   private File clientLogFolder;
   private ORB orb;
   private BufferedReader reader;
   private org.omg.CORBA.Object corbaObject;
   
   private static StudentClient aStudent = null;
   
   /**
	 * <h1>Parameter Constructor</h1> 
	 * @param institution
	 * 		String Variable for Educational Institution
	 */
   public StudentClient(String institution, ORB orb) throws IOException
   {
          scan = new Scanner(System.in);
          student = new Student();
          this.orb = orb;
      
          try {
          	reader = new BufferedReader(new FileReader(findServer(institution)));
          	String ior = reader.readLine();
          	reader.close();
          	corbaObject = orb.string_to_object(ior);
          	
          	server = CorbaLibraryServerHelper.narrow(corbaObject);
          	
	        //server = (LibraryServerInterface) Naming.lookup("rmi://localhost:"+ findServer(institution));
	  } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  
          student.setEducationalIns(institution);
          clientLogFolder = new File("clientLog");
          if(!clientLogFolder.exists()) clientLogFolder.mkdir();  
   }
   
   public StudentClient() {
	   
   }
   
   /**
	 * Finds the right server to connect to
	 * @param edu
	 */
   public static String findServer(String edu){
	
	String s = edu;
	String server;
	if (s.equals("Concordia"))
		server = "ior/ior_Concordia.txt";
	else if (s.equals("McGill"))
		server = "ior/ior_McGill.txt";
	else if (s.equals("UdeM"))
		server = "ior/ior_UdeM.txt";
	else{
		server = "server is not found";
	}
		
	return server;
	
    }
  
   
   /**
	 * <h1></h1>
	 * <p>This Function collects the username and password of theuser
	 *     and checks if the meet the requirements</p>
	 */
   public boolean validateCredentials()
   {
         String userName;
         String password;
      
      
         do {
         	
             //gets username from the consol
             System.out.print("UserName: ");
             userName = scan.next();
             System.out.println();         
             //gets password from the consol
             System.out.print("Password: ");
             password = scan.next();
             System.out.println();
         
             //inputs validation error messages
             if(userName.length() < 6 && userName.length() > 0) System.out.println("The user name is too short!!!");
             if(userName.length() > 15 && userName.length() > 0) System.out.println("The user name is too long!!!");
             if(password.length() < 8) System.out.println("The password is too short!!!");
             if(userName.length() == 0 && password.length() == 0) System.out.println("The fields are empty!!!");
             if(userName.length() == 0) System.out.println("The user name field is empty!!!");
             if(password.length() == 0) System.out.println("The password field is empty!!!");
        
        }while(userName.length() < 6 || userName.length() > 15 || password.length() < 8);
     
      	student.setUserName(userName);
      	student.setPassword(password);
      
      	return true;
      
   }
   
   /**
	 * This function keeps a record of all the activities of a Student
	 * @param fileName, logInfo
	 */
   public void logFile(String fileName, String logInfo) throws IOException {
   	
   	 File clientFile = new File("clientLog",fileName); 
   	 if(!clientFile.exists()) clientFile.createNewFile();
   	
   	 myWriter = new FileWriter(clientFile,true);
   	 myWriter.write(logInfo+"\n");
   	 myWriter.flush();
   	 myWriter.close();
   }
   
   /**
	 * <h1></h1>
	 * <p>This Function build the GUI of the user</p>
	 */
   public void showMenu() {
	   
	 System.out.println();
         System.out.println("Choose an option:");
         System.out.println();
         System.out.println("1- Create account.");
         System.out.println("2- Reserve a Book.");
         System.out.println("3- Reserve a Book Inter Library.");
         System.out.println("4- Exit");
       
         boolean valid = false;
         int choice = 0;
       
         while (!valid) {
               try {
                     choice = scan.nextInt();
                     switch (choice) {
                                       case 1:
              	                              aStudent.validateCredentials();
                                              aStudent.createAccount();
                                              valid = true;
                                              break;
                                       
                                       case 2:
               	                              aStudent.validateCredentials();
                                              aStudent.reserveBook();
                                              valid = true;
                                              break;
                                              
                                       case 3:
               	                              aStudent.validateCredentials();
                                              aStudent.reserveInterLibrary();
                                              valid = true;
                                              break;
                
                                       case 4:
                                              System.out.println("Thank you for visiting the online Library!!");
                                              System.out.println("See you soon!!");
                                              System.in.close();
	                                      System.exit(0);
                                              valid = true;
                                              break;
          
                                       default:
                                              System.out.println("please choose options 1, 2 or 3 only!!");
                                              valid = false;
                        }
                }
                catch (Exception e) {
                      System.out.println("Invalid input!!! Please enter an integer");
                }
        }
   }
   
   /**
	 * <h1></h1>
	 * <p>This Function create an account for a student on the Library server
	 *    of the institution where the student is registered</p>
	 */
   public boolean createAccount() throws IOException {
     
          String fname, lname,email,phone;
          
          do {
          	
                //gets first name from the consol
                System.out.print("First Name: ");
                fname = scan.next();
                System.out.println();
        
                //gets last name from the consol 
                System.out.print("Last Name: ");
                lname = scan.next();
                System.out.println();
        
                //gets email from the consol
                System.out.print("Email: ");
                email = scan.next();
                System.out.println();
        
                //gets phone number from the consol 
                System.out.print("Phone Number: ");
                phone = scan.next();
                System.out.println();
        
                //inputs validation error mesages
                if(fname.isEmpty()) System.out.println("The first name field is empty!!!");
                if(lname.isEmpty()) System.out.println("The last name field is empty!!!");
                if(email.isEmpty()) System.out.println("The email fields is empty!!!");
                if(phone.isEmpty()) System.out.println("The phone number field is empty!!!");
                if((fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty())) 
                    System.out.println("Try again!!!");
         }
         while(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty());
      
         student.setFirstName(fname); 
         student.setLastName(lname); 
         student.setEmailAddress(email); 
         student.setPhoneNumber(phone); 
      
         //calls the remote method on the server to actually create the account
         
      
         if(server.createAccount(student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getPhoneNumber(), student.getUserName(), student.getPassword(), student.getEducationalIns())) 
         {
              System.out.println("Account Created Successfully!!!");
              String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Account created for user: " + student.getUserName() + " on server: "
                                + student.getEducationalIns();
              logFile(student.getUserName(),logInfo);
              aStudent.showMenu();
              return true;
         }
         else {
             System.out.println("we are here");

                //TODO get an error message from the server
                return false;
        }
   }
   
   /**
	 * <h1></h1>
	 * <p>This Function reserve a book for a registered student</p>
	 */
   public void reserveBook() throws IOException {
	  
	  Book aBook = selectBook();
	  
	  if(server.reserveBook(student.getUserName(),student.getPassword(),aBook.getBookName(),aBook.getBookAuthor())) {
		  System.out.println("Reserve Success");
	  } else {
		  System.out.println("Reserve Fail");
	  }
      
          showMenu();
                     
   }
   
   /**
	 * <h1></h1>
	 * <p>This Function enables the user to select a book to reserve</p>
	 */
   public Book selectBook() {
   	
   	 String bookName, authorName;
	   
	      do {
	           //gets first name from the consol
	           System.out.print("Book Name: ");
	           bookName = scan.next();
	           System.out.println();
	        
	           //gets last name from the consol 
	           System.out.print("Author Name: ");
	           authorName = scan.next();
	           System.out.println();
	        
	 
	        
	           //inputs validation error mesages
	           if(bookName.isEmpty()) System.out.println("The Book name field is empty!!!");
	           if(authorName.isEmpty()) System.out.println("The Author name field is empty!!!");
	           if((bookName.isEmpty() || authorName.isEmpty())) System.out.println("Try again!!!");
	           
	      }
	      while(bookName.isEmpty() || authorName.isEmpty());
	      
	      Book aBook = new Book();
	      aBook.setBookName(bookName);
	      aBook.setBookAuthor(authorName);
	      
	      return aBook;
   	
   }
   
   
    /**
	 * <h1></h1>
	 * <p>This Function reserve a book for a registered student</p>
	 */
   public void reserveInterLibrary() throws IOException {
     
	  Book aBook = selectBook();
	  
	  if(server.reserveInterLibrary(student.getUserName(),student.getPassword(),aBook.getBookName(),aBook.getBookAuthor())) {
		  System.out.println("Reserve Success");
	  } else {
		  System.out.println("Reserve Fail");
	  }
      
         showMenu();
                     
   }
   
   /**
	 * <h1></h1>
	 * <p>This is Where the execution of the program starts</p>
	 */
   public static void main(String args[]) throws IOException {
   	
	 // System.setSecurityManager(new RMISecurityManager());
          System.out.println("WELCOME TO ONLINE LIBRARY SYSTEM");
          System.out.println();
          System.out.println("Select your instution:");
          System.out.println();
          System.out.println("1- Concordia");
          System.out.println("2- McGill");
          System.out.println("3- UdeM");
      
          int choice = 0;
          boolean valid = false;
          
         // @SuppressWarnings("resource")
	  Scanner scan = new Scanner(System.in);
	  
	  ORB orb = ORB.init(args,null);
      
          while (!valid) {
          	
                           try {

                                  choice = scan.nextInt();
                                  
                                  switch (choice) {
             
                                                     case 1:
                                                           aStudent = new StudentClient("Concordia",orb);
                                                           valid = true;
                                                           break;
                  
                                                     case 2:
                                                           aStudent = new StudentClient("McGill",orb);
                                                           valid = true;
                                                           break;
                  
                                                     case 3:
                                                           aStudent = new StudentClient("UdeM",orb);
                                                           valid = true;
                                                           break;
                  
                                                     default:
                                                            System.out.println("please choose options 1, 2 or 3 only!!");
                                                            valid = false;
                                                }
                                }
                                catch (Exception e) {
                                                      System.out.println("Invalid input!!! Please enter an integer");
                                                      valid = false;
                                	
                                }
                        }
      
        aStudent.showMenu();
       
   }
   
   public void demoCreateAccount(String firstName, String lastName, String emailAddress,
			String phoneNumber, String username, String password, String eduInstitution) {
	   try {
		server.createAccount(firstName, lastName, emailAddress, phoneNumber, username, password, eduInstitution);
	   } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
   }
   
   public void demoReserveBook(String username, String password, String bookName, String authorName) {
	   try {
		server.reserveBook(username, password, bookName, authorName);
	   } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   
   }
}
