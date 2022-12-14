package org.innovation.dybroadcastParser.catcher;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.proto.DanmuvoWSS;
import org.innovation.dybroadcastParser.proto.WSS;
import org.innovation.dybroadcastParser.util.Utils;
import org.innovation.dybroadcastParser.vo.WssResultBean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WssCatcher {

    private static Logger logger=Logger.getLogger(WssCatcher.class);

    /**
     * 中断标志
     */
    public static final boolean stop = false;

    /**
     * PC端直播间地址(改为批量输入)
     */
    public static final String url = "https://live.douyin.com/80017709309";

    //线程中断 临界变量
    public static volatile boolean isInterrupt = false;

    public static void testWss() {
        //获取直播间地址
//        Scanner scanner= new Scanner(System.in);
//        String url=scanner.nextLine();
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(false)
//                            .setHeadless(false) //取消无头模式，才能看见浏览器操作
//                            .setSlowMo(100) //减慢执行速度，以免太快
                            .setDevtools(true)); //打开浏览器开发者工具，默认不打开

            Page page = browser.newPage();
//                page.emulateMedia(null);
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
//                                System.out.println(wss);
                                //GZIP解压data数据
                                final byte[] uncompress = Utils.uncompress(wss.getData());
                                //解析data
                                DanmuvoWSS.Response response = DanmuvoWSS.Response.parseFrom(uncompress);
                                final List<DanmuvoWSS.Message> messagesList = response.getMessagesList();
//                                System.out.println(messagesList);
                                //根据message区分message类型解析
                                messagesList.forEach(item -> {
                                    //WebcastChatMessage
                                    if ("WebcastChatMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.ChatMessage chatMessage = DanmuvoWSS.ChatMessage.parseFrom(item.getPayload());
                                            System.out.println("[弹幕消息]" + chatMessage.getUser().getNickname() + "(" + chatMessage.getUser().getId() + ")" + "说：" + chatMessage.getContent()  +"\n");
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastLikeMessage
                                    if ("WebcastLikeMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.LikeMessage likeMessage = DanmuvoWSS.LikeMessage.parseFrom(item.getPayload());
                                            System.out.println("[点赞消息]" + likeMessage.getUser().getNickname() + "(" + likeMessage.getUser().getId() + ")" + "点赞了" + likeMessage.getCount() + "次" +"\n");
                                            System.out.println(likeMessage.getTotal());
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastGiftMessage
                                    if ("WebcastGiftMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.GiftMessage giftMessage = DanmuvoWSS.GiftMessage.parseFrom(item.getPayload());
                                            System.out.println("[礼物消息]" + giftMessage.getUser().getNickname() + "(" + giftMessage.getUser().getId() + ")" + "送了" + giftMessage.getGift().getDescribe() + "x" + giftMessage.getComboCount() + "\n");
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastMemberMessage
                                    if ("WebcastMemberMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.MemberMessage memberMessage = DanmuvoWSS.MemberMessage.parseFrom(item.getPayload());
                                            System.out.println("[成员消息]" + memberMessage.getUser().getNickname() + "(" + memberMessage.getUser().getId() + ")" + "加入了直播间" + "\n");
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastRoomStatsMessage
                                    if ("WebcastRoomStatsMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.RoomStatsMessage roomStatsMessage = DanmuvoWSS.RoomStatsMessage.parseFrom(item.getPayload());
                                            System.out.println("[直播间统计消息]"+roomStatsMessage.getRoomId()+ "当前直播间人数：" + roomStatsMessage.getLiveWatchUcnt() +"\n");
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
//                            HttpUtil.download(response.url(), new FileOutputStream("D:\\Users\\Innovation\\IdeaProjects\\dybroadcastParser\\src\\main\\resources\\data\\test.flv"), true);
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            });

            //访问页面
            page.navigate(url);
            //不知为啥 必须点一下播放才能获取流信息
//            page.click("text=播放 刷新 >> path");
            //等待页面完全加载
            page.waitForLoadState(LoadState.NETWORKIDLE);
            //设置视频静音
            page.evaluate("document.querySelector('video').muted = true");
            while (!isInterrupt) {
                try {
                    //循环访问激活页面
                    page.content();

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    public static void getWss(String liveUrl, String liveId, String roomId, String liveName, String userUrl) {
        //指定路径和编码
        CsvWriter writer = CsvUtil.getWriter("D:\\Users\\Innovation\\IdeaProjects\\dybroadcastParser\\src\\main\\resources\\data\\Wss-output"+LocalDateTime.now().toString()+".csv", CharsetUtil.CHARSET_GBK);
        //按行写出
        writer.writeHeaderLine("时间戳","消息类型","用户名","用户id","内容","总点赞","用户单次点赞","礼物Id","礼物描述","礼物数量","在线观众总数");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
//                            .setHeadless(false) //取消无头模式，才能看见浏览器操作
//                            .setSlowMo(100) //减慢执行速度，以免太快
                            .setDevtools(false)); //打开浏览器开发者工具，默认不打开

            Page page = browser.newPage();
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
                                    //WebcastChatMessage
                                    if ("WebcastChatMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.ChatMessage chatMessage = DanmuvoWSS.ChatMessage.parseFrom(item.getPayload());
                                            writer.write(new String[]{LocalDateTime.now().toString(),"WebcastChatMessage", chatMessage.getUser().getNickname(),
                                                    String.valueOf(chatMessage.getUser().getId()), chatMessage.getContent(), "", "", "", "", "", ""});
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastLikeMessage
                                    if ("WebcastLikeMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.LikeMessage likeMessage = DanmuvoWSS.LikeMessage.parseFrom(item.getPayload());
                                            writer.write(new String[]{LocalDateTime.now().toString(),"WebcastLikeMessage",
                                                    likeMessage.getUser().getNickname(), String.valueOf(likeMessage.getUser().getId()), "", String.valueOf(likeMessage.getTotal()), String.valueOf(likeMessage.getCount()), "", "", "", ""});
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastGiftMessage
                                    if ("WebcastGiftMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.GiftMessage giftMessage = DanmuvoWSS.GiftMessage.parseFrom(item.getPayload());
                                            writer.write(new String[]{LocalDateTime.now().toString(),"WebcastGiftMessage",
                                                    giftMessage.getUser().getNickname(), String.valueOf(giftMessage.getUser().getId()), "", "", "", String.valueOf(giftMessage.getGift().getId()),
                                                    giftMessage.getGift().getDescribe(), String.valueOf(giftMessage.getComboCount()), ""});

                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastMemberMessage
                                    if ("WebcastMemberMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.MemberMessage memberMessage = DanmuvoWSS.MemberMessage.parseFrom(item.getPayload());
                                            writer.write(new String[]{LocalDateTime.now().toString(),"WebcastMemberMessage",
                                                    memberMessage.getUser().getNickname(), String.valueOf(memberMessage.getUser().getId()), "", "", "", "", "", "", String.valueOf(memberMessage.getMemberCount())});

                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }
                                    //WebcastRoomStatsMessage
                                    if ("WebcastRoomStatsMessage".equals(item.getMethod())){
                                        try {
                                            DanmuvoWSS.RoomStatsMessage roomStatsMessage = DanmuvoWSS.RoomStatsMessage.parseFrom(item.getPayload());
                                            writer.write(new String[]{LocalDateTime.now().toString(),"WebcastRoomStatsMessage",
                                                    "", "", String.valueOf(roomStatsMessage.getRoomId()), "", "", "", "", "", String.valueOf(roomStatsMessage.getLiveWatchUcnt())});

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
            page.onResponse(new Consumer<Response>() {
                @Override
                public void accept(Response response) {
                    if (response.url().matches("https://pull-flv-.*.douyincdn.com/third/stream-.*.flv?.*")) {
                        System.out.println("accept flv:" + response.url());
                        try {
                            HttpUtil.download(response.url(), new FileOutputStream("D:\\Users\\Innovation\\IdeaProjects\\dybroadcastParser\\src\\main\\resources\\data\\"+LocalDateTime.now().toString()+".flv"), true);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            //访问页面
            page.navigate(liveUrl);
            //不知为啥 必须点一下播放才能获取流信息
//            page.click("text=播放 刷新 >> path");
            //等待页面完全加载
            page.waitForLoadState(LoadState.NETWORKIDLE);
            //设置视频静音
            page.evaluate("document.querySelector('video').muted = true");
            while (!isInterrupt) {
                try {
                    //循环访问激活页面
                    page.content();

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            page.close();
            browser.close();
            playwright.close();
            writer.close();
            logger.info("WssCatcher End...");
        }catch (Exception e){
            logger.error("WSSError",e);
        }finally {
            writer.close();
        }
    }
}
