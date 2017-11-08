package com.zdx.currency;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Market {

	//已有数据 新数据->新数据替代已有数据
	public Meta meta = new Meta();
	
	//已有数据 新数据->旧数据进仓库，新数据展示
	public DailySummary ds = new DailySummary();
	
	//已有数据 新数据->新旧数据合并进仓库
	public DailyHistory dh = new DailyHistory();
	
	//已有数据 新数据->旧数据进仓库，新数据展示
	public ChartDetail cd = new ChartDetail();
	
	public ArrayList<DailyDetail> exchangeActiveMarkets = new ArrayList<DailyDetail>();

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
