/**
 * 
 */
package test;

import static org.junit.Assert.*;

import models.Book;
import models.Student;

import org.junit.BeforeClass;
import org.junit.Test;

import server.LibraryServer;

/**
 * @author Haiyang Sun
 *
 */
public class LibraryServerTest {
	
	private static LibraryServer serverOfConcordia = new LibraryServer("Concordia", 4445);
	private static LibraryServer serverOfMcGill = new LibraryServer("McGill", 4447);
	private static LibraryServer serverOfUdeM = new LibraryServer("UdeM", 4449);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		new Thread(serverOfConcordia).start();
		new Thread(serverOfMcGill).start();
		new Thread(serverOfUdeM).start();
		
		Book book = new Book("TestBook","TestAuthor",1);
		serverOfConcordia.getBookshelf().add(book);
		
		Student student = new Student("Test", "Test", "cc@cccc.cc", "51411111111", "TestUser", "TestPassword", "Concordia");
		serverOfConcordia.addStudent(student);
		
		
	}
	
	@Test
	public void testCreateAccount() {
		boolean result = serverOfConcordia.createAccount("FirstName", "LastName", "email@address.com", 
				"51414141414", "username", "password", "Concordia");
		assertTrue(result);
	}
	
	@Test
	public void testReserveBook() {
		
		boolean result = serverOfConcordia.reserveBook("TestUser", "TestPassword", "TestBook", "TestAuthor");
		assertEquals(true, result);
	}
	
	@Test
	public void testGetNonReturners() {
		
		String s = serverOfConcordia.getNonRetuners("admin", "admin", "Concordia", "15");
		assertEquals("Concordia", s.substring(0, 9));
	}
}
