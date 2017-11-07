package com.zdx.exchange;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;
import io.parallec.core.ResponseOnSingleTask;

import com.zdx.common.*;

public class ExchangeMarketDetailExtractor {

	public static void execute() throws InterruptedException{
		ParallelClient pc = new ParallelClient();
		ArrayList<String> exnames = Utils.getExchangeNames();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/exchanges/$EXCHANGE/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("EXCHANGE", exnames, "coinmarketcap.com")
				.setResponseContext(responseContext);

		long t1 = System.currentTimeMillis();
		responseContext.put("startTime", t1);
		ptb.execute(new ParallecResponseHandler(){
			public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
				ExchangeMarket am = new ExchangeMarket();
				Long startTime = (Long) responseContext.get("startTime");
				Document doc = Jsoup.parse(res.getResponseContent());
				Elements e0 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > h1");
				if (!e0.text().isEmpty()){
					am.exchangeName = e0.text();
				}
				if (!am.exchangeName.isEmpty()){
					am.updateTime = startTime;
					am.lowerRegularName = Utils.getRegularString(am.exchangeName);
					Elements e1 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(1) > a");
					if (!e1.text().isEmpty()){
						am.exchangeLink = e1.text();
					}
					Elements e2 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(2) > a");
					if (!e2.text().isEmpty()){
						am.twitterLink = e2.text();
					}
					Elements e3 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-4 > ul > li:nth-child(3) > small > span");
					if (!e3.text().isEmpty()){
						am.type = e3.text();
					}
					Elements e4 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-8 > div > span");
					if (!e4.text().isEmpty()){
						//$749,805,909
						am.volUSD = Utils.getDoubleFromUSDString(e4.text());
					}
					Elements e5 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-8 > div > small");
					if (!e5.text().isEmpty()){
						//105,081 BTC
						am.volBTC = Utils.getDoubleFromBTCString(e5.text());
					}

					Elements e6 = doc.select("#markets > div.table-responsive > table > tbody");
					if (e6.size() > 0){
						Element tbody = e6.get(0);
						Elements trs = tbody.getElementsByTag("tr");
						for (int i=1; i < trs.size(); i++){
							Elements e7 = trs.get(i).getElementsByTag("td");
							if (e7.size() >= 6){
								BaseMarketData md = new BaseMarketData();
								md.rankPos = Integer.parseInt(e7.get(0).text());
								md.name = e7.get(1).text();//Bitcoin
								md.pairLink = e7.get(2).getElementsByTag("a").attr("href");////https://www.bitfinex.com/trading/BTCUSD
								md.pair = e7.get(2).text();//BTC/USD
								md.vol24USD = Utils.getDoubleFromUSDString(e7.get(3).text());//$381,311,000
								md.priceUSD = Utils.getDoubleFromUSDString(e7.get(4).text());//$7150.00
								md.vol24Ratio = Utils.getDoubleFromPercentString(e7.get(5).text());//50.70%
								md.updateTime = startTime;
								am.exchangeActiveMarkets.add(md);
							}
						}
					}
					String jsonString = JsonFormatTool.formatJson(am.toString());					
					Utils.writeFile(System.getProperty("user.dir") + File.separator + "Exchange" + File.separator + am.lowerRegularName + "_" + am.updateTime, jsonString);
				}
			}
		});
	}
}


