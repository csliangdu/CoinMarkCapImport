package com.zdx.rank;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;
import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;
import com.zdx.common.SingleHttpRequest;

public class TopGainerLoser {
	final static long startTime = System.currentTimeMillis();

	public static void execute(){
		String url = Config.topGainersLosers;

		TopGainerLoserInfo tgli = new TopGainerLoserInfo();

		String result = SingleHttpRequest.getResultFromURL(url);
		if (result == null || result.isEmpty()){
			return;
		}
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("#gainers-1h > div > table > tbody");
		tgli.gainers1H = getTableContent(e1);
		Elements e2 = doc.select("#gainers-24h > div > table > tbody");
		tgli.gainers24H = getTableContent(e2);
		Elements e3 = doc.select("#gainers-7d > div > table > tbody");
		tgli.gainers7D = getTableContent(e3);
		Elements e4 = doc.select("#losers-24h > div > table > tbody");
		tgli.losers1H = getTableContent(e4);
		Elements e5 = doc.select("#losers-1h > div > table > tbody");
		tgli.losers24H = getTableContent(e5);
		Elements e6 = doc.select("#losers-7d > div > table > tbody");
		tgli.losers7D = getTableContent(e6);

		String destDir = System.getProperty("user.dir") + File.separator + "Rank";
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String filePath = destDir + File.separator + "top_gainers_losers_aio_" + df.format(new Date()) + "_" + startTime;
		String fileContent = JsonFormatTool.formatJson(tgli.toString());
		FileHandler.writeFile(filePath, fileContent);
	}

	public static ArrayList<TopGainLoseDetail> getTableContent(Elements e){
		System.out.println(e.toString());
		ArrayList<TopGainLoseDetail> cgldList = new ArrayList<TopGainLoseDetail>();
		if (e.isEmpty()){
			return cgldList;
		}
		Elements trs = e.get(0).getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e1 = trs.get(i).getElementsByTag("td");
			if (e1.size() == 6){
				TopGainLoseDetail cgld = new TopGainLoseDetail();
				cgld.currencyRankPos = Integer.parseInt(e1.get(0).text().trim());

				String[] tmp = e1.get(1).getElementsByTag("a").attr("href").split("/");
				if (tmp.length == 3){
					cgld.currencyRegularName = tmp[2];  
				}
				String t2 = e1.get(1).getElementsByTag("a").text().trim();
				if (t2.contains("[")){
					t2 = t2.replaceAll("\\[", "");
				}
				if (t2.contains("]")){
					t2 = t2.replaceAll("\\]", "");
				}
				cgld.currencyName = t2;
				cgld.symbol = e1.get(2).text();
				cgld.vol24 = DataFormat.getDoubleFromUSDStr(e1.get(3).text());
				cgld.price24 = DataFormat.getDoubleFromUSDStr(e1.get(4).text());
				String t1 = e1.get(4).text();
				if (t1.contains("%")){
					t1 = t1.replaceAll("%", "");
					cgld.changeRatio = Double.parseDouble(t1);
				}
				cgldList.add(cgld);
			}
		}
		return cgldList;
	}

}
