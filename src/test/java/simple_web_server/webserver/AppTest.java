package simple_web_server.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.Socket;

import org.mockito.Mockito;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Test the upload endpoint to ensure it is working
     */
    public void testUpload()
    {
    	
    	boolean goodTest = false;  //Test a good URL
    	boolean badTest = false;	//Test a bad URL
    	
    	Socket socket = Mockito.mock(Socket.class);
    	
    	String fakeTestFile = "This is a fake test file";
    	    	
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    	ByteArrayInputStream inStream = new ByteArrayInputStream(fakeTestFile.getBytes());
    	
    	BufferedReader bfreader = new BufferedReader(new StringReader("Content-Length: " + fakeTestFile.getBytes().length + "\nExpect"));
    	
    	//Test a good URL and a bad URL
    	String request = "POST /upload/filename.png";
    	String badRequest = "POST /badURL/filename.png";

    	PostRequestHandler postRequestHandler = new PostRequestHandler(socket, bfreader, request);
    	
    	
    	try{
    		//Set up Mockito
    		//Return ByteArrayOutputStream when requested
    		Mockito.when(socket.getOutputStream()).thenReturn(outStream);
    		Mockito.when(socket.getInputStream()).thenReturn(inStream);
    		
    		postRequestHandler.handleRequest();
    		
    		//Test the output
    		if(outStream.toString().contains("HTTP/1.0 200")){
    			goodTest = true;
    		}
    		
        	postRequestHandler = new PostRequestHandler(socket, bfreader, badRequest);


    		postRequestHandler.handleRequest();
    		
    		if(outStream.toString().contains("HTTP/1.0 404")){
    			badTest = true;
    		}
    		
    	} catch (Exception e){
    		System.out.println(e.toString());
    		e.printStackTrace();
    	}

    	assertTrue(goodTest&&badTest);
    }
    
    public void testImages(){
    	//Create duplicate test values for good and bad testing
    	Boolean goodTest = false;
    	Boolean badTest = false;
    	
    	Socket socket = Mockito.mock(Socket.class);
    	//Create Duplicate Streams for Good and Bad testing
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    	ByteArrayOutputStream outStreamBad = new ByteArrayOutputStream();

    	
    	String goodRequest = "GET /images";
    	String badRequest = "GET /swimages";
    	
    	GetRequestHandler getRequestHandler = new GetRequestHandler(socket, goodRequest);
    	GetRequestHandler getBadRequestHandler = new GetRequestHandler(socket, badRequest);

    	
    	try{
    		Mockito.when(socket.getOutputStream()).thenReturn(outStream).thenReturn(outStreamBad);
    		getRequestHandler.handleRequest();
    		
    		getBadRequestHandler.handleRequest();
    		
    		System.out.println("AppTest.testImages: Header Response from test: \n" + outStream.toString());
    		if(outStream.toString().contains("HTTP/1.0 200")){
    			goodTest = true;
    		}
    		if(outStreamBad.toString().contains("HTTP/1.0 404")){
    			badTest = true;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	assertTrue(goodTest&&badTest);
    }
    
    public void testImage(){
    	//Create duplicate test values for good and bad testing
    	Boolean goodTest = false;
    	Boolean badTest = false;
    	
    	Socket socket = Mockito.mock(Socket.class);
    	//Create Duplicate Streams for Good and Bad testing
    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    	ByteArrayOutputStream outStreamBad = new ByteArrayOutputStream();

    	
    	String goodRequest = "GET /image/picture.png";
    	String badRequest = "GET /image/notThere.png";
    	
    	GetRequestHandler getRequestHandler = new GetRequestHandler(socket, goodRequest);
    	GetRequestHandler getBadRequestHandler = new GetRequestHandler(socket, badRequest);

    	
    	try{
    		Mockito.when(socket.getOutputStream()).thenReturn(outStream).thenReturn(outStreamBad);
    		getRequestHandler.handleRequest();
    		
    		getBadRequestHandler.handleRequest();
    		
    		System.out.println("AppTest.testImages: Header Response from test: \n" + outStream.toString());
    		if(outStream.toString().contains("HTTP/1.0 200")){
    			goodTest = true;
    		}
    		if(outStreamBad.toString().contains("HTTP/1.0 404")){
    			badTest = true;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	assertTrue(goodTest&&badTest);
    }
}
