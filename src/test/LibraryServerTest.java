package test;

import static org.junit.Assert.*;

import model.Book;
import model.LibraryServerInfo;
import model.Student;

import org.junit.BeforeClass;
import org.junit.Test;

import server.LibraryServer;

/**
 * The Class LibraryServerTest.
 *
 * @author Haiyang Sun
 */
public class LibraryServerTest {
	
	
	private static LibraryServer serverOfConcordia; 
	private static LibraryServer serverOfMcGill;
	private static LibraryServer serverOfUdeM; 

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
		serverOfConcordia = new LibraryServer(new LibraryServerInfo("Concordia",4445, "localhost"));
		serverOfMcGill = new LibraryServer(new LibraryServerInfo("McGill", 4447, "localhost"));
		serverOfUdeM = new LibraryServer(new LibraryServerInfo("UdeM", 4449, "localhost"));
		
		//Start three servers
		new Thread(serverOfConcordia).start();
		new Thread(serverOfMcGill).start();
		new Thread(serverOfUdeM).start();
		
		//Add four books
		Book book = new Book("TestBook", "TestAuthor", 1);
		serverOfConcordia.getBookshelf().add(book);
		
		book = new Book("TestReserveInterLib", "TestAuthor", 1);
		serverOfMcGill.getBookshelf().add(book);
		
		book = new Book("TestSetDuration", "TestAuthor", 1);
		serverOfConcordia.getBookshelf().add(book);
		
		book = new Book("TestAvailability", "TestAuthor", 1);
		serverOfConcordia.getBookshelf().add(book);

		
		//Add three users
		Student student = new Student("Test", "Test", "cc@cccc.cc", "51411111111", "TestUser", "TestPassword", "Concordia");
		serverOfConcordia.addStudent(student);
		
		student = new Student("Test2", "Test2", "cc@cccc.cc", "51411111111", "TestUser2", "TestPassword2", "Concordia");
		serverOfConcordia.addStudent(student);
		
		student = new Student("Test3", "Test3", "cc@cccc.cc", "51411111111", "TestUser3", "TestPassword3", "Concordia");
		serverOfConcordia.addStudent(student);
		
		
		//Reserve book for testing set duration
		student.reserveBook("TestSetDuration", 14);
	}
	
	//------------Test of compulsory methods-----------
	@Test
	public void testCreateAccount() {
		
		//Try to create a user, should success
		String result1 = serverOfConcordia.createAccount("FirstName", "LastName", "email@address.com", 
				"51414141414", "username", "password", "Concordia");
		assertEquals("true", result1);
		
		//Try to create a user with same information as last one, should fail
		String result2 = serverOfConcordia.createAccount("FirstName", "LastName", "email@address.com", 
				"51414141414", "username", "password", "Concordia");
		assertEquals("false", result2);
		
	}
	
	@Test
	public void testReserveBook() {
		
		//Try to reserve the book that already set up, should success
		String result1 = serverOfConcordia.reserveBook("TestUser", "TestPassword", "TestBook", "TestAuthor");
		assertEquals("true", result1);
		
		//Try another student to reserve again, no copy available, should fail
		String result2 = serverOfConcordia.reserveBook("TestUser2", "TestPassword2", "TestBook", "TestAuthor");
		assertEquals("false", result2);
		
		//Check if the number of copies reduced
		int number = serverOfConcordia.getBook("TestBook", "TestAuthor").getNumberCopies();
		assertEquals(0, number);
	}
	
	@Test
	public void testGetNonReturners() {
		
		String s = serverOfConcordia.getNonRetuners("admin", "admin", "Concordia", "15");
		assertEquals("Concordia", s.substring(0, 9));
	}
	
	@Test
	public void testReserveInterLibrary() {
		
		//Reserver a book available at McGill Library from Concordia Server, should success
		String result = serverOfConcordia.reserveInterLibrary("TestUser", "TestPassword", "TestReserveInterLib", "TestAuthor");
		assertEquals("true", result);
		
		//Reserve again by another user, should fail, cause only one copy available
		String result2 = serverOfConcordia.reserveInterLibrary("TestUser2", "TestPassword2", "TestReserveInterLib", "TestAuthor");
		assertEquals("false", result2);		
	}
	
	@Test 
	public void testSetDurations() {
		
		//Set duration of book "TestSetDuration" as 20, used to be 14.
		serverOfConcordia.setDuration("TestUser3", "TestSetDuration", 20);
		int result = serverOfConcordia.getStudent("TestUser3").getBooks().get("TestSetDuration");
		assertEquals(20, result);
	}
	
	//------------Test of supporting methods-----------
	
	@Test
	public void testBookAvailability() {
		
		//Check book availability from two server, first one should success
		boolean result1 = serverOfConcordia.checkBookAvailability("TestAvailability", "TestAuthor");
		boolean result2 = serverOfMcGill.checkBookAvailability("TestAvailability", "TestAuthor");
		
		assertTrue(result1);
		assertFalse(result2);
	}
	@Test
	public void testAddStudents() {
		
		//Add a student, check if it is existed in the database
		Student student = new Student("FirstName", "student", "cc@cccc.cc", "51411111111", "TestAddUser", "TestAddPssword", "Concordia");
		serverOfConcordia.addStudent(student);
		
		String result = serverOfConcordia.getStudent("TestAddUser").getFirstName();
		System.out.println(result);
		assertEquals("FirstName", result.trim());
		
	}
	
	@Test 
	public void testGetStudent() {
		
		//Get two students with same username, they should point to a same student
		Student studentA = serverOfConcordia.getStudent("TestUser");
		Student studentB = serverOfConcordia.getStudent("TestUser");
		
		assertEquals(studentA, studentB);
	}
	
	@Test 
	public void testGetBook() {
		
		//Get two books with same book name and author, they should point to a same book
		Book bookA = serverOfConcordia.getBook("TestBook", "TestAuthor");
		Book bookB = serverOfConcordia.getBook("TestBook", "TestAuthor");
		
		assertEquals(bookA, bookB);
	}
}
