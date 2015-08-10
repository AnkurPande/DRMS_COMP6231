package frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import udp.UDPSender;

public class FrontEnd implements FrontEndInterface, Runnable {
	
	
	
	public static final int PORT_OF_RMI = 5001;
	public static final String NAME_OF_FRONTEND = "LibraryServerFrontend";
	
	private Map<Integer, RMAddressAndPort> infoOfRM = new HashMap<Integer, RMAddressAndPort>();
	
	
	private Queue<Request> holdingQueue = new LinkedList<Request>();
	
	private Map<Integer,Request> sequencedRequests = new HashMap<Integer, Request>();
	
	private Map<Integer,Request> unSequencedRequests = new HashMap<Integer, Request>();
	
	
	private String ipAddress;
	
	private int portOfUDP;
	
	private int[] replicaFailCount = {0,0,0};
	

	private int requestID = 0;
	
	
	public FrontEnd() {
		
		infoOfRM.put(1, new RMAddressAndPort(6001, "localhost"));
		infoOfRM.put(2, new RMAddressAndPort(6002, "localhost"));
		infoOfRM.put(3, new RMAddressAndPort(6003, "localhost"));	
		
	}
	
	

	@Override
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber,
			String username, String password, String eduInstitution) throws RemoteException {
		
		Request request = new Request(ConstantValue.CREATE_ACCOUNT,firstName + "," + lastName + "," + emailAddress + "," + phoneNumber
				+ "," + username + "," + password + "," +  eduInstitution);
		
		holdingQueue.add(request);
		
		
		return request.getFinalResult();
	}

	@Override
	public String reserveBook(String username, String password, String bookName, String authorName)
			throws RemoteException {

		Request request = new Request(ConstantValue.RESERVE_BOOK, username + "," + password + "," + bookName + "," + authorName );
		holdingQueue.add(request);
				
		return request.getFinalResult();
	}

	@Override
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setDuration(String username, String bookName, int numOfDays) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendRequestToRM(Request request) {
		
	}
	
	public void setSequenceNumberToRequest(int requestID, int sequenceNumber) {
		
		if(this.unSequencedRequests.get(requestID) != null) {
			Request request = this.unSequencedRequests.get(requestID);
			this.unSequencedRequests.remove(requestID);
			
			request.setSequenceNumber(sequenceNumber);
			this.sequencedRequests.put(sequenceNumber, request);
		}
		
		
	}
	
	public void setResultOfRequest (int sequenceNumber, String result) {
		
		synchronized(sequencedRequests.get(sequenceNumber).getResultOfRequest()) {
			sequencedRequests.get(sequenceNumber).setResultOfRequest(result);
			this.notify();
		}
	}
	
	public void checkResult (Request request) {
		
		if (request.requestStatus == ConstantValue.WAIT_FOR_RESULT) {
			
			String[] resultFromReplicas = request.getResultFromReplica();
			
			int numOfResultReceived = 0;
			
			for(int i=1; i<=3; i++) {
				
				if (resultFromReplicas[i] != null) {
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
							this.replicaFailCount[i-1] += 1;
							if(this.replicaFailCount[i] >= 3) {
								this.informManagerForFailReplica(i);
							}
						}
					}
				}
				
				
			}
			
		}
	}
	
	
	public void findFinalResult(Request request) {
		
		String[] resultFromReplicas = request.getResultFromReplica();

		
		if(resultFromReplicas[1].equalsIgnoreCase(resultFromReplicas[2])) {
			
			synchronized(request.getResultOfRequest()) {
				
				request.setResultOfRequest(resultFromReplicas[1]);
				this.notify();
				request.setRequestStatus(ConstantValue.GOT_RESULT);
			}
		}
		if(resultFromReplicas[1].equalsIgnoreCase(resultFromReplicas[3])) {
			synchronized(request.getResultOfRequest()) {
				request.setResultOfRequest(resultFromReplicas[1]);
				this.notify();
				request.setRequestStatus(ConstantValue.GOT_RESULT);

			}
		}
		if(resultFromReplicas[2].equalsIgnoreCase(resultFromReplicas[3])) {
			synchronized(request.getResultOfRequest()) {
				request.setResultOfRequest(resultFromReplicas[2]);
				this.notify();
				request.setRequestStatus(ConstantValue.GOT_RESULT);
			}
		}
	}
	

	public void informManagerForFailReplica(int i) {
		
		RMAddressAndPort rm = infoOfRM.get(i);
		
		UDPSender sender = new UDPSender(rm.rmPort,rm.rmIpAddress);
		
		sender.sendMessage(ConstantValue.INFORM_RM_FAILURE +"," + i);
		
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			Request currentRequest = holdingQueue.poll();
			
			++requestID;
			
			currentRequest.setRequestID(requestID);;
			
			
			
			try {
				MulticastSocket multicaster = new MulticastSocket();
				
				String requestData = ConstantValue.SEND_REQUEST + "," +currentRequest.getRequestID() + "," + currentRequest.getRequestCategory()+ ","+ currentRequest.getRequestParameters();
				byte[] udpMessage = requestData.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, InetAddress.getByName(""), 4001 );
				multicaster.send(sendPacket);
				multicaster.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			currentRequest.setRequestStatus(ConstantValue.WAIT_FOR_RESULT);
			
			Timer timer = new Timer();
			timer.schedule(new SequenceNumberChecker(currentRequest,this), 50);
			
			unSequencedRequests.put(currentRequest.requestID, currentRequest);
		}
		
	}
	
	public static void main(String[] args) {
		
		FrontEnd frontend = new FrontEnd();
		
		try{
			Registry registry = LocateRegistry.createRegistry(PORT_OF_RMI);
			
			Remote obj = UnicastRemoteObject.exportObject(frontend, PORT_OF_RMI);
			registry.bind(NAME_OF_FRONTEND, obj);
			System.out.println("Front End is running!");

			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	
	public class RMAddressAndPort {
		
		public RMAddressAndPort(int port, String address) {
			this.rmPort = port;
			this.rmIpAddress = address;
		}
		int rmPort;
		String rmIpAddress;
	}
	
	public class SequenceNumberChecker extends TimerTask {
		
		Request request;
		FrontEnd frontend;
		public SequenceNumberChecker(Request request, FrontEnd frontend) {
			this.request = request;
			this.frontend = frontend;
		}
		
		@Override
		public void run() {
			if(request.getSequenceNumber() == ConstantValue.NON_SEQUENCE_NUMBER) {
				frontend.getUnSequencedRequests().remove(request.requestID, request);
				frontend.getHoldingQueue().add(request);
				
			}
			
		}
		
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

	
}
