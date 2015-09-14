package client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.Date;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.File;
import model.*;



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
   private FileWriter myWriter;
   private File clientLogFolder;
   private Client client;
   private String serverLocation;
   private WebResource resource;
   
  
   
   private static StudentClient aStudent = null;
   
   /**
	 * <h1>Parameter Constructor</h1> 
	 * @param institution
	 * 		String Variable for Educational Institution
	 */
   public StudentClient(String institution) 
   {
          scan = new Scanner(System.in);
          student = new Student();
          serverLocation = findServer(institution); 
          
          try {
        	  
        	  client = Client.create(); 
        	  
	  } catch (Exception e) {
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
	 * <h1></h1>
	 * <p>This Function collects the user name and password of the user
	 *     and checks if the meet the requirements</p>
	 */
   public boolean validateCredentials()
   {
         String userName;
         String password;
         	
        do{
        	
        	//gets user name from the console
            System.out.print("UserName: ");
            userName = scan.next();
            System.out.println();         
            //gets password from the console
            System.out.print("Password: ");
            password = scan.next();
            System.out.println();     
        	
        }while(userName.isEmpty() || password.isEmpty());
     
      	student.setUserName(userName);
      	student.setPassword(password);
      
      	return true;
      
   }
   
   /**
	 * This function keeps a record of all the activities of a Student
	 * @param fileName, logInfo
	 */
   public void logFile(String fileName, String logInfo) {
   	 try {
   		 
   		File clientFile = new File("clientLog",fileName); 
      	 if(!clientFile.exists()) clientFile.createNewFile();
      	
      	 myWriter = new FileWriter(clientFile,true);
      	 myWriter.write(logInfo+"\n");
      	 myWriter.flush();
      	 myWriter.close();
      	 
   	 } catch (IOException e) {
   		 e.printStackTrace();
   	 }
   	 
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
                                              aStudent.createAccount(false);
                                              valid = true;
                                              break;
                                       
                                       case 2:
               	                              aStudent.validateCredentials();
                                              aStudent.reserveBook(false);
                                              valid = true;
                                              break;
                                              
                                       case 3:
               	                              aStudent.validateCredentials();
                                              aStudent.reserveInterLibrary(false);
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
   public boolean createAccount(boolean test) {
     
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
         
         resource = client.resource("http://" + serverLocation +"/create-account/"+ 
                                     student.getFirstName() +"-"+ student.getLastName() + "-" + student.getEmailAddress() + "-"+
        		                     student.getPhoneNumber() + "-"+ student.getUserName() + "-"+ student.getPassword() + "-"+ 
                                     student.getEducationalIns());
         
         ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
         
      
         if(response.getStatus() == 200) 
         {
              if(response.getEntity(String.class).equalsIgnoreCase("true")) {
            	  
                  System.out.println("Account Created Successfully!!!");
                  
                  String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                          + " Account created for user: " + student.getUserName() + " on server: "
                          + student.getEducationalIns();
                  
                  logFile(student.getUserName(),logInfo);
              } else {
            	  System.out.println("Create account failed");
              }
              
              if (test){
    			  System.out.println("from Server: " + student.getEducationalIns());
    			  System.out.println();
    		  }
              
             
              if(!test) showMenu();
              return true;
              
         }
         else {
                //TODO get an error message from the server
        	    if(!test) showMenu();
                return false;
        }
   }
   
   public Student getStudent() {
	return student;
}

public void setStudent(Student student) {
	this.student = student;
}

/**
	 * <h1></h1>
	 * <p>This Function reserve a book for a registered student</p>
	 */
   public boolean reserveBook(boolean test) throws Exception {
	  
	   Book aBook = selectBook();
		  
		  resource = client.resource("http://" + serverLocation +"/reserve-book/"+ student.getUserName() +"-"+ student.getPassword() + "-" + aBook.getBookName() + "-"+ aBook.getBookAuthor());
	      ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
		  
		  if(response.getStatus() == 200) {
			  
			  if(response.getEntity(String.class).equalsIgnoreCase("true")) {
            	  
				  System.out.println("Reserve Success");
                  
				  String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
		                  + " Book: "+ aBook.getBookName()+", Reserved for user: " + student.getUserName() + " on server: "
		                  + student.getEducationalIns();
		          logFile(student.getUserName(),logInfo);
		          
              } else {
            	  System.out.println("Reserve book failed");
              }
			  
			  if (test){
				  System.out.println("from Server: " + student.getEducationalIns());
				  System.out.println();
			  }
			  
			  
	          if(!test) showMenu();
	          
	          return true;
		  } else {
			  System.out.println("Reserve Fail");
			  if(!test) showMenu();
			  return false;
		  }
                     
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
   public boolean reserveInterLibrary(boolean test) throws Exception {
     
	  Book aBook = selectBook();
	  
	  resource = client.resource("http://" + serverLocation +"/reserve-book-inter-library/"+ student.getUserName() +"-"+ student.getPassword() + "-" + aBook.getBookName() + "-"+ aBook.getBookAuthor());
      ClientResponse response = resource.accept("text/plain").get(ClientResponse.class);
	  
	  if(response.getStatus() == 200) {
		  
		  if(response.getEntity(String.class).equalsIgnoreCase("true")) {
        	  
			  System.out.println("Reserve Success");
              
			  String logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
	                  + " Book: "+ aBook.getBookName()+", Reserved for user: " + student.getUserName() + " on server: "
	                  + student.getEducationalIns();
	          logFile(student.getUserName(),logInfo);
	          
          } else {
        	  System.out.println("Reserve book failed");
          }
		  
		  if (test){
			  System.out.println("from Server: " + student.getEducationalIns());
			  System.out.println();
		  }
		  
		  if (test){
			  System.out.println("from Server: " + student.getEducationalIns());
			  System.out.println();
		  }
		  
		  
          if(!test) showMenu();
          
          return true;
	  } else {
		  System.out.println("Reserve Fail");
		  if(!test) showMenu();
		  return false;
	  }
                     
   }
   
   /**
	 * <h1></h1>
	 * <p>This is Where the execution of the program starts</p>
 * @throws Exception 
	 */
   public static void main(String args[]) throws Exception {
	   
	   aStudent = new StudentClient("Concordia");
   	
	 // System.setSecurityManager(new RMISecurityManager());
         System.out.println("WELCOME TO ONLINE LIBRARY SYSTEM");
          
      
        
                
              
        aStudent.showMenu();
       
   }
   
   public void demoCreateAccount(String firstName, String lastName, String emailAddress,
			String phoneNumber, String username, String password, String eduInstitution) {
	   
	   resource = client.resource("http://" + serverLocation +"/create-account/"+ firstName +"-"+ lastName + "-" + emailAddress + "-"+phoneNumber + "-"+ username + "-"+ password + "-"+ eduInstitution);
	   resource.accept("text/plain").get(ClientResponse.class);
   }
   
   public void demoReserveBook(String username, String password, String bookName, String authorName) {
	   
	   resource = client.resource("http://" + serverLocation +"/reserve-book/"+ username +"-"+ password + "-" + bookName + "-"+ authorName);
	   resource.accept("text/plain").get(ClientResponse.class);

   }
   
   public void demoReserveInterLibrary(String username, String password, String bookName, String authorName) {
	   
	   resource = client.resource("http://" + serverLocation +"/reserve-book-inter-library/"+ username +"-"+ password + "-" + bookName + "-"+ authorName);
	   resource.accept("text/plain").get(ClientResponse.class);

   }
   
   
   
}