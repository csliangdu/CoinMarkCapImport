package com.zdx.rank;

import java.util.ArrayList;

public class CurrencyVol24Info {

	ArrayList<CurrencyVol24Detail> currencyVol24DetailList = new ArrayList<CurrencyVol24Detail>();

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (CurrencyVol24Detail e:currencyVol24DetailList){
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
