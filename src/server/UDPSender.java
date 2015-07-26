package server;



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import model.DatagramWrapper;


/**@author Ankurp
 * This class is the reliable implementation of UDP messaging for Server to Server communication.
 * It sends the request to target server and returns the generated response.
 */
public class UDPSender {
	private DatagramSocket DGSock = null;
	private InetAddress address = null;
	private String request = null;
	private String response = null;
	byte[] bbuf;
	private int targetPort;
	private String targetIPAddress;
	
	DatagramWrapper lastSent = new DatagramWrapper();
	DatagramWrapper lastReceived = new DatagramWrapper();
	DatagramPacket packetDock = null;
	
	//==================Getter and setters.================================================//
	/**
	 * @return target machine port.
	 */
	public int getPort() {
		return targetPort;
	}

	/**
	 * @param set the target port.
	 */
	public void setPort(int port) {
		this.targetPort = port;
	}
	
	/**
	 * @return address of target Machine.
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @param set the target address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the request string.
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param set the request parameter value.
	 */
	public void setRequest(String request) {
		this.request = request;
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
		return targetIPAddress;
	}

	/**
	 * Sets the target address.
	 *
	 * @param targetAddress the new target address
	 */
	public void setTargetAddress(String targetAddress) {
		this.targetIPAddress = targetAddress;
	}
	
	//==================================================================================================//
	public UDPSender(){}
	
	/**
	 * Instantiates a new UDP sender.
	 *
	 * @param port the port
	 * @param address the address
	 */
	public UDPSender(int port, String address) {
		//Initialization
		try {
		    this.address = InetAddress.getByName(targetIPAddress);
		    this.targetPort = port;
		  } catch ( Exception e ) {
		    e.printStackTrace();
		    System.exit(-1);
		}
			
	}

	/**
	 * Send a UDP message and return the reply.
	 *
	 * @param message the message
	 * @return the string
	 */
	public String sendMessage(String message) {
		
		request = message;
		
		try {
			DGSock = new DatagramSocket();
			System.out.println("UDP sender Started at port"+DGSock.getPort());
			//Set Timeout Value on socket. Once timeout reached  socket no longer open. 
			DGSock.setSoTimeout(1000); 
			bbuf = new byte[1000];
			packetDock = new DatagramPacket(bbuf, bbuf.length);
			lastSent = new DatagramWrapper(-(request.getBytes().length), 0, request.getBytes(), address, targetPort);
			
			//guarantee delivery
		    while (true) {	       
			try {
				//SEnd datagram request and wait for server Reply
			    DGSock.send(lastSent.asDatagram());
			    DGSock.receive(packetDock);
				this.lastReceived = DatagramWrapper.fromDatagram(packetDock);
			    break;
			} catch ( SocketTimeoutException e ) {
				System.out.println("Timeout in sending ist request Or Recieving ist reply from target : "+ e.getMessage());
				DGSock.close();
				return null;
			} catch ( Exception e ) {
			    e.printStackTrace();
			    System.exit(-1);
			}
		    }
		    
		    response = "";
		    
		    //Traffic loop.
		    while (lastReceived.getEnd() != -1){
		    	if ( (lastReceived.getEnd() > 0) &&  (lastReceived.getPayload().length > 0) ) {
		    		response += new String(lastReceived.getPayload());
		    	}
		   
		    //create ACK packet
			lastSent = new DatagramWrapper(lastReceived.getStart(),
						       lastReceived.getEnd(),
						       lastReceived.asDatagram().getAddress(),
						       lastReceived.asDatagram().getPort());
					
			//guarantee delivery
			while (true) {
			    try {
			    	DGSock.send(lastSent.asDatagram());
			    	DGSock.receive(packetDock);
			    	lastReceived = DatagramWrapper.fromDatagram(packetDock);
			    	break;
			    } catch ( SocketTimeoutException e ) {
			    	System.out.println("Timeout in Sending ACK's : "+ e.getMessage());
			    	DGSock.close();
			    	return null;
			    } catch ( Exception e )
			    {
			    	e.printStackTrace();
			    }
			 }	    
		    }	
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		}finally {
			if (DGSock != null) DGSock.close();
		}
		return response;
	}
}
