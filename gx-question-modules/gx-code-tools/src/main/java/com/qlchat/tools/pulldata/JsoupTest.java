package com.qlchat.tools.pulldata;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {
    public static void main(String[] args) throws Exception {
        String url = "https://zujuan.21cnjy.com/question/list?categories=6136&question_channel_type=&difficult_index=&exam_type=&kid_num=&sortField=time&filterquestion=0&page=1&page=1&_=1516546302608";
        String cookie = "HqNL_ef65_saltkey=xAfETZMO; HqNL_ef65_lastvisit=1516019841; _csrf=e1b65184f4f07b2833d346eab9457fb555f9465a2b093c703dfbfa03bd45aa96a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22abvbrpxnPYJaNUSetf7Lz795fuxe4V-2%22%3B%7D; Hm_lvt_5d70f3704df08b4bfedf4a7c4fb415ef=1515204849,1515205926,1516023478,1516371723; chid=d79688f1e7866722fd16866f40a252116287a86b4e6e2213e3150af3ba96e038a%3A2%3A%7Bi%3A0%3Bs%3A4%3A%22chid%22%3Bi%3A1%3Bs%3A1%3A%222%22%3B%7D; xd=bbc5bced806f427ff9aca7cf1cf56a37c9016539f1da160ae385126fb0d1cc2fa%3A2%3A%7Bi%3A0%3Bs%3A2%3A%22xd%22%3Bi%3A1%3Bs%3A1%3A%223%22%3B%7D; Hm_lpvt_5d70f3704df08b4bfedf4a7c4fb415ef=1516546304";
        String result = ConnectUtil.httpGet(url, cookie);
        System.out.println(result);
        // data-page=\"2\" data-page="\&quot;2\&quot;"
    }
}