package com.zdx.rank;

import java.util.ArrayList;

public class TopGainerLoserInfo {

	long updateTime = 0;
	ArrayList<TopGainLoseDetail> gainers1H = new ArrayList<TopGainLoseDetail>();
	ArrayList<TopGainLoseDetail> gainers24H = new ArrayList<TopGainLoseDetail>();
	ArrayList<TopGainLoseDetail> gainers7D = new ArrayList<TopGainLoseDetail>();

	ArrayList<TopGainLoseDetail> losers1H = new ArrayList<TopGainLoseDetail>();
	ArrayList<TopGainLoseDetail> losers24H = new ArrayList<TopGainLoseDetail>();
	ArrayList<TopGainLoseDetail> losers7D = new ArrayList<TopGainLoseDetail>();
	
	
	
	public String toString(){
		String s1 = "{\"gainers1H\":" + gainers1H.toString() + 
				",\"gainers24H\":" + gainers24H.toString() +
				",\"gainers7D\":" + gainers7D.toString() + 
				",\"losers1H\":" + losers1H.toString() +
				",\"losers24H\":" + losers24H.toString() +
				",\"losers7D\":" + losers7D.toString() +
				",\"updateTime\":\"" + updateTime +				
				"\"}";
		return s1;
	}
	
	
	public String concatObjectList(ArrayList<TopGainLoseDetail> o) {
		StringBuffer sb = new StringBuffer();
		for (TopGainLoseDetail e: o){
			sb.append(e.toString() + ",");
		}
		String s2 = sb.toString();
		if (s2.isEmpty()){
			return "[]";
		} else {
			s2 = s2.substring(0, s2.lastIndexOf(","));
			s2 = "[" + s2 + "]";
			return s2;
		}
	}
}