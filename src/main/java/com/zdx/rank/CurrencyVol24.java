package com.zdx.rank;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;
import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;
import com.zdx.common.SingleHttpRequest;

public class CurrencyVol24 {
	final static long startTime = System.currentTimeMillis();

	public static void execute(){
		String url = Config.vol24RankURL;
		CurrencyVol24Info currVol24Info = new CurrencyVol24Info();

		String result = SingleHttpRequest.getResultFromURL(url);
		if (result == null || result.isEmpty()){
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements e6 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(8) > div > div.table-responsive > table > tbody");
		if (e6.isEmpty()){
			return ;
		}				
		Element tbody = e6.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){

			Elements e7 = trs.get(i).getElementsByTag("td");
			System.out.println(e7.toString());

			if (e7.size() == 0){
				continue;
			} else if (e7.size() == 1){				
				String[] tmp2 = e7.get(0).getElementsByTag("a").attr("href").split("/");
				if (tmp2.length == 3){
					CurrencyVol24Detail e = new CurrencyVol24Detail();
					e.currencyName = e7.get(0).getElementsByTag("a").text();
					e.currencyRegularName = tmp2[2];
					String tmp = e7.text();
					String tmp1 = tmp.substring(0,  tmp.indexOf("."));
					e.currencyRankPos = Integer.parseInt(tmp1);
					String tmp3 = "0";
					if (tmp.contains("(")){
						tmp3 = tmp.substring(tmp.lastIndexOf("(")+1, tmp.indexOf("%"));
					}
					e.percentVol24 = Double.parseDouble(tmp3);
					currVol24Info.currencyVol24DetailList.add(e);					
				}
			} else if (e7.size() == 4) {
				int idx = currVol24Info.currencyVol24DetailList.size() - 1;
				CurrencyVol24Detail e = currVol24Info.currencyVol24DetailList.get(idx);
				e.totalVol24 = DataFormat.getDoubleFromUSDStr(e7.get(1).text());
				e.priceAvgVol24 = DataFormat.getDoubleFromUSDStr(e7.get(2).text());
				currVol24Info.currencyVol24DetailList.set(idx, e);
			} else if (e7.size() == 6) {
				int idx = currVol24Info.currencyVol24DetailList.size() - 1;
				CurrencyVol24Detail e = currVol24Info.currencyVol24DetailList.get(idx);
				CurrencyExchangeVol24Detail e2 = new CurrencyExchangeVol24Detail();
				String[] tmp = e7.get(1).getElementsByTag("a").attr("href").split("/");
				if (tmp.length == 3){
					e2.exchangeRankPos = Integer.parseInt(e7.get(0).text());
					e2.exchangeName = e7.get(1).text();
					e2.exchangeURL = tmp[2];
					e2.pairName = e7.get(2).text();
					e2.pairVol24 = DataFormat.getDoubleFromUSDStr(e7.get(3).text());
					e2.priceVol24 = DataFormat.getDoubleFromUSDStr(e7.get(4).text());
					String tt = e7.get(5).text();
					if (tt.contains("%")){
						e2.exchangePercentVol24 = Double.parseDouble(tt.substring(0, tt.indexOf("%")));
					}
					e.exchangeVol24DetailList.add(e2);
					currVol24Info.currencyVol24DetailList.set(idx, e);
				}
			}
		}
		String destDir = System.getProperty("user.dir") + File.separator + "Rank";
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String filePath = destDir + File.separator + "currency_24_rank_" + df.format(new Date()) + "_" + startTime;
		String fileContent = JsonFormatTool.formatJson(currVol24Info.toString());
		FileHandler.writeFile(filePath, fileContent);
	}
}