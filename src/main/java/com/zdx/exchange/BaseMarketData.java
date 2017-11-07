package com.zdx.exchange;

import java.math.BigDecimal;

public class BaseMarketData {

	public int rankPos = 0;
	public String name = "";
	public String pair = "";
	public String pairLink = "";
	public double vol24USD = 0.0;
	public double priceUSD = 0.0;
	public double vol24Ratio = 0.0;
	public long updateTime = 0;

	public String toString(){
		String s1 = "{\"rankPos\":\"" + rankPos + 
				"\",\"name\":\"" + name +
				"\",\"pair\":\"" + pair + 
				"\",\"pairLink\":\"" + pairLink +
				"\",\"vol24USD\":\"" + new BigDecimal(vol24USD ).toString() +
				"\",\"priceUSD\":\"" + priceUSD +
				"\",\"vol24Ratio\":\"" + vol24Ratio +
				"\",\"updateTime\":\"" + updateTime +
				"\"}";
		return s1;

	}	
}
