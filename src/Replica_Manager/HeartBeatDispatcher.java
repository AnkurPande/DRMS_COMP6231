package Replica_Manager;

import java.util.Iterator;
import java.util.Map;

import server.UDPSender;
import server.UDPSocket;

public class HeartBeatDispatcher implements Runnable {

	public static int SLEEP_TIME = 5000;
	
	private final ReplicaManager rm;
	private final Map<Integer,Integer> REPLICA_MANAGER_PORTS;
	private final Map<Integer,String> REPLICA_MANAGER_IPS;
	
	
	/**
	 * Set local variables
	 * @param ReplicaManager
	 * @param rmPorts
	 * @param rmIps
	 * @return Nothing
	 */
	public HeartBeatDispatcher(final ReplicaManager rm, final Map<Integer,Integer> rmPorts, final Map<Integer,String> rmIps) {
		this.rm = rm;
		this.REPLICA_MANAGER_PORTS = rmPorts;
		this.REPLICA_MANAGER_IPS = rmIps;
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
				        it.remove(); // avoids a ConcurrentModificationException
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
		
		// Based on the response from RM we will check if each of RM replied or not. If one of the RM didn't replied we will assume it is dead and call recovery method for that RM
	}
}
