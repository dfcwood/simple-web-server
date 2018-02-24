package simple_web_server.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

import simple_web_server.pages.*;



public class GetRequestHandler {
	
	private String url;
    PrintWriter out;
    OutputStream outFile;
    Socket s;

	
	public GetRequestHandler(Socket socket,String request){
		this.s=socket;
		this.url = request.split(" ")[1];
	}
	
	protected void handleRequest() throws IOException{

		if(url.equals("/images")){
			out = new PrintWriter(s.getOutputStream(), true);
			GetImages files = new GetImages();
			printOutput(files.getContent());
		}if(url.matches("/image/.*")){
			out = new PrintWriter(s.getOutputStream(), true);
			ReturnImage file = new ReturnImage(url);
			printOutput(file.getHeaders());
			outFile = s.getOutputStream();
			IOUtils.copy(file.getFileStream(),outFile);
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
