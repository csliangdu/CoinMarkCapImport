package com.zdx.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExchangeMarket {
	//https://coinmarketcap.com/exchanges/bittrex/
	String exchangeName = "";
	String lowerRegularName = "";
	double volUSD = 0.0;
	double volBTC = 0.0;	
	String twitterLink = "";
	String exchangeLink = "";
	String type = "Exchange";	
	public long updateTime = 0;
	
	public ArrayList<BaseMarketData> exchangeActiveMarkets = new ArrayList<BaseMarketData>();

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
}
