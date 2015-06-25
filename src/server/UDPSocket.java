package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
			
			
			socket = new DatagramSocket(server.getPortOfUDP());
			byte[] buffer = new byte[1000];
			
			while(true){
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message);
				
				String[] requestParts = receivedMessageString.split(":");
				
				if(requestParts.length == 2 ) {
					//non return request
					String numDays = requestParts[1].trim();
					responseMessageString = server.checkNonRetuners(numDays);
				}
				else {
					//reserve request
					responseMessageString = server.checkBookAvailability(requestParts[1].trim(),requestParts[2].trim())?"true":"false";
					
				}
				
				message = responseMessageString.getBytes();
				
				DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
				socket.send(responsePacket);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
	}
}
