package test;

import static org.junit.Assert.*;

import java.io.IOException;

import models.*;
import client.StudentClient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;

public class StudentClientTest {
	
	private static StudentClient studentConcordia;
	private static StudentClient studentMcGill;
	private static StudentClient studentUdeM;
	private static ORB orb = ORB.init(new String[100],null);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		studentConcordia = new StudentClient("Concordia", orb);
		studentMcGill = new StudentClient("McGill", orb);
		studentUdeM = new StudentClient("UdeM", orb);
		
	}

	@Test
	public void testCreateAccount() throws IOException {
		
		/*Test create account on the different servers*/
		
		//create account on Concordia
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.createAccount(true);
		assertTrue(resultC);
		
		// create account on McGill
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.createAccount(true);
		assertTrue(resultM);
		
		// create account on UdeM
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.createAccount(true);
		assertTrue(resultU);
		
	}
	
	@Test
	public void testReserveBook() throws IOException {
		
		/*Test reserve book the on different servers*/
		
		// reserve book on Concordia server
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.reserveBook(true);
		assertTrue(resultC);
		
		// reserve book on McGill server
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.reserveBook(true);
		assertTrue(resultM);
		
		// reserve book on UdeM server
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.reserveBook(true);
		assertTrue(resultU);
		
	}
	
	@Test
	public void testReserveInterLibrary() throws IOException {
		
		/*Test reserve inter library from the different servers*/
		
		// reserve inter library from Concordia server
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.reserveInterLibrary(true);
		assertTrue(resultC);
		
		// reserve inter library from McGill server
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.reserveInterLibrary(true);
		assertTrue(resultM);
		
		// reserve inter library from Udem server
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.reserveInterLibrary(true);
		assertTrue(resultU);
	}

}
