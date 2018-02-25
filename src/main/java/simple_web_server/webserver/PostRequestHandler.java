package simple_web_server.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import simple_web_server.pages.UploadImage;


public class PostRequestHandler {
	private String url;
    private PrintWriter out;
    private Socket s;
    BufferedReader in;

	
	public PostRequestHandler(Socket socket, BufferedReader in, String request){
		this.s=socket;
		this.url = request.split(" ")[1];
		this.in = in;
	}
	
	protected void handleRequest() throws IOException{

		if(url.matches("/upload/.*")){
			
			out = new PrintWriter(s.getOutputStream(), true);

			UploadImage uploader = new UploadImage(s, out, in, url);
			
			uploader.handle();
			
			this.printOutput(uploader.getResponse());
			
		}else{
			out = new PrintWriter(s.getOutputStream(), true);
			out.println("HTTP/1.0 404");
            out.println("Content-type: text/html");
            out.println("Server-name: myserver");
		}
		
        out.flush();
        out.close();

	}
	private void printOutput(String[] responses){
		for(String response:responses){
			out.println(response);
		}
	}

}
