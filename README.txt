--------------------------------------------------------------------------
Distributed Reservation Management System with RESTful Services (DRMS PM3)
--------------------------------------------------------------------------



----------------------------
Required Runtime Environment
----------------------------
	
	- Java SE 1.8
	- JUnit 4
	- Apache Tomcat 7.0
	- Jersey RESTful Web Services Framework 1.19
	- Apache Maven


--------------------
Instructions of demo
--------------------

	- First, Install Apache Tomcat 7.0 and Add the project to the Tomcat server 
	- Start the server
	- Run Demo.java as a java application
	- Input the running duration on the console (in seconds)
	- The console will print out the start time, end time and number of remote procedure calls performed
	- There is also a demo_log maintain these informations


--------------------------
Instructions of Junit Test
--------------------------

	- Library Server Test
		- To run the library server test, just simply run the java file as JUnit test
		- Do NOT start the tomcat server during the running of library server test

	- Admin & Student Client Test
		- To run the student client test, first start the Tomcat server
		- Then run the admin or student client test as JUnit test
		- Follow the instructions on the console to input informations to perform the remote procedure