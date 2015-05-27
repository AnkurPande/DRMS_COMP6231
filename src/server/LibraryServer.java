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

public class LibraryServer implements LibraryServerInterface, Runnable {
	
	private String nameOfServer;
	private int portOfRMI;
	private int portOfUDP;
	
	private UDPSocket1 socket;
	
	private static List<Book> bookshelf;
	private static Map<String, Map<String, Student>> studentData;
	private static List<Integer> listOfUDPPorts = new ArrayList<Integer>();
	public static final int DEFAULT_DURATION = 14;
	
	
	
	
	public LibraryServer(String nameOfServer, int portOfRMI, int portOfUDP) {
		
		this.nameOfServer = nameOfServer;
		this.portOfRMI = portOfRMI;
		this.portOfUDP = portOfUDP;
		
		LibraryServer.listOfUDPPorts.add(4445);
		LibraryServer.listOfUDPPorts.add(4447);
		LibraryServer.listOfUDPPorts.add(4449);
		
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
			
			log(username, "Create a new account.");
			
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
			
			log(username, "Reserve a book. " + "Book name: "+ bookName + " Book author: " + authorName);
			
			message = "Reserve success.";
			System.out.println(message);
			return message;
		}
	}

	@Override
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays)
			throws RemoteException {
		// check admin password
		
		
		StringBuilder finalResult = new StringBuilder();
		finalResult.append(checkNonRetuners(numDays));
		
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
					finalResult.append(resultFromOther);
				
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
		if (student != null) {
			if(student.getBooks().containsKey(bookName)){
				student.getBooks().put(bookName, numOfDays);
				return true;
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
		sb.append( "..." + "\n");
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
