package com.cjwagner.InfoSecPriv;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@JsonIgnoreProperties({"objMapper"})
public class LoggerMessage {

	private List<LogData> logs;
	private Date firstLogTime;
	
	private static ObjectMapper objMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
	
	public List<LogData> getLogs() {
		return logs;
	}
	public void setLogs(List<LogData> logs) {
		this.logs = logs;
	}
	public Date getFirstLogTime() {
		return firstLogTime;
	}
	public void setFirstLogTime(Date firstLogTime) {
		this.firstLogTime = firstLogTime;
	}
	
	public static LoggerMessage fromJSON(String json) throws JsonParseException, JsonMappingException, IOException
	{
		LoggerMessage lm = objMapper.readValue(json, LoggerMessage.class);
		return lm;
	}
	
	public static String toJSON(LoggerMessage lmsg)
	{
		try {
			return objMapper.writeValueAsString(lmsg);
		} catch (JsonProcessingException e) {
			System.out.println("Failed to write LoggerMessage POJO to JSON!  ERROR: " + e.getMessage());
			return "";
		}
	}
}
