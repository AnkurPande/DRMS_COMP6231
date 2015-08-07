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
		try {
			while (true) {
				Thread.sleep(SLEEP_TIME);				
				 @SuppressWarnings("rawtypes")
				Iterator it = REPLICA_MANAGER_PORTS.entrySet().iterator();
				    while (it.hasNext()) {
				        @SuppressWarnings("rawtypes")
						Map.Entry pair = (Map.Entry)it.next();			
				        String address = REPLICA_MANAGER_IPS.get(pair.getKey()).toString();
				        this.dispatchHeartBeat(pair.getKey().toString(), (int) pair.getValue(), address);
				        it.remove(); // avoids a ConcurrentModificationException
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
	public void dispatchHeartBeat(String rmId, int port, String address) {		
		UDPSender sender = new UDPSender(port, address);		
		String udpMessage = "alive";		
		sender.sendMessage(udpMessage);		
	}
}
