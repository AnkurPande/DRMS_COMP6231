package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import net.rudp.ReliableSocket;

/**
 * The Class UDPSender.
 */
public class ReliableUDPSender {
	
	private int targetPort;
	
	private String targetAddress;
	
	/**
	 * Instantiates a new UDP sender.
	 *
	 * @param port the port
	 * @param address the address
	 */
	public ReliableUDPSender(int port, String address) {
		this.setTargetPort(port);
		this.setTargetAddress(address);
	}

	/**
	 * Send a UDP message and return the reply.
	 *
	 * @param message the message
	 * @return the string
	 * @throws IOException 
	 */
	
	
	public String sendMessage(String message) throws IOException {
		
		ReliableSocket rs = new ReliableSocket();		
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
