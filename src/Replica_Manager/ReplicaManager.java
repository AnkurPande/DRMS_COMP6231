package Replica_Manager;


public class ReplicaManager implements Runnable {
		
	public String CURRENT_RM_ID;
	public int CURRENT_RM_PORT;
	public String CURRENT_RM_IP;
	
	private HeartBeatDispatcher dispatcher;
	private HeartBeatListener listener;

	
	/* CONSTRUCTOR */
	ReplicaManager(String RmID, int RmPort, String RmIP) {
		
		dispatcher = new HeartBeatDispatcher(this);
		listener = new HeartBeatListener(this);
		
		listener.start();

		dispatcher.start();

		this.CURRENT_RM_ID = RmID;
		this.CURRENT_RM_PORT = RmPort;
		this.CURRENT_RM_IP = RmIP;
		
		this.startReplica();
	}
	
	
	
	public void startReplica() {
		
		
	}
	
	public void restartReplica() {
		
	}
	
	public void revoverReplicaManager(String rmID) {
		
		System.out.println("fuck");
	}
	
	 
	@Override
	public void run()
	{

	}
	
	
	/**
	 * This method will get the current Replica Manager's ID
	 *
	 * @param nothing
	 * @return CURRENT_RM_ID
	 */
	public String getReplicaManagerID() {
		return this.CURRENT_RM_ID;		
	}
		 
	
	
	
	
	
	public static void main(String [] args)
	{
		ReplicaManager r1 = new ReplicaManager("1", 6001, "localhost");
		Thread t1 = new Thread(r1);
		t1.start();
		
		ReplicaManager r2 = new ReplicaManager("2", 6002, "localhost");
		Thread t2 = new Thread(r2);
		t2.start();
		
		
	}
	
}
