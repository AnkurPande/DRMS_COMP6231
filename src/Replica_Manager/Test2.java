package Replica_Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Test2 {
	
	
	public static void main(String[] args) {
		
		String host= "234.1.2.1";
		try {
			
			MulticastSocket socket = new MulticastSocket(4001);
			
			socket.joinGroup(InetAddress.getByName(host));
			
			for (int i = 0;  i < 5 ; ++i)
			{
				int e = i + 1;
				byte[] udpMessage = ("1," + e + ",1,1").getBytes();
				DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, InetAddress.getByName(host), 4001);
				socket.send(sendPacket);
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
