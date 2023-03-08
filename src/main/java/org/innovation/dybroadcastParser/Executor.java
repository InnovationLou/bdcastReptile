package org.innovation.dybroadcastParser;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.catcher.BaseInfoCatcher;
import org.innovation.dybroadcastParser.catcher.ProductCatcher;
import org.innovation.dybroadcastParser.catcher.StreamDownloader;
import org.innovation.dybroadcastParser.catcher.WssCatcher;
import org.innovation.dybroadcastParser.util.Utils;
import org.innovation.dybroadcastParser.vo.BaseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executor {
    private static Logger logger=Logger.getLogger(Executor.class);

    /**
     * 第一次执行Playwright会自动下载浏览器，耗时较长
     * @param args
     */
    public static void main(String[] args) {
        try {
            //输入
            // 设置日志路径${log.base}为当前项目
            System.setProperty("log.base", System.getProperty("user.dir"));
            //read from csv
            CsvReader csvReader= CsvUtil.getReader();
            //从文件中读取CSV数据
            final List<BaseInfo> list = csvReader.read(
                    ResourceUtil.getUtf8Reader(System.getProperty("user.dir")+"\\data\\input\\list.csv"), BaseInfo.class);
            //一个开播状态的人要占用4个线程
//            int eachPoolSize=4;
//            int onLiveNum=0;
//            List<BaseInfo> onLiveList=new ArrayList<>();
//            for (BaseInfo info:list){
//                //获取开播状态 roomid authorid
//                Utils.getStatus(info);
//                if (null==info.getLiveStatus()){
//                    logger.info("not onlive:"+info.getLiveName());
//                    continue;
//                }else if (info.getLiveStatus().equals("1")){
//                    onLiveNum++;
//                    onLiveList.add(info);
//                    logger.info("onlive:"+info.getLiveName());
//                }
//            }
//            if (onLiveNum==0){
//                logger.info("监控列表没有开播的人");
//                return;
//            }
            //创建线程池
            BlockingQueue queue=new java.util.concurrent.LinkedBlockingQueue();
            ThreadPoolExecutor executor=new ThreadPoolExecutor(1*list.size(),1*list.size(),0,java.util.concurrent.TimeUnit.SECONDS,queue);

            //循环创建任务
            for (BaseInfo info:list){
//                //获取wss
//                WssCatcher wssCatcher=new WssCatcher(info);
//                executor.execute(wssCatcher);
//                //获取产品信息
//                ProductCatcher productCatcher=new ProductCatcher(info);
//                executor.execute(productCatcher);
//                //获取基本信息
//                BaseInfoCatcher baseInfoCatcher=new BaseInfoCatcher(info);
//                executor.execute(baseInfoCatcher);
                //下载直播流
                StreamDownloader downloadCatcher=new StreamDownloader(info);
                executor.execute(downloadCatcher);
            }

            //主线程休眠
            TimeUnit.MINUTES.sleep(2);
            //设置中断线程
            BaseInfoCatcher.isInterrupt=true;
            ProductCatcher.isInterrupt=true;
            WssCatcher.isInterrupt=true;
            StreamDownloader.isInterrupt=true;
            //关闭线程池
            executor.shutdown();
            logger.info("Main Thread Exiting...");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
