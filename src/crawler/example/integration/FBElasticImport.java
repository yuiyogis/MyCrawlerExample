package crawler.example.integration;

import com.github.abola.crawler.CrawlerPack;
import com.mashape.unirest.http.Unirest;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Student on 2016/7/10.
 */
public class FBElasticImport {

    static String elasticHost = "localhost" ;
    static String elasticPort = "9200" ;
    static String elasticIndex = "fb";
    static String elasticIndexType = "data";


    public static void main(String[] args) {

        // 遠端資料路徑
        // [query sample]
        // search?fields=name,id,likes,talking_about_count&limit=1000&q=靠北&type=page
//		curl -i -X GET \
        //"https://graph.facebook.com/v2.5/search?q=%E9%9D%A0%E5%8C%97&type=page&limit=10&fields=name%2Cid%2Clikes&access_token=EAACEdEose0cBAFnZB7WVs6my1yJ2zd4hP2z1evpZAgzhE4kQgvnfdjdzZCOT6vAcr6vnULgBmbZB18bTg9evDDSOtmHpbX69l9dUiSJPZBc2vB4KyafJH4wfTv2XFeUpnxencVdma2TsqZCnzTWCK0bZBrE1eZAKFx7NXBNXl90vjwZDZD"
        String uri =
                "https://graph.facebook.com/v2.5"
                        + "/YahooTWNews/feed?fields=message,likes.limit(1).summary(true),created_time&until=1452816000&since=1450137600&limit=100"
//				+ "/search?q=%E9%9D%A0%E5%8C%97&type=page&limit=10&fields=name%2Cid%2Clikes"
                        + "&access_token=EAACEdEose0cBAEh7ET4ipcHRsHZC1fsD34GynnXed0USnAB7x9fwqyfAwZBLIPND8k6ZAtlEZAc8Tazka7cZCX9o2sTCjpWdsaZCGq5fgcBHizFwiLmjFfaifbpVD3QSUvNCUbVEQZAypcUdAUthIMruV9k8V02rjeI3O8Mi9GsZAQZDZD";



        // Jsoup select 後回傳的是  Elements 物件
//		[data sample]
//		----
//		{
//			"data": [
//			{
//				"name": "靠北工程師",
//					"id": "1632027893700148",
//					"likes": 174587,
//					"talking_about_count": 188119
//			}
//		}

//        System.out.println(CrawlerPack.start()
//                .getFromJson(uri));
        Elements elems =
                CrawlerPack.start()
                        .getFromJson(uri)
                        .select("data:has(created_time)");
//System.out.println(elems);
//        String output = "id,名稱,按讚數,討論人數\n";

        // 遂筆處理
        for( Element data: elems ){


            String created_time = data.select("created_time").text();
            String id = data.select("id").text();
            String message = data.select("message").text();
            String likes = data.select("likes > summary > total_count").text();
//            String name = data.select("name").text();
//            String likes = data.select("likes").text();
//            String talking_about_count = data.select("talking_about_count").text();
//
//            output += id+",\""+name+"\","+likes+","+talking_about_count+"\n";
            System.out.println(created_time);
            System.out.println(id);
            System.out.println(message);
            System.out.println(likes);
            // Elasticsearch data format


            String elasticJson = "{" +
                    "\"created_time\":\"" + created_time + "\"" +
                    ",\"message\":\"" + message + "\"" +
                    ",\"likes\":" + likes +
                    ",\"id\":\"" + id + "\"" +
                    "}";



            System.out.println(
                    // curl -XPOST http://localhost:9200/pm25/data -d '{...}'
                    sendPost("http://" + elasticHost + ":" + elasticPort
                                    + "/" + elasticIndex + "/" + elasticIndexType
                            , elasticJson));
        }
//        System.out.println( output );
    }



    /***
     * NVL
     * if arg0 isNull then return arg1
     */
    static public <T> T nvl(T arg0, T arg1) {
        return (arg0 == null)?arg1:arg0;
    }

    static String sendPost(String url, String body){
        try{
            return Unirest.post(url)
                    .header("content-type", "text/plain")
                    .header("cache-control", "no-cache")
                    .body(body)
                    .asString().getBody();

        }catch(Exception e){return "Error:" + e.getMessage();}
    }
}
