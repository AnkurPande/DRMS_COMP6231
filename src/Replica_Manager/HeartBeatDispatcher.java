package Replica_Manager;

import java.util.Map;
import java.util.Set;

import server.UDPSender;
import server.UDPSocket;

public class HeartBeatDispatcher implements Runnable {

	public static int SLEEP_TIME = 5000;
	
	private final ReplicaManager rm;
	private final Map<Integer,Integer> REPLICA_MANAGER_PORTS;
	private final Map<Integer,String> REPLICA_MANAGER_IPS;
	
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
	public void dispatchHeartBeat(String rmId, int port) {
		
	}
}
