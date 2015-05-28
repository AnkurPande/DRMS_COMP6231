package client;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import Models.*;

public class StudentClient 
{
   private Scanner scan;
   private Student student;
   private LibraryServerInterface server;
   private FileWriter myWriter;
   private File clientLogFolder;
   
   StudentClient(String institution)
   {
      scan = new Scanner(System.in);
      student = new Student();
      server = (LibraryServerInterface) Naming.lookup("Registry path here with name of the institution");
      student.setInstitution(institution);
      clientLogFolder = new File("clientLog");
      if(!folder.exist()) clientLogFolder.mkdir();
   }
   
   
   public bool login()
   {
      String userName;
      String password;
      
      //student.setInstitution(); // Assuming the class Student has such a setter
      
      do
      {
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
        
      }
      
      student.setUsername(userName);
      student.setPassword(password);
      
      return true;
      
   }
   
   public void logFile(String fileName, String logInfo)
   {
   	File clientFile = new File("clientLog",fileName); 
   	if(!clientFile.exists()) client.createNewFile();
   	
   	myWriter = new FileWriter(clientFile,true);
   	myWriter.write(logInfo+"\n");
   	myWriter.flush();
   	myWriter.close();
   }
   
   private bool createAccount()
   {
      login();
      String fname, lname,email,phone;
      do
      {
        //gets first name from the consol
        System.out.print("First Name: ");
        fname = scan.next();
        System.out.println();
        
        //gets last name from the consol 
        System.out.print("Last Name: ");
        studen = scan.next();
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
        System.out.println("Try again!!!");
      }
      while(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty())
      
      student.setFisrtName(fname); 
      student.setLastName(lname); 
      student.setEmail(email); 
      student.setPhoneNumber(phone); 
      
      //calls the remote method on the server to actually create the account
      if(server.createAccount(student)) 
      {
         System.out.println("Account Created Successfully!!!");
         string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Account created for user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
         logFile(student.getUserName(),logInfo);
         return true;
      }
      else 
      {
         //TODO get an error message from the server
         return false;
      }
   }
   
   public void reserveBook()
   {
      bool valid = false;
      int choice = 0;
      
      System.out.println("List of Books in The Library:");
      System.out.println();
      
      ArrayList<Book> bookList = server.getBooks(); //TODO review this statement
      
      //Prints all the books of the Library
      for(int i = 0; i<bookList.length(); i++)
      {
         int num = i+1;
         System.out.print(""+num+ "- " + bookList[i].getName());
         
         //checks if there is available copies of the book
         if(bookList[i].Qty == 0)
         {
            System.out.println(" (unavailable)");
         }
         else System.out.println();   
      }
      
      System.out.println("Which book would you like to reserve?");
      
       while(!valid)
      {
         try
         {
            choice = scan.nextInt();
            switch(choice)
            {
               case 1:
                 // TODO if available, add book one to student
                 if(bookList[0] >0) 
                 {
                      server.reserveBook(student.getUserName(),student.getPassword(),bookList[0].getName(),booList[0].getAuthor());
                      string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[0].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
                 }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 2:
                  // TODO if available, add book two to student
                  if(bookList[1] >0) 
                  {
                      server.reserveBook(student.getUserName(),student.getPassword(),bookList[1].getName(),booList[1].getAuthor());
                      string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[1].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
                  }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 3:
                  // TODO if available, add book three to student
                  if(bookList[2] >0) 
                  {
                      server.reserveBook(student.getUserName(),student.getPassword(),bookList[2].getName(),booList[2].getAuthor());
                      string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[2].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
                  }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 4:
                  // TODO if available, add book four to student
                  if(bookList[3] >0) 
	          {
	              server.reserveBook(student.getUserName(),student.getPassword(),bookList[3].getName(),booList[3].getAuthor());
	              string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[3].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
	          }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 5:
                  // TODO if available, add book five to student
                  if(bookList[4] >0) 
                  {
                  	server.reserveBook(student.getUserName(),student.getPassword(),bookList[4].getName(),booList[4].getAuthor());
                  	string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[4].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
                  }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 6:
                  // TODO if available, add book six to student
                  if(bookList[5] >0) 
                  {
                  	server.reserveBook(student.getUserName(),student.getPassword(),bookList[5].getName(),booList[5].getAuthor());
                  	string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[5].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                      logFile(student.getUserName(),logInfo);
                  }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               case 7:
                  // TODO if available, add book seven to student
                  if(bookList[6] >0) server.reserveBook(student.getUserName(),student.getPassword(),bookList[6].getName(),booList[6].getAuthor());
                  {
                  	string logInfo = "[" + new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) + "]"
                                + " Book: " + bookList[6].getName() + " reservered by user: " + student.getUserName() + " on server: "
                                + student.getInstitution();
                       logFile(student.getUserName(),logInfo);
                  }
                 else System.out.println("No copies of this book are available at the moment!!");
                  valid = true;
                  break;
                  
               default:
                  System.out.println("please choose options 1 to 7 only!!");
                  valid = false;
            }
         }
         catch(Exception e)
         {
            System.out.println("Invalid input!!! Please enter an integer");
         }
      }
   }
   
   public static void main(String args[])
   {
      println("WELCOME TO ONLINE LIBRARY SYSTEM");
      println();
      println("Select your instution:");
      println();
      println("1- Concordia");
      println("2- McGill");
      println("3- UdeM");
      
      int choice = 0;
      bool valid = false;
      StudentClient aStudent;
      
      while(!valid)
      {
         try
         {
            choice = scan.nextInt();
            switch(choice)
            {
               case 1:
                  aStudent = new StudentClient("Concordia");
                  valid = true;
                  break;
                  
               case 2:
                  aStudent = new StudentClient("McGill");
                  valid = true;
                  break;
                  
               case 3:
                  aStudent = new StudentClient("UdeM");
                  valid = true;
                  break;
                  
               default:
                  System.out.println("please choose options 1, 2 or 3 only!!");
                  valid = false;
            }
         }
         catch(Exception e)
         {
            System.out.println("Invalid input!!! Please enter an integer");
         }
      }
       
      
         System.out.println();
         System.out.println("Choose an option:");
         System.out.println();
         System.out.println("1- Reserve a Book.");
         System.out.println("2- Create an Account");
          System.out.println("3- Exit");
         
         valid = false;
         
         while(!valid)
         {
            try
            {
               choice = scan.nextInt();
               switch(choice)
               {
                  case 1:
                     aStudent.login();
                     aStudent.reserveBook();
                     valid = true;
                     break;
                     
                  case 2:
                     aStudent.login();
                     aStudent.createAccount();
                     valid = true;
                     break;
                  
                  case 3:
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
         catch(Exception e)
         {
            System.out.println("Invalid input!!! Please enter an integer");
         }
      }
      
   }
   
}
