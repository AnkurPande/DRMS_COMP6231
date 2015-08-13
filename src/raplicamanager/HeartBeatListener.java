package raplicamanager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartBeatListener extends Thread {
	
	ReplicaManager rm;
	
	public HeartBeatListener (ReplicaManager rm) {
		this.rm = rm;
	}
	
	@Override
	public void run() {
		
		
		DatagramSocket socket = null;
		String responseMessageString = "";
		
		try {
			socket = new DatagramSocket(rm.CURRENT_RM_PORT, InetAddress.getByName(rm.CURRENT_RM_IP));		
			byte[] buffer = new byte[1000];
			
			while(true) {

				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
								
				byte[] message = requestPacket.getData();
				
				responseMessageString = rm.getReplicaManagerID(); // getting current replica manager ID
				message = responseMessageString.getBytes();
				
				System.out.println(responseMessageString);
				
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
 