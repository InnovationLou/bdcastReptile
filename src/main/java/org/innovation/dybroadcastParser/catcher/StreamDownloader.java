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
                    .url("https://live.douyin.com/webcast/room/web/enter/?aid=6383&live_id=1&device_platform=web&language=en-US&enter_from=web_others_homepage&cookie_enabled=true&screen_width=2560&screen_height=1440&browser_language=en-US&browser_platform=Win32&browser_name=Chrome&browser_version=110.0.0.0&" +
                            "web_rid=" + liveId+
                            "&room_id_str=" + roomId +
                            "&enter_source=&Room-Enter-User-Login-Ab=0&is_need_double_stream=false&msToken=eEaBmu8g0WVGhIz_DbuZGEaeNs40TRu5FdzkVYr67O5YZFQ9LNwrk0oywGOeRHHURFvhzlIhqAKPX514BXx5c0AA_5l-cAOpgeKNcf3n_Jlvptyh2dwFZQ==&X-Bogus=DFSzswVYWt2ANcqftavDIGXyYJlA&_signature=_02B4Z6wo00001geADOgAAIDDJCEiRxI-JWYHgAhAAOXuMmf9ZtLLYaztZoWFf8TZBhQJhycxPObKiW4g1XNqSSgGKVsaQQZT0fx3LLnPoMBXMfUiu-9hBx7LYBq7NFqcdLIN.0pwMSOJe.ah03")
