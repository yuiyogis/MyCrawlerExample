package crawler.example;

import com.github.abola.crawler.CrawlerPack;

/**
 * 處理含有Namespace的XML資料方式
 * 
 * @author Abola Lee
 *
 */
public class XmlNamespaceExample {

	public static void main(String[] args) {
		
		// 含有Namespace的XML - 水利署水庫營運資料
		String uri = "https://data.wra.gov.tw/Service/OpenData.aspx?id=416458BC-185A-469F-9ED5-739E1092960F&format=xml";

		System.out.println(
				CrawlerPack.start()
			    .getFromXml(uri)

			    // XML 含 namespace 的標籤，要將 『:』轉為『|』
			    .select("twed|dischargewatervolumeofpowergeneration")
			    
		);
	}
}
