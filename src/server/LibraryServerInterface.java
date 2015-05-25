package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * 
 * @author Haiyang Sun
 *
 */

public interface LibraryServerInterface extends Remote {
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param emailAddress
	 * @param phoneNumber
	 * @param username
	 * @param password
	 * @param eduInstitution
	 * @return
	 * @throws RemoteException
	 */
	public boolean createAccount(String firstName, String lastName, String emailAddress,
			String phoneNumber, String username, String password, String eduInstitution) throws RemoteException;
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param bookName
	 * @param authorName
	 * @return
	 * @throws RemoteException
	 */
	public boolean reserveBook(String username, String password, String bookName, String authorName) throws RemoteException;
	
	/**
	 * 
	 * @param adminUsername
	 * @param adminPassword
	 * @return
	 * @throws RemoteException
	 */
	public String getNonRetuners(String adminUsername, String adminPassword) throws RemoteException;
	
	/**
	 * 
	 * @param username
	 * @param bookName
	 * @param numOfDays
	 * @return
	 * @throws RemoteException
	 */
	public boolean setDuration (String username,String bookName,int numOfDays) throws RemoteException;	
}
