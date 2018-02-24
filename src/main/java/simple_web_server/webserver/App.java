package simple_web_server.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class App 
{
	private static final int fNumberOfThreads = 100;
    private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);
 
    public static void main(String[] args) throws IOException
    {
    	
    	  try
          {
          	preCheckFolder();
          } catch(FileNotFoundException e){
        	  e.printStackTrace();
        	  System.exit(1);
          }
        @SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(1986);
        while (true)
        {
            final Socket connection = socket.accept();
            Runnable task = new Runnable()
            {
               // @Override
                public void run()
                {
                    HandleRequest(connection);
                }
            };
            fThreadPool.execute(task);
        }
    }
 
    private static void HandleRequest(Socket s)
    {
        BufferedReader in;
        String request;
 
        try
        {
            String webServerAddress = s.getInetAddress().toString();
            System.out.println("New Connection:" + webServerAddress);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            
 
            request = in.readLine();
            System.out.println("--- Client request: " + request);
            
            String request_type = request.split(" ")[0];

            
            if(request_type.equals("GET")){
            	GetRequestHandler getRequest = new GetRequestHandler(s, request);
            	getRequest.handleRequest();
            }else if(request_type.equals("POST")){
            }
            
            s.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed respond to client request: " + e.getMessage());
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    
    private static void preCheckFolder() throws FileNotFoundException{
    	
		String fileLocation = "/tmp/webServer/images/"; //NEEDS TO BE UDPATED TO READ FROM PROPERTIES

		File folder = new File(fileLocation);
		
		if(!folder.exists()){
			if(!folder.mkdirs()){
				throw new FileNotFoundException();
			}
		}

    }
 
}