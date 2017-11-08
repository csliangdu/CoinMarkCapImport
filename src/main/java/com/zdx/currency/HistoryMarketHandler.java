package com.zdx.currency;

import java.io.File;
import java.util.ArrayList;
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

		ArrayList<HistoryDailyDetail> historyData= new ArrayList<HistoryDailyDetail>();
		if (res.getStatusCodeInt() == 200){
			Document doc = Jsoup.parse(res.getResponseContent());
			
			String[] tmp = res.getRequest().getResourcePath().split("/");
			String coinName = "";
			if (tmp.length >= 2){
				coinName = tmp[2];
			}
			
			historyData = HistoryDailyDetail.getHistoryDailyDetailFromDoc(doc, startTime);

			String filePath = destDir + File.separator + coinName;
			String jsonString = JsonFormatTool.formatJson(historyData.toString());

			FileHandler.writeFile(filePath, jsonString);
		}

	}
}
