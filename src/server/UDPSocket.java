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
	
	public static final String FRONTEND_HOST = "localhost";
	
	public static final int FRONTEND_UDP_PORT = 3100;
	
	public UDPSocket(LibraryServer server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		String responseMessageString = "";
		UDPSender frontEndSender = null;
		try {
			//Initialize sender to send response to front end
			frontEndSender = new UDPSender(FRONTEND_UDP_PORT,FRONTEND_HOST);
			
			//Initialize socket to receive response
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
				else 
				{
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
				frontEndSender.sendOnly(responseMessageString);
			}
			
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			if (socket != null) socket.close();
		}
	}
}