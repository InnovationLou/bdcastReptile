package org.innovation.dybroadcastParser.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;

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
    public static void getStatus(BaseInfo info) throws IOException {
        //截取字符串获取
        info.setLiveId(info.getLiveUrl().split("/")[3]);
        info.setSecAuthorId(info.getUserUrl().split("/")[4]);
        //模拟浏览器获取
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setDevtools(false)
                )
            ) {
                Page page = browser.newPage();

                page.onRequest(request -> {
                    if (request.url().contains("mcs.zijieapi.com/list")){
                        if (info.getAuthorId()==null){
                            String requestContext=request.postData();
                            //去掉首尾各1个字符
                            requestContext=requestContext.substring(1,requestContext.length()-1);
                            JSONObject jsonObject= JSON.parseObject(requestContext);
                            JSONArray events = jsonObject.getJSONArray("events");
                            //events
                            for (int i = 0; i < events.size(); i++) {
                                JSONObject event = events.getJSONObject(i);
                                if (event.getString("event").equals("video_cover_show")){
                                    String authorId = JSON.parseObject(event.getString("params")).getString("author_id");
                                    info.setAuthorId(authorId);
                                    break;
                                }
                            }
                        }
                    }
                });
                //当出现请求https://www.douyin.com/aweme/v1/web/user/profile/other/
                page.onResponse(response -> {
                    //获取roomid liveStatus
                    if (response.url().contains("aweme/v1/web/user/profile/other/")){
                        String body= response.text();
                        JSONObject jsonObject= JSON.parseObject(body);
                        JSONObject data=jsonObject.getJSONObject("user");
                        String liveStatus=data.getString("live_status");
                        String roomId=data.getString("room_id");
                        info.setRoomId(roomId);
                        info.setLiveStatus(liveStatus);
                    }
                });
                page.navigate(info.getUserUrl());
                Thread.sleep(1000);
                //等待NetworkIdle
                page.waitForLoadState(LoadState.NETWORKIDLE);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

}
