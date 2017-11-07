import com.zdx.currency.CurrencyDetailExtractor;
import com.zdx.currency.ExtractCurrencyRegularName;
import com.zdx.exchange.ExchangeMarketDetailExtractor;

public class Test {

	public static void main(String[] args){
		String s1 = "rankPos\":\"23\",\"name\":\"Bitcoin Gold [Futures]\",\"pair\":\"BTG/USD\",\"pairLink\":\"https://www.bitfinex.com/trading/BTGUSD;";
		System.out.println(s1.replaceAll("\\[", ""));
		s1 = "/exchanges/koineks/";
		String[] s2 = s1.split("/");
		System.out.println(s2[1]);
		s1 = "A B.";
		s1 = s1.replaceAll(" ", "");
		s1 = s1.replaceAll(".", "");
		System.out.println(s1);

		try {
			ExchangeMarketDetailExtractor.execute();
			//CurrencyDetailExtractor.excute();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
