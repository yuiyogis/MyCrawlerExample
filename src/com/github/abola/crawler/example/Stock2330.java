package com.github.abola.crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class Stock2330 {
	
	public static void main(String[] args) {
		// 2330 
		String uri = "http://www.twse.com.tw"
				+ "/ch/trading/exchange/STOCK_DAY/genpage"
				+ "/Report201602/201602_F3_1_8_2330.php"
				+ "?STK_NO=2330&myear=2016&mmon=02";
		
		//table.board_trad > tbody > tr:nth-child(2)
		System.out.println( 
			CrawlerPack.start()
				.setRemoteEncoding("big5")
				.getFromHtml(uri)
				.select("td:matchesOwn(^[\\+\\-]?([0-9]{1,3},)*[0-9]{1,3}(\\.[0-9]+)*$)")
				.toString()
		);
		
		
	}
}
