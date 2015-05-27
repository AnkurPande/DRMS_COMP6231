package server;

import java.net.DatagramSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import models.Book;
import models.Student;

public class LibraryServer implements LibraryServerInterface, Runnable {
	
	private String nameOfServer;
	private int portOfRMI;
	private int portOfUDP;
	
	private UDPSocket1 socket;
	
	private static List<Book> bookshelf;
	private static Map<String, Map<String, Student>> studentData;
	public static final int DEFAULT_DURATION = 14;
	
	
	
	
	public LibraryServer(String nameOfServer, int portOfRMI, int portOfUDP) {
		
		this.nameOfServer = nameOfServer;
		this.portOfRMI = portOfRMI;
		this.portOfUDP = portOfUDP;
		
	}

	@Override
	public boolean createAccount(String firstName, String lastName,
			String emailAddress, String phoneNumber, String username,
			String password, String eduInstitution) {
		
		if(this.getStudent(username) == null) {
			
			Student student = new Student(firstName, lastName, emailAddress, phoneNumber, username, password, eduInstitution);
			this.addStudent(student);
			return true;
		}
		return false;
	}

	@Override
	public String reserveBook(String username, String password,
			String bookName, String authorName) throws RemoteException {
		
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
			
			message = "Reserve success.";
			System.out.println(message);
			return message;
		}
	}

	@Override
	public String getNonRetuners(String adminUsername, String adminPassword)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setDuration(String username, String bookName, int numOfDays)
			throws RemoteException {
		
		Student student = this.getStudent(username);
		if (student != null) {
			if(student.getBooks().containsKey(bookName)){
				student.getBooks().put(bookName, numOfDays);
				return true;
			}
		}
		return false;
	}
	
	public String checkNonRetuners(String numDays) {
		// TODO Auto-generated method stub
		return null;
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
		
		for(Book book: bookshelf) {
			if(book.getBookName().equalsIgnoreCase(bookName)) {
				return book;
			}
		}
		return null;
	}

	@Override
	public void run() {
		try{
			Remote obj = UnicastRemoteObject.exportObject(this, this.portOfRMI);
			Registry registry = LocateRegistry.createRegistry(this.portOfRMI);
			registry.bind(this.nameOfServer, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		this.socket = new UDPSocket1(this);
		socket.start();
		
		
	}
	
	public static void main(String[] args) {
		
		LibraryServer serverOfConcordia = new LibraryServer("Concordia", 4444, 4445);
		new Thread(serverOfConcordia).start();
		LibraryServer serverOfMcGill = new LibraryServer("McGill", 4446, 4447);
		new Thread(serverOfMcGill).start();
		LibraryServer serverOfUdeM = new LibraryServer("UdeM", 4448, 4449);
		new Thread(serverOfUdeM).start();
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

	public UDPSocket1 getSocket() {
		return socket;
	}

	public void setSocket(UDPSocket1 socket) {
		this.socket = socket;
	}

	public static List<Book> getBookshelf() {
		return bookshelf;
	}

	public static void setBookshelf(List<Book> bookshelf) {
		LibraryServer.bookshelf = bookshelf;
	}

	public static Map<String, Map<String, Student>> getStudentData() {
		return studentData;
	}

	public static void setStudentData(Map<String, Map<String, Student>> studentData) {
		LibraryServer.studentData = studentData;
	}

	
}
