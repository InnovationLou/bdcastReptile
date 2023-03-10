package org.innovation.dybroadcastParser.catcher;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseInfoCatcher implements Runnable{

    private static Logger logger=Logger.getLogger(BaseInfoCatcher.class);

    //线程中断 临界变量
    public static volatile boolean isInterrupt = false;

    private BaseInfo info;

    public BaseInfoCatcher(BaseInfo info) {
        this.info = new BaseInfo(info.getLiveUrl(), info.getLiveId(), info.getRoomId(), info.getLiveName(), info.getUserUrl(),info.getAuthorId(),info.getSecAuthorId());
    }

    public static void getBaseInfoNew(String liveUrl, String liveId, String roomId, String liveName, String userUrl){
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setDevtools(false)
            )
            ) {
                //指定路径和编码
                CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\BaseInfo-output" +liveName
                        +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))+".csv", CharsetUtil.CHARSET_GBK);
                //按行写出
                writer.writeHeaderLine("时间戳","主播名字","直播间id","粉丝数","获赞数");

                Page page = browser.newPage();

                page.onResponse(response -> {
                    if (response.url().equals(userUrl)){
                        String body= response.text();
                        Document document= Jsoup.parse(body);
                        //从document中定位target
                        Elements elements=document.select("meta[name=description][data-react-helmet=true]");
                        String fans=null;
                        String likes=null;
                        for (Element element:elements){
                            System.out.println("Found Element with Pattern:"+element.attr("content"));
                            //获取粉丝数
                            fans=element.attr("content").split("已有")[1].split("个粉丝")[0];
                            //获取点赞数
                            likes=element.attr("content").split("收获了")[1].split("个喜欢")[0];
                        }
                        logger.info("粉丝数："+fans+"\t点赞数："+likes);
                        //写入csv
                        String[] content = {LocalDateTime.now().toString(),liveName,liveId,fans,likes};
                        writer.write(content);
                    }
                });

                page.navigate(userUrl);
                //等待NetworkIdle
