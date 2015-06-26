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
	private static ORB orb = ORB.init(new String[1],null);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		studentConcordia = new StudentClient("Concordia", orb);
		studentMcGill = new StudentClient("McGill", orb);
		studentUdeM = new StudentClient("UdeM", orb);
		
	}

	@Test
	public void testCreateAccount() throws IOException {
		/*create account on different servers*/
		
		// create account on Concordia server should succeed
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.createAccount();
		assertTrue(resultC);
		
		// create account on McGill server should succeed
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.createAccount();
		assertTrue(resultM);
		
		// create account on UdeM server should succeed
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.createAccount();
		assertTrue(resultU);
	}
	
	@Test
	public void testReserveBook() throws IOException {
		/*Reserve Book on different servers*/
		
		// Reserve Book on Concordia server should succeed
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.reserveBook();;
		assertTrue(resultC);
		
		// Reserve Book on McGill server should succeed
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.reserveBook();
		assertTrue(resultM);
		
		// Reserve Book on UdeM server should succeed
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.reserveBook();
		assertTrue(resultU);
	}
	
	@Test
	public void testReserveInterLibrary() throws IOException {
		/*Reserve Inter Library on different servers*/
		
		// Reserve Inter Library from Concordia server should succeed
		studentConcordia.validateCredentials();
		boolean resultC = studentConcordia.reserveInterLibrary();
		assertTrue(resultC);
		
		// Reserve Inter Library from McGill server should succeed
		studentMcGill.validateCredentials();
		boolean resultM = studentMcGill.reserveInterLibrary();
		assertTrue(resultM);
		
		// Reserve Inter Library from UdeM server should succeed
		studentUdeM.validateCredentials();
		boolean resultU = studentUdeM.reserveInterLibrary();
		assertTrue(resultU);
	}

}
