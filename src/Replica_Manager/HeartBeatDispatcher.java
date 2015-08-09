package Replica_Manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import server.UDPSender;

public class HeartBeatDispatcher implements Runnable {

	public static int SLEEP_TIME = 5;
	
	HashMap<Integer, Integer> REPLICA_PORTS = new HashMap<Integer, Integer>();
	HashMap<Integer, String> REPLICA_IPS = new HashMap<Integer, String>();
		
	HashMap<Integer, Integer> REPLICA_MANAGER_PORTS = new HashMap<Integer, Integer>();
	HashMap<Integer, String> REPLICA_MANAGER_IPS = new HashMap<Integer, String>();
	
	ReplicaManager rm;
	
	/**
	 * Constructor setting variables
	 * @return Nothing
	 */
	HeartBeatDispatcher(ReplicaManager rm) {		
		this.replicaManagerValues();
		this.setRm(rm);
	
	}
	
	
	/**
	 * This method will dump replica managers ports and IP values in hashmaps
	 *
	 * @param nothing
	 * @return nothing
	 */
	private void replicaManagerValues() {
		
		/* REPLICA MANAGER Values */
		REPLICA_MANAGER_PORTS.put(1, 6001);
		REPLICA_MANAGER_PORTS.put(2, 6002);
		REPLICA_MANAGER_PORTS.put(3, 6003);
		
		REPLICA_MANAGER_IPS.put(1, "localhost");
		REPLICA_MANAGER_IPS.put(2, "localhost");
		REPLICA_MANAGER_IPS.put(3, "localhost");
		
		
		/* REPLICA Values */
		
		REPLICA_PORTS.put(1, 7001);
		REPLICA_PORTS.put(2, 7002);
		REPLICA_PORTS.put(3, 7003);
		
		REPLICA_IPS.put(1, "localhost");
		REPLICA_IPS.put(2, "localhost");
		REPLICA_IPS.put(3, "localhost");
		
	}
	
	
	@Override
	public void run() {
		this.manageReplicaManagerHeartBeat(); // manage replica manager heartbeats
		this.manageReplicaHeartBeat(); // manage replica heartbeats
	}
	
	
	/**
	 * Manage heartbeat process between replica managers and on response from replica manager take appropriate actions like recovery of replica managers
	 * 
	 * @return Nothing
	 */
	public void manageReplicaManagerHeartBeat() {
		try {
			while (true) {
				Thread.sleep(SLEEP_TIME);
				 @SuppressWarnings("rawtypes")
				Iterator it = REPLICA_MANAGER_PORTS.entrySet().iterator();
				    while (it.hasNext()) {
				        @SuppressWarnings("rawtypes")
						Map.Entry pair = (Map.Entry)it.next();			
				        String address = REPLICA_MANAGER_IPS.get(pair.getKey()).toString();
				        this.dispatchHeartBeatToReplicaManager(pair.getKey().toString(), (int) pair.getValue(), address);
				        //it.remove(); // avoids a ConcurrentModificationException
				    }		
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Manage heartbeat process between replica and on response from replica take appropriate actions like recovery of replica
	 * 
	 * @return Nothing
	 */
	public void manageReplicaHeartBeat() {
		try {
			while (true) {
				Thread.sleep(SLEEP_TIME);
				 @SuppressWarnings("rawtypes")
				Iterator it = REPLICA_PORTS.entrySet().iterator();
				    while (it.hasNext()) {
				        @SuppressWarnings("rawtypes")
						Map.Entry pair = (Map.Entry)it.next();			
				        String address = REPLICA_IPS.get(pair.getKey()).toString();
				        this.dispatchHeartBeatToReplica(pair.getKey().toString(), (int) pair.getValue(), address);
				        //it.remove(); // avoids a ConcurrentModificationException
				        
				    }		
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Send heart beat to check if library in another replica manager is alive. The response will be handled by this.rm.
	 * @param rmId
	 * @param port
	 * @return Nothing
	 */
	public void dispatchHeartBeatToReplicaManager(String rmId, int port, String address) {
		
		String REPLICA_MANAGER_RESPOSNE = "";
		UDPSender sender = new UDPSender(port, address);		
		String udpMessage = "isAlive";		
		REPLICA_MANAGER_RESPOSNE = sender.sendMessage(udpMessage);	// UDP response from replica manager will be received here
		
		if(!REPLICA_MANAGER_RESPOSNE.equals(rmId) || REPLICA_MANAGER_RESPOSNE.isEmpty()) {
			
			rm.revoverReplicaManager(rmId);
		}
			
	}
	
	
	/**
	 * Send heart beat to check if library in another replica manager is alive. The response will be handled by this.rm.
	 * @param rmId
	 * @param port
	 * @return Nothing
	 */
	public void dispatchHeartBeatToReplica(String rId, int port, String address) {
		String REPLICA_RESPOSNE = "";
		UDPSender sender = new UDPSender(port, address);		
		String udpMessage = "isAlive";		
		REPLICA_RESPOSNE = sender.sendMessage(udpMessage);	// UDP response from replica will be received here			
		
		if(!REPLICA_RESPOSNE.equals(rId) || REPLICA_RESPOSNE.isEmpty()) {
			
			rm.restartReplica();
			
		}
	}
	 
	
	public static void main(String [] args) {		
			
	}


	public ReplicaManager getRm() {
		return rm;
	}


	public void setRm(ReplicaManager rm) {
		this.rm = rm;
	}
}
