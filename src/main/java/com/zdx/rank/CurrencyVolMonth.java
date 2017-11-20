package com.zdx.rank;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;
import com.zdx.common.FileHandler;
import com.zdx.common.JsonFormatTool;
import com.zdx.common.SingleHttpRequest;

public class CurrencyVolMonth {
	private static final Logger logger = LogManager.getLogger(CurrencyVolMonth.class);

	public static void execute(){
		CurrencyVolMonthInfo cvmi = new CurrencyVolMonthInfo();
		String url = Config.volMonthRankURL;
		String result = SingleHttpRequest.getResultFromURL(url);
		if (result == null || result.isEmpty()){
			return;
		}
		
		Document doc = Jsoup.parse(result);
		Elements e6 = doc.select("#currencies-volume > tbody");
		if (e6.isEmpty()){
			logger.warn("tbody is Empty");
			return ;
		}				
		Element tbody = e6.get(0);
		Elements trs = tbody.getElementsByTag("tr");
		for (int i=0; i < trs.size(); i++){
			Elements e7 = trs.get(i).getElementsByTag("td");
			System.out.println(e7.toString());
			System.out.println(e7.size());
			if (e7.size() == 6){
				System.out.println(e7.size());
				CurrencyExchangeMonthDetail emd = new CurrencyExchangeMonthDetail();
				emd.rankPos = Integer.parseInt(e7.get(0).text());
				emd.currencyName = e7.get(1).text();
				String[] tmp = e7.get(1).getElementsByTag("a").attr("href").split("/");
				if (tmp.length > 1){
					emd.currencyURL = tmp[tmp.length-1];
				}
				emd.symbol = e7.get(2).text();
				emd.vol1D = DataFormat.getDoubleFromUSDString(e7.get(3).text());
				emd.vol7D = DataFormat.getDoubleFromUSDString(e7.get(4).text());
				emd.vol30D = DataFormat.getDoubleFromUSDString(e7.get(5).text());
				cvmi.exchangeDetailList.add(emd);				
			} else {
				break;
			}
		}
		String destDir = System.getProperty("user.dir") + File.separator + "Rank";
		if (FileHandler.mkdir(destDir) == 0){
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String filePath = destDir + File.separator + "currency_month_rank_" + df.format(new Date());
		String fileContent = JsonFormatTool.formatJson(cvmi.toString());
		FileHandler.writeFile(filePath, fileContent);
	}
}
