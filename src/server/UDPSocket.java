package server;
import java.net.*;

public class UDPSocket {
	DatagramSocket socket =null;
	private int udpPort = 0;
	String data;
	public int getUdpPort() {
		return udpPort;
	}
	
	public void sendResponse(byte[] buf, int length, InetAddress address, int port){
		try {
		socket = new DatagramSocket(port);
		DatagramPacket reply = new DatagramPacket(buf,buf.length,address,port);
		socket.send(reply);
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		finally{
			socket.close();
		}
	}
	
	public String recieveRequest(byte buf[],int Port){
		try {
			socket = new DatagramSocket(Port);
			DatagramPacket recievedPacket = new DatagramPacket(buf,buf.length);
			socket.receive(recievedPacket);
			data = new String(recievedPacket.getData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			socket.close();
		}
		return data;
	}
}
