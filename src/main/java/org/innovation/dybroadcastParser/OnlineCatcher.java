package org.innovation.dybroadcastParser;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microsoft.playwright.*;
import org.innovation.dybroadcastParser.proto.DanmuvoWSS;
import org.innovation.dybroadcastParser.proto.WSS;
import org.innovation.dybroadcastParser.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class OnlineCatcher {

    /**
     * 中断标志
     */
    public static final boolean stop= false;

    /**
     * PC端直播间地址(改为批量输入)
     */
    public static final String url="https://live.douyin.com/80017709309";

    public static void main(String[] args) {
        //获取直播间地址
//        Scanner scanner= new Scanner(System.in);
//        String url=scanner.nextLine();
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch();
            Page page = browser.newPage();
//                page.emulateMedia(null);
            Map<String, String> map = new HashMap<>();
            Consumer<Request> listener = request -> {
//                    if (request.url().startsWith("https://live.douyin.com/webcast/im/fetch/?")) {
            };
            //监听websocket
            page.onWebSocket(new Consumer<WebSocket>() {
                @Override
                public void accept(WebSocket webSocket) {
                    webSocket.onFrameReceived(new Consumer<WebSocketFrame>() {
                        @Override
                        public void accept(WebSocketFrame webSocketFrame) {
                            final byte[] data = webSocketFrame.binary();
                            System.out.println("accept wws.....");
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
                                messagesList.forEach(item->{
                                    if ("WebcastChatMessage".equals(item.getMethod())) {
                                        try {
                                            DanmuvoWSS.ChatMessage chatMessage = DanmuvoWSS.ChatMessage.parseFrom(item.getPayload());
                                            System.out.println("[弹幕消息]" + chatMessage.getUser().getNickname() + "(" + chatMessage.getUser().getId() + ")" + "说：" + chatMessage.getContent()  +"\n");
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

            page.onRequestFinished(listener);
//                page.navigate("https://live.douyin.com/720911644621");
            //访问页面
            page.navigate(url);

            while (true) {
                try {
                    //循环访问激活页面
                    page.content();
                    if (stop) {
                        browser.close();
                        break;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    browser.close();
                }
            }

        }
    }
}
