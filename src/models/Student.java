/*
 * 
 * @author Syed Sumair Zafar
 * 
 */

package models;

import java.util.HashMap;
import java.util.Map;

public class Student {

	// Student Structure
	private String studentID = null;	
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private String phoneNumber = null;
	private String eduInstitute = null;
	private double finesAccumulated = 0.0;
	
	private String userName = null;
	private String password = null;
	
	// HashMap <bookNmae , # of days to return the book>
	private Map<String, Integer> books = new HashMap<String, Integer>();
	
	// filename to save the student data
	//private String filename = null; 
		
	
	/*
	 * Student constructor
	 */
	public Student(String studentID, String firstName, String lastName, String emailAddress, String phoneNumber, 
			String eduInstitute, String userName, String password) throws Exception
	{
		
		if (studentID.isEmpty()) 
		{ 
			throw new Exception("Please enter student ID"); 
		}
		
		if (firstName.isEmpty()) 
		{ 
			throw new Exception("Please enter student's first name"); 
		}
		
		if (lastName.isEmpty()) 
		{ 
			throw new Exception("Please enter student's last name"); 
		}
		
		if (userName.isEmpty()) 
		{ 
			throw new Exception("Please enter student's username for login purposes"); 
		}
		
		if(userName.length() < 6 || userName.length() > 15)
		{
			throw new Exception("The username should be min. of 6 characters or max 15 characters");
		}
		
		if (password.isEmpty()) 
		{ 
			throw new Exception("Please enter student's password for login purposes"); 
		}
		
		if(password.length() < 8)
		{
			throw new Exception("Password should be atleast of 8 characters");
		}

		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.eduInstitute = eduInstitute;
		
		this.userName = userName;
		this.password = password;
		
		this.finesAccumulated = 0;
	}
	
	
	// ------------------------ GETTERS Functions START --------------------------------------
	/**
	 * This function will return student's ID
	 */
	public String getStudent_id() 
	{
		return this.studentID;
	}
	
	/**
	 * This funciton will return student's first name
	 */
	public String getFirstName() 
	{
		return this.firstName;
	}
	
	
	/**
	 * This function will return student's last name
	 */
	public String getLastName() 
	{
		return this.lastName;
	}
	
	
	/**
	 * This function will return student's full name
	 */
	public String getFullName() 
	{
		return this.firstName + this.lastName;
	}
	
	
	/**
	 * This function will return student's email address
	 */
	public String getEmailAddress() 
	{
		return this.emailAddress;
	}
	
	
	/**
	 * This function will return student's phone number
	 */
	public String getPhoneNumber() 
	{
		return this.phoneNumber;
	}
	
	/**
	 * This function will return student's username
	 */
	public String getUserName() 
	{
		return this.userName;
	}
	
	/**
	 * This function will return student's password
	 */
	public String getPassword() 
	{
		return this.password;
	}
	
	/**
	 * This function will return student's institute
	 */
	public String getEducationalIns() 
	{
		return this.eduInstitute;
	}
	
	/**
	 * This function will return fines
	 */
	public double getFinesAccumulated() 
	{
		return this.finesAccumulated;
	}
	
	
	public Map<String, Integer> getBooks() 
	{
		return books;
	}
	
	// ------------------------ GETTERS Functions END --------------------------------------
	
	
	
	// ------------------------ SETTERS Functions Start --------------------------------------
	
	public void setStudent_id(String studentID) 
	{
		this.studentID = studentID;
	}
	
	
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public void setPhoneNumber(String phoneNumber) 
	{
		this.phoneNumber = phoneNumber;
	}
	
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public void setEducationalIns(String eduInstitute) 
	{
		this.eduInstitute = eduInstitute;
	}
	
	public void setFinesAccumulated(double finesAccumulated)
	{		
		this.finesAccumulated = finesAccumulated;
	}	
	
	public void setBooks(Map<String, Integer> books) 
	{
		this.books = books;
	}
	
	// ------------------------ SETTERS Functions END --------------------------------------
	
	
	// ------------------------ Misc Functions --------------------------------------------
	
	/*
	 * This function will automatically add daily fines to student who didn't returned books after deadline
	 */
	public void addDailyFine() 
	{		
		this.finesAccumulated += 1;
	}
	
	
	/*
	 * This function will reserve a book
	 */
	public void reserveBook(String book_id, int duration)
	{	
		this.books.put(book_id, duration);
	}
	
}
