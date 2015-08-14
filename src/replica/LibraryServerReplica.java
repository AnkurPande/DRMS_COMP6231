package replica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Book;
import model.Student;
import udp.UDPSender;


public class LibraryServerReplica implements Runnable{
	
	/*
	 * Properties of server
	 */
	private int replicaID;
	
	private int heartBeatListenPort;
	
	private int rmPort;
	
	private String rmIpAddress;
	
	private String nameOfServer;

	private int portOfUDP;
	
	private ReplicaUDPListener socket;
	
	private Heartbeat heartBeatSocket;
	
	private  List<Book> bookshelf = new ArrayList<Book>();
	
	private  Map<String, Map<String, Student>> studentData = new HashMap<String, Map<String, Student>>();
	
	private  Map<Integer, String> UDPInfo = new HashMap<Integer, String>();
	
	private String ipAddress;
	
	
	
	
	/**
	 * The inner class ConstantValue stores all constant values related to server implementation.
	 * 
	 * @author Haiyang Sun
	 */
	public class ConstantValue {
		
		
		//Institution names
		public static final String REPLICA1 = "Replica1";
		
		public static final String REPLICA2 = "Replica2";
		
		public static final String REPLICA3 = "Replica3";
		
		//Default Duration
		public static final int DEFAULT_DURATION = 14;
		
		//UDP Ports to listen incoming request
		public static final int REPLICA1_UDP_PORT = 4445;
		
		public static final int REPLICA2_UDP_PORT = 4447;
		
		public static final int REPLICA3_UDP_PORT = 4449;
		
		//heart beat port for incoming heart beat request
		
		public static final int REPLICA1_HEARTBEAT_PORT = 7001;
		
		public static final int REPLICA2_HEARTBEAT_PORT = 7002;
		
		public static final int REPLICA3_HEARTBEAT_PORT = 7003;
		
		//Heart beat reply port (RM ports where the heart beat response is forwarded)
		
		public static final int REPLICA1_HEARTBEAT_RM_PORT = 9001;
		
		public static final int REPLICA2_HEARTBEAT_RM_PORT = 9002;
		
		public static final int REPLICA3_HEARTBEAT_RM_PORT = 9003;
		
		//IP ADDRESS OF REPLICA
		
		public static final String REPLICA1_IP_ADDRESS = "localhost";
		
		public static final String REPLICA2_IP_ADDRESS = "localhost";
		
		public static final String REPLICA3_IP_ADDRESS = "localhost";
		
		//IP ADDRESS OF RMS
		
		public static final String REPLICA1_RM_IP_ADDRESS = "localhost";
		
		public static final String REPLICA2_RM_IP_ADDRESS = "localhost";
		
		public static final String REPLICA3_RM_IP_ADDRESS = "localhost";
		
		public static final String TRUE = "true";
		
		public static final String FALSE = "false";
	}

	//----------------------------------------Constructors---------------------------------------------------------------------//
		
	/**
	 * Instantiates a new library server.
	 *
	 * @param info the info
	 */
	public LibraryServerReplica(LibraryServerInfo info) {
		
		this.nameOfServer = info.getServerName();
		this.portOfUDP = info.getPortOfUDP();
		this.ipAddress = info.getIpAddress();
		this.heartBeatListenPort = info.getHeartbeatPort();
		this.rmPort = info.getRmPort();
		this.rmIpAddress = info.getRmIpAddress();
		
		
		
		initializeTestingData();
		
		//Initialize log file
		System.out.println(this.nameOfServer + " server is up!");
		try{
			File f = new File(this.nameOfServer +"_log.txt");
			if(!f.exists())
				f.createNewFile();
		}catch(IOException e) {
			e.getMessage();
		}
	}
	
	/**
	 * Default Constuctor
	 */
	public LibraryServerReplica() {
			
		
		
	}
	
	//------------------------------------------Library Methods -----------------------------------------------------------------//

