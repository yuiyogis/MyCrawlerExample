package crawler.example;

import com.github.abola.crawler.CrawlerPack;
import org.apache.commons.logging.impl.SimpleLog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 範例: 使用爬蟲包取得八卦版最後50篇文章的網址
 *
 *  Google Chrome 的開發人員工具
 *  在指定的區塊按右鍵 > Copy > Copy Selector
 *
 * @author Abola Lee
 */
class CSSSelector {

    public static void main(String[] args) {

        // set to debug level
        //CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_DEBUG);

        // turn off logging
        CrawlerPack.setLoggerLevel(SimpleLog.LOG_LEVEL_OFF);

        // 遠端資料路徑
        String uri = "http://data.gov.tw/wise_search?nodetype=metadataset&kw=%E5%85%AC%E8%BB%8A%E5%8B%95%E6%85%8B";
        //String uri = "https://www.ptt.cc/index.html";

        System.out.println(
                CrawlerPack.start()

                        // 參數設定
                        //.addCookie("key","value")	// 設定cookie
                        //.setRemoteEncoding("big5")// 設定遠端資料文件編碼

                        // 選擇資料格式 (三選一)
                        //.getFromJson(uri).toString()
                        .getFromHtml(uri)
                        //.getFromXml(uri)

                        // 這兒開始是 Jsoup Document 物件操作
                        .select("#main-menu > div.menu-navigation-container > ul > li.first.leaf.active-trail > a")
                        //.select("#main-menu > div.menu-navigation-container > ul > li.first.leaf.active-trail > a")
                        //.select(" #prodlist > h2")


        );
    }
}