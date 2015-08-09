package replica;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


import udp.UDPSender;

public class Heartbeat extends Thread{

private LibraryServerReplica server;
	
	private int heartBeatPort ;
	private int heartBeatReplyPort;
	
	public int getHeartBeatPort() {
		return heartBeatPort;
	}
	public void setHeartBeatPort(int heartBeatPort) {
		this.heartBeatPort = heartBeatPort;
	}
	
	public Heartbeat(LibraryServerReplica server) {
		this.server = server;
		this.heartBeatPort = server.getHeartBeatListenPort();
		this.heartBeatReplyPort = server.getHeartBeatReplyPort();
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		String responseMessageString = "";
		UDPSender heartBeatSender = null;
		try {
			
			
			//Initialize socket to receive response
			socket = new DatagramSocket(this.getHeartBeatPort(), InetAddress.getByName(server.getIpAddress()));
			byte[] buffer = new byte[1000];
			
			while(true){
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message).trim();
				
				String[] requestParts = receivedMessageString.split(",");
												
				if(requestParts[1].equals("replica")) {
					String heartBeatMessage = "True,"+server.getReplicaID();
					heartBeatSender = new UDPSender();
					heartBeatSender.setAddress(requestPacket.getAddress());
					heartBeatSender.setTargetPort(this.heartBeatReplyPort);
					heartBeatSender.sendHeartBeat(heartBeatMessage);
				}
							
			}
			
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			if (socket != null) socket.close();
		}
	}
	
}
