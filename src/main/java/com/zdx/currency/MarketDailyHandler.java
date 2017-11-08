package com.zdx.currency;

import java.io.File;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zdx.common.JsonFormatTool;
import com.zdx.common.FileHandler;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class MarketDailyHandler implements ParallecResponseHandler {

	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
		Long startTime = (Long) responseContext.get("startTime");
		String destDir = (String) responseContext.get("destDir");

		MarketInfo coinMarketData = new MarketInfo();
		
		if (res.getStatusCodeInt() == 200){
			Document doc = Jsoup.parse(res.getResponseContent());

			coinMarketData.meta = MetaInfo.getMetaDataFromDoc(doc);
			coinMarketData.meta.updateTime = startTime;

			coinMarketData.ds = MarketSummary.getDailySummaryFromDoc(doc);
			coinMarketData.ds.updateTime = startTime;

			coinMarketData.exchangeDailyMarkets = MarketDailyDetail.getCurrentDailyDetailFromDoc(doc, startTime);

			String filePath = destDir + File.separator + coinMarketData.meta.lowerRegularName;
			String jsonString = JsonFormatTool.formatJson(coinMarketData.toString());

			FileHandler.writeFile(filePath, jsonString);
		}

	}
}



