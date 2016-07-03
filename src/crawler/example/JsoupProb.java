package crawler.example;

import org.jsoup.Jsoup;

/**
 * Jsoup 目前對於壓縮格式，還沒有很容易處理的方式
 * 
 * @author Abola Lee
 *
 */
public class JsoupProb {
	public static void main(String[] args) throws Exception{
		// no compressed file support
		System.out.println(
			// Jsoup.connect("http://en.wikipedia.org/").get()
			Jsoup.connect("http://tisvcloud.freeway.gov.tw/roadlevel_info.xml.gz").get()
	    );
	}
}
