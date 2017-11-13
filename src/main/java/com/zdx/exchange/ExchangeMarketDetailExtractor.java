package com.zdx.exchange;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

import com.zdx.common.*;
import com.zdx.currency.MarketlDailyExtractor;

public class ExchangeMarketDetailExtractor {
	final static HashMap<String, Object> responseContext = new HashMap<String, Object>();
	final static long startTime = System.currentTimeMillis();
	final static String destDir = System.getProperty("user.dir") + File.separator + "Exchange" + File.separator + startTime;
	final static HashMap<String, String> failedExchangeMap = new HashMap<String, String>();

	public static void execute() throws InterruptedException{		
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/exchange_name");
		ArrayList<String> exchangeNames = FileHandler.getArrayListFromFile(is);
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}		
		int minError = Integer.MAX_VALUE;
		boolean done = false;
		while (!done){
			ArrayList<String> exchangeNamesLeft = new ArrayList<String>();
			for (int i = 0; i < exchangeNames.size(); i++){
				String key = exchangeNames.get(i);
				String status = failedExchangeMap.get(key);
				if (!failedExchangeMap.containsKey(key) || !("True".equals(status) || "PageParseError".equals(status) || "OtherError".equals(status)) ){
					exchangeNamesLeft.add(key);
				}
			}
			if (exchangeNamesLeft.isEmpty()){
				done = true;
			}
			if (exchangeNamesLeft.size() < minError){
				minError = exchangeNamesLeft.size();
			} else if (exchangeNamesLeft.size() == minError){
				done = true;//两轮结果一样，
			}
			oneBatch(exchangeNamesLeft);
			//Thread.sleep(20000);
			
		}
		FileHandler.writeFile(destDir + File.separator + "failed.json", failedToString());
	}

	public static void oneBatch(ArrayList<String> exchangeNamesLeft){
		ParallelClient pc = new ParallelClient();
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/exchanges/$EXCHANGE/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("EXCHANGE", exchangeNamesLeft, "coinmarketcap.com")
				.setResponseContext(responseContext);
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("failedCurrencyMap", failedExchangeMap);
		ExchangeMarketHandler emHandler = new ExchangeMarketHandler(); 
		ptb.execute(emHandler);
	}

	public static String failedToString(){
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> entry : failedExchangeMap.entrySet()){
			String key = entry.getKey();
			String val = entry.getValue();
			if (!"True".equals(val)){
				sb.append("{");
				sb.append("\"currency\":\"" + key + "\",");
				sb.append("\"status\":\"" + val + "\"},");
			}
		}
		String sb_tmp = sb.toString();
		if (!sb_tmp.isEmpty()){
			sb_tmp = sb_tmp.substring(0, sb_tmp.lastIndexOf(","));
		}
		sb_tmp = "[" + sb_tmp + "]";
		sb_tmp = JsonFormatTool.formatJson(sb_tmp);
		return sb_tmp;
	}	
}