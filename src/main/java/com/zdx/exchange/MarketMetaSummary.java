package com.zdx.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;

public class MarketMetaSummary {
	//https://coinmarketcap.com/exchanges/bittrex/
	String exchangeName = "";
	public String lowerRegularName = "";
	double volUSD = 0.0;
	double volBTC = 0.0;	
	String twitterLink = "";
	String exchangeLink = "";
	String type = "Exchange";	
	public long updateTime = 0;

	public ArrayList<MarketCoinDetail> exchangeActiveMarkets = new ArrayList<MarketCoinDetail>();

	public String toString(){
		String s1 = exchangeActiveMarkets.toString();
		s1 = s1.replaceAll("\\[", "");
		s1 = s1.replaceAll("\\]", "");
		return "{\"exchangeName\":\"" + exchangeName + 
				"\",\"volUSD\":\"" + new BigDecimal(volUSD).toString() +
				"\",\"volBTC\":\"" + new BigDecimal(volBTC).toString() + 
				"\",\"twitterLink\":\"" + twitterLink +
				"\",\"exchangeLink\":\"" + exchangeLink +
				"\",\"type\":\"" + type +
				"\",\"updateTime\":\"" + updateTime +
				"\",\"baseMarketData\":[" + s1 +
				"]}";
	}

	public static MarketMetaSummary getMetaSummaryFromDoc(Document doc){
		MarketMetaSummary mms = new MarketMetaSummary();
		Elements e0 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > h1");
		if (!e0.text().isEmpty()){
			mms.exchangeName = e0.text();
		}
		if (e0.text().isEmpty()){
			return mms;
		}
		mms.lowerRegularName = DataFormat.getRegularString(mms.exchangeName);
		Elements e1 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(1) > a");
		if (!e1.isEmpty()){
			mms.exchangeLink = e1.text();
		}
		Elements e2 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(2) > a");
		if (!e2.isEmpty()){
			mms.twitterLink = e2.text();
		}
		Elements e3 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(3) > small > span");
		if (!e3.isEmpty()){
			mms.type = e3.text();
		}
		Elements e4 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-8 > div > span");
		if (!e4.isEmpty()){
			//$749,805,909
			mms.volUSD = DataFormat.getDoubleFromUSDString(e4.text());
		}
		Elements e5 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-8 > div > small");
		if (!e5.isEmpty()){
			//105,081 BTC
			mms.volBTC = DataFormat.getDoubleFromBTCString(e5.text());
		}
		return mms;
	}
}
