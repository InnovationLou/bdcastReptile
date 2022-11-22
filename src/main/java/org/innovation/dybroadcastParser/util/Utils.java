package org.innovation.dybroadcastParser.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Utils {
    private static Logger logger=Logger.getLogger(Utils.class);
    public static void sleep(int mil){
        try {
            Thread.sleep(mil);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static byte[] uncompress(ByteString bytes1) {
        final byte[] bytes = bytes1.toByteArray();
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[5096];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            System.out.println("gzip uncompress error."+ e);
        }

        return out.toByteArray();
    }

    public static boolean isLiving(String liveId, String roomId ) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://live.douyin.com/webcast/web/enter/?aid=6383&live_id=1&device_platform=web&language=zh-CN&enter_from=web_live&cookie_enabled=true&screen_width=2560&screen_height=1440&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=107.0.0.0" +
                        "&web_rid=" + liveId +
                        "&room_id_str=" + roomId +
                        "&enter_source=&msToken=ZOlA2OwdfcbL_-2oBntRdOxIfMM-okpYFdIG4ZycSnDo29S3wXxofQBaNCDl7BzhRb5xJURM1Ecuqv0sk8LstvHv6UWEm-_j1IFukT7SZ2Ptmetps8kKewk=&X-Bogus=DFSzswVYZnxANCpgS87wiYXAIQRf&_signature=_02B4Z6wo00001Vc-drgAAIDANDS20ATThf1XPnIAADa3PEaCeyz4TmtmvJdxiLL29hOV9l.KhOUd6BtsuZIggLp3RXE7ehBYzzZaJdD2boCDq3bx.EjJ36UbNaFWLD1iClg4C-LJt.KcTy0993")
//                .method("GET", body)
                .addHeader("Host", "live.douyin.com")
                .addHeader("Cookie", "xgplayer_user_id=8403129333; passport_csrf_token=5637b3c360ae1c4465baab8f261c1fa1; passport_csrf_token_default=5637b3c360ae1c4465baab8f261c1fa1; s_v_web_id=verify_l9zvog3j_hXQ9potB_o2t8_4eyZ_Aosr_m0rHetHGkrjb; n_mh=phSNv06YMqa_3LBUHezwzg3w8t8cco4pBZUDeeZaiUM; passport_assist_user=Cj1X-5vuv0zUy1Ut4Jac7_4OGwvRCSutDGFg2nDJ8juvOWY0FqEJMa7drdmmYLGBcoXiLDF_LlUZn84-hcdGGkgKPON2pZ6b3AfA4DDS5tKfAXjCdrcjoBz9mhyfRhOSO1xb00HQ25OlyN9qlchGb5xKUywyLQ0rEAFs7RS2yxDgqKANGImv1lQiAQNEmpMa; sso_uid_tt=43f83a349708214bfaa3020ee28cbfdd; sso_uid_tt_ss=43f83a349708214bfaa3020ee28cbfdd; toutiao_sso_user=3b80b927ee9b1485446ddccd8c12e152; toutiao_sso_user_ss=3b80b927ee9b1485446ddccd8c12e152; sid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; ssid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; passport_auth_status=ae65d214d757adac448e795935bdbe55%2C; passport_auth_status_ss=ae65d214d757adac448e795935bdbe55%2C; sid_guard=ff5d05a2cb920807ca98fbb431e9a79d%7C1667566250%7C5183999%7CTue%2C+03-Jan-2023+12%3A50%3A49+GMT; uid_tt=c551f9c67033c71aab50d84b5868263c; uid_tt_ss=c551f9c67033c71aab50d84b5868263c; sid_tt=ff5d05a2cb920807ca98fbb431e9a79d; sessionid=ff5d05a2cb920807ca98fbb431e9a79d; sessionid_ss=ff5d05a2cb920807ca98fbb431e9a79d; sid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; ssid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; SEARCH_RESULT_LIST_TYPE=%22single%22; download_guide=%223%2F20221119%22; FRIEND_NUMBER_RED_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1668960000000%2F1668917750747%2F0%2F0%22; ttwid=1%7CRYKHmZ0CqlFDZEgxZJ4En9Q81OlDHuEK9O6HFWSyqVw%7C1668921651%7C3746f5e0d9ce51b18190e0f23f855e5eb4c4502e742dc2af3112a02e10c2b7e6; odin_tt=9f21254e2e66331709350d154e64ddd0e927726091240a6a8b1e5fed38159b40c3d34bf958a0b9330519444a8e93a2e2; FOLLOW_RED_POINT_INFO=%221%22; strategyABtestKey=%221668954006.997%22; home_can_add_dy_2_desktop=%221%22; csrf_session_id=f2a319d48960b061923899f806d2abc3; __ac_nonce=0637c55da007d0ccc146f; __ac_signature=_02B4Z6wo00f01o.7iFQAAIDD7PFIPq7kTsqP24zAAMCOBy8wjcbeV0vJRLbZZX73jJJX5N4gWvSQGO8sSsbAsYL4p7LSXS4qN4ULI7Cfut1EBeTmuGXWJH2G1LtKc4IzawRaIQnls0eZAaJJ28; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1669132800000%2F0%2F0%2F1669093662307%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1669132800000%2F0%2F1669093062308%2F0%22; msToken=nYl8ufm4giELlJtCOQRrjAGZ39kttgobsRb6Ig-zPWIsdMf9_xipgRu1Tny55dn10Rk27NEAyOiqNK-FcVcXOjvfGT7GjYl3VUULep0aR3RuyJiR6LarmlQ=; msToken=ZOlA2OwdfcbL_-2oBntRdOxIfMM-okpYFdIG4ZycSnDo29S3wXxofQBaNCDl7BzhRb5xJURM1Ecuqv0sk8LstvHv6UWEm-_j1IFukT7SZ2Ptmetps8kKewk=; tt_scid=GFzgro.N9J5YQZLc9u9fJu-gQcsqW7.SzoBK2iTf7ZsxW3xX8DaQJ.P-xzcYkUGl23e8; live_can_add_dy_2_desktop=%220%22; msToken=cYcdB3m7pP61F0SkPxH157aQK-OFxWWBY1B4RVWHpeZdTrhZyiC_acS6RHeTRdNflTBB2cPjxOTbNYJss117teoA5AlXPx-ETHZHb8zXE2XARJ85KbnDN0M=; ttwid=1%7C4-A-xybpkIWE1orz77PM_ElGZ8Yw8CkPUFbx91eAydU%7C1668915369%7C901d82f6637b4ec80f3a13cc59d632a683180787008ba2dd2136e08ca6212f6f")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"")
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("x-secsdk-csrf-token", "000100000001c69a55a4f7af9300972cc0e73dddd19060857bfedb0085d8c4484f2414b0990d1729cf5b6b898144")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
//                .addHeader("referer", "https://live.douyin.com/9409272172")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-US;q=0.7")
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            logger.error("LiveStreamInfo请求失败，正在重试");
        }
        String result=response.body().string();
        System.out.println(result);
        JSONObject object= JSON.parseObject(result);
        if (null==object || null==object.getJSONObject("data") || null==object.getJSONObject("data").getJSONArray("data")){
            logger.error("检测直播状态出现意料之外的错误,response:"+result);
        }
        JSONObject isLiving = object.getJSONObject("data")
                .getJSONArray("data")
                .getJSONObject(0)
                .getJSONObject("stream_url");
        if (isLiving==null){
            logger.info(liveId+"未直播");
            return false;
        }
        return true;
    }

}
