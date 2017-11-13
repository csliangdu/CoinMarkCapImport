package com.zdx.currency;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;

public class HistoryDailyDetail {
	double openPrice = 0.0;
	double highPrice = 0.0;
	double lowPrice = 0.0;
	double closePrice = 0.0;
	double volumeUSD = 0.0;
	double marketCapUSD = 0.0;
	String date = "";
	long updateTime = 0;
	
	public String toString(){
		String s1 = "{\"date\":\"" + date + 
				"\",\"openPrice\":\"" + new BigDecimal(openPrice ).toString() +
				"\",\"highPrice\":\"" + new BigDecimal(highPrice ).toString() +
				"\",\"lowPrice\":\"" + new BigDecimal(lowPrice ).toString() +
				"\",\"closePrice\":\"" + new BigDecimal(closePrice ).toString() +
				"\",\"volumeUSD\":\"" + new BigDecimal(volumeUSD ).toString() +
				"\",\"marketCapUSD\":\"" + new BigDecimal(marketCapUSD ).toString() +
				"\",\"updateTime\":\"" + updateTime +
				"\"}";
		return s1;

	}

	public static ArrayList<HistoryDailyDetail> getHistoryDailyDetailFromDoc(Document doc, Long startTime){
		ArrayList<HistoryDailyDetail> ddList = new ArrayList<HistoryDailyDetail>();
		Elements e6 = doc.select("#historical-data > div > div.table-responsive > table > tbody");
		if (e6.isEmpty()){
			return ddList;
		}
		if (startTime == 0){
			startTime = System.currentTimeMillis();
		}
		Element tbody = e6.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e7 = trs.get(i).getElementsByTag("td");
			if ( e7.size() >= 6){
				HistoryDailyDetail cd = new HistoryDailyDetail();
				cd.date = DataFormat.getDateFromMonth(e7.get(0).text());
				String t1 = DataFormat.removeCharacter(e7.get(1).text());
				cd.openPrice = Double.parseDouble(t1);//0.051657
				String t2 = DataFormat.removeCharacter(e7.get(2).text());
				cd.highPrice = Double.parseDouble(t2);//0.051657
				String t3 = DataFormat.removeCharacter(e7.get(3).text());
				cd.lowPrice = Double.parseDouble(t3);//0.051657
				String t4 = DataFormat.removeCharacter(e7.get(4).text());
				cd.closePrice = Double.parseDouble(t4);//0.051657
				String t5 = DataFormat.removeCharacter(e7.get(5).text());
				cd.volumeUSD = Double.parseDouble(t5);//82,603
				String t6 = DataFormat.removeCharacter(e7.get(6).text());
				cd.marketCapUSD = Double.parseDouble(t6);//82,603
				cd.updateTime = startTime;			
				ddList.add(cd);
			}
		}
		return ddList;
	}
}
