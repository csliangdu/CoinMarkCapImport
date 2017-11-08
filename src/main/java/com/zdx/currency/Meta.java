package com.zdx.currency;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.Utils;

public class Meta {
	String currencyName = "";
	String lowerRegularName = "";
	String symbol = "";
	int rankPos = 0;
	String website1 = "";
	String website2 = "";
	String website3 = "";
	String messageboard1 = "";
	String messageboard2 = "";
	String messageboard3 = "";
	String explorer1 = "";
	String explorer2 = "";
	String explorer3 = "";
	int isMineable = 0;
	int isCoin = 0;
	int isToken = 1;
	double maxSupply = 0.0;
	public long updateTime = 0;

	public Meta(){

	}

	public Meta(String symbol){
		getMetaData(symbol);
	}

	public void getMetaData(String symbol){

	}

	public void getMetaDataFromRedis(){

	}

	public void getMetaDataFromFile(){

	}

	public void getMetaDataFromDB(){

	}

	public Meta getMetaDataFromDoc(Document doc){
		Meta cm = new Meta();
		Elements e0 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-6.col-sm-4.col-md-4 > h1");

		if (e0.isEmpty()){
			return cm;
		}
		String s1 = e0.text();
		String s2 = s1.substring(s1.lastIndexOf("(")+1, s1.lastIndexOf("")-1);
		s1 = s1.substring(s1.indexOf(")")+1, s1.lastIndexOf("("));
		cm.currencyName = s1.trim();
		cm.lowerRegularName = Utils.getRegularString(cm.currencyName);
		cm.symbol = s2;

		Elements e = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-4.col-sm-pull-8 > ul");
		if (e.isEmpty()){
			return cm;
		}
		for(Element e1 : e.get(0).getElementsByTag("li")){
			Elements e11 = e1.getElementsByTag("span");
			if(e11.isEmpty()){
				return cm;
			}
			String s12 = e11.get(0).attr("title").trim();
			if ("Tags".equals(s12)){
				for (int i = 1; i < e11.size(); i++){
					String s11 = e11.get(i).text();
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
			} else if ("Rank".equals(s12)){
				if (e11.size() > 1){
					String s11 = e11.get(1).text();
					cm.rankPos = Integer.parseInt(s11.split(" ")[1]);
				}
			}
			Elements e12 = e1.getElementsByTag("a");
			if (e12.isEmpty()){
				break;
			}
			String s21 = e12.get(0).attr("href");
			String s3 = Utils.getRegularAttribute(e12.get(0).text());
			if ("website".equals(s3)){
				cm.website1 = s21;
			} else if ("website2".equals(s3)){
				cm.website2 = s21;
			} else if ("website3".equals(s3)) {
				cm.website3 = s21;
			}
			if ("messageboard".equals(s3)){
				cm.messageboard1 = s21;
			} else if ("messageboard2".equals(s3)){
				cm.messageboard2 = s21;
			} else if ("messageboard3".equals(s3)) {
				cm.messageboard3 = s21;
			}
			if ("explorer".equals(s3)){
				cm.explorer1 = s21;
			} else if ("explorer2".equals(s3)){
				cm.explorer2 = s21;
			} else if ("explorer3".equals(s3)) {
				cm.explorer3 = s21;
			}
		}
		return cm;
	}
}