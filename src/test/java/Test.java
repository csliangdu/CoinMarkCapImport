
import java.io.File;
import java.util.HashMap;

import com.zdx.currency.ChartMarketExtractor;

public class Test {

	public static void main(String[] args){


		try {
			ChartMarketExtractor.execute("C:\\ZDX\\code\\CoinMarkCapImport\\data\\currency_chart_latest_timestamp");
			//ExchangeMarketDetailExtractor.execute();
			//MarketlDailyExtractor.execute();
			//CurrencyStartDateExtractor.excute();
			//HistoryMarketExtractor.excute();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public static void toyTest(){
		String s1 = "rankPos\":\"23\",\"name\":\"Bitcoin Gold [Futures]\",\"pair\":\"BTG/USD\",\"pairLink\":\"https://www.bitfinex.com/trading/BTGUSD;";
		System.out.println(s1.replaceAll("\\[", ""));
		s1 = "/exchanges/koineks/";
		String[] s2 = s1.split("/");
		System.out.println(s2[1]);
		s1 = "A B.";
		s1 = s1.replaceAll(" ", "");
		s1 = s1.replaceAll(".", "");
		System.out.println(s1);
		String s111 = "$(document).ready(function() {		    var start = moment('2017-10-09');";
		String str = "var start = moment('";
		int p1 = s111.indexOf(str);
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("----------------------");

		System.out.println(s111.substring(p1 + str.length(), p1 + str.length() + 10));
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println(getDateFromMonth("Nov 07, 2017"));

		System.out.println(System.currentTimeMillis());   
	}
}
