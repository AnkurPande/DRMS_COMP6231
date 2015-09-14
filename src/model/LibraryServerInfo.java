package model;

/**
 * The Class LibraryServerInfo.
 */
public class LibraryServerInfo {
	
	/* Replica ID */
	private int replicaID;
	
	/* The port of udp. */
	private int portOfUDP;
	
	/* The Replica name. */
	private String serverName;
	
	/* The Replica IP Address*/
	private String ipAddress;
	
	/*The server heart beat listen port */
	private int heartbeatPort;
	
	/* Port of Replica Manager of this replica*/
	
	/*IP Address of Replica Manager of this Replica*/
	private String rmIpAddress;
	
	/**
	 * Instantiates a new library server info.
	 *
	 * @param name the name
	 * @param port the port
	 */
	public LibraryServerInfo(int replicaID, String name, int port, String inetAddress,int heartbeat, String rmIpAddress) {
		this.replicaID = replicaID;
		this.serverName = name;
		this.portOfUDP = port;
		this.setIpAddress(inetAddress);
		this.heartbeatPort = heartbeat;
		this.rmIpAddress = rmIpAddress;
	}
	
	
	//-------------------------------Getters and Setters---------------------------------------//
	
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


	public int getHeartbeatPort() {
		return heartbeatPort;
	}


	public void setHeartbeatPort(int heartbeatPort) {
		this.heartbeatPort = heartbeatPort;
	}

	public int getReplicaID() {
		return replicaID;
	}


	public void setReplicaID(int replicaID) {
		this.replicaID = replicaID;
	}





	public String getRmIpAddress() {
		return rmIpAddress;
	}


	public void setRmIpAddress(String rmIpAddress) {
		this.rmIpAddress = rmIpAddress;
	}

	
	
}