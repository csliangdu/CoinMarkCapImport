package com.zdx.currency;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdx.common.FileHandler;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class ChartMarketHandler implements ParallecResponseHandler {

	@SuppressWarnings("unchecked")
	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {		
		String destDir = (String) responseContext.get("destDir");
		HashMap<String, String> currencyLatestMap = (HashMap<String, String>) responseContext.get("currencyLatestMap");
		if (res.getStatusCodeInt() != 200){
			return;
		}
		String[] tmp = res.getRequest().getResourcePath().split("/");
		String coinName = "";
		String stamp1 = "";
		String stamp2 = "";
		if (tmp.length >= 5){
			coinName = tmp[2];
			stamp1 = tmp[3];
			stamp2 = tmp[4];
		}
		if (stamp1.isEmpty() || stamp2.isEmpty()){
			return;
		}
		currencyLatestMap.put(coinName, stamp2);
		
		//更新配置文件中的时间戳，但是不生成空Json文件
		JSONObject jsonObject = JSON.parseObject(res.getResponseContent());
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			if ("[]".equals(String.valueOf(entry.getValue()))){
				return;			
			}
		}
		String filePath = destDir + File.separator + coinName + "_" + stamp1 + "_" + stamp2;
		FileHandler.writeFile(filePath, res.getResponseContent());
	}
}