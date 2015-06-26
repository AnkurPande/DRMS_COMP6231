package models;

/**
 * The Class LibraryServerInfo.
 */
public class LibraryServerInfo {
	
	/** The port of udp. */
	private int portOfUDP;
	
	/** The server name. */
	private String serverName;
	
	/**
	 * Instantiates a new library server info.
	 *
	 * @param name the name
	 * @param port the port
	 */
	public LibraryServerInfo(String name, int port) {
		this.serverName = name;
		this.portOfUDP = port;
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
	
}
