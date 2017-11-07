package com.zdx.currency;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractCurrencyRegularName {
	public static void getCurrencyRegularName(){
		String url = "https://coinmarketcap.com/all/views/all/";
		ArrayList<String> regularNames = new ArrayList<String>();
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200){
				String result;
				try {
					result = EntityUtils.toString(response.getEntity());
					Document doc = Jsoup.parse(result);
					Elements e6 = doc.select("#currencies-all > tbody");				
					if (e6.size() > 0){
						Element tbody = e6.get(0);
						Elements trs = tbody.getElementsByTag("tr");
						for (int i=0; i < trs.size(); i++){
							Elements e7 = trs.get(i).getElementsByTag("td");
							System.out.println(e7.toString());
							if (e7.size() >= 6){
								/*
								BaseCurrencytData cd = new BaseCurrencytData();
								cd.rankPos = Integer.parseInt(e7.get(0).text());
								cd.exchangeName = e7.get(1).text();//Bitcoin
								*/
								//cd.pairLink = e7.get(2).getElementsByTag("a").attr("href");///currencies/miyucoin/
								String[] tmp = e7.get(1).getElementsByTag("a").attr("href").split("/");
								if (tmp.length > 1){
									regularNames.add(tmp[tmp.length-1]);
								}/*
								cd.pair = e7.get(2).text();//BTC/USD
								cd.vol24USD = Utils.getDoubleFromUSDString(e7.get(3).text());//$381,311,000
								cd.priceUSD = Utils.getDoubleFromUSDString(e7.get(4).text());//$7150.00
								cd.vol24Ratio = Utils.getDoubleFromPercentString(e7.get(5).text());//50.70%
								cd.updateTime = startTime;
								cm.exchangeActiveMarkets.add(cd);*/
							}
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(regularNames);


	}
}
