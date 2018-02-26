package simple_web_server.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

import simple_web_server.webserver.App;

public class UploadImage {
	PrintWriter out;  //Used to send response codes to the client
	FileOutputStream fileOut;  //Used to write the input data to a file
	Socket s;  //Socket for the connection
    BufferedReader in;  //Used to read the header from the client
    String fileName;  //Filename of the file being uploaded
    
    Boolean proceed = true;  //Used to determine if file upload can proceed
    
    private Properties prop;  //Property file to read save file directory

    
	String responseCode;  //HTML response code
	String emptyString = "";


	/**
	 * Constructor
	 * 
	 * @param s Socket used for the connection
	 * @param out PrintWriter used to send HTML response codes to the client
	 * @param in BufferedReader used to read the header from the client
	 * @param url String contains the full URL and the name of the file from the client
	 */
	public UploadImage(Socket s, PrintWriter out, BufferedReader in, String url){
		this.s = s;
		this.out = out;
		this.in = in;
		//Filename is taken from the URL and is split on "/"
		fileName=url.split("/")[2];
		//Load properties
    	prop = new Properties();
    	try{
    		prop.load(App.class.getResourceAsStream("/custom.properties"));
    	}catch(Exception e){
    		System.out.println("Failed to load properties");
    		System.exit(1);
    	}

	}
	/**
	 * handle
	 * After instantiation, call handle to transact the request.  Filename will be validated and upload will be completed.
	 * @throws IOException
	 */
	public void handle() throws IOException{
		//Validate the filename to include .png
		this.validateFileName();
		
		//If the filename validates, proceed with upload
		if(proceed){
			//Get directory from properties
			String fileLocation = prop.getProperty("saved.files.directory");
			
			//create file object from directory and filename
			File file = new File(fileLocation + fileName);
			
			//Create a starting variable to hold file size and initialize
			int fileSize = 0;
			
			String line = "";
			//Cycle through request headers util the Excpect
			while(!(line = in.readLine()).contains("Expect")){
				System.out.println("UploadImage.handle:  Header Info: " + line);
				
				//Assign the value of header Content-Length to filesize
				if(line.contains("Content-Length")){
					fileSize = Integer.valueOf(line.split(": ")[1].trim());
				}
			}
			
			//Sent an HTTP 100 telling client to proceed with upload
			out.println("HTTP/1.0 100 Continue");
			fileOut = new FileOutputStream(file);
			
			//Create the InputStream to stream file to server
			InputStream initialStream = s.getInputStream();
			
			//Declare a byte buffer
			byte[] buffer = new byte[8 * 1024];
		    int bytesRead;
		    int totalBytes = 0;
		    
		    
			System.out.println("UploadImage.handle:  Attempting to copy");

		    //While look takes bytes from the stream and passes them to a FileOutStream object to insert into a file
		    //Continues until the amount of bytes transfered matches the Content-Length
		    while ( totalBytes < fileSize) {
		    	bytesRead = initialStream.read(buffer);
		    	totalBytes = totalBytes + bytesRead;
		        fileOut.write(buffer, 0, bytesRead);
		        System.out.println("UploadImage.handle:  Total bytes copied " + totalBytes);
		    }
	    	
	    	System.out.println("UploadImage.handle:  Copying Complete");
	    	
	    	createResponse("200");

		}
	}
	
	/**
	 * validateFileName
	 * Validates that the filename ends with .png.  Only .png files are supported.
	 */
	public void validateFileName(){
		if(!fileName.endsWith(".png")){
			proceed=false;
			this.createResponse("400");
		}
	}
	
	/**
	 * createResponse
	 * Populates the response header variables.  In this case, we are just using the responseCode.
	 * @param code String Literal for the HTTP response code, plus an additional response info (i.e. Bad Request, Not Found etc.)
	 */
	public void createResponse(String code){
		responseCode = "HTTP/1.0 " + code;
	}
	
	/**
	 * getResponse
	 * Creates a string array with any applicable response headers to the client.
	 * @return String array of response headers
	 */
	public String[] getResponse(){
		String[] response = {responseCode};
		return response;
	}
}
