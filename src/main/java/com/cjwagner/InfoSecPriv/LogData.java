package com.cjwagner.InfoSecPriv;

import java.util.Date;
import java.util.Map;

public class LogData {
	private Date date;
	private String url;
	private Map<String, String> loggedInputs;
	
	public LogData(){}
	
	public LogData(Date date, String url, Map<String,String> loggedInputs)
	{
		this.date = date;
		this.url = url;
		this.loggedInputs = loggedInputs;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, String> getLoggedInputs() {
		return loggedInputs;
	}
	public void setLoggedInputs(Map<String, String> loggedInputs) {
		this.loggedInputs = loggedInputs;
	}
}
