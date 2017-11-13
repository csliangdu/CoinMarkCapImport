package com.zdx.currency;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.zdx.common.DataFormat;
import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

public class HistoryMarketExtractor {

	final static HashMap<String, String> failedCurrencyMap = new HashMap<String, String>();
	final static HashMap<String, Object> responseContext = new HashMap<String, Object>();
	final static long startTime = System.currentTimeMillis();
	final static String destDir = System.getProperty("user.dir") + File.separator + "CurrencyHistory" + File.separator + startTime;

	public static void execute() {
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/currency_history");
		ArrayList<String> currencyHistoryNames = FileHandler.getArrayListFromFile(is);
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		String currentDate = DataFormat.getCurrentDate();
		ArrayList<String> currencyNames = new ArrayList<String>();
		for(int i = 0; i < currencyHistoryNames.size(); i ++){
			currencyHistoryNames.set(i, currencyHistoryNames.get(i) + currentDate);
			currencyNames.add(currencyHistoryNames.get(i).split("/")[0]);
		}

		int minError = Integer.MAX_VALUE;
		boolean done = false;
		while (!done){
			ArrayList<String> currencyHistoryNamesLeft = new ArrayList<String>();
			for (int i = 0; i < currencyNames.size(); i++){
				String key = currencyNames.get(i);
				String status = failedCurrencyMap.get(key);
				if (!failedCurrencyMap.containsKey(key) || !("True".equals(status) || "PageParseError".equals(status) || "OtherError".equals(status)) ){
					currencyHistoryNamesLeft.add(currencyHistoryNames.get(i));
				}
			}
			if (currencyHistoryNamesLeft.isEmpty()){
				done = true;
			}
			if (currencyHistoryNamesLeft.size() < minError){
				minError = currencyHistoryNamesLeft.size();
			} else if (currencyHistoryNamesLeft.size() == minError){
				done = true;//两轮结果一样，
			}
			oneBatch(currencyHistoryNamesLeft);
			System.out.println(currencyHistoryNamesLeft.toString());			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileHandler.writeFile(destDir + File.separator + "failed.json", failedToString());		
	}

	public static void oneBatch(ArrayList<String> currencyHistoryNamesLeft){
		ParallelClient pc = new ParallelClient();		
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyHistoryNamesLeft, "coinmarketcap.com")
				.setResponseContext(responseContext);
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("failedCurrencyMap", failedCurrencyMap);
		HistoryMarketHandler hmHandler = new HistoryMarketHandler(); 
		ptb.execute(hmHandler);
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
