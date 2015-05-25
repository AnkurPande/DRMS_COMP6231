package client;

import java.util.Scanner;
import java.rmi.Naming;

public class StudentClient 
{
   private Scanner scan;
   private Student student;
   private LibraryServerInterface server;
   
   StudentClient()
   {
      scan = new Scanner(System.in);
      student = new Student();
      server = (LibraryServerInterface) Naming.lookup("Registry path here"); 
   }
   
   public void login(String institution)
   {
      String userName;
      String password;
      
      student.setInstitution(); // Assuming the class Student has such a setter
      
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
      
      if(server.login(userName,password,institution)) // inputs are valid, check credential
      {
         //valid credentials
         //show students options or menu (reserve books)
      }
      else
      {
         System.out.println("Login details do not exist...");
         System.out.println("Create your account now:");
         
         createAccount(userName,password);
      }
      
   }
   
   private void createAccount(String userName, String password)
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
        System.out.println("Try again!!!");
      }
      while(fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty())
      
      student.setFName(fname); //Assuming that the Student class has such a setter
      student.setLName(lname); //Assuming that the Student class has such a setter
      student.setEmail(email); //Assuming that the Student class has such a setter
      student.setPhoneNbr(phone); //Assuming that the Student class has such a setter
      
      //calls the remote method on the server to actually create the account
      server.createAccount(student,userName,password);
   }
   
}
