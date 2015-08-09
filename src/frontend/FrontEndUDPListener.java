package frontend;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class FrontEndUDPListener extends Thread {
	
	private FrontEnd frontend;
	
	public FrontEndUDPListener(FrontEnd frontend) {
		this.frontend = frontend;
		
	}
	@Override
	public void run() {
		
		DatagramSocket socket = null;

		try {
					
			socket = new DatagramSocket(frontend.getPortOfUDP(), InetAddress.getByName(frontend.getIpAddress()));
			byte[] buffer = new byte[1000];
			
			while(true){
				
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message).trim();
				
				String[] requestParts = receivedMessageString.split(",");
				
				if(requestParts.length == 3 ) {
					
					int requestID = Integer.parseInt(requestParts[1].trim());
					int sequenceNumber = Integer.parseInt(requestParts[1].trim());
					frontend.setSequenceNumberToRequest(requestID, sequenceNumber);
				}
				else {
					int replicaID = Integer.parseInt(requestParts[1].trim());
					int sequenceNumber = Integer.parseInt(requestParts[2].trim());
					String result = requestParts[3].trim();
					Request request = frontend.getSequencedRequests().get(sequenceNumber);
					
					if(replicaID == 1) {
						request.getResultFromReplica()[1] = result;
					} else if (replicaID == 2) {
						request.getResultFromReplica()[2] = result;
					} else if (replicaID == 3) {
						request.getResultFromReplica()[3] = result;
					}
					
					frontend.checkResult(request);
										
				}
				
//				message = responseMessageString.getBytes();
//				
//				DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
//				socket.send(responsePacket);
			}
			
			
		} catch (Exception e) {
			System.out.println("UDP Exception");
		} finally {
			if (socket != null) socket.close();
		}
	}
}
