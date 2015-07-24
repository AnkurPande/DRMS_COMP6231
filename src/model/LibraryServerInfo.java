package model;

/**
 * The Class LibraryServerInfo.
 */
public class LibraryServerInfo {
	
	/** The port of udp. */
	private int portOfUDP;
	
	/** The server name. */
	private String serverName;
	
	private String ipAddress;
	
	/**
	 * Instantiates a new library server info.
	 *
	 * @param name the name
	 * @param port the port
	 */
	public LibraryServerInfo(String name, int port, String inetAddress) {
		this.serverName = name;
		this.portOfUDP = port;
		this.setIpAddress(inetAddress);
	}
	
	
	/**
	 * Instantiates a new library server info.
	 */
	public LibraryServerInfo() {};
	
	/**
	 * Gets the port of udp.
	 *
	 * @return the port of udp
	 */
	public int getPortOfUDP() {
		return portOfUDP;
	}
	
	/**
	 * Sets the port of udp.
	 *
	 * @param portOfUDP the new port of udp
	 */
	public void setPortOfUDP(int portOfUDP) {
		this.portOfUDP = portOfUDP;
	}
	
	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public String getServerName() {
		return serverName;
	}
	
	/**
	 * Sets the server name.
	 *
	 * @param serverName the new server name
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	
	
}
