package com.zdx.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DataFormat {

	public static Double getDoubleFromUSDString(String val){
		//$381,311,000
		val = val.trim();
		if (!val.startsWith("$")){
			return 0.0;
		}
		val = val.substring(1);
		val = val.replaceAll(",", "");
		return Double.parseDouble(val);
	}

	public static Double getDoubleFromBTCString(String val){
		//105,081 BTC
		val = val.trim();
		val = val.substring(0, val.indexOf(" "));
		val = val.replaceAll(",", "");
		return  Double.parseDouble(val);
	}

	public static Double getDoubleFromPercentString(String val){
		//65.03%
		if (!val.endsWith("%")){
			return 0.0;
		}
		val = val.substring(0, val.length() - 1);
		return  Double.parseDouble(val)/100;
	}

	public static String getRegularString(String val){
		//replace space with - 
		//remove .
		val = val.replaceAll(" ", "-");
		val = val.replaceAll("\\.", "");
		val = val.toLowerCase();
		return val;
	}

	public static String getSymbolFromParenthesesString(String val){
		//(BTC)
		val = val.replaceAll("(", "");
		val = val.replaceAll(")", "");
		val = val.toUpperCase();
		return val;
	}

	public static String getRegularAttribute(String val){
		//Message Board 2
		//messageboard2
		val = val.replaceAll(" ", "");
		val = val.toLowerCase();
		val = val.trim();
		return val;
	}

	public static String getCurrentDate(){
		Date now = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String currentDate = sdf.format(now);
		return currentDate;
	}

	public static String getDateFromMonth(String val){
		//Nov 07, 2017
		HashMap<String, String> monthMap = new HashMap<String, String>();
		monthMap.put("jan", "01");monthMap.put("feb", "02");monthMap.put("mar", "03");monthMap.put("apr", "04");
		monthMap.put("may", "05");monthMap.put("jun", "06");monthMap.put("july", "07");monthMap.put("aug", "08");
		monthMap.put("sep", "09");monthMap.put("oct", "10");monthMap.put("nov", "11");monthMap.put("dec", "12");
		val = val.toLowerCase().trim();
		String[] tmp = val.split(" ");
		if (tmp.length == 3){
			String mm = monthMap.get(tmp[0]);
			String dd = tmp[1].substring(0, tmp[1].indexOf(","));
			String y = tmp[2];
			return y + mm + dd;	
		}
		return "";
	}
}
