package com.zdx.currency;


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

public class CurrencyDetailExtractor {

	public static void excute() throws InterruptedException{
		ParallelClient pc = new ParallelClient();
		ArrayList<String> currencyNames = Utils.getCurrencyNames();
		final HashMap<String, Object> responseContext = new HashMap<String, Object>();
		ParallelTaskBuilder ptb = 
				pc.prepareHttpGet("/currencies/$CURRENCY/")
				.setProtocol(RequestProtocol.HTTPS)
				.setHttpPort(443)
				.setReplaceVarMapToSingleTargetSingleVar("CURRENCY", currencyNames, "coinmarketcap.com")
				.setResponseContext(responseContext);

		long t1 = System.currentTimeMillis();
		responseContext.put("startTime", t1);
		ptb.execute(new ParallecResponseHandler(){
			public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
				CurrencyMarket cm = new CurrencyMarket();
				Long startTime = (Long) responseContext.get("startTime");
				Document doc = Jsoup.parse(res.getResponseContent());
				
				Elements e0 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-6.col-sm-4.col-md-4 > h1");
				if (!e0.text().isEmpty()){
					String s1 = e0.text();
					String s2 = s1.substring(s1.lastIndexOf("(")+1, s1.lastIndexOf("")-1);
					s1 = s1.substring(s1.indexOf(")")+1, s1.lastIndexOf("("));
					cm.currencyName = s1.trim();
					cm.symbol = s2;
				}
				if (!cm.currencyName.isEmpty()){
					cm.updateTime = startTime;
					cm.lowerRegularName = Utils.getRegularString(cm.currencyName);

					Elements e00 = doc.select("#quote_price");
					if (!e00.text().isEmpty()){
						cm.price = Utils.getDoubleFromUSDString(e00.text());
					}
					Elements e01 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(1) > div.coin-summary-item-detail");
					if (!e0.text().isEmpty()){
						String s1 = e01.text();
						String s2 = e01.get(0).getElementsByTag("span").text();
						s1 = s1.substring(0, s1.indexOf(s2));
						cm.marketCapUSD = Utils.getDoubleFromUSDString(s1);
						cm.marketCapBTC = Utils.getDoubleFromBTCString(s2);
					}
					Elements e02 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(2) > div.coin-summary-item-detail");
					if (!e0.text().isEmpty()){
						String s1 = e02.text();
						String s2 = e02.get(0).getElementsByTag("span").text();
						s1 = s1.substring(0, s1.indexOf(s2));
						cm.vol24USD = Utils.getDoubleFromUSDString(s1);
						cm.vol24BTC = Utils.getDoubleFromBTCString(s2);
					}
					Elements e21 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(4) > div.coin-summary-item-detail");
					if (!e21.text().isEmpty()){
						cm.circulatingSupply = Utils.getDoubleFromBTCString(e21.text());
					}
					Elements e22 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(5) > div.coin-summary-item-detail");
					if (!e22.text().isEmpty()){
						cm.maxSupply = Utils.getDoubleFromBTCString(e22.text());
					}
					Elements e30 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-4.col-sm-pull-8 > ul");
					if (!e30.text().isEmpty()){
						for(Element e301 : e30.get(0).getElementsByTag("li")){
							Elements e3011 = e301.getElementsByTag("span");
							if(!e3011.isEmpty()){
								String s1 = e3011.get(0).attr("title").trim();
								if ("Tags".equals(s1)){
									if (e3011.size() > 1){
										for (int i3 = 1; i3 < e3011.size(); i3++){
											String s11 = e3011.get(i3).text();
											if ("Mineable".equals(s11)){
												cm.isMineable = 1;
											} else if ("Coin".equals(s11)){
												cm.isCoin = 1;
												cm.isToken = 0;
											} else if ("Token".equals(s11)) {
												cm.isCoin = 0;
												cm.isToken = 1;
											}
										}
									}
								} else if ("Rank".equals(s1)){
									if (e3011.size() > 1){
										String s11 = e3011.get(1).text();
										cm.rankPos = Integer.parseInt(s11.split(" ")[1]);
									}
								}
							}							
							Elements e3012 = e301.getElementsByTag("a");
							if (!e3012.text().isEmpty()){
								String s2 = e3012.get(0).attr("href");
								String s3 = Utils.getRegularAttribute(e3012.get(0).text());
								if ("website".equals(s3)){
									cm.website1 = s2;
								} else if ("website2".equals(s3)){
									cm.website2 = s2;
								} else if ("website3".equals(s3)) {
									cm.website3 = s2;
								}
								if ("messageboard".equals(s3)){
									cm.messageboard1 = s2;
								} else if ("messageboard2".equals(s3)){
									cm.messageboard2 = s2;
								} else if ("messageboard3".equals(s3)) {
									cm.messageboard3 = s2;
								}
								if ("explorer".equals(s3)){
									cm.explorer1 = s2;
								} else if ("explorer2".equals(s3)){
									cm.explorer2 = s2;
								} else if ("explorer3".equals(s3)) {
									cm.explorer3 = s2;
								}
							}
						}
					}

					Elements e6 = doc.select("#markets-table > tbody");				
					if (e6.size() > 0){
						Element tbody = e6.get(0);
						Elements trs = tbody.getElementsByTag("tr");
						for (int i=0; i < trs.size(); i++){
							Elements e7 = trs.get(i).getElementsByTag("td");
							if (e7.size() >= 6){
								BaseCurrencytData cd = new BaseCurrencytData();
								cd.rankPos = Integer.parseInt(e7.get(0).text());
								cd.exchangeName = e7.get(1).text();//Bitcoin
								cd.pairLink = e7.get(2).getElementsByTag("a").attr("href");////https://www.bitfinex.com/trading/BTCUSD
								cd.pair = e7.get(2).text();//BTC/USD
								cd.vol24USD = Utils.getDoubleFromUSDString(e7.get(3).text());//$381,311,000
								cd.priceUSD = Utils.getDoubleFromUSDString(e7.get(4).text());//$7150.00
								cd.vol24Ratio = Utils.getDoubleFromPercentString(e7.get(5).text());//50.70%
								cd.updateTime = startTime;
								cm.exchangeActiveMarkets.add(cd);
							}
						}
					}
					String jsonString = JsonFormatTool.formatJson(cm.toString());
					Utils.writeFile(System.getProperty("user.dir") + File.separator + "Currency" + File.separator + cm.lowerRegularName + "_" + cm.updateTime, jsonString);
				}
			}
		});
	}
}


