package crawler.example;

import com.github.abola.crawler.CrawlerPack;

/**
 * 每次呼叫都去爬指定個股，目前畫面上的交易明細資料
 * 
 * @author Abola Lee
 *
 */
public class YahooStock {

	// 每次取得最後50筆交易的內容
	static String uri_format = "https://tw.stock.yahoo.com/q/ts?s=%s&t=50";
	
	public static void main(String[] args) {
		String stockNumber = "";
		// 要有輸入參數															
		if ( args.length >= 1){
			stockNumber = args[0];
		}else{
			// 沒輸入參數
			return ;
		}
		
		// 遠端資料路徑
		String uri = String.format(uri_format, stockNumber);
		System.out.println(uri);
System.out.println(
		// 取得遠端資料
		CrawlerPack.start()
			.setRemoteEncoding("big5")// 設定遠端資料文件編碼
			.getFromHtml(uri)
			// 目標 <td align="center" width="240">2330 台積電 成 交 明 細</td>
			.select("table:contains(成 交 明 細)") 
			);
			    
		
	}	
}
