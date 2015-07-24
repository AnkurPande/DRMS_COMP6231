package server;

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
import model.LibraryServerInfo;


/**
 * The Class LibraryServer.
 *
 * @author Haiyang Sun
 */


public class LibraryServer implements LibraryServerInterface, Runnable {
	
	/*
	 * Properties of server
	 */
	private String nameOfServer;

	private int portOfUDP;
	
	private UDPSocket socket;
	
	private  List<Book> bookshelf = new ArrayList<Book>();
	
	private  Map<String, Map<String, Student>> studentData = new HashMap<String, Map<String, Student>>();
	
	private  Map<Integer, String> UDPInfo = new HashMap<Integer, String>();
	
	private String ipAddress;
	
	private TransactionDelegate currentDelegate;
	
	
	/**
	 * The inner class ConstantValue stores all constant values related to server implementation.
	 * 
	 * @author Haiyang Sun
	 */
	public class ConstantValue {
		
		
		//Institution names
		public static final String CONCORDIA = "Concordia";
		
		public static final String MCGILL = "McGill";
		
		public static final String UDEM = "UdeM";
		
		//Default Duration
		public static final int DEFAULT_DURATION = 14;
		
		//UDP Ports
		public static final int COCORDIA_UDP_PORT = 4445;
		
		public static final int MCGILL_UDP_PORT = 4447;
		
		public static final int UDEM_UDP_PORT = 4449;
		
//		public static final String CONCORDIA_IP_ADDRESS = "132.205.94.90";
//		public static final String MCGILL_IP_ADDRESS = "132.205.94.89";
//		public static final String UDEM_IP_ADDRESS = "132.205.94.89";
		
		public static final String CONCORDIA_IP_ADDRESS = "localhost";
		
		public static final String MCGILL_IP_ADDRESS = "localhost";
		
		public static final String UDEM_IP_ADDRESS = "localhost";
		
		public static final String TRUE = "true";
		
		public static final String FALSE = "false";
	}


	
	/*
	 * Constructors
	 */
	
