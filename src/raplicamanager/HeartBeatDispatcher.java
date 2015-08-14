package raplicamanager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import udp.Multicaster;
import udp.UDPSender;

public class HeartBeatDispatcher extends Thread  {

	public static int SLEEP_TIME = 20;
	
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

		try {
			REPLICA_MANAGER_RESPOSNE = sender.sendMessage(udpMessage).trim();
			
			if(!REPLICA_MANAGER_RESPOSNE.equalsIgnoreCase(rmId)) {
				rm.revoverReplicaManager(rmId);
			}
			
		} catch (SocketTimeoutException e) {
			
			if(rm.getSequencer().getCordinatorID() == Integer.parseInt(rmId)) {
				rm.electNewCoordinator();
			}
			
			rm.revoverReplicaManager(rmId);
			
			Multicaster multicaster = new Multicaster(4001,"234.1.2.1"); 
			
			String message = "cordinator" + rm.getSequencer().getCordinatorID();
			
			multicaster.sendMessage(message);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		try {
			REPLICA_RESPOSNE = sender.sendMessage(udpMessage);
			
		} catch (SocketTimeoutException e) {
			rm.restartReplica();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


