package replica;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Heartbeat extends Thread{

private LibraryServerReplica server;
	
	//--------------------------------------------Constructor------------------------------------------------------------------//
	
	public Heartbeat(LibraryServerReplica server) {
		this.server = server;
	}
	
	//-----------------------------------------------Run-----------------------------------------------------------------------//
	
	@Override
	public void run() {
		
		DatagramSocket socket = null;
		String responseMessageString = "";
		
		try {
			socket = new DatagramSocket(server.getHeartBeatListenPort(), InetAddress.getByName(server.getIpAddress()));		
			byte[] buffer = new byte[1000];
			
			while(true) {

				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
								
				byte[] message = requestPacket.getData();
				
				responseMessageString = "true"; 
				message = responseMessageString.getBytes();
				
				
				DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
				socket.send(responsePacket);
			}
		} catch (Exception e) {
			
		} finally {
			if (socket != null) socket.close();
		}

	}
}
