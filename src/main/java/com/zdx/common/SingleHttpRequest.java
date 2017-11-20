package com.zdx.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SingleHttpRequest {
	private static final Logger logger = LogManager.getLogger(SingleHttpRequest.class);

	public static String getResultFromURL(String url){
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = HttpClients.createDefault().execute(request);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException");
			e.printStackTrace();
		}
		if (response == null){
			logger.warn("Http Response = NULL ");
			return null;
		}
		if (response.getStatusLine().getStatusCode() != 200){
			logger.warn("Http Response status = " + response.getStatusLine().getStatusCode());
			return null;
		}
		String result = "";
		try {
			result = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			logger.error("ParseException");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException");
			e.printStackTrace();
		}
		if (result.isEmpty()){
			logger.warn("JSOUP parse result = NULL");
			return null;
		}
		return result;
	}
}
