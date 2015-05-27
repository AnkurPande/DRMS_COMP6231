package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSocket1 extends Thread {
	
	private LibraryServer server;
	
	public UDPSocket1(LibraryServer server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(server.getPortOfUDP());
			byte[] buffer = new byte[200];
			
			while(true){
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message);

				String numDays = receivedMessageString.trim();
				String responseMessageString = server.checkNonRetuners(numDays);
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
