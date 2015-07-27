package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * The Class UDPSender.
 */
public class UDPSender {
	
	private int targetPort;
	
	private String targetAddress;
	
	/**
	 * Instantiates a new UDP sender.
	 *
	 * @param port the port
	 * @param address the address
	 */
	public UDPSender(int port, String address) {
		this.setTargetPort(port);
		this.setTargetAddress(address);
	}

	/**
	 * Send a UDP message and return the reply.
	 *
	 * @param message the message
	 * @return the string
	 */
	
	
	public String sendMessage(String message) {
		DatagramSocket socket = null;

		try {
			socket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(this.getTargetAddress());
			
			byte[] udpMessage = (message).getBytes();
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
	
	/**
	 * Gets the target port.
	 *
	 * @return the target port
	 */
	public int getTargetPort() {
		return targetPort;
	}

	/**
	 * Sets the target port.
	 *
	 * @param targetPort the new target port
	 */
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	/**
	 * Gets the target address.
	 *
	 * @return the target address
	 */
	public String getTargetAddress() {
		return targetAddress;
	}

	/**
	 * Sets the target address.
	 *
	 * @param targetAddress the new target address
	 */
	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}
}
