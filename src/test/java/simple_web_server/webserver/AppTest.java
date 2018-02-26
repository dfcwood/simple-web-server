package simple_web_server.webserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
     * Test the socket connection
     */
    public void testSocketConnection()
    {
    	
    	try{
    		App.main(null);
    	}catch(IOException e){
    		assertTrue(false);
    		return;
    	}
    	  
    	Socket socket;
    	try{
    		socket = new Socket(InetAddress.getLocalHost(), 9991);
    	} catch(UnknownHostException e){
    		assertTrue(false);
    		return;
    	} catch(IOException e){
    		assertTrue(false);
    		return;
    	}

        assertTrue( socket.isConnected() );
        try{
        	socket.close();
        } catch (IOException e){
        	System.out.println(e.toString());
        }
        
    }
}
