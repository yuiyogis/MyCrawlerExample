package com.github.abola.crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class HighwayStaticInfo {


	public static void main(String[] args) {
		
		// 遠端資料路徑
		String uri = "gz:http://tisvcloud.freeway.gov.tw/roadlevel_info.xml.gz";

		System.out.println(
				CrawlerPack.start()
				
				// 參數設定
			    //.addCookie("key","value")	// 設定cookie
//				.setRemoteEncoding("utf8")// 設定遠端資料文件編碼
				
				// 選擇資料格式 (三選一)
//				.getFromJson(uri)
			    //.getFromHtml(uri)
			    .getFromXml(uri)
			    
			    // 這兒開始是 Jsoup Document 物件操作
//			    .select(".css .selector ")
			    
		);
	}
}
