package com.zdx.currency;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.Utils;

public class DailyDetail {

	public int rankPos = 0;
	public String exchangeName = "";
	public String pair = "";
	public String pairLink = "";
	public double vol24USD = 0.0;
	public double priceUSD = 0.0;
	public double vol24Ratio = 0.0;
	public long updateTime = 0;

	public String toString(){
		String s1 = "{\"rankPos\":\"" + rankPos + 
				"\",\"exchangeName\":\"" + exchangeName +
				"\",\"pair\":\"" + pair + 
				"\",\"pairLink\":\"" + pairLink +
				"\",\"vol24USD\":\"" + new BigDecimal(vol24USD ).toString() +
				"\",\"priceUSD\":\"" + priceUSD +
				"\",\"vol24Ratio\":\"" + vol24Ratio +
				"\",\"updateTime\":\"" + updateTime +
				"\"}";
		return s1;

	}

	public ArrayList<DailyDetail>  getCurrentDailyDetailFromDoc(Document doc){
		ArrayList<DailyDetail> ddList = new ArrayList<DailyDetail>();
		Elements e6 = doc.select("#markets-table > tbody");				
		if (e6.isEmpty()){
			return ddList;
		}
		long startTime = System.currentTimeMillis();
		Element tbody = e6.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e7 = trs.get(i).getElementsByTag("td");
			if (e7.size() >= 6){
				DailyDetail cd = new DailyDetail();
				cd.rankPos = Integer.parseInt(e7.get(0).text());
				cd.exchangeName = e7.get(1).text();//Bitcoin
				cd.pairLink = e7.get(2).getElementsByTag("a").attr("href");////https://www.bitfinex.com/trading/BTCUSD
				cd.pair = e7.get(2).text();//BTC/USD
				cd.vol24USD = Utils.getDoubleFromUSDString(e7.get(3).text());//$381,311,000
				cd.priceUSD = Utils.getDoubleFromUSDString(e7.get(4).text());//$7150.00
				cd.vol24Ratio = Utils.getDoubleFromPercentString(e7.get(5).text());//50.70%
				cd.updateTime = startTime;
				ddList.add(cd);
			}
		}
		return ddList;
	}

}

