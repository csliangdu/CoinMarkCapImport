package com.zdx.rank;

import java.math.BigDecimal;
import java.util.ArrayList;


public class CurrencyVol24Detail {
	int currencyRankPos = 0;
	String currencyName = "";
	String currencyRegularName = "";
	double percentVol24 = 0.0;
	double totalVol24 = 0.0;
	double priceAvgVol24 = 0.0;
	long updateTime = 0;
	ArrayList<CurrencyExchangeVol24Detail> exchangeVol24DetailList = new ArrayList<CurrencyExchangeVol24Detail>();
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (CurrencyExchangeVol24Detail e:exchangeVol24DetailList){
			sb.append(e.toString() + ",");
		}
		String s2 = sb.toString();
		if (s2.isEmpty()){
			return "[]";
		} else {
			s2 = s2.substring(0, s2.lastIndexOf(","));
			s2 = "[" + s2 + "]";			
		}
		String s1 = "{\"currencyRankPos\":\"" + currencyRankPos + 
				"\",\"currencyName\":\"" + currencyName +
				"\",\"currencyRegularName\":\"" + currencyRegularName + 
				"\",\"percentVol24\":\"" + new BigDecimal(percentVol24 ).toString() +
				"\",\"totalVol24\":\"" + new BigDecimal(totalVol24 ).toString() +
				"\",\"priceAvgVol24\":\"" + new BigDecimal(priceAvgVol24 ).toString() +
				"\",\"exchangeDetail\":" + s2 +
				",\"updateTime\":\"" + updateTime +				
				"\"}";
		return s1;
	}
}
