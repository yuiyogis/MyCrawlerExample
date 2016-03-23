package com.github.abola.crawler.example;

import org.jsoup.Jsoup;

public class JsoupProb {
	public static void main(String[] args) throws Exception{
		// no compressed file support
		System.out.println(
			// Jsoup.connect("http://en.wikipedia.org/").get()
			Jsoup.connect("http://tisvcloud.freeway.gov.tw/roadlevel_info.xml.gz").get()
	    );
	}
}
