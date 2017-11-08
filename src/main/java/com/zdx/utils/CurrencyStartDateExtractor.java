package com.zdx.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zdx.common.FileHandler;
import com.zdx.currency.MarketDailyHandler;
import com.zdx.currency.MarketlDailyExtractor;

import io.parallec.core.ParallecHeader;
import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;
import io.parallec.core.ResponseOnSingleTask;

public class CurrencyStartDateExtractor {

	public final static HashMap<String, String> nameDateMap = new HashMap<String, String>();
	public static void excute() throws InterruptedException{
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/currency_name");
		ArrayList<String> currencyNames = FileHandler.getArrayListFromFile(is);
		for (int i = 0; i < currencyNames.size(); i++){
			nameDateMap.put(currencyNames.get(i), "");
		}

		ParallelClient pc = new ParallelClient();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();				
		
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY/historical-data/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
		
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyNames, "coinmarketcap.com")
				.setResponseContext(responseContext);
		
		long startTime = System.currentTimeMillis();


		String destDir = System.getProperty("user.dir") + File.separator + "Currency" + File.separator + startTime;
		int r = FileHandler.mkdir(destDir);
		if (r == 0){
			return;
		}

		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("nameDateMap", nameDateMap);

		ptb.execute(new ParallecResponseHandler(){
			public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> nameDateMap = (HashMap<String, String>) responseContext.get("nameDateMap");
				if (res.getStatusCodeInt() != 200){
					System.out.println(res.getResponseContent());
					return;
				}
				String content = res.getResponseContent();
				String str = "var start = moment('";
				int p1 = content.indexOf(str);
				String date = content.substring(p1 + str.length(), p1 + str.length() + 10);
				String path = res.getRequest().getResourcePath();
				String[] tmp = path.split("/");
				if (tmp.length >= 2){
					nameDateMap.put(tmp[2], date);
				}
			};
		});
		System.out.println("----------------------");
		System.out.println("----------------------");

		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		for (Entry<String, String> entry : nameDateMap.entrySet()){
			sb1.append(entry.getKey() + "=" + entry.getValue() + "\n");
			sb1.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		String filePath = "";
		FileHandler.writeFile(filePath, sb1.toString());
		String filePath2 = "";
		FileHandler.writeFile(filePath, sb1.toString());
		
		System.out.println(sb1.toString());
		System.out.println(nameDateMap.toString());
		System.out.println("----------------------");
		System.out.println("----------------------");
	}
}
