package server;


/**
 * The Interface LibraryServerInterface.
 */
public interface LibraryServerInterface {
	
	
	/**
	 * Creates the account.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param emailAddress the email address
	 * @param phoneNumber the phone number
	 * @param username the username
	 * @param password the password
	 * @param eduInstitution the edu institution
	 * @return the string
	 */
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String eduInstitution);
	
	/**
	 * Reserve book.
	 *
	 * @param username the username
	 * @param password the password
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return the string
	 */
	public String reserveBook(String username, String password, String bookName, String authorName);

	/**
	 * Reserve inter library.
	 *
	 * @param username the username
	 * @param password the password
	 * @param bookName the book name
	 * @param authorName the author name
	 * @return the string
	 */
	public String reserveInterLibrary(String username, String password, String bookName, String authorName);
	
	/**
	 * Gets the non retuners.
	 *
	 * @param adminUsername the admin username
	 * @param adminPassword the admin password
	 * @param eduInstitution the edu institution
	 * @param numDays the num days
	 * @return the non retuners
	 */
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays);
	
	/**
	 * Sets the duration.
	 *
	 * @param username the username
	 * @param bookName the book name
	 * @param numOfDays the num of days
	 * @return the string
	 */
	public String setDuration(String username, String bookName, int numOfDays);
}
