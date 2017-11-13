package com.zdx.utils;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.zdx.common.SingleHttpRequest;

public class ExtractCurrencyRegularName {
	private static final Logger logger = LogManager.getLogger(ExtractCurrencyRegularName.class);
	public static void getCurrencyRegularName(){
		String url = "https://coinmarketcap.com/all/views/all/";
		ArrayList<String> regularNames = new ArrayList<String>();
		String result = SingleHttpRequest.getResultFromURL(url);
		if (result == null || result.isEmpty()){
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements e6 = doc.select("#currencies-all > tbody");
		if (e6.isEmpty()){
			logger.warn("tbody is Empty");
			return ;
		}
		Elements trs = e6.get(0).getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e7 = trs.get(i).getElementsByTag("td");
			System.out.println(e7.toString());
			if (e7.size() >= 6){
				String[] tmp = e7.get(1).getElementsByTag("a").attr("href").split("/");
				if (tmp.length > 1){
					regularNames.add(tmp[tmp.length-1]);
				}
			}
		}
		System.out.println(regularNames);

	}
}
