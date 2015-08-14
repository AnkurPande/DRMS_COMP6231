package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Multicaster {

	private int targetPort;
	
	private String targetAddress;
	

	public Multicaster(int port, String address) {
		this.setTargetPort(port);
		this.setTargetAddress(address);
	}
	
	public String sendMessage(String message) {
		MulticastSocket socket = null;

		try {
			socket = new MulticastSocket();
			InetAddress host = InetAddress.getByName(this.getTargetAddress());
			String requestData = message;
			byte[] udpMessage = requestData.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, this.getTargetPort());
			socket.send(sendPacket);
			
			byte[] buffer = new byte[1000];
			DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
			socket.receive(receivedPacket);
			
			String result = new String(receivedPacket.getData());
			
			return result.trim();
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
		return null;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	public String getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}

}
