package org.innovation.dybroadcastParser;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.catcher.BaseInfoCatcher;
import org.innovation.dybroadcastParser.catcher.ProductCatcher;
import org.innovation.dybroadcastParser.catcher.WssCatcher;
import org.innovation.dybroadcastParser.vo.BaseInfoResultBean;
import org.innovation.dybroadcastParser.vo.ProductResultBean;
import org.innovation.dybroadcastParser.vo.WssResultBean;

public class Executor {

    private static Logger logger=Logger.getLogger(Executor.class);

    /**
     * 第一次执行Playwright会自动下载浏览器，耗时较长
     * @param args
     */
    public static void main(String[] args) {
        try {
            //输入

            //东方甄选Demo
            String liveUrl="https://live.douyin.com/80017709309";
            String liveId="80017709309";
            String roomId="7167876918040103710";
            String liveName="东方甄选";
            String userUrl="https://www.douyin.com/user/MS4wLjABAAAAcud_llwUN1kpfpzeb3Xqbq8nsRwU7lxVzg3OSv31hNMPz95UspEw1L53dX-UDrE4";

            //执行
//            ProductCatcher.testProduct();
//            BaseInfoCatcher.testBaseInfo();
//            WssCatcher.testWss();
            ProductCatcher.getProduct(liveUrl,liveId,roomId,liveName,userUrl);
//            BaseInfoCatcher.getBaseInfo(liveUrl,liveId,roomId,liveName,userUrl);
//            WssCatcher.getWss(liveUrl,liveId,roomId,liveName,userUrl);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
