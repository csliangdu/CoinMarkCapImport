package com.zdx.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;

public class MarketCoinDetail {

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

	public static ArrayList<MarketCoinDetail> getCurrentDailyDetailFromDoc(Document doc, Long startTime){
		ArrayList<MarketCoinDetail> ddList = new ArrayList<MarketCoinDetail>();
		Elements e6 = doc.select("#markets > div.table-responsive > table > tbody");
		if (e6.isEmpty()){
			return ddList;
		}
		if (startTime == 0){
			startTime = System.currentTimeMillis();
		}
		Element tbody = e6.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		for (int i=1; i < trs.size(); i++){
			Elements e7 = trs.get(i).getElementsByTag("td");
			if (e7.size() >= 6){
				MarketCoinDetail md = new MarketCoinDetail();
				md.rankPos = Integer.parseInt(e7.get(0).text());
				md.name = e7.get(1).text();//Bitcoin
				md.pairLink = e7.get(2).getElementsByTag("a").attr("href");////https://www.bitfinex.com/trading/BTCUSD
				md.pair = e7.get(2).text();//BTC/USD
				md.vol24USD = DataFormat.getDoubleFromUSDString(e7.get(3).text());//$381,311,000
				md.priceUSD = DataFormat.getDoubleFromUSDString(e7.get(4).text());//$7150.00
				md.vol24Ratio = DataFormat.getDoubleFromPercentString(e7.get(5).text());//50.70%
				md.updateTime = startTime;
				ddList.add(md);
			}
		}
		return ddList;
	}
}
