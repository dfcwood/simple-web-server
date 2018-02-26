package simple_web_server.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * Contains the main method.  Primary responsible for opening the socket and spawn threads to handle any incomming connections.
 * The the class also determines if the incoming request is of the two support types:  POST, GET.
 * @author dcalde202
 *
 */


public class App 
{
	private static final int fNumberOfThreads = 100;		//Max number of threads allowed
    private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);  //Executor for threads
    private static Properties prop;
    /**
     * Main method.  Opens Socket and spawns threads to handle incoming connections.
     * @param args  Not currently used
     * @throws IOException Throws any IOExceptions received
     */
    public static void main(String[] args) throws IOException
    {
    	//Load properties values
    	System.out.println("App.Main:  Loading Properties");
    	prop = new Properties();
    	try{
    		prop.load(App.class.getResourceAsStream("/custom.properties"));
    	}catch(Exception e){
    		System.out.println("Failed to load properties");
    		System.exit(1);
    	}
    	
    	//Precheck on the folder that is used to store
    	System.out.println("App.main:  Performing Precheck on file directory");
    	try{
    		preCheckFolder();
    	} catch(FileNotFoundException e){
    		e.printStackTrace();
    		System.exit(1);
    	}
    	  
    	//Load the socket from properties File
    	int socketNum = Integer.valueOf(prop.getProperty("socket.port"));
    	  
    	//Open the Socket
        @SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(socketNum);
        
        //Listen for new connections on the socket and create new threads for each socket
        System.out.println("App.main:  Listening for new connections on port " + socketNum);
        while (true)
        {
            final Socket connection = socket.accept();
            Runnable task = new Runnable()
            {
                public void run()
                {
                    HandleRequest(connection);
                }
            };
            fThreadPool.execute(task);
        }
    }
    /**
     * HandleRequest
     * Handles new incoming connections as a new thread.  Determines the type of request (GET or POST) and handles them appropriately
     * @param s Socket object for the incoming connection
     */
    private static void HandleRequest(Socket s)
    {
        BufferedReader in;
        String request;
        
        //Try to read from the incomming connection
        try {
        	//Get the Address of the incoming connection
            String webServerAddress = s.getInetAddress().toString();
            
            System.out.println("App.HandleRequest:  New Connection:" + webServerAddress);
            
            //Get a buffered input stream to read from
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            //Load the first line to get the request
            request = in.readLine();
            
            //Split the request on SPACE to get the request type:  GET or POST
            String requestType = request.split(" ")[0];
            
            System.out.println("App.HandleRequest:  --- Client request: " + request + ". Handle new " + requestType);

            //Action based on request type or return 400 bad request
            
            if(requestType.equals("GET")){
            	
            	//Call appropriate handler for a GET request
            	GetRequestHandler getRequest = new GetRequestHandler(s, request);
            	getRequest.handleRequest();
            }else if(requestType.equals("POST")){
            	
            	//Call Appropriate handler for a POST request
            	PostRequestHandler postRequest = new PostRequestHandler(s, in, request);
            	postRequest.handleRequest();
            }else {
            	//Not a get or a post, return 400
            	System.out.println("App.HandleRequest:  Not a GET or a POST, returning 400 Bad request" );
            	PrintWriter out = new PrintWriter(s.getOutputStream());
            	out.println("HTTP/1.0 400 Bad Request");
            	
            	out.flush();
            	out.close();
            }
            
            //Close the socket, out streams should be closed by the handlers
            //Closing the socket closes the inputstreams
            s.close();
        }
        catch (IOException e) {
        	//Catch Exceptions
            System.out.println("Failed respond to client request: " + e.getMessage());
        }
        finally {
        	//After exceptions, close the sockets
            if (s != null) {
                try {
                    s.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    
    private static void preCheckFolder() throws FileNotFoundException{
    	
    	System.out.println("App.preCheckFolder:  Getting property:  saved.files.directory");
    	
		String fileLocation = prop.getProperty("saved.files.directory");
		
		System.out.println("App.preCheckFolder:  Loaded property value " + fileLocation);
		
		File folder = new File(fileLocation);
		
		System.out.println("App.preCheckFolder:  Checking Folder");
		if(!folder.exists()){
			System.out.println("App.preCheckFolder:  Folder doesn't exist, creating it");
			if(!folder.mkdirs()){
				System.out.println("App.preCheckFolder:  Failed to create folder");
				throw new FileNotFoundException();
			}
			System.out.println("App.preCheckFolder:  Folder created");
		}

    }
 
}