//                .method("GET", body)
                    .addHeader("Host", "live.douyin.com")
                    .addHeader("Cookie", "ttwid=1%7CyeXN7Bcjnp2aipO7OoAYLO6icv26VC22KPmklZOJ8CY%7C1670501465%7C50cd7859d603f029c65447a0def481a477e7a14f0fe9ae32892850435aae9424; xgplayer_user_id=897224754202; n_mh=phSNv06YMqa_3LBUHezwzg3w8t8cco4pBZUDeeZaiUM; store-region=cn-gd; store-region-src=uid; passport_csrf_token=479e499638449a3f81abe69878d7c57f; passport_csrf_token_default=479e499638449a3f81abe69878d7c57f; sso_uid_tt=d947fd68227e46e44641c5051fefa7d2; sso_uid_tt_ss=d947fd68227e46e44641c5051fefa7d2; toutiao_sso_user=165ce97c03f9e15b155e8de8de04b9ee; toutiao_sso_user_ss=165ce97c03f9e15b155e8de8de04b9ee; passport_assist_user=Cj0GAPSQEkW6qzmUGHeCIvaQKxiDLDiP_dWWwvzwQP3tb5kvP4zMHRq_GakLY6aITQioYME0BgW9Fga3bKdWGkgKPHBF62ldfnIfrGX_iwjHPKTej1WmR2f9Fuez96OY4UfbJ39vRtXqrZYNTr5OxYZTptivVlJTQ1BIrSkgPxD526kNGImv1lQiAQPgZTJ4; sid_ucp_sso_v1=1.0.0-KDU1NmQ5MzA0OTNjZWYwZjU2OTZmNDQwMWRlNjFjNmZkNWRjYjZiMWYKHQiu0N-6gQMQx7THnwYY7zEgDDDJlPjbBTgGQPQHGgJobCIgMTY1Y2U5N2MwM2Y5ZTE1YjE1NWU4ZGU4ZGUwNGI5ZWU; ssid_ucp_sso_v1=1.0.0-KDU1NmQ5MzA0OTNjZWYwZjU2OTZmNDQwMWRlNjFjNmZkNWRjYjZiMWYKHQiu0N-6gQMQx7THnwYY7zEgDDDJlPjbBTgGQPQHGgJobCIgMTY1Y2U5N2MwM2Y5ZTE1YjE1NWU4ZGU4ZGUwNGI5ZWU; odin_tt=1afed81799aae892640fbb9961bc07d1c60687ad9933affd1f4d16e20e38fe2a43b35aba8140efcb3782f077b64fb29d7551fbd290824840e47b108dcb051b43; passport_auth_status=cb57b11f6431b8ecf222f86be07b8b27%2C; passport_auth_status_ss=cb57b11f6431b8ecf222f86be07b8b27%2C; uid_tt=a3cc7889881b8ffd537f829b70b0d8b3; uid_tt_ss=a3cc7889881b8ffd537f829b70b0d8b3; sid_tt=a9f083703b247257d5b2d96af7389e5d; sessionid=a9f083703b247257d5b2d96af7389e5d; sessionid_ss=a9f083703b247257d5b2d96af7389e5d; LOGIN_STATUS=1; sid_guard=a9f083703b247257d5b2d96af7389e5d%7C1676794442%7C5183997%7CThu%2C+20-Apr-2023+08%3A13%3A59+GMT; sid_ucp_v1=1.0.0-KDE1MTEyZmMxZDdlMTEyMGIzZTZlZGYyNThlZTdhMWNkZTc3NDgzNjYKGQiu0N-6gQMQyrTHnwYY7zEgDDgGQPQHSAQaAmhsIiBhOWYwODM3MDNiMjQ3MjU3ZDViMmQ5NmFmNzM4OWU1ZA; ssid_ucp_v1=1.0.0-KDE1MTEyZmMxZDdlMTEyMGIzZTZlZGYyNThlZTdhMWNkZTc3NDgzNjYKGQiu0N-6gQMQyrTHnwYY7zEgDDgGQPQHSAQaAmhsIiBhOWYwODM3MDNiMjQ3MjU3ZDViMmQ5NmFmNzM4OWU1ZA; SEARCH_RESULT_LIST_TYPE=%22single%22; __ac_nonce=0640081c1004d0d85da4c; __ac_signature=_02B4Z6wo00f01P2MlGQAAIDB3i26yw0w1Lz9rJDAAFtp3p7ksuv0Mzi4FiKdNCKaVJSWqKQXIqkGoI3TK2bN3oYzE758l-K.Iuxa64bMDK5dF-XFEBJlNxB4IoB2KJA0fth9gD1i0m1tfrN325; device_web_cpu_core=8; device_web_memory_size=8; live_can_add_dy_2_desktop=%220%22; csrf_session_id=9519b013f8bac1bc1e806e424e03119d; strategyABtestKey=%221677754822.323%22; FOLLOW_NUMBER_YELLOW_POINT_INFO=%22MS4wLjABAAAACL8dhqidN7ktadiZUMWbPx35a0jt4OqEqDrnupzj-do%2F1677772800000%2F0%2F1677754822688%2F0%22; home_can_add_dy_2_desktop=%221%22; tt_scid=aVdXC2luI12xo074L9sCDDE-J6FSt0QhP5eWz-njXrFKWRoONmeXxlLQMYDKxX.cbc98; download_guide=%222%2F20230302%22; msToken=eEaBmu8g0WVGhIz_DbuZGEaeNs40TRu5FdzkVYr67O5YZFQ9LNwrk0oywGOeRHHURFvhzlIhqAKPX514BXx5c0AA_5l-cAOpgeKNcf3n_Jlvptyh2dwFZQ==; msToken=zhLV_JXQrYNfv6XrKGAoBDOYxHhLGiv9DDKnXQC4MO_Ii7iROQXTjSuya1R7ag4GIb1yq8ThPJPtbs37t-50lMKgB5zvIHO7W09Y-XmBGl02vXGqTlI_ww==; passport_fe_beating_status=false; ttwid=1%7C5O6R7vMD10lsy0muNZkU9aN9tHjaRUBg6iDG7hHfvow%7C1669213215%7Cd3cc5388e622a3f97498f7b70fa5ed0007342a7f335fd350fb2b8a0b5290980f")
                    .addHeader("sec-ch-ua", "\"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("x-secsdk-csrf-token", "000100000001ca7f031a53cecfdfe77348f71bf760ffc8a3beda6b369d718cecf149f82c8981174895b0031a2574")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://live.douyin.com/168465302284")
                    .addHeader("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6")
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
            pb.command("ffmpeg", "-i", streamUrl, "-c", "copy", "-y", System.getProperty("user.dir")+"/data/"+filename);

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
            File file=new File(System.getProperty("user.dir")+"/data/"+filename);
            while (!file.exists()){
                logger.info("文件不存在,可能正在生成....请等待");
                TimeUnit.SECONDS.sleep(1);
            }
            TimeUnit.SECONDS.sleep(3);
            Runtime.getRuntime().exec("killall ffmpeg");
            TimeUnit.SECONDS.sleep(1);
            file.renameTo(new File(System.getProperty("user.dir")+"/data/"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4"));
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
