package com.zdx.common;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Utils {

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
	public static void writeFile(String filePath, String str) {  
		try {
			FileWriter fw = new FileWriter(filePath);  
			PrintWriter out = new PrintWriter(fw);  
			out.write(str); 
			out.println();  
			fw.close();  
			out.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getExchangeNames(){
		InputStream is = Utils.class.getClass().getResourceAsStream("/exchange_name");
		ArrayList<String> exnames = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String y ="";
		try {
			while((y = br.readLine())!=null){
				System.out.println(y);
				exnames.add(y);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exnames;
	}

	public static ArrayList<String> getCurrencyNames(){
		InputStream is = Utils.class.getClass().getResourceAsStream("/currency_name");
		ArrayList<String> exnames = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String y ="";
		try {
			while((y = br.readLine())!=null){
				System.out.println(y);
				exnames.add(y);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exnames;
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
}
