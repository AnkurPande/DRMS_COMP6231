package frontend;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import udp.UDPSender;

public class FrontEnd implements Runnable {
	
	
	
	public static final String NAME_OF_FRONTEND = "LibraryServerFrontend";
	
	private Map<Integer, RMAddressAndPort> infoOfRM = new HashMap<Integer, RMAddressAndPort>();
	
	
	private Queue<Request> holdingQueue = new LinkedList<Request>();
	
	private Map<Integer,Request> sequencedRequests = new HashMap<Integer, Request>();
	
	private Map<Integer,Request> unSequencedRequests = new HashMap<Integer, Request>();
	
	
	private String ipAddress;
	
	private int portOfUDP;
	
	private int[] replicaFailCount = {0,0,0};
	

	private int requestID = 0;
		
	private FrontEndSender frontEndSender;
	private FrontEndUDPListener listener;
	
	public FrontEnd() {
		
		this.ipAddress = "localhost";
		this.portOfUDP = 5002;
		
		infoOfRM.put(1, new RMAddressAndPort(6501, "localhost"));
		infoOfRM.put(2, new RMAddressAndPort(6502, "localhost"));
		infoOfRM.put(3, new RMAddressAndPort(6503, "localhost"));	
		
		frontEndSender = new FrontEndSender(this);
		listener = new FrontEndUDPListener(this);
		
		frontEndSender.start();
		listener.start();
		
		
		
	}
	
	

	
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber,
			String username, String password, String eduInstitution)  {
		
		
		
		Request request = new Request(ConstantValue.CREATE_ACCOUNT,firstName + "-" + lastName + "-" + emailAddress + "-" + phoneNumber
				+ "-" + username + "-" + password + "-" +  eduInstitution);
		

		
		holdingQueue.add(request);
		
		
		return request.getFinalResult();
	}

	
	public String reserveBook(String username, String password, String bookName, String authorName) {

		
		
		Request request = new Request(ConstantValue.RESERVE_BOOK, username + "," + password + "," + bookName + "," + authorName );
		
		request.start();
		
		holdingQueue.add(request);		
		
		return request.getFinalResult();
	}

	
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String setDuration(String username, String bookName, int numOfDays)  {
		// TODO Auto-generated method stub
		return null;
	}


	
	public void setSequenceNumberToRequest(int requestID, int sequenceNumber) {
		
		
			Request request = this.unSequencedRequests.get(requestID);
			this.unSequencedRequests.remove(requestID);
			
			request.setSequenceNumber(sequenceNumber);
			this.sequencedRequests.put(sequenceNumber, request);
			System.out.println("Set sequencer number to request "  + sequenceNumber);
		
		
		
	}
	
//	public void setResultOfRequest (int sequenceNumber, String result) {
//		
//		synchronized(sequencedRequests.get(sequenceNumber).getResultOfRequest()) {
//			sequencedRequests.get(sequenceNumber).setResultOfRequest(result);
//			this.notify();
//		}
//	}
	
	public void checkResult (Request request) {
		
		if (request.requestStatus == ConstantValue.WAIT_FOR_RESULT) {
			
			String[] resultFromReplicas = request.getResultFromReplica();
			
			int numOfResultReceived = 0;
			
			for(int i=1; i<=3; i++) {
				
				if (!resultFromReplicas[i].equals("empty" + i) ) {
					++ numOfResultReceived;
				}
			}
			
			if(numOfResultReceived == 2) {
				
				this.findFinalResult(request);
				
			}
			
			if(numOfResultReceived == 3) {
				
				if(request.getRequestStatus() == ConstantValue.WAIT_FOR_RESULT) {
					
					this.findFinalResult(request);
					
				}
				
				
				if(request.getRequestStatus() == ConstantValue.GOT_RESULT) {
					for(int i=1; i<=3; i++) {
						if(!request.getResultFromReplica()[i].equalsIgnoreCase(request.getResultOfRequest())) {
							
							System.out.println("Get result " + request.getResultFromReplica()[i] );
							System.out.println("Actually got " + request.getResultOfRequest());
							
							this.replicaFailCount[i-1] += 1;
							System.out.println("replica got wrong result " + (i-1) );
							if(this.replicaFailCount[i-1] >= 3) {
								this.informManagerForFailReplica(i);
								this.replicaFailCount[i-1] = 0;
							}
						}
					}
				}
				
				
			}
			
		}
	}
	
	
	public void findFinalResult(Request request) {
		
		String[] resultFromReplicas = request.getResultFromReplica();

		
		
		if(resultFromReplicas[1].equals(resultFromReplicas[2])) {
			
			
				
				request.setResultOfRequest(resultFromReplicas[1]);
				
				request.setRequestStatus(ConstantValue.GOT_RESULT);
			
		}
		if(resultFromReplicas[1].equals(resultFromReplicas[3])) {
			
				request.setResultOfRequest(resultFromReplicas[1]);
				request.setRequestStatus(ConstantValue.GOT_RESULT);

			
		}
		if(resultFromReplicas[2].equals(resultFromReplicas[3])) {
			
				request.setResultOfRequest(resultFromReplicas[2]);
			
				request.setRequestStatus(ConstantValue.GOT_RESULT);
			
		}
	}
	

	public void informManagerForFailReplica(int i) {
		
		RMAddressAndPort rm = infoOfRM.get(i);
		
		UDPSender sender = new UDPSender(rm.rmPort,rm.rmIpAddress);
		
	
		sender.sendOnly(ConstantValue.INFORM_RM_FAILURE +"," + i);
		
		System.out.println("Replica Failure Detected: ID: " + i + ", Restart the replica");
		
	}
	

	
	
	public class RMAddressAndPort {
		
		public RMAddressAndPort(int port, String address) {
			this.rmPort = port;
			this.rmIpAddress = address;
		}
		int rmPort;
		String rmIpAddress;
	}
	
	
	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public int getPortOfUDP() {
		return portOfUDP;
	}



	public void setPortOfUDP(int portOfUDP) {
		this.portOfUDP = portOfUDP;
	}



	public int getRequestID() {
		return requestID;
	}



	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}



	public Map<Integer, RMAddressAndPort> getInfoOfRM() {
		return infoOfRM;
	}



	public void setInfoOfRM(Map<Integer, RMAddressAndPort> infoOfRM) {
		this.infoOfRM = infoOfRM;
	}



	public Queue<Request> getHoldingQueue() {
		return holdingQueue;
	}



	public void setHoldingQueue(Queue<Request> holdingQueue) {
		this.holdingQueue = holdingQueue;
	}



	public Map<Integer, Request> getSequencedRequests() {
		return sequencedRequests;
	}



	public void setSequencedRequests(Map<Integer, Request> sequencedRequests) {
		this.sequencedRequests = sequencedRequests;
	}



	public Map<Integer, Request> getUnSequencedRequests() {
		return unSequencedRequests;
	}



	public void setUnSequencedRequests(Map<Integer, Request> unSequencedRequests) {
		this.unSequencedRequests = unSequencedRequests;
	}


	public FrontEndSender getFrontEndSender() {
		return frontEndSender;
	}




	public void setFrontEndSender(FrontEndSender frontEndSender) {
		this.frontEndSender = frontEndSender;
	}




	public FrontEndUDPListener getListener() {
		return listener;
	}




	public void setListener(FrontEndUDPListener listener) {
		this.listener = listener;
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
}
