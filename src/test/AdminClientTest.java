package test;

import static org.junit.Assert.*;
import client.AdminClient;
import org.junit.BeforeClass;
import org.junit.Test;


public class AdminClientTest {
	
	private static AdminClient adminConcordia;
	private static AdminClient adminMcGill;
	private static AdminClient adminUdeM;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		adminConcordia = new AdminClient("Concordia");
		adminMcGill = new AdminClient("McGill");
		adminUdeM = new AdminClient("UdeM");
		
	}
	
	@Test
	public void testGetNonreturners() throws Exception {
		
		/*Test get non returners from different admin*/
		
		//get non returners admin Concordia
		boolean resultC = adminConcordia.getNonReturners(false);
		assertTrue(resultC);
		
		// get non returners admin McGill
		boolean resultM = adminMcGill.getNonReturners(false);
		assertTrue(resultM);
		
		// get non returners admin UdeM
		boolean resultU = adminUdeM.getNonReturners(false);
		assertTrue(resultU);
		
	}
	
	@Test
	public void testSetDuration() throws Exception {
		
		/*Test set duration from different admin*/
		
		// set duration admin Concordia
		boolean resultC = adminConcordia.setDuration();
		assertTrue(resultC);
		
		//set duration admin McGill 
		boolean resultM = adminConcordia.setDuration();
		assertTrue(resultM);
		
		// set duration admin UdeM 
		boolean resultU = adminConcordia.setDuration();
		assertTrue(resultU);
		
	}

}
