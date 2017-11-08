package com.zdx.currency;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.DataFormat;

public class MetaInfo {
	String currencyName = "";
	String lowerRegularName = "";
	String symbol = "";
	int rankPos = 0;
	String website1 = "";
	String website2 = "";
	String website3 = "";
	String announcement1 = "";
	String announcement2 = "";
	String announcement3 = "";
	String messageboard1 = "";
	String messageboard2 = "";
	String messageboard3 = "";
	String explorer1 = "";
	String explorer2 = "";
	String explorer3 = "";
	String chat = "";
	String sourcecode = "";
	int isMineable = 0;
	int isCoin = 0;
	int isToken = 1;
	double maxSupply = 0.0;
	long updateTime = 0;

	public MetaInfo(){

	}

	public MetaInfo(String symbol){
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

	public static MetaInfo getMetaDataFromDoc(Document doc){
		MetaInfo cm = new MetaInfo();
		Elements e0 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div:nth-child(5) > div.col-xs-6.col-sm-4.col-md-4 > h1");
		System.out.println(e0.text());
		if (e0.isEmpty()){
			return cm;
		}
		String s1 = e0.text();
		String s2 = s1.substring(s1.lastIndexOf("(")+1, s1.lastIndexOf("")-1);
		s1 = s1.substring(s1.indexOf(")")+1, s1.lastIndexOf("("));
		cm.currencyName = s1.trim();
		cm.lowerRegularName = DataFormat.getRegularString(cm.currencyName);
		cm.symbol = s2;
		System.out.println(cm.currencyName);
		System.out.println(cm.lowerRegularName);
		Elements e22 = doc.select("body > div.container > div > div.col-xs-12.col-sm-12.col-md-12.col-lg-10 > div.row.bottom-margin-2x > div.col-sm-8.col-sm-push-4 > div:nth-child(5) > div.coin-summary-item-detail");
		if (!e22.text().isEmpty()){
			cm.maxSupply = DataFormat.getDoubleFromBTCString(e22.text());
		}
		
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
			String s3 = DataFormat.getRegularAttribute(e12.get(0).text());
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
			if ("chat".equals(s3)){
				cm.chat = s21;
			}
			if ("announcement".equals(s3)){
				cm.announcement1 = s21;
			} else if ("announcement2".equals(s3)){
				cm.announcement2 = s21;
			} else if ("announcement3".equals(s3)) {
				cm.announcement3 = s21;
			}
			if ("sourcecode".equals(s3)){
				cm.sourcecode = s21;
			}
		}
		return cm;
	}
}