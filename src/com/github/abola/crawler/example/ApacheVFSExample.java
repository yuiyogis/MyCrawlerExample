package com.github.abola.crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class ApacheVFSExample {

	public static void main(String[] args) {
		
		// 遠端資料路徑
		String uri = "tar:gz:http://crawler:12345678@128.199.204.20:8080"
				+ "/httpLogin/download.tar.gz"
				+ "!download.tar"
				+ "!/path/data";

		System.out.println(
				CrawlerPack.start()
				
				// 參數設定
			    //.addCookie("key","value")	// 設定cookie
				//.setRemoteEncoding("big5")// 設定遠端資料文件編碼
				
				// 選擇資料格式 (三選一)
				.getFromJson(uri)
			    //.getFromHtml(uri)
			    //.getFromXml(uri)
			    
			    // 這兒開始是 Jsoup Document 物件操作
			    .select("desc")
			    
		);
	}

}
