package raplicamanager;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import udp.Multicaster;

public class ReplicaManager implements Runnable {
		
	public String CURRENT_RM_ID;
	public int CURRENT_RM_PORT;
	public String CURRENT_RM_IP;
	
	private HeartBeatDispatcher dispatcher;
	private HeartBeatListener listener;

	private Sequencer sequencer;
	
	private String replicaName;
	
	private Process replicaProcess;
	

	
	
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
		
		System.out.println("STARTING REPLICA " + replicaName);
		ProcessBuilder pb = new ProcessBuilder();
		String path = "./" + replicaName + ".jar";
		pb.command("java", "-jar",  path);
		pb.redirectError(Redirect.INHERIT);
		pb.redirectOutput(Redirect.INHERIT);
		pb.directory(new File("./replicas/" + replicaName + "/"));
		
		try {
			replicaProcess = pb.start();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void restartReplica() {
		System.out.println("Restarting replica: "+this.CURRENT_RM_ID);
		
		if (replicaProcess != null) replicaProcess.destroy();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startReplica();
	}
	
	public void revoverReplicaManager(String rmID) {
		
		System.out.println("Recovering replica manager: "+ rmID);
		
		ProcessBuilder pb = new ProcessBuilder();
		String path = "./" + replicaName + ".jar";
		pb.command("java", "-jar",  path);
		pb.redirectError(Redirect.INHERIT);
		pb.redirectOutput(Redirect.INHERIT);
		pb.directory(new File("./replicas/" + replicaName + "/"));
		
		try {
			replicaProcess = pb.start();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
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
		 
	
	public void electNewCoordinator() {
		
		//Send multicast message to other sequencer for election 
		//The other sequencer reply to this message with id
		//decide which id is smaller, the smaller one is the sequencer
		//Multicast result set sequncer
		//Change current sequencer id to result
		
		Multicaster multicaster = new Multicaster(4001,"234.1.2.1"); 
		
		String message = "election" + sequencer.getSequencerID();
		
		multicaster.sendMessage(message);
		
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



	public Sequencer getSequencer() {
		return sequencer;
	}



	public void setSequencer(Sequencer sequencer) {
		this.sequencer = sequencer;
	}



	public String getReplicaName() {
		return replicaName;
	}



	public void setReplicaName(String replicaName) {
		this.replicaName = replicaName;
	}



	public Process getProcess() {
		return replicaProcess;
	}



	public void setProcess(Process process) {
		this.replicaProcess = process;
	}
	
}
