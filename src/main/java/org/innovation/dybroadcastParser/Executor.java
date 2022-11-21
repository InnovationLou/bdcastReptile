package org.innovation.dybroadcastParser;

import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.catcher.BaseInfoCatcher;
import org.innovation.dybroadcastParser.catcher.ProductCatcher;
import org.innovation.dybroadcastParser.catcher.WssCatcher;

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

            //东方甄选Demo
            String liveUrl="https://live.douyin.com/80017709309";
            String liveId="80017709309";
            String roomId="7167876918040103710";
            String liveName="东方甄选";
            String userUrl="https://www.douyin.com/user/MS4wLjABAAAAcud_llwUN1kpfpzeb3Xqbq8nsRwU7lxVzg3OSv31hNMPz95UspEw1L53dX-UDrE4";

            //交个朋友Demo
//            String liveUrl="https://live.douyin.com/168465302284";
//            String liveId="168465302284";
//            String roomId="7168003384478042917";
//            String liveName="交个朋友";
//            String userUrl="https://www.douyin.com/user/MS4wLjABAAAAlwXCzzm7SmBfdZAsqQ_wVVUbpTvUSX1WC_x8HAjMa3gLb88-MwKL7s4OqlYntX4r";

            //执行
//            ProductCatcher.testProduct();
//            BaseInfoCatcher.testBaseInfo();
//            WssCatcher.testWss();
            //创建线程池
            BlockingQueue queue=new java.util.concurrent.LinkedBlockingQueue();
            ThreadPoolExecutor executor=new ThreadPoolExecutor(3,3,0,java.util.concurrent.TimeUnit.SECONDS,queue);

            //创建任务
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProductCatcher.getProduct(liveUrl,liveId,roomId,liveName,userUrl);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            });
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BaseInfoCatcher.getBaseInfo(liveUrl,liveId,roomId,liveName,userUrl);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            });
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        WssCatcher.getWss(liveUrl,liveId,roomId,liveName,userUrl);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            });

//            ProductCatcher.getProduct(liveUrl,liveId,roomId,liveName,userUrl);
//            BaseInfoCatcher.getBaseInfo(liveUrl,liveId,roomId,liveName,userUrl);
//            WssCatcher.getWss(liveUrl,liveId,roomId,liveName,userUrl);
            //主线程休眠
            TimeUnit.MINUTES.sleep(5);
            //设置中断线程
            BaseInfoCatcher.isInterrupt=true;
            ProductCatcher.isInterrupt=true;
            WssCatcher.isInterrupt=true;
            //关闭线程池
            executor.shutdown();
            logger.info("Main Thread Exiting...");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
