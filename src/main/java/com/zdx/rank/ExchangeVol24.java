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

public class ExchangeVol24 {
	final static long startTime = System.currentTimeMillis();

	public static void execute(){

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		ArrayList<ExchangeVol24Info> exchangeAll24 = 
				oneBatch(Config.exchange24ALLRankURL, 
						"body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(9) > div > div > table > tbody");
		String filePath = "exchange_rank_all_" + df.format(new Date()) + "_" + startTime;
		String fileContent = JsonFormatTool.formatJson(exchangeAll24.toString());
		oneBatchToFile(filePath, fileContent);

		ArrayList<ExchangeVol24Info> exchangeFee24 = 
				oneBatch(Config.exchange24FeeRankURL, 
						"body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(9) > div > div > table > tbody");
		filePath = "exchange_rank_fee_" + df.format(new Date()) + "_" + startTime;
		fileContent = JsonFormatTool.formatJson(exchangeFee24.toString());
		oneBatchToFile(filePath, fileContent);	

		ArrayList<ExchangeVol24Info> exchangeNoFee24 = 
				oneBatch(Config.exchange24NoFeeRankURL, 
						"body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(9) > div > div > table > tbody");
		filePath = "exchange_rank_nofee_" + df.format(new Date()) + "_" + startTime;
		fileContent = JsonFormatTool.formatJson(exchangeNoFee24.toString());
		oneBatchToFile(filePath, fileContent);	
	}

	public static ArrayList<ExchangeVol24Info> oneBatch(String url, String xselector){
		ArrayList<ExchangeVol24Info> tmp = new ArrayList<ExchangeVol24Info>();
		String result = SingleHttpRequest.getResultFromURL(url);
		if (result == null || result.isEmpty()){
			return tmp;
		}
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(9) > div > div > table > tbody");
		tmp = getTableContent(e1);
		return tmp;
	}

	public static void oneBatchToFile(String fileName, String fileContent){
		String destDir = System.getProperty("user.dir") + File.separator + "Rank";
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		String filePath = destDir + File.separator + fileName;
		FileHandler.writeFile(filePath, fileContent);
	}

	public static ArrayList<ExchangeVol24Info> getTableContent(Elements e){
		ArrayList<ExchangeVol24Info> evdList = new ArrayList<ExchangeVol24Info>();
		if (e.isEmpty()){
			return evdList;
		}

		Elements trs = e.get(0).getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e1 = trs.get(i).getElementsByTag("td");
			if (e1.size() == 0){
				continue;
			} else if (e1.size() == 1){
				String[] tmp2 = e1.get(0).getElementsByTag("a").attr("href").split("/");
				if (!"View More".equals(e1.get(0).getElementsByTag("a").text())){
					if (tmp2.length == 3){
						ExchangeVol24Info evd = new ExchangeVol24Info();

						evd.exchangeRegularName = tmp2[2];
						String t2 = e1.get(0).getElementsByTag("a").text().trim();
						if (t2.contains("[")){
							t2 = t2.replaceAll("\\[", "");
						}
						if (t2.contains("]")){
							t2 = t2.replaceAll("\\]", "");
						}
						evd.exchangeName = t2;
						String tmp = e1.text();
						String tmp1 = tmp.substring(0,  tmp.indexOf("."));
						evd.exchangeRankPos = Integer.parseInt(tmp1);
						evdList.add(evd);
					}
				}
			} else if (e1.size() == 2) {
				int idx = evdList.size() - 1;
				ExchangeVol24Info e2 = evdList.get(idx);
				e2.totalVol24 = DataFormat.getDoubleFromUSDStr(e1.get(1).text());
				evdList.set(idx, e2);
			} else if (e1.size() == 6) {
				int idx = evdList.size() - 1;
				ExchangeVol24Info e2 = evdList.get(idx);
				ExchangeCurrencyVol24Detail e3 = new ExchangeCurrencyVol24Detail();

				String[] tmp = e1.get(1).getElementsByTag("a").attr("href").split("/");
				if (tmp.length == 3){
					e3.currencyRankPos = Integer.parseInt(e1.get(0).text());

					String t2 = e1.get(1).text().trim();
					if (t2.contains("[")){
						t2 = t2.replaceAll("\\[", "");
					}
					if (t2.contains("]")){
						t2 = t2.replaceAll("\\]", "");
					}
					e3.currencyRegularName = t2;
					e3.pair = e1.get(2).text();
					e3.vol24 = DataFormat.getDoubleFromUSDStr(e1.get(3).text());
					e3.price24 = DataFormat.getDoubleFromUSDStr(e1.get(4).text());
					String tt = e1.get(5).text();
					if (tt.contains("%")){
						e3.volRatio = Double.parseDouble(tt.substring(0, tt.indexOf("%")));
					}
					e2.currencyList.add(e3);
					evdList.set(idx, e2);
				}
			}
		}
		return evdList;
	}
}
