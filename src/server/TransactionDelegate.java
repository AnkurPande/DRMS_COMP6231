package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class TransactionDelegate {
	
	private int targetUdpPort;
	private String targetIpAddress;
	
	private LibraryServer server;
	
	private String bookName;
	private String bookAuthor;
	
	private String sutdentUsername;
	
	public TransactionDelegate(String address, int port, LibraryServer server) {
		
		this.setTargetIpAddress(address);
		this.setTargetUdpPort(port);
		this.setServer(server);
		
	}
	
	public boolean bookIsReadyToReserve(String username, String bookName, String authorName) {
		
		this.setBookName(bookName);
		this.setBookAuthor(authorName);
		this.setSutdentUsername(username);
		
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(this.getTargetIpAddress());
			
			byte[] udpMessage = ("1," + bookName + "," + authorName).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, this.getTargetUdpPort());
			socket.send(sendPacket);
			
			byte[] buffer = new byte[1000];
			DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
			socket.receive(receivedPacket);
			
			String result = new String(receivedPacket.getData());
			
			if(result.trim().equalsIgnoreCase("true")) {
				
				
				try{

					if(!server.serverIsAlive(username, bookName)) {
						this.rollBack();
					}
				}catch(Exception e) {
					this.rollBack();
				}
				return true;
			}
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
		return false;
	}
	
	public boolean rollBack() {
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(this.getTargetIpAddress());
			
			byte[] udpMessage = ("2," + bookName + "," + bookAuthor).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, this.getTargetUdpPort());
			socket.send(sendPacket);
			
			byte[] buffer = new byte[1000];
			DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
			socket.receive(receivedPacket);
			
			String result = new String(receivedPacket.getData());
			
			if(result.trim().equalsIgnoreCase("true")) {
				
				
				
				return true;
			}
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
		return false;
	}
	
	public void performInterLibraryReservation() {
		
		
		server.updateStudentDataFromInterLibraryReservation(this.getSutdentUsername(), this.getBookName());
	}
	
	public int getTargetUdpPort() {
		return targetUdpPort;
	}
	
	public void setTargetUdpPort(int targetUdpPort) {
		this.targetUdpPort = targetUdpPort;
	}
	
	public String getTargetIpAddress() {
		return targetIpAddress;
	}
	
	public void setTargetIpAddress(String targetIpAddress) {
		this.targetIpAddress = targetIpAddress;
	}

	public LibraryServer getServer() {
		return server;
	}

	public void setServer(LibraryServer server) {
		this.server = server;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getSutdentUsername() {
		return sutdentUsername;
	}

	public void setSutdentUsername(String sutdentUsername) {
		this.sutdentUsername = sutdentUsername;
	}
}
