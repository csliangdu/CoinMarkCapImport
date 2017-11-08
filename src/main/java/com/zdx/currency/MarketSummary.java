package com.zdx.currency;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;

public class MarketSummary {

	double price = 0.0;
	double marketCapUSD = 0.0;
	double marketCapBTC = 0.0;
	double vol24USD = 0.0;
	double vol24BTC = 0.0;
	double circulatingSupply = 0.0;
	double maxSupply = 0.0;
	public long updateTime = 0;

	public static MarketSummary getDailySummaryFromDoc(Document doc){
		MarketSummary ds= new MarketSummary();
		ds.updateTime = System.currentTimeMillis();

		Elements e00 = doc.select("#quote_price");
		if (!e00.isEmpty()){
			ds.price = DataFormat.getDoubleFromUSDString(e00.text());
		}
		Elements e01 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(1) > div.coin-summary-item-detail");
		if (!e01.isEmpty()){
			String s1 = e01.text();
			String s2 = e01.get(0).getElementsByTag("span").text();
			s1 = s1.substring(0, s1.indexOf(s2));
			ds.marketCapUSD = DataFormat.getDoubleFromUSDString(s1);
			ds.marketCapBTC = DataFormat.getDoubleFromBTCString(s2);
		}
		Elements e02 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(2) > div.coin-summary-item-detail");
		if (!e02.isEmpty()){
			String s1 = e02.text();
			String s2 = e02.get(0).getElementsByTag("span").text();
			s1 = s1.substring(0, s1.indexOf(s2));
			ds.vol24USD = DataFormat.getDoubleFromUSDString(s1);
			ds.vol24BTC = DataFormat.getDoubleFromBTCString(s2);
		}
		Elements e21 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(4) > div.coin-summary-item-detail");
		if (!e21.isEmpty()){
			ds.circulatingSupply = DataFormat.getDoubleFromBTCString(e21.text());
		}
		Elements e22 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(5) > div.coin-summary-item-detail");
		if (!e22.isEmpty()){
			ds.maxSupply = DataFormat.getDoubleFromBTCString(e22.text());
		}
		return ds;
	}
}
