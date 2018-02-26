package simple_web_server.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import simple_web_server.pages.UploadImage;

/**
 * Handles POST requests received.
 * @author dcalde202
 *
 */

public class PostRequestHandler {
	private String url;
    private PrintWriter out;
    private Socket s;
    BufferedReader in;

	/**
	 * Constructor.  Request a socket, a BufferedReader stream in, and the request in String.
	 * @param socket  Socket object used to create the appropriate InputStreams for handling the post request
	 * @param in  BufferedReader Used to read lines from the request
	 * @param request  String contains the specific URL of the request
	 */
	public PostRequestHandler(Socket socket, BufferedReader in, String request){
		this.s=socket;
		this.url = request.split(" ")[1];
		this.in = in;
	}
	
	/**
	 * handleRequest
	 * Method to action on the request.  Checks that the URL being used is a supported one for a POST request.  Returns a 404 if request is not supported.
	 * @throws IOException Any IOExceptions are thrown for catching elsewhere
	 */
	protected void handleRequest() throws IOException{

		//Check to see if the URL of the request is supported
		if(url.matches("/upload/.*")){
			//Instantiates out as a PrintWriter
			out = new PrintWriter(s.getOutputStream(), true);
			
			//Calls the appropriate page class "UploadImage" with required variables and calls it to be handled
			UploadImage uploader = new UploadImage(s, out, in, url);
			uploader.handle();
			
			//Sends the response to the client
			this.printOutput(uploader.getResponse());
			
		}else{
			//Unsupported URL, send a 404
			out = new PrintWriter(s.getOutputStream(), true);
			out.println("HTTP/1.0 404");
            out.println("Content-type: text/html");
            out.println("Server-name: myserver");
		}
		
		//Close out any open Output Streams
        out.flush();
        out.close();

	}
	/**
	 * printOutput 
	 * Cycles through a String array and outputs each line to the class variable "out".
	 * out needs to be instantiated before calling printOutput. 
	 * @param responses String Array of response to be returned to the client
	 */
	private void printOutput(String[] responses){
		for(String response:responses){
			out.println(response);
		}
	}

}
