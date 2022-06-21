package client;
/*********************************************************************
 * Name: 					Martin Tran, Jordan Aikey, Sam Donaldson
 * Username:				dist112, dist500,
 * Problem Set: 			Server-Client Lab
 * Due Date:				11/17/2020
 * Class:					Distributed Systems
 *********************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.Part;
/***
 * Client to connect to the master server and send/remove/list files
 * 
 * @author Jordan
 *
 */
public class Client {
	private Socket socket;  
	private final int fourBytePage = 4096;
	private BufferedReader br;
	private PrintWriter pr;
	private String userName;
	private final String dlPath = "C:\\Users\\Jordan\\Downloads\\";

	/***
	 * Initializes a new client.
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 */
	public Client(String host, int port,String userName) {
		
		try {
			socket = new Socket(host, port);  
			this.userName = userName;
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pr = new PrintWriter(socket.getOutputStream(),true);
			
			pr.println("CL");	
			pr.println(userName);
			
			if(!userName.equals("Authentication")) {
				sendCommandInit(userName);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

	}
	
	/***
	 * Gets this clients username
	 * 
	 * @return
	 */
	public String getUser() {
		return userName;
	}
	
	/***
	 * Sends command to initialize new worker on file server.
	 * 
	 * @param username
	 * @throws IOException
	 */
	public void sendCommandInit(String username) throws IOException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pr.println("init>"+userName);
		br.readLine();
	}
	
	/***
	 * Sends command to add file.
	 * 
	 * @param username
	 * @param filename
	 * @param parts
	 * @throws IOException
	 */
	public void sendCommandAddFile(String username, String filename, Collection<Part> parts) throws IOException {

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
        long size = 0;
        for(Part p : parts) {
        	size += p.getSize();
        }
        
        pr.println("add>"+filename + ">" + size + ">" + username);
        
        for(Part p : parts) {
        	readFile(p,size);
        }
        
        String response = br.readLine();
	}
	
	/***
	 * Sends command to remove file.
	 * 
	 * @param username
	 * @param filename
	 * @throws IOException
	 */
	public void sendCommandRemoveFile(String username, String filename) throws IOException {

		String packetCmd = "remove>"+filename+">"+username;
		pr.println(packetCmd);
		String response = br.readLine();
	}
	
	/***
	 * Sends command to download file.
	 * @param username
	 * @param filename
	 * @throws IOException
	 */
	public void sendCommandDownloadFile(String username, String filename) throws IOException {

		System.out.println("Sending download command for file "+filename);
		pr.println("get>"+filename+">"+username);
		
		String input = br.readLine();
		System.out.println(input);
		long size = Long.parseLong(input);
		
		System.out.println("SIZE: "+size);
		
        writeFile(filename, size);
        
    }
	
	/***
	 * Sends command to authenticate user.
	 * 
	 * @param user
	 * @param pass
	 * @return
	 * @throws IOException
	 */
	public Boolean sendCommandAuthenticate(String user, char[] pass) throws IOException {

		String message = "authenticate>"+user+">";
		
		for(char c : pass) {
			message+=c;
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pr.println(message);
		System.out.println("Sent auth message.");
        String response = br.readLine();

        System.out.println(response);
        
        if(response.equals("success")) {
        	return true;
        }else {
        	return false;
        }
    }
	
	/***
	 * Sends command to create a new account.
	 * 
	 * @param user
	 * @param pass
	 * @return
	 * @throws IOException
	 */
	public Boolean sendCommandCreateAccount(String user, String pass) throws IOException {

		String message = "createaccount>"+user+">"+pass;
		
		pr.println(message);
		
		String response = br.readLine();
        
        System.out.println(response);
        System.out.println(br);
        
        if(response.equals("success")) {
        	return true;
        }else {
        	return false;
        }
	}
	
	/***
	 * Sends command to list files.
	 * 
	 * @param username
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> getAllFiles(String username) throws IOException{

		ArrayList<String> files = new ArrayList<String>();
		String packetCmd = "list>"+username;
		
		//Send request for all files.
		pr.println(packetCmd);
		
		String line = br.readLine();
		
		if(line.equals("success")) {
			return getAllFiles(username);
		}
		
		System.out.println("Recieved response from master server...");
		System.out.println("INPUT: "+line);
		
		if(!line.equals("failure")) {
			//Grab all files. Output is expect to be as follows: File1.txt,File2.txt,File3.jpg
			if(line.length() > 0) {
				String[] fileData = line.split(",");
				for(int i = 0; i < fileData.length; i++) {
					String file = fileData[i];
					if(file.length() > 0) {
						files.add(file);
					}
				}
			}
		}
		
		return files;
	}
	
	public void writeFile(String filename, long size) throws IOException {

		byte[] bytes = new byte[fourBytePage];

		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(dlPath+filename));

		System.out.println("Starting read from master server...");
        int count = fourBytePage;
        while((count = in.read(bytes, 0, (int)Math.min(fourBytePage, size))) > 0) {
            System.out.println("Writing " + count + " bytes to local file...");
            out.write(bytes, 0 , count);
            size -= count;
        }
        
        System.out.println("Done!");
        
        out.close();

	}
	
	public void readFile(Part p, long size) throws IOException {

		byte[] bytes = new byte[fourBytePage];

		BufferedInputStream in = new BufferedInputStream(p.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		System.out.println("Starting read from local file...");
        int count = fourBytePage;
        while((count = in.read(bytes, 0, (int)Math.min(fourBytePage, size))) > 0) {
            System.out.println("Sending " + count + " bytes to master server...");
            out.write(bytes, 0 , count);
            size -= count;
        }
        
        System.out.println("Done!");

	}
	

}
