package server;

/**
 * The Class TransactionDelegate.
 * @author Haiyang Sun
 */
public class TransactionDelegate {
	
	private int targetUdpPort;
	
	private String targetIpAddress;
	
	private LibraryServer server;
	
	private String bookName;
	
	private String bookAuthor;
	
	private String sutdentUsername;
	
	/**
	 * Instantiates a new transaction delegate.
	 *
	 * @param address the address
	 * @param port the port
	 * @param server the server
	 */
	public TransactionDelegate(String address, int port, LibraryServer server) {
		
		this.setTargetIpAddress(address);
		this.setTargetUdpPort(port);
		this.setServer(server);
		
	}
	
	/**
	 * Book is ready to reserve.First, send UDP message to target server, check if book is available there.
	 * If it is, pre reserve it, check the calling server is alive or not. If the calling server is alive, then 
	 * perform reservation. Otherwise, rollback and release the pre reserved book.
	 *
	 * @param username the username
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return true, if successful
	 */
	public boolean bookIsReadyToReserve(String username, String bookName, String authorName) {
		
		this.setBookName(bookName);
		this.setBookAuthor(authorName);
		this.setSutdentUsername(username);
		
		UDPSender sender = new UDPSender(this.targetUdpPort, this.targetIpAddress);
		String result = sender.sendMessage("1," + bookName + "," + authorName);
		
			
		if(result.trim().equalsIgnoreCase("true")) {
				
			try{

				if(!server.serverIsAlive(username, bookName)) {
					this.rollBack();
					return false;
				}
				
			} catch (Exception e) {
				this.rollBack();
				return false;
			}
			
			return true;
		}
			
		
		return false;
	}
	
	/**
	 * Roll back.
	 *
	 * @return true, if successful
	 */
	public boolean rollBack() {
		
		UDPSender sender = new UDPSender(this.targetUdpPort,this.targetIpAddress);
		
		String result = sender.sendMessage("2," + bookName + "," + bookAuthor);
		
		if(result.trim().equalsIgnoreCase("true")) {
				
			return true;
		}
			
		
		return false;
	}
	
	/**
	 * Perform inter library reservation.
	 */
	public void performInterLibraryReservation() {
		
		
		server.updateStudentDataFromInterLibraryReservation(this.getSutdentUsername(), this.getBookName(),this.getBookAuthor());
		
		UDPSender sender = new UDPSender(this.targetUdpPort,this.targetIpAddress);
		
		sender.sendMessage("3," + bookName + "," + bookAuthor);
		
		
	}
	
	/**
	 * Gets the target udp port.
	 *
	 * @return the target udp port
	 */
	public int getTargetUdpPort() {
		return targetUdpPort;
	}
	
	/**
	 * Sets the target udp port.
	 *
	 * @param targetUdpPort the new target udp port
	 */
	public void setTargetUdpPort(int targetUdpPort) {
		this.targetUdpPort = targetUdpPort;
	}
	
	/**
	 * Gets the target ip address.
	 *
	 * @return the target ip address
	 */
	public String getTargetIpAddress() {
		return targetIpAddress;
	}
	
	/**
	 * Sets the target ip address.
	 *
	 * @param targetIpAddress the new target ip address
	 */
	public void setTargetIpAddress(String targetIpAddress) {
		this.targetIpAddress = targetIpAddress;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public LibraryServer getServer() {
		return server;
	}

	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(LibraryServer server) {
		this.server = server;
	}

	/**
	 * Gets the book name.
	 *
	 * @return the book name
	 */
	public String getBookName() {
		return bookName;
	}

	/**
	 * Sets the book name.
	 *
	 * @param bookName the new book name
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/**
	 * Gets the book author.
	 *
	 * @return the book author
	 */
	public String getBookAuthor() {
		return bookAuthor;
	}

	/**
	 * Sets the book author.
	 *
	 * @param bookAuthor the new book author
	 */
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	/**
	 * Gets the sutdent username.
	 *
	 * @return the sutdent username
	 */
	public String getSutdentUsername() {
		return sutdentUsername;
	}

	/**
	 * Sets the sutdent username.
	 *
	 * @param sutdentUsername the new sutdent username
	 */
	public void setSutdentUsername(String sutdentUsername) {
		this.sutdentUsername = sutdentUsername;
	}
}
