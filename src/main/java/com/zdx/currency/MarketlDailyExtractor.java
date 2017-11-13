package com.zdx.currency;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

import com.zdx.common.*;

public class MarketlDailyExtractor {
	final static HashMap<String, String> failedCurrencyMap = new HashMap<String, String>();
	final static HashMap<String, Object> responseContext = new HashMap<String, Object>();
	final static long startTime = System.currentTimeMillis();
	final static String destDir = System.getProperty("user.dir") + File.separator + "Currency" + File.separator + startTime;

	public static void execute() throws InterruptedException{
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/currency_name2");
		ArrayList<String> currencyNames = FileHandler.getArrayListFromFile(is);
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		int minError = Integer.MAX_VALUE;
		boolean done = false;
		while (!done){
			ArrayList<String> currencyNamesLeft = new ArrayList<String>();
			for (int i = 0; i < currencyNames.size(); i++){
				String key = currencyNames.get(i);
				String status = failedCurrencyMap.get(key);
				if (!failedCurrencyMap.containsKey(key) || !("True".equals(status) || "PageParseError".equals(status) || "OtherError".equals(status)) ){
					currencyNamesLeft.add(key);
				}
			}
			if (currencyNamesLeft.isEmpty()){
				done = true;
			}
			if (currencyNamesLeft.size() < minError){
				minError = currencyNamesLeft.size();
			} else if (currencyNamesLeft.size() == minError){
				done = true;//两轮结果一样，
			}
			oneBatch(currencyNamesLeft);
			System.out.println(currencyNamesLeft.toString());
			Thread.sleep(20000);
		}
		FileHandler.writeFile(destDir + File.separator + "failed.json", failedToString());

	}

	public static void oneBatch(ArrayList<String> currencyNamesLeft){
		ParallelClient pc = new ParallelClient();		
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyNamesLeft, "coinmarketcap.com")
				.setResponseContext(responseContext);
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("failedCurrencyMap", failedCurrencyMap);
		MarketDailyHandler dmHandler = new MarketDailyHandler(); 
		ptb.execute(dmHandler);
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