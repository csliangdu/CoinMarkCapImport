package com.zdx.exchange;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	final static HashMap<String, HashSet<String>> topVol100MMap = new HashMap<String, HashSet<String>>(); 

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
		FileHandler.writeFile(destDir + File.separator + "topVol100M.json", topVol100MToString());
		//FileHandler.writeFile(destDir + File.separator + "topVol100MPair.json", topVol100MPairToString());
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
		responseContext.put("topVol100MMap", topVol100MMap);
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

	public static String topVol100MToString(){
		StringBuffer sb = new StringBuffer();		
		for (Entry<String, HashSet<String>> x : topVol100MMap.entrySet()){
			sb.append("{");
			sb.append("\"exchangeName\":\"" + x.getKey() + "\",");
			HashSet<String> y = x.getValue();
			StringBuffer sb2 = new StringBuffer();
			sb2.append("\"tickerPair\":[" );
			for (String z : y){
				sb2.append("\"" + z + "\",");
			}
			String sb2_tmp = sb2.toString();
			if (!sb2_tmp.isEmpty()){
				sb2_tmp = sb2_tmp.substring(0, sb2_tmp.lastIndexOf(","));
			}
			sb2_tmp = sb2_tmp + "]},";
			sb.append(sb2_tmp);
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