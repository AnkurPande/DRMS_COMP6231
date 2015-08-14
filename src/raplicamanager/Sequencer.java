package raplicamanager;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map;

public class Sequencer extends Thread{
	
	private int lastAgreedSequenceNumber;
	private int lastProposedSequenceNumber;
	private int sequencerID;
	private boolean cordinator;
	

	private int portNumber;
	private int replicaPortNumber;
	private String groupAddress;
	private MulticastSocket socketM;
	private InetAddress host;
	private InetAddress replicaHost;
	private Map<Integer,Vector<Integer>> proposedNumbers;
	private Map<Integer,String> requests;
	private int cordinatorID;
	
	
	
	
	/**
	 * Sequencer constructor.
	 *
	 * @param isCordinator (True/False)
	 * @param the sequencer port for listening for incoming messages and sending messages
	 * @param the replica port
	 * @param the sequencers group IP address
	 * @param the replica IP address
	 */
	
	Sequencer(boolean cordinator, int port,int replicaPort, String address, String replicaAddress, int ID) throws IOException
	{
		sequencerID = ID;
		lastAgreedSequenceNumber = 0;
		proposedNumbers = new HashMap<Integer,Vector<Integer>>();
		requests = new HashMap<Integer,String>();
		lastProposedSequenceNumber = 0;
		this.cordinator = cordinator;
		portNumber = port;
		groupAddress = address;
		replicaPortNumber = replicaPort;
		replicaHost = InetAddress.getByName(replicaAddress);
		socketM = new MulticastSocket(port);
		host = InetAddress.getByName(groupAddress);
		socketM.joinGroup(host);
		cordinatorID = 3;
		
	}
	
	@Override
	public void run()
	{
		receiveMessage();
	}
	
	
	/**
	 * Receives a UDP messages and respond accordingly.
	 *
	 */
	
