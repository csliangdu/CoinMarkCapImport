package com.zdx.currency;

import java.io.File;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zdx.common.JsonFormatTool;
import com.zdx.common.Utils;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

public class DailyDetailExtractor implements ParallecResponseHandler {

	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {
		// TODO Auto-generated method stub
		Market cm = new Market();
		Long startTime = (Long) responseContext.get("startTime");
		Document doc = Jsoup.parse(res.getResponseContent());

		String jsonString = JsonFormatTool.formatJson(cm.toString());
		Utils.writeFile(System.getProperty("user.dir") + File.separator + "Currency" + File.separator + cm.lowerRegularName + "_" + cm.updateTime, jsonString);
	}
}

}

}
