package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.spi.resource.Singleton;

import frontend.FrontEnd;

@Path("/library-service-frontend")
@Singleton
public class FrontEndService extends FrontEnd {
	
	public FrontEndService() {
		super();
		System.out.println("Frontend UP");
	}

	@Override
	@Path("/create-account/{firstName}-{lastName}-{emailAddress}-{phoneNumber}-{username}-{password}-{eduInstitution}")
	@GET
	@Produces("text/plain")
	public String createAccount(@PathParam("firstName")String firstName, @PathParam("lastName")String lastName,
			@PathParam("emailAddress")String emailAddress, @PathParam("phoneNumber")String phoneNumber, @PathParam("username")String username,
			@PathParam("password")String password, @PathParam("eduInstitution")String eduInstitution) {
		
		return super.createAccount(firstName, lastName, emailAddress, phoneNumber, username, password, eduInstitution);
	}
	
	@Override
	@Path("/reserve-book/{username}-{password}-{bookName}-{authorName}")
	@GET
	@Produces("text/plain")
	public String reserveBook(@PathParam("username")String username, @PathParam("password")String password,
			@PathParam("bookName")String bookName, @PathParam("authorName")String authorName)  {
		
		return super.reserveBook(username, password, bookName, authorName);
		
	}
		
	
}
