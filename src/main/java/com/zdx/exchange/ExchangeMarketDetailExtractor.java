package com.zdx.exchange;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

import com.zdx.common.*;
import com.zdx.currency.MarketlDailyExtractor;

public class ExchangeMarketDetailExtractor {

	public static void execute() throws InterruptedException{
		ParallelClient pc = new ParallelClient();
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/exchange_name");
		ArrayList<String> exchangeNames = FileHandler.getArrayListFromFile(is);
		
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/exchanges/$EXCHANGE/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("EXCHANGE", exchangeNames, "coinmarketcap.com")
				.setResponseContext(responseContext);

		String destDir = System.getProperty("user.dir") + File.separator + "Exchange";
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		long startTime = System.currentTimeMillis();		
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);

		ExchangeMarketHandler emHandler = new ExchangeMarketHandler(); 

		ptb.execute(emHandler);	
	}
}


