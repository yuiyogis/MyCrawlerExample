package crawler.example;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.abola.crawler.CrawlerPack;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * 每次呼叫都去爬指定個股，目前畫面上的交易明細資料
 * 
 * @author Abola Lee
 *
 */
public class YahooStock {

	static Logger log = LoggerFactory.getLogger(YahooStock.class);
	final static String mongodbServer = "128.199.204.20";
	final static String mongodbDB = "mydb";
	
	static String stockNumber;
	
	// 每次取得最後50筆交易的內容
	static String uri_format = "https://tw.stock.yahoo.com/q/ts?s=%s&t=50";
	
	public static void main(String[] args) {
		
		// 要有輸入參數															
		if ( args.length >= 1){
			stockNumber = args[0];
		}else{
			// 沒輸入參數
			return ;
		}
		
		crawlerStockByNumber(stockNumber);
	}
	
	static void crawlerStockByNumber(String stockNumber){

		// 遠端資料路徑
		String uri = String.format(uri_format, stockNumber);
		
		log.debug("Call remote uri: {}", uri);
		
		// 取得交易明細資料		
		Elements transDetail = 
			CrawlerPack.start()
				.setRemoteEncoding("big5")// 設定遠端資料文件編碼
				.getFromHtml(uri)
				// 目標 <td align="center" width="240">2330 台積電 成 交 明 細</td>
				.select("table:contains(成 交 明 細) ") ;
		
//		log.debug( transDetail.toString() );
		
		List<DBObject> parsedTransDetail = parseTransDetail(transDetail);
		
		saveTransDetail(parsedTransDetail);
	}
	
	static List<DBObject> parseTransDetail(Elements transDetail){

		List<DBObject> result = new ArrayList<>();
		
		
		String day = transDetail
						.select("td:matchesOwn(資料日期)")
						.text().substring(5,14);
		
		// 取出 header 以外的所有交易資料
		for(Element detail: transDetail.select("td > table tr:gt(0)") ){
			
			Map<String, String> data = new HashMap<>();
			
//			log.debug( detail.toString() );
/*			資料格式範例
  			<tr align="center" bgcolor="#ffffff" height="25">
			 <td>09:45:46</td>
			 <td class="low">158.00</td>
			 <td>158.50</td>
			 <td>158.50</td>
			 <td>－</td>
			 <td>1</td>
			</tr>
*/			
//			log.debug( detail.select("td:eq(0)").text() );
			data.put("stock", stockNumber);
			data.put("day", day);
			data.put("time", detail.select("td:eq(0)").text());
			data.put("buy", detail.select("td:eq(1)").text());
			data.put("sell", detail.select("td:eq(2)").text());
			data.put("strike", detail.select("td:eq(3)").text());
			data.put("volume", detail.select("td:eq(5)").text());
			
			result.add( new BasicDBObject(data) );
		}
		return result;
	} 
	
	static void saveTransDetail(List<DBObject> parsedTransDetail){

		MongoClient mongoClient ;
		try {
			mongoClient = new MongoClient( mongodbServer );

			DB db = mongoClient.getDB( mongodbDB );
			
			db.getCollection("TransDetail").insert(parsedTransDetail);
			
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}
}
