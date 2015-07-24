package server;


public interface LibraryServerInterface {
	
	
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String eduInstitution);
	
	public String reserveBook(String username, String password, String bookName, String authorName);

	public String reserveInterLibrary(String username, String password, String bookName, String authorName);
	
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays);
	
	public String setDuration(String username, String bookName, int numOfDays);
}
