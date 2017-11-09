

import com.zdx.currency.MarketlDailyExtractor;
import com.zdx.utils.ExchangePairExtractor;

public class Test {

	public static void main(String[] args){


		try {
			//ChartMarketExtractor.execute("C:\\ZDX\\code\\CoinMarkCapImport\\data\\currency_chart_latest_timestamp");
			//ExchangeMarketDetailExtractor.execute();
			//ExchangePairExtractor.execute("C:\\ZDX\\code\\CoinMarkCapImport\\data\\exchange_pairs.json");
			MarketlDailyExtractor.execute();
			//CurrencyStartDateExtractor.excute();
			//HistoryMarketExtractor.excute();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
