package org.innovation.dybroadcastParser.catcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        AtomicReference<String> streamUrl= new AtomicReference<>("");
        //模拟浏览器获取
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setDevtools(false)
            )
            ) {

                Page page = browser.newPage();

                page.onResponse(response -> {
                    if (response.url().contains("live.douyin.com/webcast/room/web/enter")){
                        String body= response.text();
                        JSONObject jsonObject= JSON.parseObject(body);
                        streamUrl.set(jsonObject.getJSONObject("data")
                                .getJSONArray("data")
                                .getJSONObject(0)
                                .getJSONObject("stream_url")//未开播时这里为空
                                .getJSONObject("flv_pull_url")
                                .getString("SD2"));
                        System.out.println(streamUrl.get());
                    }
                });

                page.navigate(liveUrl);
                //等待NetworkIdle
                page.waitForLoadState(LoadState.NETWORKIDLE);

                Long startTime=System.currentTimeMillis();
                String filename=liveName+"_"+startTime+".mp4";
                //使用ffmpeg下载streamUrl到本地
                ProcessBuilder pb = new ProcessBuilder()
                        .redirectErrorStream(true)
                        .redirectInput(ProcessBuilder.Redirect.PIPE)
                        .redirectOutput(ProcessBuilder.Redirect.INHERIT);
                //下载原画视频
                pb.command("ffmpeg", "-i", streamUrl.get(), "-c", "copy", "-y", System.getProperty("user.dir")+"/data/"+filename);
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
                Runtime.getRuntime().exec("taskkill /F /IM ffmpeg.exe");
                TimeUnit.SECONDS.sleep(1);
                file.renameTo(new File("Z:\\share\\zxf\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4"));
//            //分离音频ffmpeg -i a.mp4 -acodec copy -vn r2.mp3
//            pb.command("ffmpeg", "-i", System.getProperty("user.dir")+"\\data\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4", "-acodec", "copy", "-vn", System.getProperty("user.dir")+"\\data\\"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp3");
//            pb.start();
                page.close();
                browser.close();
                playwright.close();
                logger.info("直播流下载完毕"+liveName+timestampToChar(endtime));
            } catch (Exception e) {
                logger.error(e);
            }
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
