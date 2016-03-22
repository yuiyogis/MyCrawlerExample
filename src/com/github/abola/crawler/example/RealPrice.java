package com.github.abola.crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class RealPrice {
	public static void main(String[] args) {
		
		String uri = "zip:http://plvr.land.moi.gov.tw//Download?type=zip&fileName=lvr_landxml.zip!/A_LVR_LAND_A.XML";
		
		//
		System.out.println( 
			CrawlerPack.start()
				.setRemoteEncoding("big5")
				.getFromXml(uri)
//				.select("td:matchesOwn(^[\\+\\-]?([0-9]{1,3},)*[0-9]{1,3}(\\.[0-9]+)*$)")
				.toString()
		);
		
		
	}	
	
}
