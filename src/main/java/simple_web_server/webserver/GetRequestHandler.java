package simple_web_server.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

import simple_web_server.pages.*;


/**
 * Handles GET requests from the main method.
 * @author dcalde202
 *
 */
public class GetRequestHandler {
	
	String url;				//String value of the URL being requested
    PrintWriter out;		//PrintWriter output object
    OutputStream outFile;	//OutputStream for returning a file
    Socket s;				//Socket object needed for getting the output stream

	/**
	 * Class constructor
	 * @param socket	Socket object containing the request, input and output streams
	 * @param request	String defining the request and URL
	 */
	public GetRequestHandler(Socket socket,String request){
		this.s=socket;
		//get url from request string
		this.url = request.split(" ")[1];
	}
	/**
	 * Handles the request.  
	 * Determines which endpoint the request is for and forwards to the appropriate class to respond to the request
	 * @throws IOException
	 */
	protected void handleRequest() throws IOException{
		
		//Determines the endpoint
		if(url.equals("/images")){
			out = new PrintWriter(s.getOutputStream(), true);
			//Instantiates class object for the endpoint
			GetImages files = new GetImages();
			//Outputs the response
			printOutput(files.getContent());
		}if(url.matches("/image/.*")){
			
			//Assigns the output stream to a PrintWriter object
			out = new PrintWriter(s.getOutputStream(), true);
			
			//Instantiates class object for endpoint passing the whole URL to the class
			ReturnImage file = new ReturnImage(url);
			
			
			//Creates an output OutputStream object that will be used to stream the file
			outFile = s.getOutputStream();
			
			
			InputStream inputStream = file.getFileStream();
			
			//Gets response headers and sends them to the client
			printOutput(file.getHeaders());
			
			//Copies the file to the outputstream using IOUTils from apache commons
			//Checks for null inputstream first
			if(inputStream!=null){
				IOUtils.copy(inputStream,outFile);
				file.closeStream();
			}
			
		}else{
			//All other non-supported endpoints are returned a 404
			out = new PrintWriter(s.getOutputStream(), true);
			out.println("HTTP/1.0 404");
            out.println("Content-type: text/html");
            out.println("Server-name: myserver");
		}
		
		//Closes all output streams
        out.flush();
        out.close();

	}
	/**
	 * Helper method to print headers from the endpoint class.
	 * @param responses		String array with all headers to be sent to the client
	 */
	private void printOutput(String[] responses){
		for(String response:responses){
			out.println(response);
		}
	}
}
