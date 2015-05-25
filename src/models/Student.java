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
	private String filename = null; 
		
	
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
	
	
}
