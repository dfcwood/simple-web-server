package simple_web_server.pages;

import java.io.File;
import java.util.Properties;

import simple_web_server.webserver.App;

/**
 * Handles reqeuests to /images/ endpoint and returns a list of files from the specified directory to the client
 * @author dcalde202
 *
 */
public class GetImages {
	
	private static Properties prop;
	
	
	/**
	 * Class constructor
	 */
	public GetImages(){
		//Loads properties
    	prop = new Properties();
    	System.out.println("GetImages.GetImages:  Loading Properties");
    	try{
    		prop.load(App.class.getResourceAsStream("/custom.properties"));
    	}catch(Exception e){
    		System.out.println("Failed to load properties");
    		System.exit(1);
    	}

	}
	/**
	 * Returns the response headers
	 * @return String array with headers
	 */
	public String[] getContent(){
		String responseCode = "HTTP/1.0 200";
		String contentType = "Content-type: text/html";
		String serverName = "Server-name: myserver";
		String bodyText = getListOfFiles();
		String contentLength = "Content-length: " + (bodyText.length()+1);
		String emptyString = "";

		String[] content = {responseCode, contentType, serverName, contentLength, emptyString, bodyText};
		
		return content;
	}
	/**
	 * Gets a list of files from the specified directory and sends them as a comma separated list
	 * @return Comma separated String list of files in directory
	 */
	private static String getListOfFiles(){
		
		String fileLocation = prop.getProperty("saved.files.directory");
		
		String list = "";
	
		//Creates file object pointing to the specified directory
		File folder = new File(fileLocation);
		
		//Creates an array of files in the directory
		File[] listOfFiles = folder.listFiles();
		
		//Cycles through the files and adds the files to the return String
		for(File file: listOfFiles){
			if(file.isFile()){
				list = list + file.getName() + ",";
			}
		}

		return list;
	}
}
