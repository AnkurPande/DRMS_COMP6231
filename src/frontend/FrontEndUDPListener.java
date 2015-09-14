package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class FrontEndUDPListener extends Thread {
	
	private FrontEnd frontend;
	private ReplicaListener1 listener1;
	private ReplicaListener2 listener2;
	private ReplicaListener3 listener3;

	
	
	public FrontEndUDPListener(FrontEnd frontend) {
		this.frontend = frontend;
		listener1 = new ReplicaListener1();
		listener2 = new ReplicaListener2();
		listener3 = new ReplicaListener3();
		
		listener1.start();
		listener2.start();
		listener3.start();
		
	}
	@Override
	public void run() {
		
		

		try {
			
			while(true){
				

				DatagramSocket socket = null;

				socket = new DatagramSocket(frontend.getPortOfUDP(), InetAddress.getByName(frontend.getIpAddress()));
				byte[] buffer = new byte[1000];
				
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(requestPacket);
				
				byte[] message = requestPacket.getData();
				String receivedMessageString = new String(message).trim();
				
				
				String[] requestParts = receivedMessageString.split(",");
				
				if(requestParts.length == 3 ) {
					
					int requestID = Integer.parseInt(requestParts[1].trim());
					int sequenceNumber = Integer.parseInt(requestParts[1].trim());
					frontend.setSequenceNumberToRequest(requestID, sequenceNumber);
					String responseMessageString = "true"; 
					message = responseMessageString.getBytes();
					
					System.out.println("sent back");
					
					DatagramPacket responsePacket = new DatagramPacket(message, message.length, requestPacket.getAddress(), requestPacket.getPort());
					socket.send(responsePacket);
					
					socket.close();

				}
				
//				if(requestParts.length == 4) {
//
//					socket.close();
//
//					int replicaID = Integer.parseInt(requestParts[1].trim());
//					int sequenceNumber = Integer.parseInt(requestParts[2].trim());
//					String result = requestParts[3].trim();
//					
//					
//					
//					Request request = frontend.getSequencedRequests().get(sequenceNumber);
//					
//					if(request!=null) {
//						
//						if(replicaID == 1) {
//							request.getResultFromReplica()[1] = result;
//							System.out.println("Got result from 1  " + result);
//						} else if (replicaID == 2) {
//							request.getResultFromReplica()[2] = result;
//							System.out.println("Got result from 2  " + result);
//
//						} else if (replicaID == 3) {
//							request.getResultFromReplica()[3] = result;
//							System.out.println("Got result from 3  " + result);
//							
//						}	
//						
//						frontend.checkResult(request);
//					} else {
//						System.out.println("didn't find request");
//					}
//					
//										
//				}
				


			}
			
			
		} catch (IOException e) {
			System.out.println("UDP Exception: " + e.getMessage());
		} 
	}
	
	
	public class ReplicaListener1 extends Thread{
		
		@Override
		public void run() {
			
			DatagramSocket socket = null;

			try {
				
				socket = new DatagramSocket(4501, InetAddress.getByName(frontend.getIpAddress()));
				byte[] buffer = new byte[1000];
				
				
				while(true){
						
					DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
					socket.receive(requestPacket);
					
					byte[] message = requestPacket.getData();
					String receivedMessageString = new String(message).trim();
					
					
					String[] requestParts = receivedMessageString.split(",");
							
					
					if(requestParts.length == 4) {
	
	
						int replicaID = Integer.parseInt(requestParts[1].trim());
						int sequenceNumber = Integer.parseInt(requestParts[2].trim());
						String result = requestParts[3].trim();
						
						
						
						Request request = frontend.getSequencedRequests().get(sequenceNumber);
						
						if(request!=null) {
							
							if(replicaID == 1) {
								request.getResultFromReplica()[1] = result;
								System.out.println("Got result from 1  " + result);
							} else if (replicaID == 2) {
								request.getResultFromReplica()[2] = result;
								System.out.println("Got result from 2  " + result);
	
							} else if (replicaID == 3) {
								request.getResultFromReplica()[3] = result;
								System.out.println("Got result from 3  " + result);
								
							}	
							
							frontend.checkResult(request);
						} else {
							System.out.println("didn't find request");
						}
											
					}

				}
			
			} catch (IOException e) {
				System.out.println("UDP Exception: " + e.getMessage());
			} 
		}
			
	}
	
	
	public class ReplicaListener2 extends Thread{

		@Override
		public void run() {
			
			DatagramSocket socket = null;

			try {
				
				socket = new DatagramSocket(4502, InetAddress.getByName(frontend.getIpAddress()));
				byte[] buffer = new byte[1000];
				
				
				while(true){
						
					DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
					socket.receive(requestPacket);
					
					byte[] message = requestPacket.getData();
					String receivedMessageString = new String(message).trim();
					
					
					String[] requestParts = receivedMessageString.split(",");
							
					
					if(requestParts.length == 4) {
	
	
						int replicaID = Integer.parseInt(requestParts[1].trim());
						int sequenceNumber = Integer.parseInt(requestParts[2].trim());
						String result = requestParts[3].trim();
						
						
						
						Request request = frontend.getSequencedRequests().get(sequenceNumber);
						
						if(request!=null) {
							
							if(replicaID == 1) {
								request.getResultFromReplica()[1] = result;
								System.out.println("Got result from 1  " + result);
							} else if (replicaID == 2) {
								request.getResultFromReplica()[2] = result;
								System.out.println("Got result from 2  " + result);
	
							} else if (replicaID == 3) {
								request.getResultFromReplica()[3] = result;
								System.out.println("Got result from 3  " + result);
								
							}	
							
							frontend.checkResult(request);
						} else {
							System.out.println("didn't find request");
						}
											
					}

				}
			
			} catch (IOException e) {
				System.out.println("UDP Exception: " + e.getMessage());
			} 
		}
	}
	
	public class ReplicaListener3 extends Thread{

		@Override
		public void run() {
			
			DatagramSocket socket = null;

			try {
				
				socket = new DatagramSocket(4503, InetAddress.getByName(frontend.getIpAddress()));
				byte[] buffer = new byte[1000];
				
				
				while(true){
						
					DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
					socket.receive(requestPacket);
					
					byte[] message = requestPacket.getData();
					String receivedMessageString = new String(message).trim();
					
					
					String[] requestParts = receivedMessageString.split(",");
							
					
					if(requestParts.length == 4) {
	
	
						int replicaID = Integer.parseInt(requestParts[1].trim());
						int sequenceNumber = Integer.parseInt(requestParts[2].trim());
						String result = requestParts[3].trim();
						
						
						
						Request request = frontend.getSequencedRequests().get(sequenceNumber);
						
						if(request!=null) {
							
							if(replicaID == 1) {
								request.getResultFromReplica()[1] = result;
								System.out.println("Got result from 1  " + result);
							} else if (replicaID == 2) {
								request.getResultFromReplica()[2] = result;
								System.out.println("Got result from 2  " + result);
	
							} else if (replicaID == 3) {
								request.getResultFromReplica()[3] = result;
								System.out.println("Got result from 3  " + result);
								
							}	
							
							frontend.checkResult(request);
						} else {
							System.out.println("didn't find request");
						}
											
					}

				}
			
			} catch (IOException e) {
				System.out.println("UDP Exception: " + e.getMessage());
			} 
		}
	}
}
