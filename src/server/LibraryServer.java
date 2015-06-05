package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Book;
import models.Student;
/**
 * 
 * @author Haiyang Sun
 *
 */
public class LibraryServer implements LibraryServerInterface, Runnable {
	
	
	private String nameOfServer;
	private int portOfRMI;
	private int portOfUDP;
	
	private UDPSocket socket;
	
	private  List<Book> bookshelf = new ArrayList<Book>();
	private  Map<String, Map<String, Student>> studentData = new HashMap<String, Map<String, Student>>();
	private  List<Integer> listOfUDPPorts = new ArrayList<Integer>();
	public static final int DEFAULT_DURATION = 14;
	public static final int PORT_OF_RMI = 4444;
	
	
	
	
	public LibraryServer(String nameOfServer, int portOfUDP) {
		
		this.nameOfServer = nameOfServer;
		this.portOfUDP = portOfUDP;
		
		this.listOfUDPPorts.add(4445);
		this.listOfUDPPorts.add(4447);
		this.listOfUDPPorts.add(4449);
		
		initializeTestingData();
		
		System.out.println(this.nameOfServer + " server is up!");
		try{
			File f = new File(this.nameOfServer +"_log.txt");
			if(!f.exists())
				f.createNewFile();
		}catch(IOException e) {
			e.getMessage();
		}
			
	}
	
	@Override
	public boolean createAccount(String firstName, String lastName,
			String emailAddress, String phoneNumber, String username,
			String password, String eduInstitution) {
		
		if(this.getStudent(username) == null) {
			
			Student student = new Student(firstName, lastName, emailAddress, phoneNumber, username, password, eduInstitution);
			this.addStudent(student);
			
			System.out.println(this.nameOfServer + " server create a new account: " + username);
			
			log(username, "Create a new account.");
			
			return true;
		}
		return false;
	}

	@Override
	public String reserveBook(String username, String password,
			String bookName, String authorName)  {
		
		String message = null;
		
		Student student = this.getStudent(username);
		if (student == null) {
			message = "Student Client is NOT existed.";
			System.out.println(message);
			return message;
		}
		
		if (!student.getPassword().equals(password)) {
			message = "Wrong Password.";
			System.out.println(message);
			return message;
		}
		
		Book book = this.getBook(bookName);
		if (book == null){
			message = "Book is NOT existed.";
			System.out.println(message);
			return message;
		}
		
		synchronized (book) {
			if (book.getNumberCopies() <= 0) {
				message = "No copies available.";
				System.out.println(message);
				return message;
			}
			
			student.getBooks().put(bookName, DEFAULT_DURATION);
			
			book.setNumberCopies(book.getNumberCopies() -1);
			
			log(username, "Reserve a book. " + "Book name: "+ bookName + " Book author: " + authorName);
		
			message = "Reserve success.";
			System.out.println(message);
			return message;
		}
	}

