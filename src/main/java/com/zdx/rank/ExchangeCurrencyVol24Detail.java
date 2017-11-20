package com.zdx.rank;

import java.math.BigDecimal;

public class ExchangeCurrencyVol24Detail {
	int currencyRankPos = 0;
	String currencyName = "";
	String currencyRegularName = "";
	String pair = "";
	double vol24 = 0.0;
	double price24 = 0.0;
	double volRatio = 0.0;
	long updateTime = 0;

	public String toString(){
		String s1 = "{\"currencyRankPos\":\"" + currencyRankPos + 
				"\",\"currencyName\":\"" + currencyName +
				"\",\"currencyRegularName\":\"" + currencyRegularName + 
				"\",\"pair\":\"" + pair +
				"\",\"vol24\":\"" + new BigDecimal(vol24 ).toString() +
				"\",\"price24\":\"" + new BigDecimal(price24 ).toString() +
				"\",\"changeRatio\":\"" + new BigDecimal(volRatio).toString() +
				"\",\"updateTime\":\"" + updateTime +
				"\"}";
		return s1;
	}	
}

