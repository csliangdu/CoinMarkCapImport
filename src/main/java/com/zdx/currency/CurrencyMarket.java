package com.zdx.currency;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CurrencyMarket {
	//https://coinmarketcap.com/exchanges/bittrex/
	String currencyName = "";
	String lowerRegularName = "";
	String symbol = "";
	int rankPos = 0;
	double price = 0.0;
	String website1 = "";
	String website2 = "";
	String website3 = "";
	String messageboard1 = "";
	String messageboard2 = "";
	String messageboard3 = "";
	String explorer1 = "";
	String explorer2 = "";
	String explorer3 = "";
	int isMineable = 0;
	int isCoin = 0;
	int isToken = 1;
	double marketCapUSD = 0.0;
	double marketCapBTC = 0.0;
	double vol24USD = 0.0;
	double vol24BTC = 0.0;
	double circulatingSupply = 0.0;
	double maxSupply = 0.0;
	public long updateTime = 0;
	
	public ArrayList<BaseCurrencytData> exchangeActiveMarkets = new ArrayList<BaseCurrencytData>();

	public String toString(){
		String s1 = exchangeActiveMarkets.toString();
		s1 = s1.replaceAll("\\[", "");
		s1 = s1.replaceAll("\\]", "");
		return "{\"currencyName\":\"" + currencyName + 
				"\",\"lowerRegularName\":\"" + lowerRegularName +
				"\",\"symbol\":\"" + symbol +
				"\",\"price\":\"" + new BigDecimal(price).toString() +
				"\",\"rankPos\":\"" + rankPos +
				"\",\"marketCapUSD\":\"" + new BigDecimal(marketCapUSD).toString() +
				"\",\"marketCapBTC\":\"" + new BigDecimal(marketCapBTC).toString() + 
				"\",\"vol24USD\":\"" + new BigDecimal(vol24USD).toString() +
				"\",\"vol24BTC\":\"" + new BigDecimal(vol24BTC).toString() + 
				"\",\"circulatingSupply\":\"" + new BigDecimal(circulatingSupply).toString() +
				"\",\"maxSupply\":\"" + new BigDecimal(maxSupply).toString() + 
				"\",\"isMineable\":\"" + isMineable +
				"\",\"isCoin\":\"" + isCoin +
				"\",\"isToken\":\"" + isToken +
				"\",\"website1\":\"" + website1 +
				"\",\"website2\":\"" + website2 +
				"\",\"website3\":\"" + website3 +
				"\",\"messageboard1\":\"" + messageboard1 +
				"\",\"messageboard2\":\"" + messageboard2 +
				"\",\"messageboard3\":\"" + messageboard3 +				
				"\",\"explorer1\":\"" + explorer1 +
				"\",\"explorer2\":\"" + explorer2 +
				"\",\"explorer3\":\"" + explorer3 +
				"\",\"updateTime\":\"" + updateTime +
				"\",\"baseMarketData\":[" + s1 +
				"]}";
	}
}
