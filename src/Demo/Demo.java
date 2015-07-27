package Demo;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import client.StudentClient;

/**
 * The Class Demo.
 * 
 * @author Haiyang Sun
 */
public class Demo implements Runnable {
	
	/** The thread number. */
	private int threadNumber;
	
	/** The running time. */
	private int runningTime;
	
	private long numberOfRPC = 0;
	/**
	 * Instantiates a new demo.
	 *
	 * @param i the i
	 */
	public Demo(int i) {
		setThreadNumber(i);
	}
	
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
			
			/*
			 * RPCs, after each call, number of RPC plus one.
			 */
			
			//CreateAccount
			concordiaStudentClient.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameConcordia,passwordConcordia, getRandomString());
			++ numberOfRPC;
			
			mcgillStudentClient.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameMcGill,passwordMcGill, getRandomString());
			++ numberOfRPC;
			
			udemStudentClient.demoCreateAccount(getRandomString(), getRandomString(), getRandomString(),getRandomString(),
					usernameUdeM,passwordUdeM, getRandomString());
			++ numberOfRPC;
			
			//ReserveBook
			concordiaStudentClient.demoReserveBook(usernameConcordia, passwordConcordia, bookPicker()[0].trim(), bookPicker()[1].trim());
			++ numberOfRPC;
			mcgillStudentClient.demoReserveBook(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			++ numberOfRPC;
			udemStudentClient.demoReserveBook(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			++ numberOfRPC;
			
			//ReserveBookInterLibrary
			concordiaStudentClient.demoReserveInterLibrary(usernameConcordia, passwordConcordia, bookPicker()[0], bookPicker()[1]);
			++ numberOfRPC;
			mcgillStudentClient.demoReserveInterLibrary(usernameMcGill, passwordMcGill, bookPicker()[0], bookPicker()[1]);
			++ numberOfRPC;
			udemStudentClient.demoReserveInterLibrary(usernameUdeM, passwordUdeM, bookPicker()[0], bookPicker()[1]);
			++ numberOfRPC;
			
			
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
		
		System.out.println("Please input demo running time (in seconds).");
		Scanner sc = new Scanner(System.in);
		int runningDuration = sc.nextInt();
		sc.close();
		
		//Initialize log file
		try{
			File f = new File( "demo_log.txt");
			if(!f.exists())
				f.createNewFile();
		}catch(IOException e) {
			e.getMessage();
		}
		
		for(int i=0; i<10; i++) {
			
			Demo demo = new Demo(i+1);
			demo.setRunningTime(runningDuration);
			Thread thread = new Thread(demo);
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

	
	public int getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	public long getNumberOfRPC() {
		return numberOfRPC;
	}

	public void setNumberOfRPC(long numberOfRPC) {
		this.numberOfRPC = numberOfRPC;
	}


	/**
	 * Inner-class Stop use to stop running.
	 */
	public class Stop extends TimerTask {

		
		@Override
		public void run() {
			
			flag = false;
			String activity = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +" Demo Thread " + threadNumber + "  stop running! "
					+ "Running time: " + runningTime + "s! " + "total number of RPCs " + numberOfRPC + "!";
			System.out.println(activity);
			log(activity);
		}
		
	}
	
	
	
}
