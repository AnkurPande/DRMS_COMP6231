package replica;

import udp.UDPSender;
import udp.UDPReciever;


/**
 * @author Ankurp
 *
 */
public class ReplicaUDPListener extends Thread {
	
	private LibraryServerReplica server;
	
	public static final String FRONTEND_HOST = "localhost";
	
	public static final int FRONTEND_UDP_PORT =5002 ;
	
	public ReplicaUDPListener(LibraryServerReplica server) {
		this.server = server;
		
	}
	@Override
	public void run() {
		String responseMessageString = "";
		UDPSender frontEndSender = null;
		UDPReciever requestReciever = null;
		try {
			//Initialize sender to send response to front end
			frontEndSender = new UDPSender(FRONTEND_UDP_PORT,FRONTEND_HOST);
			
			//Initialize socket to receive response
			requestReciever = new UDPReciever(server.getIpAddress(),server.getPortOfUDP());
						
			while(true){
				String receivedMessageString = requestReciever.recieveRequest();
				
				String[] requestParts = receivedMessageString.split(",");
				
				String[] parameters = requestParts[3].split(" : ");
				
				if(requestParts[0] == "1"){
					responseMessageString = "1,"+server.getReplicaID()+","+ requestParts[1]+",";
					responseMessageString = responseMessageString + server.createAccount(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5], parameters[6]);
					frontEndSender.sendOnly(responseMessageString);
				}
			}
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			requestReciever.close();
		}
	}
}