package crawler.example;

import com.github.abola.crawler.CrawlerPack;
import org.apache.commons.logging.impl.SimpleLog;
import org.jsoup.select.Elements;

import javax.lang.model.element.Element;

/**
 * 上傳GitHub測試
 * 爬蟲包程式的全貌，就只有這固定的模式
 * 
 * @author Abola Lee
 *
 */
public class BasicExample {
	// commit test
	public static void main(String[] args) {

		// set to debug level
		//CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_DEBUG);

		// turn off logging
		CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_OFF);

		// 遠端資料路徑
		String uri = "https://www.ptt.cc/bbs/Gossiping/M.1491704283.A.A17.html";
		//String uri = "https://www.ptt.cc/bbs/Beauty/M.1491661536.A.DCC.html";

		/*System.out.println(
				CrawlerPack.start()

				// 參數設定
			    .addCookie("over18","1")	// 設定cookie
				//.setRemoteEncoding("big5")// 設定遠端資料文件編碼

				// 選擇資料格式 (三選一)
				//.getFromJson(uri)
			    .getFromHtml(uri)
			    //.getFromXml(uri)

			    // 這兒開始是 Jsoup Document 物件操作
			    //.select("div.push:contains(噓)")
				.select("#main-content.bbs-screen.bbs-content")
				//.select("a[href]")
		);
		*/

		Elements jsoup = CrawlerPack.start()

				// 參數設定
				.addCookie("over18","1")	// 設定cookie
				//.setRemoteEncoding("big5")// 設定遠端資料文件編碼

				// 選擇資料格式 (三選一)
				//.getFromJson(uri)
				.getFromHtml(uri)
				//.getFromXml(uri)

				// 這兒開始是 Jsoup Document 物件操作
				.select("#main-content.bbs-screen.bbs-content");

		jsoup.select("div,span").remove();
		System.out.println(jsoup.text());

	}
}
