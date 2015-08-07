package Replica_Manager;

import java.util.HashMap;

public class ReplicaManager implements Runnable {
		
	public final int CURRENT_RM_ID = 1;
	
	HashMap<Integer, Integer> rmPorts = new HashMap<Integer, Integer>();
	HashMap<Integer, String> rmIps = new HashMap<Integer, String>();

	
	/* CONSTRUCTOR */
	ReplicaManager() {
		
		this.replicaManagerValues();
	}
	
	
	@Override
	public void run()
	{
		handleHeartBeatResponse();
	}
	
	
	/**
	 * This method will dump replica managers ports and IP values in hashmaps
	 *
	 * @param nothing
	 * @return nothing
	 */
	private void replicaManagerValues()
	{
		rmPorts.put(1, 5001);
		rmPorts.put(2, 5002);
		rmPorts.put(3, 5003);
		
		rmIps.put(1, "localhost");
		rmIps.put(2, "localhost");
		rmIps.put(3, "localhost");
	}
	
	
	/**
	 * This method will get the current Replica Manager's ID
	 *
	 * @param nothing
	 * @return CURRENT_RM_ID
	 */
	public int getReplicaManagerID() {
		return this.CURRENT_RM_ID;		
	}
	
	
	/**
	 * This method will start a thread to send heart beat messages to Replica Managers
	 *
	 * @param 
	 * @return nothing
	 */
	public void startHeartBeatProcess()	{
		HeartBeatDispatcher dispatcher = new HeartBeatDispatcher(this, this.rmPorts, this.rmIps);
		Thread t = new Thread(dispatcher);
		t.start();
	}
	
	
	/**
	 * This method will receive a response from heart beat response and take action according to response
	 *
	 * @param 
	 * @return a string value
	 */
	public String handleHeartBeatResponse()	{
		
		return null;		
	}
	
	/**
	 * This method will recover a dead replica manager.
	 *
	 * @param RmID: Replica Manager ID
	 * @return a boolean indicate success or not
	 */
	public boolean recoverReplicaManager(int RmID)
	{
		return true;
		
	}
	
}
