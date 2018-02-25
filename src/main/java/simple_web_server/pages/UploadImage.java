package simple_web_server.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UploadImage {
	PrintWriter out;
	FileOutputStream fileOut;
	Socket s;
    BufferedReader in;
    String fileName;
    
    Boolean proceed = true;
    
	String responseCode;
	String contentType;
	String serverName;
	String emptyString = "";


	
	public UploadImage(Socket s, PrintWriter out, BufferedReader in, String url){
		this.s = s;
		this.out = out;
		this.in = in;
		fileName=url.split("/")[2];
		
	}
	
	public void handle() throws IOException{
		
		this.validateFileName();
		
		if(proceed){
			
			String fileLocation = "/tmp/webServer/images/"; //NEEDS TO BE UDPATED TO READ FROM PROPERTIES

			File file = new File(fileLocation + fileName);
						
			out.println("HTTP/1.0 100 Continue");
			fileOut = new FileOutputStream(file);
			
			int fileSize = 0;
			
			System.out.println("Attempting to copy");
			String line = "";
			while(!(line = in.readLine()).contains("Expect")){
				System.out.println(line);
				if(line.contains("Content-Length")){
					fileSize = Integer.valueOf(line.split(": ")[1].trim());
				}
			}
			
			InputStream initialStream = s.getInputStream();
			byte[] buffer = new byte[8 * 1024];
		    int bytesRead;
		    int totalBytes = 0;
		    while ( totalBytes < fileSize) {
		    	bytesRead = initialStream.read(buffer);
		    	totalBytes = totalBytes + bytesRead;
		        fileOut.write(buffer, 0, bytesRead);
		        System.out.println(totalBytes);
		    }
	    	//FileUtils.copyInputStreamToFile(s.getInputStream(), file);
	    	
	    	System.out.println("Copying Complete");
	    	
	    	createResponse("200");

		}
	}
	public void validateFileName(){
		if(!fileName.endsWith(".png")){
			proceed=false;
			this.createResponse("400");
		}
	}
	
	public void createResponse(String code){
		responseCode = "HTTP/1.0 " + code;
	}
	
	public String[] getResponse(){
		String[] response = {responseCode};
		return response;
	}
}
