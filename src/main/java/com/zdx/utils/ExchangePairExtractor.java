package com.zdx.utils;

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

public class ExchangePairExtractor {
	final static HashMap<String, Object> responseContext = new HashMap<String, Object>();
	final static HashMap<String, HashSet<String>> exchangePairsMap = new HashMap<String, HashSet<String>>();

	public static void execute(String confPath) throws InterruptedException{

		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/exchange_name");
		ArrayList<String> exchangeNames = FileHandler.getArrayListFromFile(is);

		boolean done = false;
		while (!done){
			ArrayList<String> exchangeNamesLeft = new ArrayList<String>();
			for (int i = 0; i < exchangeNames.size(); i++){
				String key = exchangeNames.get(i);
				if (!exchangePairsMap.containsKey(key) || exchangePairsMap.get(key).isEmpty()){
					exchangeNamesLeft.add(key);
				}
			}
			if (exchangeNamesLeft.isEmpty()){
				done = true;
			}
			oneBatch(exchangeNamesLeft);
		}
		String X = dataToString();
		FileHandler.writeFile(confPath, X);
		//System.out.println(exchangePairsMap.toString());		
	}

	public static void oneBatch(ArrayList<String> exchangeNamesLeft){
		ParallelClient pc = new ParallelClient();
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/exchanges/$EXCHANGE/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("EXCHANGE", exchangeNamesLeft, "coinmarketcap.com")
				.setResponseContext(responseContext);

		String destDir = System.getProperty("user.dir") + File.separator + "Exchange";		
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		long startTime = System.currentTimeMillis();		
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("exchangePairsMap", exchangePairsMap);
		ExchangePairHandler epHandler = new ExchangePairHandler(); 
		ptb.execute(epHandler);
	}

	public static String dataToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Entry<String, HashSet<String>> entry : exchangePairsMap.entrySet()){
			String key = entry.getKey();
			HashSet<String> tmp = entry.getValue();
			sb.append("{");
			sb.append("\"exchangeName\":\"" + key + "\",");
			sb.append("\"pairs\":[");
			StringBuffer sb2 = new StringBuffer();
			for (String pair : tmp){
				sb2.append("\"" + pair + "\",");				
			}
			String sb2_tmp = sb2.toString();
			sb2_tmp = sb2_tmp.substring(0, sb2_tmp.lastIndexOf(","));
			sb.append(sb2_tmp);
			sb.append("]},");			
		}
		String sb_tmp = sb.toString();
		sb_tmp = sb_tmp.substring(0, sb_tmp.lastIndexOf(","));
		sb.append("[");
		sb_tmp = sb_tmp + "]";
		sb_tmp = JsonFormatTool.formatJson(sb_tmp);
		System.out.print(sb_tmp);
		return sb_tmp;
	}
}