package com.zdx.currency;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.zdx.common.FileHandler;
import com.zdx.common.LoadConfig;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

public class ChartMarketExtractor {
	//private static final Logger logger = LogManager.getLogger(ChartMarketExtractor.class);
	
	public static void execute(String confPath) throws InterruptedException{
		HashMap<Object, Object> currencyLatestConfMap = new HashMap<Object, Object>();
		currencyLatestConfMap = LoadConfig.LoadConf(confPath);
		
		Long currentStamp = System.currentTimeMillis();
		
		final HashMap<String, String> currencyLatestMap = new HashMap<String, String>();
		ArrayList<String> currencyURLs = new ArrayList<String>();
		for(Entry<Object, Object> entry :currencyLatestConfMap.entrySet()){
			String key = String.valueOf(entry.getKey());
			String val = String.valueOf(entry.getValue());
			currencyLatestMap.put(key, val);
			currencyURLs.add(entry.getKey() + "/" + entry.getValue() + "/" + currentStamp + "/");
		}
		
		ParallelClient pc = new ParallelClient();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyURLs, "graphs.coinmarketcap.com")
				.setResponseContext(responseContext);
		long startTime = System.currentTimeMillis();


		String destDir = System.getProperty("user.dir") + File.separator + "CurrencyChart";
		if (0 == FileHandler.mkdir(destDir)){
			return;
		}

		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);
		responseContext.put("currencyLatestMap", currencyLatestMap);
		
		ChartMarketHandler cmHandler = new ChartMarketHandler();

		ptb.execute(cmHandler);
		
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> entry :currencyLatestMap.entrySet()){
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		FileHandler.writeFile(confPath, sb.toString());
		
	}
}
