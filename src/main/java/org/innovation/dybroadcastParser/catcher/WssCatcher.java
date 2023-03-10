package org.innovation.dybroadcastParser.catcher;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.proto.DanmuvoWSS;
import org.innovation.dybroadcastParser.proto.WSS;
import org.innovation.dybroadcastParser.util.Utils;
import org.innovation.dybroadcastParser.vo.BaseInfo;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class WssCatcher implements Runnable{

    private BaseInfo info;

    private static Logger logger=Logger.getLogger(WssCatcher.class);
    //线程中断 临界变量
    public static volatile boolean isInterrupt = false;

    public WssCatcher(BaseInfo info) {
        //浅拷贝???
//        this.info=info;
        //深拷贝
        this.info = new BaseInfo(info.getLiveUrl(), info.getLiveId(), info.getRoomId(), info.getLiveName(), info.getUserUrl(),info.getAuthorId(),info.getSecAuthorId());
    }

    public void getWss(String liveUrl, String liveId, String roomId, String liveName, String userUrl) {
        //指定路径和编码
        CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\Wss-output" + liveName
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))+".csv", CharsetUtil.CHARSET_GBK);
        //按行写出
        writer.writeHeaderLine("时间戳","消息类型","用户名","用户id","内容","总点赞","用户单次点赞","礼物Id","礼物描述","礼物数量","在线观众总数","直播间id","主播名字");
        AtomicReference<String> streamUrl= new AtomicReference<>("");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
//                            .setHeadless(false) //取消无头模式，才能看见浏览器操作
//                            .setSlowMo(100) //减慢执行速度，以免太快
                            .setDevtools(false)); //打开浏览器开发者工具，默认不打开

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
                    new Thread(() -> {
                        try {
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
                            file.renameTo(new File(System.getProperty("user.dir")+"/data/"+liveName+"_"+timestampToChar(startTime)+"_"+timestampToChar(endtime)+".mp4"));
                            logger.info("直播流下载完毕"+liveName+timestampToChar(endtime));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }, "ffmpegThread").start();

                }
            });

            Map<String, String> map = new HashMap<>();
            //监听websocket
            page.onWebSocket(new Consumer<WebSocket>() {
                @Override
                public void accept(WebSocket webSocket) {
                    webSocket.onFrameReceived(new Consumer<WebSocketFrame>() {
                        @Override
                        public void accept(WebSocketFrame webSocketFrame) {
                            final byte[] data = webSocketFrame.binary();
                            try {
                                //最外层解析 try catch 不指定ws路径也可以，解析失败不会崩溃
                                //proto解析外层
                                WSS.WssResponse wss = WSS.WssResponse.parseFrom(data);
                                //GZIP解压data数据
                                final byte[] uncompress = Utils.uncompress(wss.getData());
                                //解析data
                                DanmuvoWSS.Response response = DanmuvoWSS.Response.parseFrom(uncompress);
                                final List<DanmuvoWSS.Message> messagesList = response.getMessagesList();
                                //根据message区分message类型解析
                                messagesList.forEach(item -> {
//                                    logger.info("wss income:"+item.getMethod());
                                    //WebcastChatMessage
                                    if ("WebcastChatMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.ChatMessage chatMessage = DanmuvoWSS.ChatMessage.parseFrom(item.getPayload());
                                            synchronized (this){
                                                writer.write(new String[]{LocalDateTime.now().toString(),"WebcastChatMessage", chatMessage.getUser().getNickname(),
                                                        String.valueOf(chatMessage.getUser().getId()), chatMessage.getContent(), "", "", "", "", "", "",liveId,liveName});
                                            }
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastLikeMessage
                                    if ("WebcastLikeMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.LikeMessage likeMessage = DanmuvoWSS.LikeMessage.parseFrom(item.getPayload());
                                            synchronized (this){
                                                writer.write(new String[]{LocalDateTime.now().toString(),"WebcastLikeMessage",
                                                        likeMessage.getUser().getNickname(), String.valueOf(likeMessage.getUser().getId()), "", String.valueOf(likeMessage.getTotal()), String.valueOf(likeMessage.getCount())
                                                        , "", "", "", "",liveId,liveName});
                                            }
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastGiftMessage
                                    if ("WebcastGiftMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.GiftMessage giftMessage = DanmuvoWSS.GiftMessage.parseFrom(item.getPayload());
                                            synchronized (this){
                                                writer.write(new String[]{LocalDateTime.now().toString(),"WebcastGiftMessage",
                                                        giftMessage.getUser().getNickname(), String.valueOf(giftMessage.getUser().getId()), "", "", "", String.valueOf(giftMessage.getGift().getId()),
                                                        giftMessage.getGift().getDescribe(), String.valueOf(giftMessage.getComboCount()), "",liveId,liveName});

                                            }
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastMemberMessage
                                    if ("WebcastMemberMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.MemberMessage memberMessage = DanmuvoWSS.MemberMessage.parseFrom(item.getPayload());
                                            synchronized (this){
                                                writer.write(new String[]{LocalDateTime.now().toString(),"WebcastMemberMessage",
                                                        memberMessage.getUser().getNickname(), String.valueOf(memberMessage.getUser().getId()), "", "", "", "", "", ""
                                                        , String.valueOf(memberMessage.getMemberCount()),liveId,liveName});

                                            }
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastRoomStatsMessage
                                    if ("WebcastRoomStatsMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.RoomStatsMessage roomStatsMessage = DanmuvoWSS.RoomStatsMessage.parseFrom(item.getPayload());
                                            synchronized (this){
                                                writer.write(new String[]{LocalDateTime.now().toString(),"WebcastRoomStatsMessage",
                                                        "", "", String.valueOf(roomStatsMessage.getRoomId()), "", "", "", "", ""
                                                        , String.valueOf(roomStatsMessage.getLiveWatchUcnt()),liveId,liveName});
                                            }
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }

                                });
                            } catch (InvalidProtocolBufferException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            //下载直播流
//            page.onResponse(new Consumer<Response>() {
//                @Override
//                public void accept(Response response) {
//                    if (response.url().matches("https://pull-flv-.*.douyincdn.com/third/stream-.*.flv?.*")) {
//                        System.out.println("accept flv:" + response.url());
//                        try {
//                            HttpUtil.download(response.url(), new FileOutputStream("D:\\Users\\Innovation\\IdeaProjects\\dybroadcastParser\\src\\main\\resources\\data\\"+LocalDateTime.now().toString()+".flv"), true);
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            });

            //访问页面
            page.navigate(liveUrl);
            page.waitForLoadState(LoadState.NETWORKIDLE);

            //不知为啥 必须点一下播放才能获取流信息
//            page.click("text=播放 刷新 >> path");
            //等待页面完全加载
//            page.waitForLoadState(LoadState.NETWORKIDLE);
            //设置视频静音
//            page.evaluate("document.querySelector('video').muted = true");
            while (!isInterrupt){
                page.content();
                Thread.sleep(2000);
            }
            page.close();
            browser.close();
            playwright.close();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writer.close();
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
        getWss(this.info.getLiveUrl(),this.info.getRoomId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl());
    }
}