	@Override
	public  String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays) {
		
		if(!adminUsername.equalsIgnoreCase("admin") || !adminPassword.equalsIgnoreCase("admin")) {
			return "Wrong Username or Password!";
		}
		
		
		StringBuilder finalResult = new StringBuilder();
		finalResult.append(checkNonRetuners(numDays) + "\n");
		
		for(int port: listOfUDPPorts) {
			if(port != this.portOfUDP) {
				
				DatagramSocket socket = null;
				String resultFromOther = "";
				
				try {
					socket = new DatagramSocket();
					InetAddress host = InetAddress.getByName("localhost");
					
					byte[] message = numDays.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(message, message.length, host, port);
					socket.send(sendPacket);
					
					byte[] buffer = new byte[1000];
					DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
					socket.receive(receivedPacket);
					
					resultFromOther = new String(receivedPacket.getData());
					
					finalResult.append(resultFromOther + "\n");
				
				} catch (SocketException e) {
					System.out.println("Socket: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("IO: " + e.getMessage());
				} finally {
					if (socket != null) socket.close();
				}
			}
		}
		
		log(adminUsername, "Get non Returners for " + numDays + "days.");
		return finalResult.toString();
	}

	@Override
	public boolean setDuration(String username, String bookName, int numOfDays)
			throws RemoteException {
		
		Student student = this.getStudent(username);
		synchronized(student) {
			if (student != null) {
				if(student.getBooks().containsKey(bookName)){
					student.getBooks().put(bookName, numOfDays);
					
					System.out.println(bookName + " " + student.getBooks().get(bookName));
					return true;
				}
			}
		}
		
		return false;
	}
	
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
	
	//------------DATA-------------
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
	
	public Book getBook(String bookName) {
		
		for(Book book: getBookshelf()) {
			if(book.getBookName().equalsIgnoreCase(bookName)) {
				return book;
			}
		}
		return null;
	}

	@Override
	public void run() {
		this.socket = new UDPSocket(this);
		socket.start();	
	}
	
	public static void main(String[] args) {
		
		
		LibraryServer serverOfConcordia = new LibraryServer("Concordia", 4445);
		new Thread(serverOfConcordia).start();
		LibraryServer serverOfMcGill = new LibraryServer("McGill", 4447);
		new Thread(serverOfMcGill).start();
		LibraryServer serverOfUdeM = new LibraryServer("UdeM", 4449);
		new Thread(serverOfUdeM).start();
		
		try{
			Registry registry = LocateRegistry.createRegistry(PORT_OF_RMI);
			
			Remote obj = UnicastRemoteObject.exportObject(serverOfConcordia, PORT_OF_RMI);
			registry.bind(serverOfConcordia.nameOfServer, obj);
			System.out.println(serverOfConcordia.nameOfServer + " Server is running!");

			obj = UnicastRemoteObject.exportObject(serverOfMcGill, PORT_OF_RMI);
			registry.bind(serverOfMcGill.nameOfServer, obj);
			System.out.println(serverOfMcGill.nameOfServer + " Server is running!");

			obj = UnicastRemoteObject.exportObject(serverOfUdeM, PORT_OF_RMI);
			registry.bind(serverOfUdeM.nameOfServer, obj);
			System.out.println(serverOfUdeM.nameOfServer + " Server is running!");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	//------------Log----------------
	public void log(String username,String activity)  {
		try{
			File f = new File(this.nameOfServer+"_log.txt");
			FileWriter fw = new FileWriter(f,true);
			fw.write(new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(new Date()) +" " +  username+": "+activity);
			fw.write("\r\n");
			fw.flush();
			fw.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

	//------------Getters & Setters ----------------

	public String getNameOfServer() {
		return nameOfServer;
	}

	public void setNameOfServer(String nameOfServer) {
		this.nameOfServer = nameOfServer;
	}

	public int getPortOfRMI() {
		return portOfRMI;
	}

	public void setPortOfRMI(int portOfRMI) {
		this.portOfRMI = portOfRMI;
	}

	public int getPortOfUDP() {
		return portOfUDP;
	}

	public void setPortOfUDP(int portOfUDP) {
		this.portOfUDP = portOfUDP;
	}

	public UDPSocket getSocket() {
		return socket;
	}

	public void setSocket(UDPSocket socket) {
		this.socket = socket;
	}
	
	public List<Book> getBookshelf() {
		return bookshelf;
	}

	public void setBookshelf(List<Book> bookshelf) {
		this.bookshelf = bookshelf;
	}

	//-------------Initialize Testing Data-------------------
	public void initializeTestingData() {
		if(this.nameOfServer.equalsIgnoreCase("Concordia")) {
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "xxxxxx", "Concordia");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "Concordia");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "Concordia");
			this.addStudent(student);
		}else if (this.nameOfServer.equalsIgnoreCase("McGill")) { 
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "xxxxxx", "McGill");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "McGill");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "McGill");
			this.addStudent(student);
		}else if (this.nameOfServer.equalsIgnoreCase("UdeM")) {
			Student student = new Student("Aaa", "Bbb", "cc@cccc.cc", "51411111111", "aaabbb", "xxxxxx", "UdeM");
			this.addStudent(student);
			student = new Student("Bbb","Ccc", "dd@dddd.dd","5141111111", "bbbccc", "xxxxx", "UdeM");
			this.addStudent(student);
			student = new Student("Ccc","Ddd", "dd@dddd.dd","5141111111", "cccddd", "xxxxx", "UdeM");
			this.addStudent(student);
		}
		
		Book book = new Book("AAA","BBB",1000);
		getBookshelf().add(book);
		book = new Book("CCC","DDD",3000);
		getBookshelf().add(book);
		book = new Book("EEE","FFF",3000);
		getBookshelf().add(book);
		book = new Book("GGG","HHH",3000);
		getBookshelf().add(book);
		book = new Book("III","JJJ",3000);
		getBookshelf().add(book);
		
		
	}

	
}
