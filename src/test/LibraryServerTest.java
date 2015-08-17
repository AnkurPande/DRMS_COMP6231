package test;

import static org.junit.Assert.*;
import model.Book;
import model.LibraryServerInfo;
import model.Student;
import org.junit.BeforeClass;
import org.junit.Test;
import replica.LibraryServerReplica;

/**
 * The Class LibraryServerTest.
 *
 * @author Haiyang Suen
 */

public class LibraryServerTest {
	
	private static LibraryServerReplica replica3; 
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		replica3 = new LibraryServerReplica(new LibraryServerInfo(1,"Replica3",4445,"localhost",7003,9003,"localhost"));
		
		//Start three servers
		new Thread(replica3).start();
			
		//Add four books
		Book book = new Book("TestBook", "TestAuthor", 1);
		replica3.getBookshelf().add(book);
		
		book = new Book("TestSetDuration", "TestAuthor", 1);
		replica3.getBookshelf().add(book);
		
		book = new Book("TestAvailability", "TestAuthor", 1);
		replica3.getBookshelf().add(book);
		
		//Add three users
		Student student = new Student("Test", "Test", "cc@cccc.cc", "51411111111", "TestUser", "TestPassword", "Concordia");
		replica3.addStudent(student);
		
		student = new Student("Test2", "Test2", "cc@cccc.cc", "51411111111", "TestUser2", "TestPassword2", "Concordia");
		replica3.addStudent(student);
		
		student = new Student("Test3", "Test3", "cc@cccc.cc", "51411111111", "TestUser3", "TestPassword3", "Concordia");
		replica3.addStudent(student);
		
		//Reserve book for testing set duration
		student.reserveBook("TestSetDuration", 14);
	}
	
	//------------Test of compulsory methods-----------
	@Test
	public void testCreateAccount() {
		
		//Try to create a user, should success
		String result1 = replica3.createAccount("FirstName", "LastName", "email@address.com", 
				"51414141414", "username", "password", "Concordia");
		assertEquals("true", result1);
		
		//Try to create a user with same information as last one, should fail
		String result2 = replica3.createAccount("FirstName", "LastName", "email@address.com", 
				"51414141414", "username", "password", "Concordia");
		assertEquals("false", result2);
	}
	
	@Test
	public void testReserveBook() {
		
		//Try to reserve the book that already set up, should success
		String result1 = replica3.reserveBook("TestUser", "TestPassword", "TestBook", "TestAuthor");
		assertEquals("true", result1);
		
		//Try another student to reserve again, no copy available, should fail
		String result2 = replica3.reserveBook("TestUser2", "TestPassword2", "TestBook", "TestAuthor");
		assertEquals("false", result2);
		
		//Check if the number of copies reduced
		int number = replica3.getBook("TestBook", "TestAuthor").getNumberCopies();
		assertEquals(0, number);
	}

	//------------Test of supporting methods-----------
	@Test
	public void testBookAvailability() {
		
		//Check book availability from two server, first one should success
		boolean result1 = replica3.checkBookAvailability("TestAvailability", "TestAuthor");
		assertTrue(result1);
	}
	
	@Test
	public void testAddStudents() {
		
		//Add a student, check if it is existed in the database
		Student student = new Student("FirstName", "student", "cc@cccc.cc", "51411111111", "TestAddUser", "TestAddPssword", "Concordia");
		replica3.addStudent(student);

		String result = replica3.getStudent("TestAddUser").getFirstName();
		System.out.println(result);
		assertEquals("FirstName", result.trim());
	}
	
	@Test 
	public void testGetStudent() {
		
		//Get two students with same username, they should point to a same student
		Student studentA = replica3.getStudent("TestUser");
		Student studentB = replica3.getStudent("TestUser");
		assertEquals(studentA, studentB);
	}
	
	@Test 
	public void testGetBook() {
		
		//Get two books with same book name and author, they should point to a same book
		Book bookA = replica3.getBook("TestBook", "TestAuthor");
		Book bookB = replica3.getBook("TestBook", "TestAuthor");
		assertEquals(bookA, bookB);
	}
}