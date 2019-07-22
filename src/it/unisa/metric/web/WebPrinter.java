package it.unisa.metric.web;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint.Basic;

import it.unisa.metric.web.socket.SocketEventType;
import it.unisa.metric.web.socket.SocketResponse;

import javax.websocket.Session;

public class WebPrinter extends PrintStream{
	
	private Session session;
	private String buffer;
	
	private BufferedWriter writer;
	
	private File file;
	private static String lOG_PATH= WebConstants.APPLICATION_PATH+File.separator+WebConstants.LOGS_DIRECTORY;
	
	public WebPrinter(OutputStream out, Session session, String logFilename) {
		super(out);
		this.session= session;
		buffer="";
		File dir = new File(lOG_PATH);
		if (!dir.exists()) 
			dir.mkdirs();
		file = new File(dir,logFilename);
		try {
			//Create if not exists
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			writer = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void println(String s) {
		
		try {
			if(buffer.length()>WebConstants.WEBSOCKET_BUFFER_LIMIT) {
				if(session.isOpen())
					session.getBasicRemote().sendText(new SocketResponse(buffer, "", SocketEventType.STREAM_TEXT).toJson());
				buffer="";
			}
			else {
				buffer+=s;
			}
			super.println(s);
			writer.write(s+"\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printFinalText() {
		try {
			depleteBuffer();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile()  {
		return file;
	}
	
	public void depleteBuffer() {
		try {
			if(buffer.length()>0) {
				if(session.isOpen())
					session.getBasicRemote().sendText(new SocketResponse(buffer, "", SocketEventType.STREAM_TEXT).toJson());
				buffer="";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}
