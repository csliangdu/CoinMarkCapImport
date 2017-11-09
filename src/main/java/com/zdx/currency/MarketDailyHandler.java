package com.zdx.currency;

import java.io.File;
import java.util.HashSet;
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
		@SuppressWarnings("unchecked")
		HashSet<String> failedCurrencySet = (HashSet<String>) responseContext.get("failedCurrencySet");

		MarketInfo coinMarketData = new MarketInfo();
		if (res.getError() || res.getStatusCodeInt() != 200){
			failedCurrencySet.add("!!" + res.getRequest().getResourcePath());
			return;
		} else if (res.getStatusCodeInt() == 200){

			Document doc = Jsoup.parse(res.getResponseContent());

			coinMarketData.meta = MetaInfo.getMetaDataFromDoc(doc);
			coinMarketData.meta.updateTime = startTime;

			coinMarketData.ds = MarketSummary.getDailySummaryFromDoc(doc);
			coinMarketData.ds.updateTime = startTime;

			coinMarketData.exchangeDailyMarkets = MarketDailyDetail.getCurrentDailyDetailFromDoc(doc, startTime);
			if (coinMarketData.meta.lowerRegularName.isEmpty()){
				failedCurrencySet.add("@@" + res.getRequest().getResourcePath());
			}

			String filePath = destDir + File.separator + coinMarketData.meta.lowerRegularName;
			String jsonString = JsonFormatTool.formatJson(coinMarketData.toString());

			FileHandler.writeFile(filePath, jsonString);

		} else {
			failedCurrencySet.add("##" + res.getRequest().getResourcePath());
			return;
		}
	}
}



