package com.zdx.rank;

import java.math.BigDecimal;

public class CurrencyExchangeVol24Detail {
	int exchangeRankPos = 0;
	String exchangeName = "";
	String exchangeURL = "";
	String pairName = "";
	double pairVol24 = 0.0;
	double priceVol24 = 0.0;
	double exchangePercentVol24 = 0.0;
	long updateTime = 0;
	
	public String toString(){
		String s1 = "{\"exchangeRankPos\":\"" + exchangeRankPos + 
				"\",\"currencyName\":\"" + exchangeName +
				"\",\"currencyURL\":\"" + exchangeURL + 
				"\",\"pairName\":\"" + pairName +
				"\",\"pairVol24\":\"" + new BigDecimal(pairVol24 ).toString() +
				"\",\"priceVol24\":\"" + new BigDecimal(priceVol24 ).toString() +
				"\",\"exchangePercentVol24\":\"" + new BigDecimal(exchangePercentVol24 ).toString() +
				"\",\"updateTime\":\"" + updateTime +
				"\"}";
		return s1;
	}
	
}
