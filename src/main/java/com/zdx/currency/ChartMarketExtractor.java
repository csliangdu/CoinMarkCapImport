package com.zdx.currency;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;
import com.zdx.common.LoadConfig;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

public class ChartMarketExtractor {
	final static HashMap<String, String> failedCurrencyMap = new HashMap<String, String>();
	final static HashMap<String, Object> responseContext = new HashMap<String, Object>();
	final static long startTime = System.currentTimeMillis();
	final static String destDir = System.getProperty("user.dir") + File.separator + "CurrencyChart" + File.separator + startTime;
	final static HashMap<String, String> currencyLatestMap = new HashMap<String, String>();
	
	public static void execute(String confPath) throws InterruptedException{
		HashMap<Object, Object> currencyLatestConfMap = new HashMap<Object, Object>();
		currencyLatestConfMap = LoadConfig.LoadConf(confPath);
		if (0 == FileHandler.mkdir(destDir)){
			return;
		}
		Long currentStamp = System.currentTimeMillis();		
		
		ArrayList<String> currencyURLs = new ArrayList<String>();
		ArrayList<String> currencyNames = new ArrayList<String>();
		for(Entry<Object, Object> entry :currencyLatestConfMap.entrySet()){
			String key = String.valueOf(entry.getKey());
			String val = String.valueOf(entry.getValue());
			currencyNames.add(key);
			currencyLatestMap.put(key, val);
			currencyURLs.add(entry.getKey() + "/" + entry.getValue() + "/" + currentStamp + "/");
		}

		int minError = Integer.MAX_VALUE;
		boolean done = false;
		while (!done){
			ArrayList<String> currencyURLLeft = new ArrayList<String>();
			for (int i = 0; i < currencyNames.size(); i++){
				String key = currencyNames.get(i);
				String status = failedCurrencyMap.get(key);
				if (!failedCurrencyMap.containsKey(key) || !("True".equals(status) || "PageParseError".equals(status) || "OtherError".equals(status)) ){
					currencyURLLeft.add(currencyURLs.get(i));
				}
			}
			if (currencyURLLeft.isEmpty()){
				done = true;
			}
			if (currencyURLLeft.size() < minError){
				minError = currencyURLLeft.size();
			} else if (currencyURLLeft.size() == minError){
				done = true;//两轮结果一样，
			}
			oneBatch(currencyURLLeft);
			System.out.println(currencyURLLeft.toString());			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileHandler.writeFile(destDir + File.separator + "failed.json", failedToString());
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> entry :currencyLatestMap.entrySet()){
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		FileHandler.writeFile(confPath, sb.toString());
		
	}
	
	public static void oneBatch(ArrayList<String> currencyURLsLeft){
		ParallelClient pc = new ParallelClient();		
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyURLsLeft, "graphs.coinmarketcap.com")
				.setResponseContext(responseContext);
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("currencyLatestMap", currencyLatestMap);
		responseContext.put("failedCurrencyMap", failedCurrencyMap);
		ChartMarketHandler cmHandler = new ChartMarketHandler();
		ptb.execute(cmHandler);
	}

	public static String failedToString(){
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> entry : failedCurrencyMap.entrySet()){
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
