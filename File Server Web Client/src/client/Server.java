package client;
/*********************************************************************
 * Name: 					Martin Tran, Jordan Aikey, Sam Donaldson
 * Username:				dist112, dist500,
 * Problem Set: 			Server-Client Lab
 * Due Date:				11/17/2020
 * Class:					Distributed Systems
 *********************************************************************/

import java.util.Hashtable;

/***
 * Static server class to authenticate clients.
 * 
 * @author Jordan
 *
 */
public class Server {
	private static final String host = "dist.cs.uafs.edu";
	private static final int port = 32211;
	private static Hashtable<String,Client> connections = new Hashtable<String,Client>();
	private static Client client = new Client(host,port,"Authentication");
	
	/***
	 * Checks if the session is already connected.
	 * 
	 * @param sessionID
	 * @return
	 * 
	 */
	public synchronized static Boolean isConnected(String sessionID) {
		if(connections.get(sessionID) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	/***
	 * Gets the authorization client
	 * 
	 * @return
	 * 
	 */
	public synchronized static Client getAuthClient() {
		return client;
	}
	
	/***
	 * Gets the client with the session ID
	 * 
	 * @param sessionID
	 * @return
	 * 
	 */
	public synchronized static Client getClient(String sessionID) {
		return connections.get(sessionID);
	}
	
	/***
	 * Adds a new client with the session and the username
	 * 
	 * @param sessionID
	 * @param username
	 * 
	 */
	public synchronized static void addClient(String sessionID,String username) {
		System.out.println("USER LOGGED: "+username);
		Client cl = new Client(host,port,username);
		connections.put(sessionID,cl);
	}
}	
