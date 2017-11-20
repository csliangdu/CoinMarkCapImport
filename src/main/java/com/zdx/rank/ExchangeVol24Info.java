package com.zdx.rank;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExchangeVol24Info {
	ArrayList<ExchangeCurrencyVol24Detail> currencyList = new ArrayList<ExchangeCurrencyVol24Detail>();
	
	int exchangeRankPos = 0;
	String exchangeName = "";
	String exchangeRegularName = "";
	double totalVol24 = 0.0;
	long updateTime = 0;
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (ExchangeCurrencyVol24Detail e:currencyList){
			sb.append(e.toString() + ",");
		}
		String s2 = sb.toString();
		if (s2.isEmpty()){
			return "[]";
		} else {
			s2 = s2.substring(0, s2.lastIndexOf(","));
			s2 = "[" + s2 + "]";			
		}
		String s1 = "{\"exchangeRankPos\":\"" + exchangeRankPos + 
				"\",\"exchangeName\":\"" + exchangeName +
				"\",\"exchangeRegularName\":\"" + exchangeRegularName +
				"\",\"totalVol24\":\"" + new BigDecimal(totalVol24 ).toString() +
				"\",\"currencyDetail\":" + s2 +
				",\"updateTime\":\"" + updateTime +				
				"\"}";
		return s1;
	}
}
