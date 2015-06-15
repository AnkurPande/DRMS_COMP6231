package client;

import java.io.BufferedReader;
import java.io.FileReader;

import org.omg.CORBA.ORB;

import corbaLibrary.CorbaLibraryServer;
import corbaLibrary.CorbaLibraryServerHelper;

public class TestClient {

	public static void main(String[] args) {
		ORB orb = ORB.init(args, null);
		try {
			BufferedReader br = new BufferedReader(new FileReader("ior/ior_Concordia.txt"));
			
			String ior = br.readLine();
			br.close();
			System.out.println(ior);
			org.omg.CORBA.Object o = orb.string_to_object(ior);
			CorbaLibraryServer server = CorbaLibraryServerHelper.narrow(o);
			System.out.println("awef");

			server.createAccount(" ", "", "", "", "asefwae", "awef", "Concordia");
			
			System.out.println("awef");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