	public void receiveMessage()
	{
		
		socketM = null;
		String result = "";

		try {
			
			socketM = new MulticastSocket(portNumber);
			socketM.joinGroup(host);
			
			while(true)
			{
				byte[] buffer = new byte[1000];
				DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
				socketM.receive(receivedPacket);
				
				result = new String(receivedPacket.getData()).trim();
				String[] request = result.split(","); 
				
				if (request.length == 2)
				{
					if (request[0].equals("election"))
					{
						if(Integer.parseInt(request[1]) > sequencerID)
						{
							String message = "cordinator" + request[1];
							sendMessage(message, socketM);
						}
						
						if (Integer.parseInt(request[1]) < sequencerID)
						{
							String message = "cordinator" + sequencerID;
							cordinator = true;
							sendMessage(message, socketM);
						}
					}
					
					if (request[0].equals("cordinator"))
					{
						cordinatorID = Integer.parseInt(request[1]);
						
						if (Integer.parseInt(request[1]) == sequencerID) cordinator = true;
						
						System.out.println("the cordinator ID is: " + cordinatorID);
					}
					
					
				}
				
				if (request.length == 3)
				{
					//Isis for agreeing on the sequence number
					if(request[0].equals("vote"))
					{
						if (!cordinator)
						{
							this.lastProposedSequenceNumber = Math.max(this.lastAgreedSequenceNumber,this.lastProposedSequenceNumber) + 1;
							String reply = "propose,"+ request[1] + "," + lastProposedSequenceNumber;
							sendMessage(reply,socketM);
						}
					}
					
					
					if (request[0].equals("propose"))
					{
						if (cordinator)
						{
							if (lastAgreedSequenceNumber < Integer.parseInt(request[2]))
							{
								if (proposedNumbers.get(Integer.parseInt(request[1])).size() < 3) proposedNumbers.get(Integer.parseInt(request[1])).addElement(Integer.parseInt(request[2]));
								
								if (proposedNumbers.get(Integer.parseInt(request[1])).size() == 3)
								{
									int max = Math.max(Math.max(proposedNumbers.get(Integer.parseInt(request[1])).get(0), proposedNumbers.get(Integer.parseInt(request[1])).get(1)), proposedNumbers.get(Integer.parseInt(request[1])).get(2));
								
									String reply = "agreed," + request[1] + "," + max;
									sendMessage(reply,socketM);
									System.out.println("agreed sequence number ("+max+") dispatched for request: " + request[1]);
								}
							}
						}
					}
					
					if(request[0].equals("agreed"))
					{
						this.lastAgreedSequenceNumber = Integer.parseInt(request[2]);

						if(cordinator)
						{
							String reply = ""+ request[1] + "," + lastAgreedSequenceNumber;
							replyToFE(reply);
							System.out.println("FE got: sequence number("+lastAgreedSequenceNumber+") for request " + request[1]);
							proposedNumbers.remove(Integer.parseInt(request[1]));
							
						}
						
						String[] aRequest = requests.get(Integer.parseInt(request[1])).split(",");
						
						String aMessage = "1,"+ lastAgreedSequenceNumber + aRequest[2] + aRequest[3];
						
						// Transfers the request to the replica
						transferRequestToReplica(aMessage);
						
						System.out.println("replica got request("+request[1]+") with sequence: " + lastAgreedSequenceNumber);
						
						requests.remove(Integer.parseInt(request[1]));
							
					}
					
				}
				
				if (request.length > 3)
				{
					//request form the front end
					
					if (request[0].equals("1"))
					{
						requests.put(Integer.parseInt(request[1]), result);
						System.out.println(result+"FE");
						
						if (cordinator)
						{
							Vector<Integer> vec = new Vector<>();
							proposedNumbers.put(Integer.parseInt(request[1]), vec);
							
							lastProposedSequenceNumber = lastProposedSequenceNumber + 1;
							String vote = "vote," + request[1] + "," + lastProposedSequenceNumber;
							sendMessage(vote,socketM);
							proposedNumbers.get(Integer.parseInt(request[1])).addElement(lastProposedSequenceNumber);
						}
					}
					
				}
				
			}
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socketM != null) socketM.close();
		}
		
	}
	
	public int getSequencerID() {
		return sequencerID;
	}

	public void setSequencerID(int sequencerID) {
		this.sequencerID = sequencerID;
	}

	public int getCordinatorID() {
		return cordinatorID;
	}

	public void setCordinatorID(int cordinatorID) {
		this.cordinatorID = cordinatorID;
	}

	/**
	 * Send a UDP message.
	 *
	 * @param message the message
	 */
	
	public void sendMessage(String message, MulticastSocket mySocket)
	{
		try {
			
			byte[] udpMessage = (message).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, portNumber);
			mySocket.send(sendPacket);
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} 
	}
	
	public void replyToFE(String message)
	{
		DatagramSocket socket = null;
		
		try{
			socket = new DatagramSocket(5002,InetAddress.getByName("localhost"));
			
			byte[] udpMessage = (message).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, portNumber);
			socket.send(sendPacket);
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
	}
	
	public void transferRequestToReplica(String message)
	{
		DatagramSocket socket = null;
		
		try{
			socket = new DatagramSocket(replicaPortNumber,replicaHost);
			
			byte[] udpMessage = (message).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, portNumber);
			socket.send(sendPacket);
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socket != null) socket.close();
		}
	}
	
	public static void main(String[] args)
	{
		try{
			
			Sequencer s1 = new Sequencer(true, 4001,8001,"234.1.2.1", "localhost",3);
			Sequencer s2 = new Sequencer(false, 4001,8002,"234.1.2.1", "localhost",2);
			Sequencer s3 = new Sequencer(false, 4001,8003,"234.1.2.1", "localhost",1);
			
			s1.start();
			s2.start();
			s3.start();
			
		}
		catch(IOException e)
		{
			System.out.println("IOa: " + e.getMessage());
		}
	}
	
	public boolean isCordinator() {
		return cordinator;
	}

	public void setCordinator(boolean cordinator) {
		this.cordinator = cordinator;
	}
	
}

