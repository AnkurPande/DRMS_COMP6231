package client;

import java.util.Scanner;
import java.rmi.Naming;
//add the library containing all the models here

public class StudentClient 
{
   private Scanner scan;
   private Student student;
   private LibraryServerInterface server;
   
   StudentClient(String institution)
   {
      scan = new Scanner(System.in);
      student = new Student();
      server = (LibraryServerInterface) Naming.lookup("Registry path here with name of the institution");
      student.setInstitution(institution);
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
      while(userName.length() < 6 || userName.length() > 15 || password.length() < 8)
      
      if(server.login(userName,password)) // inputs are valid, check credential
      {
         //valid credentials
         //show students options or menu (reserve books)
         return true;
      }
      else
      {
         System.out.println("Login details do not exist...");
         System.out.println("Create your account now:");
         
         if(createAccount(userName,password)) return true;
      }
      
      return false;
      
   }
   
   private bool createAccount(String userName, String password)
   {
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
      student.setUserName(userName);
      student.setPassword(password);
      
      //calls the remote method on the server to actually create the account
      if(server.createAccount(student)) 
      {
         System.out.println("Account Created Successfully!!!");
         return true;
      }
      else 
      {
         /*get an error message from the server*/
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
                  valid = true;
                  break;
                  
               case 2:
                  // TODO if available, add book two to student
                  valid = true;
                  break;
                  
               case 3:
                  // TODO if available, add book three to student
                  valid = true;
                  break;
                  
               case 4:
                  // TODO if available, add book four to student
                  valid = true;
                  break;
                  
               case 5:
                  // TODO if available, add book five to student
                  valid = true;
                  break;
                  
               case 6:
                  // TODO if available, add book six to student
                  valid = true;
                  break;
                  
               case 7:
                  // TODO if available, add book seven to student
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
       
      if(aStudent.login())
      {
         System.out.println();
         System.out.println("Choose an option:");
         System.out.println();
         System.out.println("1- Reserve a Book.");
         System.out.println("2- Logout(Exit)");
         
         valid = false;
         
         while(!valid)
         {
            try
            {
               choice = scan.nextInt();
               switch(choice)
               {
                  case 1:
                     aStudent.reserveBook();
                     valid = true;
                     break;
                  
                  case 2:
                     System.out.println("Thank you for visiting the online Library!!");
                     System.out.println("See you soon!!");
                     System.in.close();
				         System.exit(0);
                     valid = true;
                     break;
            
                  default:
                     System.out.println("please choose options 1 or 2 only!!");
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
   
}
