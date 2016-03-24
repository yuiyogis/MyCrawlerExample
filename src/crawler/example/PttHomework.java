package crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class PttHomework {
	
	public static void main(String[] args) {
		String uri = "https://www.ptt.cc/bbs/Gossiping/M.1458547807.A.32A.html";
		
		//
		System.out.println( 
			CrawlerPack.start()
			    .addCookie("over18", "1")
//				.setRemoteEncoding("big5")
				.getFromHtml(uri)
				.select(".push-tag:matchesOwn(推) + .push-userid")
				.toString()
		);
	}
}