//                page.waitForLoadState(LoadState.NETWORKIDLE);
                Thread.sleep(10*1000);
                page.reload();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * <div class="Y150jDoF">粉丝</div><div class="TxoC9G6_">182.2w</div>
     * <meta data-react-helmet="true" name="description" content="小点新：🍰今天也来一份小点新呀～ 可甜可盐，各种口味随便选😜 📮合作邮箱：hz01@yy.com 欢迎
     * 有颜值/才艺小伙伴联系：whzxzm@yy.com。小点新入驻抖音，TA的抖音号是Naomi_xdx，已有1822294个粉丝，收获了24452967个喜欢，欢迎观看小点新在抖音发布的视频作品，来抖音，记录美好生活！"/>
     *
     * 每分钟刷新一次
     * @throws IOException
     */
    public static void getBaseInfo(String liveUrl, String liveId, String roomId, String liveName, String userUrl) {
        //指定路径和编码
        CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\BaseInfo-output" +liveName
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))+".csv", CharsetUtil.CHARSET_GBK);
        //按行写出
        writer.writeHeaderLine("时间戳","主播名字","直播间id","粉丝数","获赞数");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        try {
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(userUrl)
//                .method("GET", body)
                    .addHeader("Host", "www.douyin.com")
                    .addHeader("Cookie", "douyin.com; ttwid=1%7CRYKHmZ0CqlFDZEgxZJ4En9Q81OlDHuEK9O6HFWSyqVw%7C1667392111%7C1d379a7062af687c91e12e5e464a98ff5f190ec8c0d811b1210f8fe2fd572f08; s_v_web_id=verify_l9zm373k_DmdBOngF_RCWm_4pt5_9Pmv_WHOZCPVXbHJi; passport_csrf_token=5637b3c360ae1c4465baab8f261c1fa1; passport_csrf_token_default=5637b3c360ae1c4465baab8f261c1fa1; n_mh=phSNv06YMqa_3LBUHezwzg3w8t8cco4pBZUDeeZaiUM; passport_assist_user=Cj1X-5vuv0zUy1Ut4Jac7_4OGwvRCSutDGFg2nDJ8juvOWY0FqEJMa7drdmmYLGBcoXiLDF_LlUZn84-hcdGGkgKPON2pZ6b3AfA4DDS5tKfAXjCdrcjoBz9mhyfRhOSO1xb00HQ25OlyN9qlchGb5xKUywyLQ0rEAFs7RS2yxDgqKANGImv1lQiAQNEmpMa; sso_uid_tt=43f83a349708214bfaa3020ee28cbfdd; sso_uid_tt_ss=43f83a349708214bfaa3020ee28cbfdd; toutiao_sso_user=3b80b927ee9b1485446ddccd8c12e152; toutiao_sso_user_ss=3b80b927ee9b1485446ddccd8c12e152; sid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; ssid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; odin_tt=071d6c2e51efe3a49d36ccf642827c3ffa217329e429b54782206020991c5ca64d39b49592be07ad7001e6c0a86a15fbf81ba1cea5f56999835cbd6811a2f4eb; passport_auth_status=ae65d214d757adac448e795935bdbe55%2C; passport_auth_status_ss=ae65d214d757adac448e795935bdbe55%2C; sid_guard=ff5d05a2cb920807ca98fbb431e9a79d%7C1667566250%7C5183999%7CTue%2C+03-Jan-2023+12%3A50%3A49+GMT; uid_tt=c551f9c67033c71aab50d84b5868263c; uid_tt_ss=c551f9c67033c71aab50d84b5868263c; sid_tt=ff5d05a2cb920807ca98fbb431e9a79d; sessionid=ff5d05a2cb920807ca98fbb431e9a79d; sessionid_ss=ff5d05a2cb920807ca98fbb431e9a79d; sid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; ssid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; live_can_add_dy_2_desktop=%221%22; msToken=8Xb5ouLZvNPVSk40h_WZQl0myotSn02-b3RLH-Djmsfr5EaB5FJ73X10sYDmYM-4H4jYroSl4Gjp6CKA0q9dgMSiIes5r4lcJcYheYvTm1Z1SeSfBm2eodA=; download_guide=%221%2F20221119%22; __ac_nonce=06378a4d300e7cc84f935; __ac_signature=_02B4Z6wo00f01s1pp.wAAIDDrmNnlgW8xDrNSaNAANAvIVzA7G6EntqFGX89SBHVDhHBmpdc56X0o9IcM-PRZTaDTONorVCREfO-H32pjWBDI1MG4iqQ4oyUUYpUXvhmeob5gMrlKLtZy2bf7e; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1668873600000%2F0%2F1668850882580%2F0%22; tt_scid=WHgFdcHSSeVAoWkvPrA2x.9E6ZV3r4vZfphBI9CZ8KEfDYPPuNv6mn3mH5c4qBmb08fe; strategyABtestKey=%221668850922.927%22; home_can_add_dy_2_desktop=%221%22; msToken=FYqxpB9UUBiDsZkJREb6uHOrXu9thNSulMVmSPR_4DoseWUHdaVWCVUWr5LrIUMFxi_zTcwlSYpwQYScB4vPuKuiqCtAiorItUOr02Wc6ARL06bwJqAL7_4=; ttwid=1%7CRYKHmZ0CqlFDZEgxZJ4En9Q81OlDHuEK9O6HFWSyqVw%7C1667392111%7C1d379a7062af687c91e12e5e464a98ff5f190ec8c0d811b1210f8fe2fd572f08")
                    .addHeader("upgrade-insecure-requests", "1")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-site", "none")
                    .addHeader("sec-fetch-mode", "navigate")
                    .addHeader("sec-fetch-user", "?1")
                    .addHeader("sec-fetch-dest", "document")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-US;q=0.7")
                    .build();
            Response response;
            //每10秒发送一次请求
            while (!isInterrupt){
                response = client.newCall(request).execute();
                while (response.code() != 200) {
                    logger.error("BaseInfo请求失败，正在重试");
                    response = client.newCall(request).execute();
                }
                Document document= Jsoup.parse(response.body().string());
                //从document中定位target
                Elements elements=document.select("meta[name=description][data-react-helmet=true]");
                String fans=null;
                String likes=null;
                for (Element element:elements){
                    System.out.println("Found Element with Pattern:"+element.attr("content"));
                    //获取粉丝数
                    fans=element.attr("content").split("已有")[1].split("个粉丝")[0];
                    //获取点赞数
                    likes=element.attr("content").split("收获了")[1].split("个喜欢")[0];
                }
                logger.info("粉丝数："+fans+"\t点赞数："+likes);
                //写入csv
                String[] content = {LocalDateTime.now().toString(),liveName,liveId,fans,likes};
                writer.write(content);
                Thread.sleep(10000);
            }
            logger.info("BaseInfoCatch end.....");
        }catch (Exception e){
            logger.error("获取商品信息失败",e);
        }finally {
            writer.close();
        }

    }


    @Override
    public void run() {
        getBaseInfoNew(this.info.getLiveUrl(),this.info.getLiveId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl());
    }
}