	/**
	 * Method that create accounts at local database.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param emailAddress the email address
	 * @param phoneNumber the phone number
	 * @param username the username
	 * @param password the password
	 * @param eduInstitution the edu institution
	 * @return a boolean indicate success or not
	 */
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String eduInstitution) {
				
		if(this.getStudent(username) == null) {
			
			Student student = new Student(firstName, lastName, emailAddress, phoneNumber, username, password, eduInstitution);
			this.addStudent(student);
						
			log(username, "Create a new account.");
			
			return ConstantValue.TRUE;
		}
		return ConstantValue.FALSE;
	}

	/**
	 * Reserve book from local library database.
	 *
	 * @param username the username
	 * @param password the password
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return true, if successful
	 * @see corbaLibrary.CorbaLibraryServerOperations#reserveBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String reserveBook(String username, String password, String bookName,String authorName)  {
		
		String message = null;
		
		//Check student exist or not
		Student student = this.getStudent(username);
		if (student == null) {
			message = "Student Client is NOT existed.";
			System.out.println(this.nameOfServer + ": " + message);
			return ConstantValue.FALSE;
		}
		
		//Check student password
		if (!student.getPassword().equals(password)) {
			message = "Wrong Password.";
			System.out.println(this.nameOfServer + ": " +message);
			return ConstantValue.FALSE;
		}
		
		//Check book exist or not
		Book book = this.getBook(bookName, authorName);
		if (book == null){
			message = "Book is NOT existed.";
			System.out.println(this.nameOfServer + ": " +message);
			return ConstantValue.FALSE;
		}
		
		synchronized (book) {
			
			//Check book availability
			if (book.getNumberCopies() - book.getNumberOfCopiesIsHolding() <= 0) {
				message = "No copies available.";
				System.out.println(this.nameOfServer + ": " +message);
				return ConstantValue.FALSE;
			}
			
			
			//Reserver the book
			student.getBooks().put(bookName, ConstantValue.DEFAULT_DURATION);
			
			book.setNumberCopies(book.getNumberCopies() -1);
			
			log(username, "Reserve a book. " + "Book name: "+ bookName + " Book author: " + authorName);
		
			message = "Reserve success.";
			return ConstantValue.TRUE;
		}
	}
	

	/**
	 * Check non returners from all the related server by UDP message, then return a String value indicates all non returners.
	 *
	 * @param adminUsername the admin username
	 * @param adminPassword the admin password
	 * @param eduInstitution the edu institution
	 * @param numDays the number of days
	 * @return the non retuners
	 * @see corbaLibrary.CorbaLibraryServerOperations#getNonRetuners(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays) {
		
		//Check administrator account information
		if(!adminUsername.equalsIgnoreCase("admin") || !adminPassword.equalsIgnoreCase("admin")) {
			return "Wrong Username or Password!";
		}
		
		//Create result string by combining value from each of the servers
		StringBuilder finalResult = new StringBuilder();
		finalResult.append(checkNonRetuners(numDays) + "\n");
		
		for(Map.Entry<Integer, String> info : UDPInfo.entrySet()) {
			if(info.getKey() != this.portOfUDP) {
				
				UDPSender sender = new UDPSender(info.getKey(), info.getValue());
				
				
				String resultFromOther = sender.sendMessage("0," + numDays);
				
				
					
				finalResult.append(resultFromOther + "\n");
				
				
			}
		}
		
		log(adminUsername, "Get non Returners for " + numDays + "days.");
		return finalResult.toString();
	}

	/**
	 * Debug method set duration.
	 *
	 * @param username the username
	 * @param bookName the book name
	 * @param numOfDays the num of days
	 * @return true, if successful
	 * @see corbaLibrary.CorbaLibraryServerOperations#setDuration(java.lang.String, java.lang.String, int)
	 */
	
	public String setDuration(String username, String bookName, int numOfDays) {
		
		Student student = this.getStudent(username);
		
		//Check if the student exists
		if(student == null) {
			return ConstantValue.FALSE;
		}
		synchronized(student) {
			if (student != null) {
				if(student.getBooks().containsKey(bookName)){
					student.getBooks().put(bookName, numOfDays);
					
					log(username, "set duration " +bookName + " " + student.getBooks().get(bookName)+ " "  + numOfDays + " days.");

					System.out.println(this.nameOfServer + " set duration: " +bookName + " " + student.getBooks().get(bookName));
					return ConstantValue.TRUE;
				}
			}
		}
		
		return ConstantValue.FALSE;
	}
	
	
	//--------------------------------------------Supporting methods ---------------------------------------------------------------//

	/**
	 * Check non retuners, called by UDP message, used to implement getNonReturners.
	 *
	 * @param numDays the num days
	 * @return the string
	 * @see getNonReturners
	 */
	public String checkNonRetuners(String numDays) {
		
		int numberOfDays = Integer.valueOf(numDays);
		StringBuilder sb = new StringBuilder();
		sb.append(this.getNameOfServer() + " : ");
		for(Map<String, Student> value: studentData.values()) {
			for(Student student: value.values()) {
				for(int duration: student.getBooks().values()) {
					if(duration - 14 == numberOfDays) {
						sb.append(student.getFirstName() + " " + student.getLastName() + " " + student.getPhoneNumber() + "\n");
					}
				}
			}
		}
		sb.append( "..." );
		return sb.toString();
		
	}
	
	/**
	 * Check book availability, called by UDP message, used to implement reserver inter-library.
	 *
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return true, if successful
	 * @see reserveInterLibrary
	 */
	public boolean checkBookAvailability (String bookName, String authorName) {
		
		
		Book book = this.getBook(bookName, authorName);
		
		if (book == null){
			
			return false;
		}
		
		synchronized (book) {
			if (book.getNumberCopies() - book.getNumberOfCopiesIsHolding() <= 0) {
				System.out.println(this.nameOfServer + " check book availability: " +"no copies book");

				return false;
			}
			
			book.setNumberOfCopiesIsHolding(book.getNumberOfCopiesIsHolding() + 1);
			
			log("Remote Library", "Local book pre-reserved by a remote library. " + "Book name: "+ bookName + " Book author: " + authorName);

			return true;
		}
	}
	
	/**
	 * Gets the student by username.
	 *
	 * @param username the username
	 * @return the student
	 */
	public Student getStudent(String username) {
		
		String firstChar = username.substring(0, 1);
		Map<String, Student> charNameList = studentData.get(firstChar);
		if(charNameList != null) {
			if(charNameList.get(username) != null) {
				return charNameList.get(username);
			}	
		}
		return null;	
	}
	
	/**
	 * Adds the student.
	 *
	 * @param student the student
	 */
	public void addStudent(Student student){
		String firstChar = student.getUserName().substring(0, 1);
		Map<String, Student> charNameList = studentData.get(firstChar);
		
		if (charNameList == null) {
			charNameList = new HashMap<String, Student>();
			studentData.put(firstChar, charNameList);
		}
		synchronized(charNameList) {
			charNameList.put(student.getUserName(), student);
			
		}	
	} 
	
	/**
	 * Gets the book by book name and author.
	 *
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return the book
	 */
	public Book getBook(String bookName, String authorName) {
		
		for(Book book: getBookshelf()) {
			if(book.getBookName().equalsIgnoreCase(bookName) && book.getBookAuthor().equalsIgnoreCase(authorName)) {
				return book;
			}
		}
		return null;
	}

	
	//------------------------------------------------Log----------------------------------------------------------------------//
	/**
	 * The logger method.
	 *
	 * @param username the username
	 * @param activity the activity
	 */
	public void log(String username,String activity)  {
		try{
			File f = new File(this.nameOfServer+"_log.txt");
			FileWriter fw = new FileWriter(f,true);
			String logString = new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) +" " +  username+": "+activity;
			System.out.println("[" + this.getNameOfServer() + "]: " + logString);
			fw.write(logString);
			fw.write("\r\n");
			fw.flush();
			fw.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

	//--------------------------------------------Getters & Setters -----------------------------------------------------------//

	/**
	 * Gets the name of server.
	 *
	 * @return the name of server
	 */
	public String getNameOfServer() {
		return nameOfServer;
	}

	/**
	 * Sets the name of server.
	 *
	 * @param nameOfServer the new name of server
	 */
	public void setNameOfServer(String nameOfServer) {
		this.nameOfServer = nameOfServer;
	}


	/**
	 * Gets the port of udp.
	 *
	 * @return the port of udp
	 */
	public int getPortOfUDP() {
		return portOfUDP;
	}

	/**
	 * Sets the port of udp.
	 *
	 * @param portOfUDP the new port of udp
	 */
	public void setPortOfUDP(int portOfUDP) {
		this.portOfUDP = portOfUDP;
	}

	/**
	 * Gets the socket.
	 *
	 * @return the socket
	 */
	public ReplicaUDPListener getSocket() {
		return socket;
	}

	/**
	 * Sets the socket.
	 *
	 * @param socket the new socket
	 */
	public void setSocket(ReplicaUDPListener socket) {
		this.socket = socket;
	}
	
	/**
	 * Gets the bookshelf.
	 *
	 * @return the bookshelf
	 */
	public List<Book> getBookshelf() {
		return bookshelf;
	}

	/**
	 * Sets the bookshelf.
	 * @param bookshelf the new bookshelf
	 */
	public void setBookshelf(List<Book> bookshelf) {
		this.bookshelf = bookshelf;
	}
	
	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	

	public int getHeartBeatListenPort() {
		return heartBeatListenPort;
	}

	public void setHeartBeatListenPort(int heartBeatListenPort) {
		this.heartBeatListenPort = heartBeatListenPort;
	}

	public int getReplicaID() {
		return replicaID;
	}

	public void setReplicaID(int replicaID) {
		this.replicaID = replicaID;
	}

	public int getRmPort() {
		return rmPort;
	}

	public void setRmPort(int rmPort) {
		this.rmPort = rmPort;
	}

	public String getRmIpAddress() {
		return rmIpAddress;
	}

	public void setRmIpAddress(String rmIpAddress) {
		this.rmIpAddress = rmIpAddress;
	}
	
	//--------------------------------------------------Run----------------------------------------------------------------//
	
	@Override
	public void run() {
		this.socket = new ReplicaUDPListener(this);
		this.heartBeatSocket = new Heartbeat(this);
		heartBeatSocket.start();
		socket.start();	
	}
	
	//--------------------------------------------------Main--------------------------------------------------------------//
	
	public static void main(String args[]){
		
		LibraryServerInfo info1 = new LibraryServerInfo(1,
														ConstantValue.CONCORDIA,
														ConstantValue.COCORDIA_UDP_PORT,
														ConstantValue.CONCORDIA_IP_ADDRESS,
														ConstantValue.CONCORDIA_HEARTBEAT_PORT,
														ConstantValue.COCORDIA_HEARTBEAT_RM_PORT,
														ConstantValue.CONCORDIA_RM_IP_ADDRESS);
		
		
		
		LibraryServerReplica concordia = new LibraryServerReplica(info1);
		concordia.initializeTestingData();
		
		
		Thread library = new Thread(concordia);
		library.start();
			
		
		
	}

	//------------------Initialize Testing Data-------------------------------------------------------------------------------//
	
	public void initializeTestingData() {
		Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "12345678", "Concordia");
		this.addStudent(student);
		student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "Concordia");
		this.addStudent(student);
		student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "Concordia");
		this.addStudent(student);
		
		
		
		Book book = new Book("AAA","BBB",Integer.MAX_VALUE);
		getBookshelf().add(book);
		book = new Book("CCC","DDD",Integer.MAX_VALUE);
		getBookshelf().add(book);
		book = new Book("EEE","FFF",Integer.MAX_VALUE);
		getBookshelf().add(book);
		book = new Book("GGG","HHH",3000);
		getBookshelf().add(book);
		book = new Book("III","JJJ",3000);
		getBookshelf().add(book);
		
	}

	
}
