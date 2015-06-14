package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author Ankurp
 *
 */
public class UDPSocket extends Thread {
	
	private LibraryServer server;
	
	public UDPSocket(LibraryServer server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		DatagramSocket socket = null;
		
		try try {
			ORB orb = ORB.init(args,null);
			POA rootPoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			
			
			byte[] id = rootPoa.activate_object(this); 
			org.omg.CORBA.Object ref = rootPoa.id_to_reference(id);
			
			String ior = orb.object_to_string(ref);
			System.out.println(ior);
		
			socket = new DatagramSocket(server.getPortOfUDP());
			byte[] buffer = new byte[10000];
			
			while(true){
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message);
				
				String[] requestParts = receivedMessageString.split(":");
				String responseMessageString = "";
				if(requestParts.length == 2 ) {
					//non return request
					String numDays = receivedMessageString.trim();
					String responseMessageString = server.checkNonRetuners(numDays);
				}
				else {
					//reserve request
					responseMessageString = server.checkBookAvailability(requestParts[1],requestParts[2])?"true":"false";
					
				}
				
				message = responseMessageString.getBytes();
				
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
