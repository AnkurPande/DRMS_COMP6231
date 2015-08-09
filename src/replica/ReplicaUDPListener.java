package replica;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import udp.UDPSender;



/**
 * @author Ankurp
 *
 */
public class ReplicaUDPListener extends Thread {
	
	private LibraryServerReplica server;
	
	public static final String FRONTEND_HOST = "localhost";
	
	public static final int FRONTEND_UDP_PORT = 3100;
	
	public ReplicaUDPListener(LibraryServerReplica server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		String responseMessageString = "";
		UDPSender frontEndSender = null;
		UDPSender heartBeatSender = null;
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
					frontEndSender.sendOnly(responseMessageString);
				}
				else if(requestParts[1].equals("isAlive")) {
					String heartBeatMessage = "";
					heartBeatSender.sendMessage(heartBeatMessage);
				}
							
			}
			
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			if (socket != null) socket.close();
		}
	}
}