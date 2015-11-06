package com.cjwagner.InfoSecPriv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJSONGen {
	public static void main(String[] args)
	{
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("txtbxUserName", "steven90");
		datas.put("txtbxPass", "StevensEasyPass");
		datas.put("captcha", "99L8KSDF24S");
		
		Map<String, String> datas2 = new HashMap<String, String>();
		datas2.put("txtbxAccount", "0123456789");
		datas2.put("SSN", "999-99-9999");
		datas2.put("txtAddress", "69 Steve st");
		
		List<LogData> logs = new ArrayList<LogData>();
		logs.add(new LogData(new Date(), "http://notpornhub.com/notpornography/index.html", datas));
		logs.add(new LogData(new Date(), "http://notpornhub.com/notpornography/home.html", datas2));
		
		LoggerMessage lm = new LoggerMessage();
		lm.setLogs(logs);
		lm.setFirstLogTime(new Date());
		
		
		String json;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.writeValueAsString(lm);
			FileUtils.writeStringToFile(new File("testOutput.txt"), json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
