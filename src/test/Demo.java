package test;

import java.util.Random;

import org.omg.CORBA.ORB;

import client.StudentClient;

public class Demo implements Runnable {
	
	
	ORB orb = ORB.init(new String[1],null);

	
	private StudentClient concordiaStudent = new StudentClient("Concordia", orb);
	private StudentClient mcgillStudent = new StudentClient("McGill",orb);
	private StudentClient udemStudent = new StudentClient("UdeM",orb);
	
	
	private boolean flag;
	
	public void demo() {
		
		flag = true;
		
		while(flag) {
			
			String usernameConcordia = getRandomString();
			String passwordConcordia = getRandomString();
			
			String usernameMcGill = getRandomString();
			String passwordMcGill = getRandomString();
			
			String usernameUdeM = getRandomString();
			String passwordUdeM = getRandomString();
			
			concordiaStudent.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameConcordia,passwordConcordia, getRandomString());
			mcgillStudent.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameMcGill,passwordMcGill, getRandomString());
			udemStudent.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameUdeM,passwordUdeM, getRandomString());
			
			concordiaStudent.demoReserveBook(usernameConcordia, passwordConcordia, bookPicker()[0].trim(), bookPicker()[1].trim());
			mcgillStudent.demoReserveBook(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			udemStudent.demoReserveBook(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			
			concordiaStudent.demoReserveInterLibrary(usernameConcordia, passwordConcordia, bookPicker()[0], bookPicker()[1]);
			mcgillStudent.demoReserveInterLibrary(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			udemStudent.demoReserveInterLibrary(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			
			
		}
	}
	
	private String getRandomString() {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomString = new StringBuilder();
        Random rnd = new Random();
        while (randomString.length() < 8) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            randomString.append(CHARS.charAt(index));
        }
       
        return randomString.toString();
    }
	
	private String[] bookPicker() {
		
		String[] bookA = new String[2];
		bookA[0] = "AAA";
		bookA[1] = "BBB";
		
		String[] bookC = new String[2];
		bookC[0] = "CCC";
		bookC[1] = "DDD";
		
		String[] bookE = new String[2];
		bookA[0] = "EEE";
		bookA[1] = "FFF";
		
		String[] bookG = new String[2];
		bookC[0] = "GGG";
		bookC[1] = "HHH";
		
		String[] bookI = new String[2];
		bookA[0] = "III";
		bookA[1] = "JJJ";
		
		Random rnd = new Random();
		int index = (int) (rnd.nextFloat() * 4);
		switch (index) {
			case 0: return bookA;
			case 1: return bookC;
			case 2: return bookE;
			case 3: return bookG;
			case 4: return bookI;
			default: return bookA;	
		}
		
		
	}
	@Override
	public void run() {
		
		demo();
	}
	
	
	public static void main(String[] args) {
		
		for(int i=0; i<10; i++) {
			Thread thread = new Thread(new Demo());
			thread.start();
		}
	}
}
