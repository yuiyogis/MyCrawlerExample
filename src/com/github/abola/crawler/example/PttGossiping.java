package com.github.abola.crawler.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.abola.crawler.CrawlerPack;

/**
 * 範例: 使用爬蟲包取得八卦版最後50篇文章的網址
 */
class PttGossiping {

    final static String gossipMainPage = "https://www.ptt.cc/bbs/Gossiping/index.html";
    final static String gossipIndexPage = "https://www.ptt.cc/bbs/Gossiping/index%s.html";
    // 取得最後幾篇的文章數量
    static Integer loadLastPosts = 50;

    public static void main(String[] argv){

        String prevPage =
            CrawlerPack.start()
                .addCookie("over18","1")                // 八卦版進入需要設定cookie
                .getFromHtml(gossipMainPage)            // 遠端資料格式為 HTML
                .select(".action-bar .pull-right > a")  // 取得右上角『前一頁』的內容
                .get(1).attr("href")
                .replaceAll("/bbs/Gossiping/index([0-9]+).html", "$1");
        
        // 目前最末頁 index 編號
        Integer lastPage = Integer.valueOf(prevPage)+1;

        List<String> lastPostsLink = new ArrayList<String>();

        while ( loadLastPosts > lastPostsLink.size() ){
            String currPage = String.format(gossipIndexPage, lastPage--);

            Elements links =
                CrawlerPack.start()
                    .addCookie("over18", "1")
                    .getFromHtml(currPage)
                    .select(".title > a");

            for( Element link: links) lastPostsLink.add( link.attr("href") );
        }

        // 個別分頁每一頁資訊
        for(String url : lastPostsLink){
        	System.out.println( analyzeFeed(url) );
        	try{Thread.sleep(150);}catch(Exception e){}
        }
    }
    
    /**
     * 分析輸入的文章，簡易統計 
     * 
     * @param url
     * @return
     */
    static String analyzeFeed(String url){
    	
    	// 取得 Jsoup 物件，稍後要做多次 select
        Document feed = 
        	CrawlerPack.start()
		        .addCookie("over18","1")                // 八卦版進入需要設定cookie
		        .getFromHtml("https://www.ptt.cc"+url);           // 遠端資料格式為 HTML
        
//        System.out.println(feed);
        
        // 1. 文章作者
        String feedAuthor = feed.select("span:contains(作者) + span").text();
        
        // 2. 文章標題
        String feedTitle = feed.select("span:contains(標題) + span").text();
        
         
        // 3. 按推總數
        Integer feedLikeCount = 
        		countReply(feed.select(".push-tag:matchesOwn(推) + .push-userid"));
        
        // 4. 不重複推文數
        Integer feedLikeCountNoRep = 
        		countReplyNoRepeat(feed.select(".push-tag:matchesOwn(推) + .push-userid"));

        // 5. 按噓總數
        Integer feedUnLikeCount = 
        		countReply(feed.select(".push-tag:matchesOwn(噓) + .push-userid"));
        	
        // 6. 不重複噓文數
        Integer feedUnLikeCountNoRep = 
        		countReplyNoRepeat(feed.select(".push-tag:matchesOwn(噓) + .push-userid"));

        // 7. 不重複噓文數
        Integer feedReplyCountNoRep = 
        		countReplyNoRepeat(feed.select(".push-tag + .push-userid"));
      
        String output = "\"" + feedAuthor +"\","
        				+ "\"" + feedTitle +"\","
        				+ feedLikeCount +","
        				+ feedLikeCountNoRep +","
        				+ feedUnLikeCount +","
        				+ feedUnLikeCountNoRep +","
        				+ feedReplyCountNoRep;
//        System.out.println(output);
    	return output;
    }
    
    /**
     * 推文人數總計
     * @param reply
     * @return
     */
    static Integer countReply(Elements reply){
    	return reply.text().split(" ").length;
    }
    
    /**
     * 推文人數總計
     * @param reply
     * @return
     */
    static Integer countReplyNoRepeat(Elements reply){
    	return new HashSet<String>( 
    			Arrays.asList(
    				reply.text().split(" ") 
    			) 
    		).size();    	
    }    
}