package com.zdx.rank;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.zdx.common.JsonFormatTool;

public class CurrencyVolMonthInfo {
	ArrayList<CurrencyExchangeMonthDetail> exchangeDetailList = new ArrayList<CurrencyExchangeMonthDetail>();

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (CurrencyExchangeMonthDetail emd: exchangeDetailList){
			String s1 = "{\"rankPos\":\"" + emd.rankPos + 
					"\",\"currencyName\":\"" + emd.currencyName +
					"\",\"currencyURL\":\"" + emd.currencyURL + 
					"\",\"symbol\":\"" + emd.symbol +
					"\",\"vol1D\":\"" + new BigDecimal(emd.vol1D ).toString() +
					"\",\"vol7D\":\"" + new BigDecimal(emd.vol7D ).toString() +
					"\",\"vol30D\":\"" + new BigDecimal(emd.vol30D ).toString() +
					"\",\"updateTime\":\"" + emd.updateTime +
					"\"}";
			sb.append(s1 + ",");
		}
		String s2 = sb.toString();
		if (s2.isEmpty()){
			return "[]";
		} else {
			s2 = s2.substring(0, s2.lastIndexOf(","));
			s2 = "[" + s2 + "]";
			return JsonFormatTool.formatJson(s2);
		}
	}
}
