package com.zdx.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MarketInfo {
	MarketMetaSummary mms = new MarketMetaSummary();

	ArrayList<MarketCoinDetail> marketCoinDetailList = new ArrayList<MarketCoinDetail>();

	public String toString(){
		String s1 = marketCoinDetailList.toString();
		s1 = s1.replaceAll("\\[", "");
		s1 = s1.replaceAll("\\]", "");
		return"{\"exchangeName\":\"" + mms.exchangeName + 
				"\",\"volUSD\":\"" + new BigDecimal(mms.volUSD).toString() +
				"\",\"volBTC\":\"" + new BigDecimal(mms.volBTC).toString() + 
				"\",\"twitterLink\":\"" + mms.twitterLink +
				"\",\"exchangeLink\":\"" + mms.exchangeLink +
				"\",\"type\":\"" + mms.type +
				"\",\"updateTime\":\"" + mms.updateTime +
				"\",\"baseMarketData\":[" + s1 +
				"]}";
	}
}
