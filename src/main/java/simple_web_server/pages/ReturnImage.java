package simple_web_server.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import simple_web_server.webserver.App;

/**
 * Handles the endpoint /image/filename.png and returns the image to the client
 * @author dcalde202
 *
 */
public class ReturnImage {
	InputStream in;			
	String fileName;		//name of the file
	String fileLocation;	//Directory of the files
	File file;				//File object
	private static Properties prop;
	
	//Variables containing headers
	String responseCode;
	String contentType;
	String contentLength;
	String disposition;
	String emptyString;


	/**
	 * Constructor
	 * @param url String URL contains the requested filename
	 */
	public ReturnImage(String url){
		
		//Get the filename from the URL
		fileName = url.split("/image/")[1];
		
		//Get the file location from properties
		prop = new Properties();
    	System.out.println("ReturnImage.ReturnImage:  Loading Properties");
    	try{
    		prop.load(App.class.getResourceAsStream("/custom.properties"));
    	}catch(Exception e){
    		System.out.println("Failed to load properties");
    		System.exit(1);
    	}
    	
    	//Gets the property for the directory
    	fileLocation = prop.getProperty("saved.files.directory");
		
		file = new File(fileLocation + fileName);

	}
	/**
	 * Opens the file and gets an Inputstream from the file.
	 * @return  FileInputStream of the requested file
	 * @throws FileNotFoundException If the file isn't found in the specified directory
	 */
	public InputStream getFileStream() throws FileNotFoundException{

		try{
			in = new FileInputStream(file);
		}catch(FileNotFoundException e){
			System.out.println("ReturnImage.getFileStream:  Unable to find file " + fileName + " in directory " + fileLocation);
			
			//Since file not found, change headers to 404
			responseCode = "HTTP/1.0 404";
			contentType = "";
			contentLength = "";
			disposition = "";
			emptyString = "";
			return null;
		}
		
		//Successfully found file, set headers
		responseCode = "HTTP/1.0 200";
		contentType = "Content-type: image/png";
		contentLength = "Content-length: " + (file.length());
		disposition = "Content-Disposition: attachment; filename=\"" + fileName + "\"";
		emptyString = "";
		
		return in;
		
	}
	/**
	 * Puts all the headers into a String array to send to the client
	 * @return String array of reponse headers
	 */
	public String[] getHeaders(){
		
	String[] response = {responseCode, contentType, contentLength, disposition, emptyString };
	
	return response;
	}
	
	/**
	 * Closes the file stream
	 * @throws IOException
	 */
	public void closeStream() throws IOException{
		in.close();
	}
}




