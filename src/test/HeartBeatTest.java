package test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Test;

import raplicamanager.*;


public class HeartBeatTest {

	private static ReplicaManager rm;
	
	
	@Test
	public void testHeartBeat ()  throws Exception
	{
		rm =  new ReplicaManager("1", 6001, "localhost", "Replica1", 7001, "localhost", 6501);
	}
	
	
	@Test
	public void UdpListener()  throws Exception
	{
		System.out.println("listening to socket");
		DatagramSocket socket = null;
		String responseMessageString = "";
		
		try {
			socket = new DatagramSocket(6501, InetAddress.getByName("localhost"));		
			byte[] buffer = new byte[1000];
			
			while(true) {

				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
								
				byte[] message = requestPacket.getData();
				
				responseMessageString = rm.getReplicaManagerID(); // getting current replica manager ID
				message = responseMessageString.getBytes();
				
				
				DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
				socket.send(responsePacket);
				System.out.println(responsePacket);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
	}
	
}
