package com.zdx.currency;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class HistoryMarketHandler implements ParallecResponseHandler {

	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
		Long startTime = (Long) responseContext.get("startTime");
		String destDir = (String) responseContext.get("destDir");
		@SuppressWarnings("unchecked")
		HashMap<String, String> failedCurrencyMap = (HashMap<String, String>) responseContext.get("failedCurrencyMap");

		String[] tmp1 = res.getRequest().getResourcePath().split("/");
		String exchangeName = "";
		if (tmp1.length >= 3){
			exchangeName = tmp1[2];
		}
		if (res.getError()){
			failedCurrencyMap.put(exchangeName,"ParallecError");
			return;
		} else if (res.getStatusCodeInt() != 200){
			failedCurrencyMap.put(exchangeName,"StatusNot200");
			return;
		} else if (res.getStatusCodeInt() == 200){
			ArrayList<HistoryDailyDetail> historyData= new ArrayList<HistoryDailyDetail>();
			Document doc = Jsoup.parse(res.getResponseContent());
			String[] tmp = res.getRequest().getResourcePath().split("/");
			String coinName = "";
			if (tmp.length >= 2){
				coinName = tmp[2];
			}
			if (coinName.isEmpty()){
				failedCurrencyMap.put(exchangeName,"PageParseError");
				System.out.println(res.getResponseContent());
				return;
			}

			historyData = HistoryDailyDetail.getHistoryDailyDetailFromDoc(doc, startTime);
			if (historyData.size() == 0){
				failedCurrencyMap.put(exchangeName,"PageParseError");
				return;
			}
			String filePath = destDir + File.separator + coinName;
			String jsonString = JsonFormatTool.formatJson(historyData.toString());
			FileHandler.writeFile(filePath, jsonString);
			failedCurrencyMap.put(exchangeName,"True");
		} else {
			failedCurrencyMap.put(exchangeName,"OtherError");
			return;
		}
	}
}
