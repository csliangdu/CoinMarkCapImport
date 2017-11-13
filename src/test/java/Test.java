

import com.zdx.currency.ChartMarketExtractor;
import com.zdx.currency.HistoryMarketExtractor;
import com.zdx.currency.MarketlDailyExtractor;
import com.zdx.exchange.ExchangeMarketDetailExtractor;
import com.zdx.rank.CurrencyVol24;
import com.zdx.rank.CurrencyVolMonth;
import com.zdx.rank.ExchangeVol24;
import com.zdx.rank.TopGainerLoser;
import com.zdx.utils.ExchangePairExtractor;
import com.zdx.utils.ExtractCurrencyRegularName;

public class Test {

	public static void main(String[] args){

		
		try {
			ChartMarketExtractor.execute("C:\\ZDX\\code\\CoinMarkCapImport\\data\\currency_chart_latest_timestamp");
			//ExchangeMarketDetailExtractor.execute();
			//ExchangePairExtractor.execute("C:\\ZDX\\code\\CoinMarkCapImport\\data\\exchange_pairs.json");
			//MarketlDailyExtractor.execute();
			//CurrencyStartDateExtractor.excute();
			//HistoryMarketExtractor.execute();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		/*
		TopGainerLoser.execute();
		ExchangeVol24.execute();
		CurrencyVolMonth.execute();
		CurrencyVol24.execute();
		*/
		//ExtractCurrencyRegularName.getCurrencyRegularName();


	}
}
