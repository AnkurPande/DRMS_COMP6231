package Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReplicaManager implements Runnable {
		
	public String CURRENT_RM_ID;
	public int CURRENT_RM_PORT;
	public String CURRENT_RM_IP;

	
	/* CONSTRUCTOR */
	ReplicaManager(String RmID, int RmPort, String RmIP) {
		this.CURRENT_RM_ID = RmID;
		this.CURRENT_RM_PORT = RmPort;
		this.CURRENT_RM_IP = RmIP;
	}
	
	
	@Override
	public void run()
	{
		handleHeartBeatResponse();
	}
	
	
	/**
	 * This method will get the current Replica Manager's ID
	 *
	 * @param nothing
	 * @return CURRENT_RM_ID
	 */
	public String getReplicaManagerID() {
		return this.CURRENT_RM_ID;		
	}
		 
	
	/**
	 * This method will receive a response from heart beat response and return replica manager ID
	 *
	 * @param 
	 * @return a string value
	 */
	public void handleHeartBeatResponse()	{
		DatagramSocket socket = null;
		String responseMessageString = "";
		
		try {
			socket = new DatagramSocket(this.CURRENT_RM_PORT, InetAddress.getByName(this.CURRENT_RM_IP));		
			byte[] buffer = new byte[1000];
			
			while(true) {
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
								
				byte[] message = requestPacket.getData();
				
				responseMessageString = this.getReplicaManagerID(); // getting current replica manager ID
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
	
	
	public static void main(String [] args)
	{
		ReplicaManager r1 = new ReplicaManager("1", 5001, "localhost");
		Thread t1 = new Thread(r1);
		t1.run();
		
		ReplicaManager r2 = new ReplicaManager("2", 5002, "localhost");
		Thread t2 = new Thread(r2);
		t2.run();	
		
		ReplicaManager r3 = new ReplicaManager("3", 5003, "localhost");
		Thread t3 = new Thread(r3);
		t3.run();	
	}
	
}
