package frontend;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FrontEndInterface extends Remote {
	
	public String createAccount(String firstName, String lastName, String emailAddress,
			String phoneNumber, String username, String password, String eduInstitution) throws RemoteException;

	public String reserveBook(String username, String password, String bookName, String authorName) throws RemoteException;
	
	
	public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays) throws RemoteException;
	
	
	public String setDuration (String username,String bookName,int numOfDays) throws RemoteException;	

}
