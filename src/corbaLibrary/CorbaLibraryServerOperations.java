package corbaLibrary;

/**
 * Interface definition: CorbaLibraryServer.
 * 
 * @author OpenORB Compiler
 */
public interface CorbaLibraryServerOperations
{
    /**
     * Operation createAccount
     */
    public boolean createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String eduInstitution);

    /**
     * Operation reserveBook
     */
    public boolean reserveBook(String username, String password, String bookName, String authorName);

    /**
     * Operation reserveInterLibrary
     */
    public boolean reserveInterLibrary(String username, String password, String bookName, String authorName);

    /**
     * Operation getNonRetuners
     */
    public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays);

    /**
     * Operation setDuration
     */
    public boolean setDuration(String username, String bookName, int numDays);

}
