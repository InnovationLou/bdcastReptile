package org.innovation.dybroadcastParser.catcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StreamDownloader implements Runnable{
    private static Logger logger=Logger.getLogger(StreamDownloader.class);

    public static volatile boolean isInterrupt = false;
    private BaseInfo info;

    public StreamDownloader(BaseInfo info) {
        this.info = new BaseInfo(info.getLiveUrl(), info.getLiveId(), info.getRoomId(), info.getLiveName(), info.getUserUrl(),info.getAuthorId(),info.getSecAuthorId());
    }

    /**
     * {
     * 	"data": {
     * 		"data": [{
     * 			"id_str": "7168691894279473928",
     * 			"status": 2,
     * 			"status_str": "2",
     * 			"title": "刷大钱，凯大瑞，记住自己为什么上场！（上车上分定位私信）",
     * 			"user_count_str": "400+",
     * 			"cover": {
     * 				"url_list": ["https://p3-webcast-sign.douyinpic.com/webcast-cover/21334ae7e185d530ed4a73ac8e813028.jpg~tplv-qz53dukwul-common-resize:0:0.image?x-expires=1671685128\u0026x-signature=7%2FXG6WX22XDHMIU6uX7v0l5JCvs%3D", "https://p6-webcast-sign.douyinpic.com/webcast-cover/21334ae7e185d530ed4a73ac8e813028.jpg~tplv-qz53dukwul-common-resize:0:0.image?x-expires=1671685128\u0026x-signature=kuhQ9wVAjukdmL0CVf%2FEA24Ww5w%3D"]
     *                        },
     * 			"stream_url": {
     * 				"flv_pull_url": {
     * 					"FULL_HD1": "http://pull-flv-l11.douyincdn.com/third/stream-112010812736930268.flv",
     * 					"HD1": "http://pull-flv-l11.douyincdn.com/third/stream-112010812736930268_hd.flv",
     * 					"SD1": "http://pull-flv-l11.douyincdn.com/third/stream-112010812736930268_ld.flv",
     * 					"SD2": "http://pull-flv-l11.douyincdn.com/third/stream-112010812736930268_sd.flv"
     *                },
     * @param liveUrl
     * @param liveId
     * @param roomId
     * @param liveName
     * @param userUrl
     */
    public static void getLiveStream(String liveUrl, String liveId, String roomId, String liveName, String userUrl){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        try {
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://live.douyin.com/webcast/web/enter/?aid=6383&live_id=1&device_platform=web&language=zh-CN&enter_from=web_live&cookie_enabled=true&screen_width=2560&screen_height=1440&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=107.0.0.0" +
                            "&web_rid=" + liveId +
                            "&room_id_str=" + roomId +
                            "&enter_source=&msToken=ZOlA2OwdfcbL_-2oBntRdOxIfMM-okpYFdIG4ZycSnDo29S3wXxofQBaNCDl7BzhRb5xJURM1Ecuqv0sk8LstvHv6UWEm-_j1IFukT7SZ2Ptmetps8kKewk=&X-Bogus=DFSzswVYZnxANCpgS87wiYXAIQRf&_signature=_02B4Z6wo00001Vc-drgAAIDANDS20ATThf1XPnIAADa3PEaCeyz4TmtmvJdxiLL29hOV9l.KhOUd6BtsuZIggLp3RXE7ehBYzzZaJdD2boCDq3bx.EjJ36UbNaFWLD1iClg4C-LJt.KcTy0993")
//                .method("GET", body)
                    .addHeader("Host", "live.douyin.com")
                    .addHeader("Cookie", "xgplayer_user_id=8403129333; passport_csrf_token=5637b3c360ae1c4465baab8f261c1fa1; passport_csrf_token_default=5637b3c360ae1c4465baab8f261c1fa1; s_v_web_id=verify_l9zvog3j_hXQ9potB_o2t8_4eyZ_Aosr_m0rHetHGkrjb; n_mh=phSNv06YMqa_3LBUHezwzg3w8t8cco4pBZUDeeZaiUM; passport_assist_user=Cj1X-5vuv0zUy1Ut4Jac7_4OGwvRCSutDGFg2nDJ8juvOWY0FqEJMa7drdmmYLGBcoXiLDF_LlUZn84-hcdGGkgKPON2pZ6b3AfA4DDS5tKfAXjCdrcjoBz9mhyfRhOSO1xb00HQ25OlyN9qlchGb5xKUywyLQ0rEAFs7RS2yxDgqKANGImv1lQiAQNEmpMa; sso_uid_tt=43f83a349708214bfaa3020ee28cbfdd; sso_uid_tt_ss=43f83a349708214bfaa3020ee28cbfdd; toutiao_sso_user=3b80b927ee9b1485446ddccd8c12e152; toutiao_sso_user_ss=3b80b927ee9b1485446ddccd8c12e152; sid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; ssid_ucp_sso_v1=1.0.0-KDY5YjBlOWZkYWRmMGFiNGFjN2ZlNzc4ZTE5ZDdmMjA0MDViZWRmMjcKHQiu0N-6gQMQqZWUmwYY7zEgDDDJlPjbBTgGQPQHGgJsZiIgM2I4MGI5MjdlZTliMTQ4NTQ0NmRkY2NkOGMxMmUxNTI; passport_auth_status=ae65d214d757adac448e795935bdbe55%2C; passport_auth_status_ss=ae65d214d757adac448e795935bdbe55%2C; sid_guard=ff5d05a2cb920807ca98fbb431e9a79d%7C1667566250%7C5183999%7CTue%2C+03-Jan-2023+12%3A50%3A49+GMT; uid_tt=c551f9c67033c71aab50d84b5868263c; uid_tt_ss=c551f9c67033c71aab50d84b5868263c; sid_tt=ff5d05a2cb920807ca98fbb431e9a79d; sessionid=ff5d05a2cb920807ca98fbb431e9a79d; sessionid_ss=ff5d05a2cb920807ca98fbb431e9a79d; sid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; ssid_ucp_v1=1.0.0-KDNlZTMwYzA1ODhmZjFlY2JiNzIyYjg0NzUzODc0OTg1OGM5ZDFkYzQKFwiu0N-6gQMQqpWUmwYY7zEgDDgGQPQHGgJscSIgZmY1ZDA1YTJjYjkyMDgwN2NhOThmYmI0MzFlOWE3OWQ; SEARCH_RESULT_LIST_TYPE=%22single%22; download_guide=%223%2F20221119%22; FRIEND_NUMBER_RED_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1668960000000%2F1668917750747%2F0%2F0%22; ttwid=1%7CRYKHmZ0CqlFDZEgxZJ4En9Q81OlDHuEK9O6HFWSyqVw%7C1668921651%7C3746f5e0d9ce51b18190e0f23f855e5eb4c4502e742dc2af3112a02e10c2b7e6; odin_tt=9f21254e2e66331709350d154e64ddd0e927726091240a6a8b1e5fed38159b40c3d34bf958a0b9330519444a8e93a2e2; FOLLOW_RED_POINT_INFO=%221%22; strategyABtestKey=%221668954006.997%22; home_can_add_dy_2_desktop=%221%22; csrf_session_id=f2a319d48960b061923899f806d2abc3; __ac_nonce=0637c55da007d0ccc146f; __ac_signature=_02B4Z6wo00f01o.7iFQAAIDD7PFIPq7kTsqP24zAAMCOBy8wjcbeV0vJRLbZZX73jJJX5N4gWvSQGO8sSsbAsYL4p7LSXS4qN4ULI7Cfut1EBeTmuGXWJH2G1LtKc4IzawRaIQnls0eZAaJJ28; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1669132800000%2F0%2F0%2F1669093662307%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1669132800000%2F0%2F1669093062308%2F0%22; msToken=nYl8ufm4giELlJtCOQRrjAGZ39kttgobsRb6Ig-zPWIsdMf9_xipgRu1Tny55dn10Rk27NEAyOiqNK-FcVcXOjvfGT7GjYl3VUULep0aR3RuyJiR6LarmlQ=; msToken=ZOlA2OwdfcbL_-2oBntRdOxIfMM-okpYFdIG4ZycSnDo29S3wXxofQBaNCDl7BzhRb5xJURM1Ecuqv0sk8LstvHv6UWEm-_j1IFukT7SZ2Ptmetps8kKewk=; tt_scid=GFzgro.N9J5YQZLc9u9fJu-gQcsqW7.SzoBK2iTf7ZsxW3xX8DaQJ.P-xzcYkUGl23e8; live_can_add_dy_2_desktop=%220%22; msToken=AzdsFEr69t2z7dMUiRg1jlX60AoNtwEWyZv_DFACzJz9d9-H_bh4gyAiiL5gnj_wD0bXKlb3RRxM41PRpNT2E_FJHsoymY5F1couDi-xJeR14dHW-LYq-94=; ttwid=1%7C4-A-xybpkIWE1orz77PM_ElGZ8Yw8CkPUFbx91eAydU%7C1668915369%7C901d82f6637b4ec80f3a13cc59d632a683180787008ba2dd2136e08ca6212f6f")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("x-secsdk-csrf-token", "000100000001c69a55a4f7af9300972cc0e73dddd19060857bfedb0085d8c4484f2414b0990d1729cf5b6b898144")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
//                    .addHeader("referer", "https://live.douyin.com/9409272172")
                    .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-US;q=0.7")
                    .build();
            Response response;
            response = client.newCall(request).execute();
            while (response.code() != 200) {
                logger.error("LiveStreamInfo请求失败，正在重试");
                response = client.newCall(request).execute();
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
                logger.info(liveName+"未直播");
                return;
            }
            String streamUrl = object.getJSONObject("data")
                    .getJSONArray("data")
                    .getJSONObject(0)
                    .getJSONObject("stream_url")//未开播时这里为空
                    .getJSONObject("flv_pull_url")
                    .getString("SD2");
            logger.info(liveName+"正在直播,流地址:"+streamUrl);
            Long startTime=System.currentTimeMillis();
            String filename=liveName+"_"+startTime+".mp4";
            //使用ffmpeg下载streamUrl到本地
            ProcessBuilder pb = new ProcessBuilder()
                    .redirectErrorStream(true)
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT);
//            String ffmpegPath= Arrays.stream(System.getenv("Path").split(";"))
//                    .filter(path->path.contains("ffmpeg"))
//                    .findFirst()
//                    .orElseThrow(()->new RuntimeException("环境变量中未找到ffmpeg"));
//            String cmd="cmd.exe /c start D:\\java\\ffmpeg\\bin\\ffmpeg.exe -i "+streamUrl+" -c copy -y " + System.getProperty("user.dir")+"\\data\\"+liveId+".mp4";
//            //写入到bat
//            File file = new File(System.getProperty("user.dir")+"\\data\\temp.bat");
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileWriter fw = new FileWriter(file);
//            fw.write(bat);
//            fw.close();
            //下载原画视频
            pb.command("ffmpeg", "-i", streamUrl, "-c", "copy", "-y", System.getProperty("user.dir")+"\\data\\"+filename);

            Process process = pb.start();
            //捕捉process输出
//            new Thread(() -> {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//                try {
//                    while ((line = reader.readLine()) != null) {
//                        logger.info(line);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
            rename:
            //check isInterrupt
            while (true) {
                while (isInterrupt) {
                    //杀死进程ffmpeg.exe 会导致文件损坏 dnm
//                    Runtime.getRuntime().exec("taskkill /F /IM ffmpeg.exe");
                    //PERFECT SOLUTION!!!!!!!!
                    OutputStream ostream = process.getOutputStream(); //Get the output stream of the process, which translates to what would be user input for the commandline
                    ostream.write("q\n".getBytes());       //write out the character Q, followed by a newline or carriage return so it registers that Q has been 'typed' and 'entered'.
                    ostream.flush();                          //Write out the buffer.
                    break rename;
                }
                TimeUnit.SECONDS.sleep(1);
            }
            Long endtime=System.currentTimeMillis();
            logger.info("已停止下载,下载持续时间:"+((endtime-startTime)/1000)+"s.开始重命名文件...");
            File file=new File(System.getProperty("user.dir")+"\\data\\"+filename);
            while (!file.exists()){
                logger.info("文件不存在,可能正在生成....请等待");
                TimeUnit.SECONDS.sleep(1);
            }
            TimeUnit.SECONDS.sleep(3);
            Runtime.getRuntime().exec("killall ffmpeg");
            TimeUnit.SECONDS.sleep(1);
            file.renameTo(new File(System.getProperty("user.dir")+"\\data\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4"));
//            //分离音频ffmpeg -i a.mp4 -acodec copy -vn r2.mp3
//            pb.command("ffmpeg", "-i", System.getProperty("user.dir")+"\\data\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4", "-acodec", "copy", "-vn", System.getProperty("user.dir")+"\\data\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp3");
//            pb.start();
        }catch (Exception e){
            logger.error("下载直播流失败",e);
        }
    }

    /**
     * timestampe to YYYYMMDDHHmmss
     * @param timestamp
     * @return
     */
    private static String timestampToChar(Long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return sdf.format(new Date(timestamp));
    }

    @Override
    public void run() {
        getLiveStream(this.info.getLiveUrl(),this.info.getLiveId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl());
    }
}
