package com.zdx.exchange;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class ExchangeMarketHandler implements ParallecResponseHandler {

	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
		Long startTime = (Long) responseContext.get("startTime");
		String destDir = (String) responseContext.get("destDir");

		MarketInfo exchangeMarketData = new MarketInfo();
		@SuppressWarnings("unchecked")
		HashMap<String, String> failedExchangeMap = (HashMap<String, String>) responseContext.get("failedCurrencyMap");

		String[] tmp = res.getRequest().getResourcePath().split("/");
		String exchangeName = "";
		if (tmp.length >= 3){
			exchangeName = tmp[2];
		}
		failedExchangeMap.put(exchangeName,"UnknowError");

		if (res.getError()){
			failedExchangeMap.put(exchangeName,"ParallecError");
			return;
		} else if (res.getStatusCodeInt() != 200){
			failedExchangeMap.put(exchangeName,"StatusNot200");
			return;
		} else if (res.getStatusCodeInt() == 200){
			Document doc = Jsoup.parse(res.getResponseContent());

			exchangeMarketData.mms = MarketMetaSummary.getMetaSummaryFromDoc(doc);
			exchangeMarketData.mms.updateTime = startTime;

			exchangeMarketData.marketCoinDetailList = MarketCoinDetail.getCurrentDailyDetailFromDoc(doc, startTime);

			String filePath = destDir + File.separator + exchangeMarketData.mms.lowerRegularName;
			String jsonString = JsonFormatTool.formatJson(exchangeMarketData.toString());

			FileHandler.writeFile(filePath, jsonString);
			failedExchangeMap.put(exchangeName,"True");
		} else {
			failedExchangeMap.put(exchangeName,"OtherError");
			return;
		}
	}
}