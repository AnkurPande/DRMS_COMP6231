package Replica_Manager;

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
	private boolean isCordinator;
	//private boolean isSequenceAggreed;
	private int portNumber;
	private int replicaPortNumber;
	private String groupAddress;
	private MulticastSocket socketM;
	private InetAddress host;
	private InetAddress replicaHost;
	private Map<Integer,Vector<Integer>> proposedNumbers;
	private Map<Integer,String> requests;
	
	
	
	/**
	 * Sequencer constructor.
	 *
	 * @param isCordinator (True/False)
	 * @param the sequencer port for listening for incoming messages and sending messages
	 * @param the replica port
	 * @param the sequencers group IP address
	 * @param the replica IP address
	 */
	
	Sequencer(boolean cordinator, int port,int replicaPort, String address, String replicaAddress) throws IOException
	{
		lastAgreedSequenceNumber = 0;
		proposedNumbers = new HashMap<Integer,Vector<Integer>>();
		requests = new HashMap<Integer,String>();
		lastProposedSequenceNumber = 0;
		isCordinator = cordinator;
		//isSequenceAggreed = false;
		portNumber = port;
		groupAddress = address;
		replicaPortNumber = replicaPort;
		replicaHost = InetAddress.getByName(replicaAddress);
		socketM = new MulticastSocket(port);
		host = InetAddress.getByName(groupAddress);
		socketM.joinGroup(host);
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
				
				result = new String(receivedPacket.getData());
				String[] request = result.split(","); 
				
				if (request.length == 3)
				{
					//Isis for agreeing on the sequence number
					if(request[0].equals("vote"))
					{
						if (!isCordinator)
						{
							this.lastProposedSequenceNumber = Math.max(this.lastAgreedSequenceNumber,this.lastProposedSequenceNumber) + 1;
							String reply = "propose,"+ request[1] + "," + lastProposedSequenceNumber;
							sendMessage(reply);
						}
					}
					
					if (request[0].equals("propose"))
					{
						if (isCordinator)
						{
							if (lastAgreedSequenceNumber < Integer.parseInt(request[2]))
							{
								if (proposedNumbers.get(Integer.parseInt(request[1])).size() < 3) proposedNumbers.get(Integer.parseInt(request[1])).addElement(Integer.parseInt(request[2]));
								
								if (proposedNumbers.get(Integer.parseInt(request[1])).size() == 3)
								{
									int max = Math.max(Math.max(proposedNumbers.get(Integer.parseInt(request[1])).get(0), proposedNumbers.get(Integer.parseInt(request[1])).get(1)), proposedNumbers.get(Integer.parseInt(request[1])).get(2));
									String reply = "agreed" + request[1] + "," + max;
									sendMessage(reply);
									//isSequenceAggreed = true;
									proposedNumbers.clear();
								}
							}
						}
					}
					
					if(request[0].equals("agreed"))
					{
						this.lastAgreedSequenceNumber = Integer.parseInt(request[2]);
						
						if(isCordinator)
						{
							String reply = ""+ request[1] + "," + lastAgreedSequenceNumber;
							replyToFE(reply);
							
							String[] aRequest = requests.get(request[1]).split(",");
							
							String aMessage = "1,"+ lastAgreedSequenceNumber + aRequest[2] + aRequest[3];
							
							
							// Transfers the request to the replica
							DatagramSocket socket = new DatagramSocket(replicaPortNumber,replicaHost);
							
							byte[] udpMessage = aMessage.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, replicaHost, replicaPortNumber);
							socket.send(sendPacket);
							
					
							if (socket != null) socket.close();
							
							requests.remove(request[1]);
							proposedNumbers.remove(request[1]);
							
							
						}
						else
						{
							lastAgreedSequenceNumber = Integer.parseInt(request[2]);
							//isSequenceAggreed = true;
						}
					}
					
				}
				
				if (request.length > 3)
				{
					//request form the front end
					//isSequenceAggreed = false;
					
					if (request[0] == "1")
					{
						if (isCordinator)
						{
							requests.put(Integer.parseInt(request[1]), result);
							Vector<Integer> vec = new Vector<>();
							proposedNumbers.put(Integer.parseInt(request[1]), vec);
							
							lastProposedSequenceNumber = lastProposedSequenceNumber + 1;
							String vote = "vote," + request[1] + "," + lastProposedSequenceNumber;
							sendMessage(vote);
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
	
	/**
	 * Send a UDP message.
	 *
	 * @param message the message
	 */
	
	public void sendMessage(String message)
	{
		socketM = null;

		try {
			socketM = new MulticastSocket(portNumber);
			socketM.joinGroup(host);
			
			byte[] udpMessage = (message).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(udpMessage, udpMessage.length, host, portNumber);
			socketM.send(sendPacket);
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (socketM != null) socketM.close();
		}
	}
	
	public void replyToFE(String message)
	{
		DatagramSocket socket = null;
		
		try{
			socket = new DatagramSocket(5002,InetAddress.getByName("address here"));
			
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
			
			Sequencer s1 = new Sequencer(true, 8001,9001,"224.0.0.1", "localhost");
			Sequencer s2 = new Sequencer(false, 8001,9001,"224.0.0.1", "localhost");
			Sequencer s3 = new Sequencer(false, 8001,9001,"224.0.0.1", "localhost");
			
			s1.start();
			s2.start();
			s3.start();
			
			System.out.println("I'm here!!!");
		}
		catch(IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
	}
	
}
