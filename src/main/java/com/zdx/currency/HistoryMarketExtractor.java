package com.zdx.currency;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.zdx.common.DataFormat;
import com.zdx.common.FileHandler;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

public class HistoryMarketExtractor {
	public static void execute() throws InterruptedException{
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/currency_history");
		ArrayList<String> currencyHistory = FileHandler.getArrayListFromFile(is);

		String currentDate = DataFormat.getCurrentDate();
		for(int i = 0; i < currencyHistory.size(); i ++){
			currencyHistory.set(i, currencyHistory.get(i) + currentDate);
		}
		System.out.println(currencyHistory.toString());
		
		ParallelClient pc = new ParallelClient();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyHistory, "coinmarketcap.com")
				.setResponseContext(responseContext);
		long startTime = System.currentTimeMillis();


		String destDir = System.getProperty("user.dir") + File.separator + "CurrencyHistory" + File.separator + currentDate;
		int r = FileHandler.mkdir(destDir);
		if (r == 0){
			return;
		}
		
		responseContext.put("startTime", startTime);
		responseContext.put("destDir", destDir);

		HistoryMarketHandler hmHandler = new HistoryMarketHandler(); 

		ptb.execute(hmHandler);
	}
}
