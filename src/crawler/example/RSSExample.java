package crawler.example;

import com.github.abola.crawler.CrawlerPack;

/**
 * RSS也是XML格式的一種類型
 * 
 * 練習取得蘋果新聞提供的RSS
 * 
 * @author Abola Lee
 *
 */
public class RSSExample {
	public static void main(String[] args) {
		
		// 遠端資料路徑

		// 蘋果即時新聞
		String uri = "http://www.appledaily.com.tw/rss/newcreate/kind/rnews/type/new";

		// 痞客幫
//		String uri = "http://feed.pixnet.net/blog/posts/rss/yao55";

		// Yahoo 台北電影
//		String uri = "https://tw.movies.yahoo.com/rss/tpeboxoffice";

		// Mobile01
//		String uri = "http://www.mobile01.com/rss/hottopics.xml";

		// 104 外包網
//		String uri = "https://case.104.com.tw/rss/newcase_cat1.xml";

		System.out.println(
				CrawlerPack.start()
				    .getFromXml(uri)
				    .select("item title")
		);
	}
}
