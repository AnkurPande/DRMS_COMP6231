package replica;

import udp.UDPReciever;

public class Heartbeat extends Thread{

private LibraryServerReplica server;
	
	//--------------------------------------------Constructor------------------------------------------------------------------//
	
	public Heartbeat(LibraryServerReplica server) {
		this.server = server;
	}
	
	//-----------------------------------------------Run-----------------------------------------------------------------------//
	
	@Override
	public void run() {
		String responseMessageString = null;
		UDPReciever heartBeatReciever = null;
		try {
			//Initialize socket to receive response
			heartBeatReciever = new UDPReciever(server.getIpAddress(),server.getHeartBeatListenPort());		
			while(true){
					
				String receivedMessageString = heartBeatReciever.recieveRequest();
				
				String[] requestParts = receivedMessageString.split(",");
												
				if(requestParts[1].equals("isAlive")) {
					responseMessageString = "True,"+server.getReplicaID();
					heartBeatReciever.sendResponse(responseMessageString);
				}
			}
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		}
		finally{
			heartBeatReciever.close();
		}
	}
}
