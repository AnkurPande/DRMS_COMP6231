package Demo;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.omg.CORBA.ORB;

import client.StudentClient;
import model.Student;

/**
 * The Class Demo.
 * 
 * @author Haiyang Sun
 */
public class Demo implements Runnable {
	
	/** The thread number. */
	private int threadNumber;
	
	/** The running time. */
	private long runningTime;
	
	/**
	 * Instantiates a new demo.
	 *
	 * @param i the i
	 */
	public Demo(int i) {
		setThreadNumber(i);
	}
	
	/** The orb. */
	ORB orb = ORB.init(new String[1],null);
	
	/** The concordia student. */
	//Initialize three student client
	private StudentClient concordiaStudentClient = new StudentClient("Concordia");
	
	/** The mcgill student. */
	private StudentClient mcgillStudentClient = new StudentClient("McGill");
	
	/** The udem student. */
	private StudentClient udemStudentClient = new StudentClient("UdeM");
	
	/** The flag to control demo. */
	private boolean flag;
	
	/**
	 * Demo method.
	 */
	public void demo() {
		
		flag = true;
		
		while(flag) {
			
			String usernameConcordia = getRandomString();
			String passwordConcordia = getRandomString();
			
			String usernameMcGill = getRandomString();
			String passwordMcGill = getRandomString();
			
			String usernameUdeM = getRandomString();
			String passwordUdeM = getRandomString();
			
			Student concordiaStudent = new Student(getRandomString(),getRandomString(),getRandomString(),getRandomString(),usernameConcordia, passwordConcordia,"Concordia");
			Student mcgillStudent = new Student(getRandomString(),getRandomString(),getRandomString(),getRandomString(),usernameMcGill, passwordMcGill,"McGill");
			Student udemStudent = new Student(getRandomString(),getRandomString(),getRandomString(),getRandomString(),usernameUdeM, passwordUdeM,"UdeM");
			concordiaStudentClient.setStudent(concordiaStudent);
			mcgillStudentClient.setStudent(mcgillStudent);
			udemStudentClient.setStudent(udemStudent);
			
			//CreateAccount

			concordiaStudentClient.createAccount(false);
			mcgillStudentClient.createAccount(false);
			udemStudentClient.createAccount(false);
			//ReserveBook
			concordiaStudentClient.demoReserveBook(usernameConcordia, passwordConcordia, bookPicker()[0].trim(), bookPicker()[1].trim());
			mcgillStudentClient.demoReserveBook(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			udemStudentClient.demoReserveBook(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			
			//ReserveBookInterLibrary
			concordiaStudentClient.demoReserveInterLibrary(usernameConcordia, passwordConcordia, bookPicker()[0], bookPicker()[1]);
			mcgillStudentClient.demoReserveInterLibrary(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			udemStudentClient.demoReserveInterLibrary(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			
			
		}
	}
	
	/**
	 * Gets the random string.
	 *
	 * @return the random string
	 */
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
	
	/**
	 * Book picker.
	 *
	 * @return the string[]
	 */
	private String[] bookPicker() {
		
		String[] bookA = new String[2];
		bookA[0] = "AAA";
		bookA[1] = "BBB";
		
		String[] bookC = new String[2];
		bookC[0] = "CCC";
		bookC[1] = "DDD";
		
		String[] bookE = new String[2];
		bookE[0] = "EEE";
		bookE[1] = "FFF";
		
		String[] bookG = new String[2];
		bookG[0] = "GGG";
		bookG[1] = "HHH";
		
		String[] bookI = new String[2];
		bookI[0] = "testbook";
		bookI[1] = "testauthor";
		
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//Schedule time to stop.
		this.setRunningTime(180);
		Timer timer = new Timer();
		timer.schedule(new Stop(), runningTime*1000);
		demo();

	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		//Initialize log file
		try{
			File f = new File( "demo_log.txt");
			if(!f.exists())
				f.createNewFile();
		}catch(IOException e) {
			e.getMessage();
		}
		
		for(int i=0; i<10; i++) {
			Thread thread = new Thread(new Demo(i+1));
			thread.start();
			String activity = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +" Demo Thread " + (i+1) + " Start running!";
			log(activity);
			System.out.println(activity);
		}
	}
	
	/**
	 * Logger.
	 *
	 * @param activity the activity
	 */
	public static void log(String  activity)  {
		try{
			File f = new File("demo_log.txt");
			FileWriter fw = new FileWriter(f,true);
			fw.write(activity);
			fw.write("\r\n");
			fw.flush();
			fw.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Getters & Setters
	 */
	
	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	
	public long getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(long runningTime) {
		this.runningTime = runningTime;
	}

	/**
	 * Inner-class Stop use to stop running.
	 */
	public class Stop extends TimerTask {

		
		@Override
		public void run() {
			
			flag = false;
			String activity = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +" Demo Thread " + threadNumber + "  stop!"
					+ "Running time: " + runningTime + "s!";
			System.out.println(activity);
			log(activity);
		}
		
	}
	
	
	
}
