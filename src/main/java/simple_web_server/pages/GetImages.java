package simple_web_server.pages;

import java.io.File;
import java.util.Properties;

/**
 * 
 * @author dcalde202
 *
 */
public class GetImages {
	
	private Properties props = new Properties();  //NEEDS TO BE UPDATED TO REAL PROPERTIES
	
	
	/**
	 * Class constructor
	 */
	public GetImages(){
		
	}
	/**
	 * Returns the requested content for the URL
	 * @return
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
	
	private static String getListOfFiles(){
		String fileLocation = "/tmp/webServer/images/"; //NEEDS TO BE UDPATED TO READ FROM PROPERTIES
		String list = "";
	
		
		File folder = new File(fileLocation);
		File[] listOfFiles = folder.listFiles();
		for(File file: listOfFiles){
			if(file.isFile()){
				list = list + file.getName() + ",";
			}
		}

		return list;
	}
}
