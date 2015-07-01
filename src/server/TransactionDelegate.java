package server;

public class TransactionDelegate {
	
	private int targetUdpPort;
	private String targetIpAddress;
	
	private LibraryServer server;
	
	private String bookName;
	private String bookAuthor;
	
	private String sutdentUsername;
	
	public TransactionDelegate(String address, int port, LibraryServer server) {
		
		this.setTargetIpAddress(address);
		this.setTargetUdpPort(port);
		this.setServer(server);
		
	}
	
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
	
	public boolean rollBack() {
		
		UDPSender sender = new UDPSender(this.targetUdpPort,this.targetIpAddress);
		
		String result = sender.sendMessage("2," + bookName + "," + bookAuthor);
		
		if(result.trim().equalsIgnoreCase("true")) {
				
			return true;
		}
			
		
		return false;
	}
	
	public void performInterLibraryReservation() {
		
		
		server.updateStudentDataFromInterLibraryReservation(this.getSutdentUsername(), this.getBookName(),this.getBookAuthor());
		
		UDPSender sender = new UDPSender(this.targetUdpPort,this.targetIpAddress);
		
		sender.sendMessage("3," + bookName + "," + bookAuthor);
		
		
	}
	
	public int getTargetUdpPort() {
		return targetUdpPort;
	}
	
	public void setTargetUdpPort(int targetUdpPort) {
		this.targetUdpPort = targetUdpPort;
	}
	
	public String getTargetIpAddress() {
		return targetIpAddress;
	}
	
	public void setTargetIpAddress(String targetIpAddress) {
		this.targetIpAddress = targetIpAddress;
	}

	public LibraryServer getServer() {
		return server;
	}

	public void setServer(LibraryServer server) {
		this.server = server;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getSutdentUsername() {
		return sutdentUsername;
	}

	public void setSutdentUsername(String sutdentUsername) {
		this.sutdentUsername = sutdentUsername;
	}
}
