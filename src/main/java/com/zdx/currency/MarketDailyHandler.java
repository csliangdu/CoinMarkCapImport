package com.zdx.currency;

import java.io.File;
import java.util.HashMap;
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
		HashMap<String, String> failedCurrencyMap = (HashMap<String, String>) responseContext.get("failedCurrencyMap");

		String[] tmp = res.getRequest().getResourcePath().split("/");
		String exchangeName = "";
		if (tmp.length >= 3){
			exchangeName = tmp[2];
		}
		//failedCurrencyMap.put(exchangeName,"UnknowError");
		MarketInfo coinMarketData = new MarketInfo();
		if (res.getError()){
			failedCurrencyMap.put(exchangeName,"ParallecError");
			return;
		} else if (res.getStatusCodeInt() != 200){
			failedCurrencyMap.put(exchangeName,"StatusNot200");
			return;
		} else if (res.getStatusCodeInt() == 200){
			Document doc = Jsoup.parse(res.getResponseContent());
			coinMarketData.meta = MetaInfo.getMetaDataFromDoc(doc);
			coinMarketData.meta.updateTime = startTime;
			coinMarketData.ds = MarketSummary.getDailySummaryFromDoc(doc);
			coinMarketData.ds.updateTime = startTime;
			coinMarketData.exchangeDailyMarkets = MarketDailyDetail.getCurrentDailyDetailFromDoc(doc, startTime);			
			if (coinMarketData.meta.lowerRegularName.isEmpty()){
				failedCurrencyMap.put(exchangeName,"PageParseError");
				return;
			}
			String filePath = destDir + File.separator + coinMarketData.meta.lowerRegularName;
			String jsonString = JsonFormatTool.formatJson(coinMarketData.toString());
			FileHandler.writeFile(filePath, jsonString);
			failedCurrencyMap.put(exchangeName,"True");
		} else {
			failedCurrencyMap.put(exchangeName,"OtherError");
			return;
		}
	}
}