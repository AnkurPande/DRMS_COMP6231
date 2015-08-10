package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPReciever {

	private InetAddress			host;
	private int 				port;
	private DatagramSocket 		socket;
	private DatagramPacket 		request;
	private byte [] 			buffer;
	private int 				length;
	
	public UDPReciever(String host, int port) throws SocketException, UnknownHostException{
		this.length			= 	1000; 
		this.port 			= 	port;
		this.host           = 	InetAddress.getByName(host);
		
		this.buffer			= 	new byte[length];
		this.socket			= 	new DatagramSocket(this.port,this.host);
		this.request 		= 	new DatagramPacket(buffer,buffer.length);
	}

	public String recieveRequest() {
		String response = null;
		try {
			this.socket.receive(this.request);
			response = new String(this.request.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public void close() {
		if (this.socket!=null) {
			this.socket.close();
		}
	}
}

