package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Ankurp
 *
 */
public class UDPSocket extends Thread {
	
	private LibraryServer server;
	
	public UDPSocket(LibraryServer server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		String responseMessageString = "";
		try {
			
			
			socket = new DatagramSocket(server.getPortOfUDP(), InetAddress.getByName(server.getIpAddress()));
			byte[] buffer = new byte[1000];
			
			while(true){
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message).trim();
				
				String[] requestParts = receivedMessageString.split(",");
				
				if(requestParts.length == 2 ) {
					//non return request
					String numDays = requestParts[1].trim();
					responseMessageString = server.checkNonRetuners(numDays);
				}
				else {
					/*
					 * reserve request
					 * 
					 * Prefix Definition
					 * 1 -- check book availability
					 * 2 -- release book
					 * 3 -- confirm remote reservation
					 */
					if(requestParts[0].trim().equalsIgnoreCase("1")) {
						responseMessageString = server.checkBookAvailability(requestParts[1].trim(),requestParts[2].trim())?"true":"false";
					}
					if(requestParts[0].trim().equalsIgnoreCase("2")) {
						server.releaseBook(requestParts[1].trim(),requestParts[2].trim());
						responseMessageString = "true";

					}
					if(requestParts[0].trim().equalsIgnoreCase("3")) {

						server.confirmRemoteReservation(requestParts[1].trim(),requestParts[2].trim());
						responseMessageString = "true";
					}
					
				}
				
				message = responseMessageString.getBytes();
				
				DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
				socket.send(responsePacket);
			}
			
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			if (socket != null) socket.close();
		}
	}
}