	/**
	 * Instantiates a new library server.
	 *
	 * @param info the info
	 */
	public LibraryServer(LibraryServerInfo info) {
		
		this.nameOfServer = info.getServerName();
		this.portOfUDP = info.getPortOfUDP();
		this.ipAddress = info.getIpAddress();
		
		//Initialize list of UDPPorts
		this.UDPInfo.put(ConstantValue.COCORDIA_UDP_PORT, ConstantValue.CONCORDIA_IP_ADDRESS);
		this.UDPInfo.put(ConstantValue.MCGILL_UDP_PORT, ConstantValue.MCGILL_IP_ADDRESS);
		this.UDPInfo.put(ConstantValue.UDEM_UDP_PORT, ConstantValue.UDEM_IP_ADDRESS);
		
		
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
	public LibraryServer() {
			
		//Initialize list of UDPPorts
		this.UDPInfo.put(ConstantValue.COCORDIA_UDP_PORT, ConstantValue.CONCORDIA_IP_ADDRESS);
		this.UDPInfo.put(ConstantValue.MCGILL_UDP_PORT, ConstantValue.MCGILL_IP_ADDRESS);
		this.UDPInfo.put(ConstantValue.UDEM_UDP_PORT, ConstantValue.UDEM_IP_ADDRESS);
		
	}
	
	

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
	@Override
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
	@Override
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
	 * Reserve inter library, First, check the book availability at local database, if not available, try to reserver from another library server.
	 *
	 * @param username the username
	 * @param password the password
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return true, if successful
	 * @see corbaLibrary.CorbaLibraryServerOperations#reserveInterLibrary(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String reserveInterLibrary(String username, String password, String bookName, String authorName) {
		

		String message = null;
		
		Student student = this.getStudent(username);
		
		//Check student exist or not
		if (student == null) {
			message = "Student Client is NOT existed.";
			System.out.println(this.nameOfServer + ": " +message);
			return ConstantValue.FALSE;
		}
		
		//Check student password
		if (!student.getPassword().equals(password)) {
			message = "Wrong Password.";
			System.out.println(this.nameOfServer + ": " +message);
			return ConstantValue.FALSE;
		}
	
		//Try to reserve at local library
		if(this.reserveBook(username, password, bookName, authorName).equalsIgnoreCase(ConstantValue.TRUE)) {
			return ConstantValue.TRUE;
		} else {
			
			//Try to reserver from a remote library
			for(Map.Entry<Integer, String> info : UDPInfo.entrySet()) {
				
				if(!info.getKey().equals(this.portOfUDP)) {
					
					currentDelegate = new TransactionDelegate(info.getValue(),info.getKey(),this);
					boolean bookIsReadyToReserve = currentDelegate.bookIsReadyToReserve(username, bookName, authorName);
					
					if(bookIsReadyToReserve) {
						return ConstantValue.TRUE;
					}
					
				}
			}
		}
		return ConstantValue.FALSE;
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
	
	@Override
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
	@Override
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
	
	
	//------------Supporting methods -------------

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
	 * Server is alive.
	 *
	 * @param username the username
	 * @param bookName the book name
	 * @return true, if successful
	 */
	public boolean serverIsAlive(String username, String bookName) {
		
		currentDelegate.performInterLibraryReservation();
		return true;
	}
	
	/**
	 * Update student data from inter library reservation.
	 *
	 * @param username the username
	 * @param bookName the book name
	 * @param authorName the author name
	 */
	public void updateStudentDataFromInterLibraryReservation(String username, String bookName, String authorName) {
		
		Student student = this.getStudent(username);
		student.getBooks().put(bookName, ConstantValue.DEFAULT_DURATION);
		log(username, "Reserve a book from remote library. " + "Book name: "+ bookName + " Book author: " + authorName);

	}
	
	/**
	 * Release book.
	 *
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return true, if successful
	 */
	public boolean releaseBook(String bookName, String authorName) {
		
		Book book = this.getBook(bookName, authorName);

		synchronized(book) {
			
			book.setNumberOfCopiesIsHolding(book.getNumberOfCopiesIsHolding()-1);
		}
		
		log("Remote Library", "Pre-reserved book released. " + "Book name: "+ bookName + " Book author: " + authorName);

		return true;
		
	}
	
	/**
	 * Confirm remote reservation.
	 *
	 * @param bookName the book name
	 * @param bookAuthor the book author
	 */
	public void confirmRemoteReservation(String bookName, String bookAuthor) {
		
		Book book = this.getBook(bookName, bookAuthor);
		
		synchronized(book) {
			book.setNumberOfCopiesIsHolding(book.getNumberOfCopiesIsHolding()-1);
			book.setNumberCopies(book.getNumberCopies() -1);

		}
		
		log("Remote Library", "Local book reservation confirmed by a remote library. " + "Book name: "+ bookName + " Book author: " + bookAuthor);

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

	

	//------------Run----------------
	
	@Override
	public void run() {
		
		
		this.socket = new UDPSocket(this);
		socket.start();	
	}
	
	//------------Log----------------
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
	

	//------------Getters & Setters ----------------

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
	public UDPSocket getSocket() {
		return socket;
	}

	/**
	 * Sets the socket.
	 *
	 * @param socket the new socket
	 */
	public void setSocket(UDPSocket socket) {
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
	 *
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


	/*
	 * Initialize testing data.
	 */
	public void initializeTestingData() {
		if(this.nameOfServer.equalsIgnoreCase("Concordia")) {
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "12345678", "Concordia");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "Concordia");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "Concordia");
			this.addStudent(student);
		}else if (this.nameOfServer.equalsIgnoreCase("McGill")) { 
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "12345678", "McGill");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "McGill");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "McGill");
			this.addStudent(student);
		}else if (this.nameOfServer.equalsIgnoreCase("UdeM")) {
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "12345678", "UdeM");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "UdeM");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "UdeM");
			this.addStudent(student);
		}
		
		if(this.getNameOfServer().equalsIgnoreCase("McGill")) {
			Book book = new Book("testbook", "testauthor", Integer.MAX_VALUE);
			this.getBookshelf().add(book);
			
		}
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
