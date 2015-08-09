package Replica_Manager;

public class Test {


	public static void main(String[] args) {
		ReplicaManager r3 = new ReplicaManager("3", 6003, "localhost");
		Thread t3 = new Thread(r3);
		t3.start();
	}
}
