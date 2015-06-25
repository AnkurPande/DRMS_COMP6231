package models;

public class LibraryServerInfo {
	
	private int portOfUDP;
	private String serverName;
	
	public LibraryServerInfo(String name, int port) {
		this.serverName = name;
		this.portOfUDP = port;
	}
	
	public LibraryServerInfo() {};
	
	public int getPortOfUDP() {
		return portOfUDP;
	}
	
	public void setPortOfUDP(int portOfUDP) {
		this.portOfUDP = portOfUDP;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
}
