package com.github.abola.crawler.example;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.abola.crawler.CrawlerPack;

public class FBInsights {
	
	public static void main(String[] args) {
		
		// 遠端資料路徑
		String uri = 
				"https://graph.facebook.com/v2.5"
				+ "/search?q=%E9%9D%A0%E5%8C%97&type=page&limit=1000&fields=name,id,likes,talking_about_count"
				+ "&access_token=CAACEdEose0cBADZBZAMGGAbkJro6lPJJPz58jdZCVGnonyZCzcOEwBjbZAdOWu0RHrbsZBlyBZBN6t50cZASDUUUlB67r2VX2zfHiJgPSWLxeAN1ed3ordetDjeo5eGvbODQ8i60vANxB7L0B3n2Xm8Yo6LyDwcBztK5C61NLymP1wN8o47piZBy2OS5eJwFWFH3XA7ZAZCsy5DT7MYo9xmLfDq";

		// Jsoup select 後回傳的是  Elements 物件
		Elements elems =
				CrawlerPack.start()
				.getFromJson(uri)
				.select("data");
		
		String output = "id,likes,name,talking_about_count\n";
		
		// 遂筆處理
		for( Element data: elems ){
			String id = data.select("id").text();
			String likes = data.select("likes").text();
			String name = data.select("name").text();
			String talking_about_count = data.select("talking_about_count").text();
			
			output += id+","+likes+",\""+name+"\","+talking_about_count+"\n";
		}
		
		System.out.println( output );
	} 
}
