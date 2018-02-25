package simple_web_server.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReturnImage {
	InputStream in;
	String fileName;
	File file;
	
	public ReturnImage(String url){
		fileName = url.split("/image/")[1];
		String fileLocation = "/tmp/webServer/images/"; //NEEDS TO BE UDPATED TO READ FROM PROPERTIES
		
		file = new File(fileLocation + fileName);

	}
	
	public InputStream getFileStream() throws FileNotFoundException{

		try{
			in = new FileInputStream(file);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			throw e;
		}
		
		
		return in;
		
	}
	public String[] getHeaders(){
	
	String responseCode = "HTTP/1.0 200";
	String contentType = "Content-type: image/png";
	String contentLength = "Content-length: " + (file.length());
	String disposition = "Content-Disposition: attachment; filename=\"" + fileName + "\"";
	String emptyString = "";

	
	String[] response = {responseCode, contentType, contentLength, disposition, emptyString };
	
	return response;
	}
	
	public void closeStream() throws IOException{
		in.close();
	}
}




