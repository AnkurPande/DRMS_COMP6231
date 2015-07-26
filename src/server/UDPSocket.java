package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import model.DatagramWrapper;

/**
 * @author Ankurp
 * This class is the reliable implementation of UDP messaging for Server to Server communication.
 * It listens for incoming request and response is returned through Reliable UDP
 *
 */
public class UDPSocket extends Thread {
	private DatagramSocket DGSock = null;
	private int port = 0;
	private int bytesRead = 0;
	private int bookmark = 0;
	private LibraryServer server;
	private static final int MAX_SIZE = 480;
	
	byte[] bbuff = null;
	byte[] bytes = null;
	String response = "";
	String request = null;
	long responseSize = 0;
	DatagramWrapper lastReceived = new DatagramWrapper();
	DatagramWrapper lastSent = new DatagramWrapper();;
	DatagramPacket packetDock = null;

	
	//===== Getter and setters.==========================================			
	/**
	 * @return the UDP Socket reference.
	 */ 
	public DatagramSocket getDGSock() {
		return DGSock;
	}
	
	/**
	 * @return port on which server is listening.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return the request parameter value.
	 */
	public String getRequest() {
		return request;
	}
	
	/**
	 * @param set the listening socket.
	 */
	public void setDGSock(DatagramSocket dGSock) {
		DGSock = dGSock;
	}
	
	/**
	 * @param set the listening port.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @param set request parameter value.
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/*======================================================================================*/
	
	/**
	 *  Default construtor.
	 */
	public UDPSocket(){}
	
	//Initialization of server.
 	public UDPSocket(LibraryServer server){
		//usage checking
    	try {	
    			this.server = server;
    			this.port = server.getPortOfUDP();
    			
    		} catch ( Exception e ) {
    			System.out.println(e.getMessage());
    			System.exit(-1);
    		}
    	System.out.println("Server Started at port"+port);
	}
	    	
	@Override
	public void run() {
		//setup
    	try {
    	    bbuff = new byte[1000];
    	    DGSock = new DatagramSocket(port);
    	    packetDock = new DatagramPacket(bbuff, bbuff.length);
    	    
    	    try {
    	    	DGSock.receive(packetDock);
	    		DGSock.setSoTimeout(1000);
	    		lastReceived = DatagramWrapper.fromDatagram(packetDock);
	    		request = new String(lastReceived.getPayload());
	       	} catch ( SocketTimeoutException e ) {
		    	System.out.println("Socket Timeout Exception while Recieve request :"+e.getMessage());
		    } catch ( Exception e ) {
    	    	System.out.println("Received bad request!\nTerminating program...");
    	    	System.out.println(e.getMessage());
    	    	System.exit(-1);
    	    }
    	    
    	    String[] requestParts = request.split(",");
			
			if(requestParts.length == 2 ) {
				//non return request
				String numDays = requestParts[1].trim();
				response = server.checkNonRetuners(numDays);
			}
			else {
				/*
				 * reserve request
				 * 
				 * Prefix Definition
				 * 1 -- check book availability
				 * 2 -- release book
				 * 3 -- confirm remote reservation
				 */
				if(requestParts[0].trim().equalsIgnoreCase("1")) {
					response = server.checkBookAvailability(requestParts[1].trim(),requestParts[2].trim())?"true":"false";
				}
				if(requestParts[0].trim().equalsIgnoreCase("2")) {
					server.releaseBook(requestParts[1].trim(),requestParts[2]);
					response = "true";

				}
				if(requestParts[0].trim().equalsIgnoreCase("3")) {

					server.confirmRemoteReservation(requestParts[1].trim(),requestParts[2].trim());
					response = "true";
				}
			}
			
    	    long offset = 0;
    	    responseSize = response.length();
    	
    	    //traffic loop
		    while ( lastReceived.getEnd() < responseSize ) {
			
			//create next packet to send		
			bytes = new byte[MAX_SIZE+32];
			offset = responseSize -lastReceived.getEnd();
			if(offset>=MAX_SIZE) {
				bytes = response.substring(bookmark, MAX_SIZE).getBytes();
				bytesRead = response.substring(bookmark, MAX_SIZE).length();
			}
			else{
				bytes = response.substring(bookmark, response.length()-1).getBytes();
				bytesRead = response.length() -bookmark ;
			} 
			
			
			lastSent = new DatagramWrapper(bookmark, bookmark+bytesRead, bytes,
										lastReceived.asDatagram().getAddress(),
										lastReceived.asDatagram().getPort());
	
			//guarantee delivery
			while (true) {
			    try {
				DGSock.send(lastSent.asDatagram());
				DGSock.receive(packetDock);
				lastReceived = DatagramWrapper.fromDatagram(packetDock);
				if ( lastReceived.getEnd() < lastSent.getEnd() ) continue;
				break;
			    } catch ( SocketTimeoutException e ) {
			    	System.out.println("Socket Timeout Exception while Send response OR recieving ACK."+e.getMessage());
			    	break;
			    } catch ( Exception e ) {
			    	System.out.println(e.getMessage());
			    }
			}
			if ( (bookmark < responseSize) && (bytesRead > 0) ) bookmark += bytesRead;
		    }
    	}
    	catch(Exception e){
    		System.out.println("Exception while Sending response: "+e.getMessage());
   		}
    	
    	//FIN cycle
    	lastSent = new DatagramWrapper(-1, -1, lastReceived.asDatagram().getAddress(), lastReceived.asDatagram().getPort());
    	System.out.println("Sending FINS. Will terminate momentarily.");
    	
    	for ( int i=0; i<20; i++ ) {
    	   try {
    			DGSock.send(lastSent.asDatagram());
    		} catch ( Exception e ) { e.printStackTrace(); }
    	}    
    	if (DGSock != null) DGSock.close();
	}
}
