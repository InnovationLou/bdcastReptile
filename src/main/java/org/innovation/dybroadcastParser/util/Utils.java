package org.innovation.dybroadcastParser.util;

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
                    .setHeadless(false)
                    .setDevtools(false)
                )
            ) {
                Page page = browser.newPage();
                page.onResponse(response -> {
                    if (response.url().contains(info.getUserUrl())){
                        String body= response.text();
                        if (body.contains("直播中")){
                            info.setLiveStatus("1");
                        }
                    }
                });
                page.navigate(info.getUserUrl());
                //等待NetworkIdle
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.pause();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

}
