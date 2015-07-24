/*
 * 
 * @author Syed Sumair Zafar
 * 
 */

package model;

import java.util.HashMap;
import java.util.Map;

public class Student {

	// Student Structure
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
	 * Default student constructor
	 */
	public Student()
	{
		
	}
	
	
	/*
	 * Student constructor overloading
	 */
	public Student(String firstName, String lastName, String emailAddress, String phoneNumber, 
			 String userName, String password, String eduInstitute)
	{
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
