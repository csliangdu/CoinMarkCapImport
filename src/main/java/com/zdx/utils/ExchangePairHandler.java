package com.zdx.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;
import com.zdx.exchange.MarketCoinDetail;
//import com.zdx.exchange.MarketInfo;
import com.zdx.exchange.MarketMetaSummary;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class ExchangePairHandler implements ParallecResponseHandler {

	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
		Long startTime = (Long) responseContext.get("startTime");
		String destDir = (String) responseContext.get("destDir");
		@SuppressWarnings("unchecked")
		HashMap<String, HashSet<String>>  exchangePairsMap = (HashMap<String, HashSet<String>>) responseContext.get("exchangePairsMap");

		com.zdx.exchange.MarketInfo exchangeMarketData = new com.zdx.exchange.MarketInfo();
		String[] tmp = res.getRequest().getResourcePath().split("/");
		String exchangeName = "";
		if (tmp.length >= 3){
			exchangeName = tmp[2];
			HashSet<String> pairSet = new HashSet<String>();
			exchangePairsMap.put(exchangeName, pairSet);
		}
		if (exchangeName.isEmpty()){
			return;
		}

		if (res.getStatusCodeInt() == 200){
			Document doc = Jsoup.parse(res.getResponseContent());

			exchangeMarketData.mms = MarketMetaSummary.getMetaSummaryFromDoc(doc);
			exchangeMarketData.mms.updateTime = startTime;

			exchangeMarketData.marketCoinDetailList = MarketCoinDetail.getCurrentDailyDetailFromDoc(doc, startTime);
			HashSet<String> pairSet = exchangePairsMap.get(exchangeName);

			for (int i = 0; i < exchangeMarketData.marketCoinDetailList.size(); i++){
				pairSet.add(exchangeMarketData.marketCoinDetailList.get(i).pair);
			}
			exchangePairsMap.put(exchangeName, pairSet);

			String filePath = destDir + File.separator + exchangeMarketData.mms.lowerRegularName;
			String jsonString = JsonFormatTool.formatJson(exchangeMarketData.toString());

			FileHandler.writeFile(filePath, jsonString);
		}
	}
}