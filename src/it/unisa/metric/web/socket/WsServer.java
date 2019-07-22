package it.unisa.metric.web.socket;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import it.unisa.metric.Core;
import it.unisa.metric.LocalException;
import it.unisa.metric.web.Analysis;
import it.unisa.metric.web.WebConstants;
import it.unisa.metric.web.WebCore;
import it.unisa.metric.web.WebPrinter;
import it.unisa.metric.web.utils.FileUtils;
import it.unisa.metric.web.utils.MD5;
import it.unisa.metric.web.utils.ZipUtils;
import jdk.nashorn.api.scripting.JSObject;

@ServerEndpoint(value = "/websocket/{file}", configurator=CustomSocketConfigurator.class)
public class WsServer {
	
	public Basic remote;
	private WebPrinter printer;
	private PrintStream sysPrintStrem;
	private volatile String file;
	private String pathToAnalize;
	private HttpSession httpSession;
	private String sessionId;
	private String analysisId;
	private String resultPath = WebConstants.RESULTS_PATH;
	private ZipUtils zip;
	
	@OnOpen
	public void onOpen(@PathParam("file") String file,Session session, EndpointConfig config) throws IOException, EncodeException {
		this.file=file;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
		//HTTP session ID
		sessionId = MD5.getMd5((String) httpSession.getAttribute("user"));
	    //Analysis ID
		analysisId = file.split("\\.")[0]+"-"+new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
		printer = new WebPrinter(System.out, session, MD5.getMd5(analysisId)+".log");
		remote= session.getBasicRemote();
		//Get HTTP session
       
       
        
        
        //Set path to save results 
        resultPath += sessionId+File.separator+ analysisId;
		remote.sendText(new SocketResponse(SocketEventType.BEGIN_UNIZIP).toJson());
		System.out.println("Socket opening..");
		sysPrintStrem = System.out;
		System.setOut(printer);
		//Unzip file
		zip = new ZipUtils(file, analysisId);
		pathToAnalize = zip.unZipIt(); //per ora solo uno alla volta
		printer.depleteBuffer();
		remote.sendText(new SocketResponse(SocketEventType.END_UNZIP).toJson());
		onMessage(null, session);
	}
	
	
	
	@OnClose
	public void onClose() {
		System.setOut(sysPrintStrem);
		System.out.println("Socket closing...");
	}
	
	@OnMessage
	public void onMessage(String message, Session session){
		String[] args = { 
	 			"-path", 
	 			pathToAnalize
	 	};
	 	//start analisys
		
		try {
			remote.sendText(new SocketResponse(SocketEventType.BEGIN_ANALISYS).toJson());
			WebCore core = new WebCore(args);
			saveResults(core);
			printer.printFinalText();
			remote.sendText(new SocketResponse(SocketEventType.END_ANALISYS).toJson());
			remote.sendText(new SocketResponse("",analysisId,SocketEventType.GET_RESULTS).toJson());
			if(session.isOpen())
				session.close(
					new CloseReason(
							CloseReason.CloseCodes.NORMAL_CLOSURE,
							"FINISH"
					)	
				);
			
		} catch (LocalException | IOException e) {
			e.printStackTrace();
		}
	    	
	}
	
	@OnError
	public void onError(Throwable e){
	    e.printStackTrace();
	}
	
	private void saveResults(WebCore core) {
		Analysis a = new Analysis(file.split("\\.")[0], new Date(), core.GetJSONResult(), printer.getFile().getName(), core.getJSONTree());
		if(FileUtils.serializeObj(a, resultPath, "analysis.metric")) {
			
			try {
				//Delete zipped file
				FileUtils.deleteFile(zip.getZipFile());
				//Delete unzipped directory
				FileUtils.DeleteDir(zip.getDestinationFolder());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
