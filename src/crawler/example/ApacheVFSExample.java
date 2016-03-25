package crawler.example;

import com.github.abola.crawler.CrawlerPack;

/**
 * 練習 Apache VFS 
 * 
 * 重點
 * 1. URI 中包含帳密
 * 2. 需指定port
 * 3. download.tar.gz 在第一次解完gz 後會變成 download.tar
 * 4. 從打包檔中取出資料 
 * 
 * 請參考
 * 
 * @author Abola Lee
 *
 */
public class ApacheVFSExample {

	public static void main(String[] args) {
		
		// 遠端資料路徑
		String uri = "tar:gz:http://crawler:12345678@128.199.204.20:8080"
				+ "/httpLogin/download.tar.gz"
				+ "!download.tar"
				+ "!/path/data";

		System.out.println(
				CrawlerPack.start()
				.getFromJson(uri)
			    .select("desc")
			    
		);
	}

}
