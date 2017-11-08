package com.zdx.currency;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;

import com.zdx.common.*;

public class MarketlDailyExtractor {

	public static void execute() throws InterruptedException{
		InputStream is = MarketlDailyExtractor.class.getClass().getResourceAsStream("/currency_name2");
		ArrayList<String> currencyNames = FileHandler.getArrayListFromFile(is);

		ParallelClient pc = new ParallelClient();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/currencies/$CURRENCY/")
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

		MarketDailyHandler dmHandler = new MarketDailyHandler(); 

		ptb.execute(dmHandler);
	}
}


