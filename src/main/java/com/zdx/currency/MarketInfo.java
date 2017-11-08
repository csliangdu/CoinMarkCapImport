package com.zdx.currency;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MarketInfo {

	//已有数据 新数据->新数据替代已有数据
	public MetaInfo meta = new MetaInfo();
	
	//已有数据 新数据->旧数据进仓库，新数据展示
	public MarketSummary ds = new MarketSummary();
	
	//已有数据 新数据->新旧数据合并进仓库
	public HistoryDailyDetail dh = new HistoryDailyDetail();
	
	//已有数据 新数据->旧数据进仓库，新数据展示
	public ChartDetail cd = new ChartDetail();
	
	public ArrayList<MarketDailyDetail> exchangeDailyMarkets = new ArrayList<MarketDailyDetail>();

	public String toString(){
		String s1 = exchangeDailyMarkets.toString();
		s1 = s1.replaceAll("\\[", "");
		s1 = s1.replaceAll("\\]", "");
		return "{\"currencyName\":\"" + meta.currencyName + 
				"\",\"lowerRegularName\":\"" + meta.lowerRegularName +
				"\",\"symbol\":\"" + meta.symbol +
				"\",\"price\":\"" + new BigDecimal(ds.price).toString() +
				"\",\"rankPos\":\"" + meta.rankPos +
				"\",\"marketCapUSD\":\"" + new BigDecimal(ds.marketCapUSD).toString() +
				"\",\"marketCapBTC\":\"" + new BigDecimal(ds.marketCapBTC).toString() + 
				"\",\"vol24USD\":\"" + new BigDecimal(ds.vol24USD).toString() +
				"\",\"vol24BTC\":\"" + new BigDecimal(ds.vol24BTC).toString() + 
				"\",\"circulatingSupply\":\"" + new BigDecimal(ds.circulatingSupply).toString() +
				"\",\"maxSupply\":\"" + new BigDecimal(ds.maxSupply).toString() + 
				"\",\"isMineable\":\"" + meta.isMineable +
				"\",\"isCoin\":\"" + meta.isCoin +
				"\",\"isToken\":\"" + meta.isToken +
				"\",\"website1\":\"" + meta.website1 +
				"\",\"website2\":\"" + meta.website2 +
				"\",\"website3\":\"" + meta.website3 +
				"\",\"announcement1\":\"" + meta.announcement1 +
				"\",\"announcement2\":\"" + meta.announcement2 +
				"\",\"announcement3\":\"" + meta.announcement3 +				
				"\",\"messageboard1\":\"" + meta.messageboard1 +
				"\",\"messageboard2\":\"" + meta.messageboard2 +
				"\",\"messageboard3\":\"" + meta.messageboard3 +				
				"\",\"explorer1\":\"" + meta.explorer1 +
				"\",\"explorer2\":\"" + meta.explorer2 +
				"\",\"explorer3\":\"" + meta.explorer3 +
				"\",\"chat\":\"" + meta.chat +
				"\",\"sourcecode\":\"" + meta.sourcecode +
				"\",\"updateTime\":\"" + meta.updateTime +
				"\",\"baseMarketData\":[" + s1 +
				"]}";
	}
}
