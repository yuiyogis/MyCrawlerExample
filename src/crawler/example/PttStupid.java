package crawler.example;

import com.github.abola.crawler.CrawlerPack;

public class PttStupid {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ptt 笨版最新文章列表
		String url = "https://www.ptt.cc/bbs/Gossiping/M.1119222611.A.7A9.html";

		System.out.println(
				CrawlerPack.start()
			    .addCookie("over18","1")  // 必需在 getFromXXX 前設定Cookie
			    .getFromHtml(url)
			    .select("span:containsOwn(標題) + span:eq(1)").text()
		);
	}

}
