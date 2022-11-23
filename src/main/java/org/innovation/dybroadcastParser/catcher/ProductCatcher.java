package org.innovation.dybroadcastParser.catcher;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;
import org.innovation.dybroadcastParser.vo.ProductResultBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductCatcher implements Runnable{

    private static Logger logger=Logger.getLogger(ProductCatcher.class);

    //线程中断 临界变量
    public static volatile boolean isInterrupt = false;

    private BaseInfo info;

    public ProductCatcher(BaseInfo info) {
        this.info = new BaseInfo(info.getLiveUrl(), info.getLiveId(), info.getRoomId(), info.getLiveName(), info.getUserUrl());
    }


    public static void getProduct(String liveUrl, String liveId, String roomId, String liveName, String userUrl) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        //指定路径和编码
        CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\Product-output"
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss"))+".csv", CharsetUtil.CHARSET_GBK);
        //按行写出
        writer.writeHeaderLine("时间戳","productId","promotionId","商品标题","商品活动价格","商品常规价格","商品销量","直播间id","主播名称");
        try{
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?ecom_sdk_version=2580&author_id=2384194153219051&sec_author_id=MS4wLjABAAAAcud_llwUN1kpfpzeb3Xqbq8nsRwU7lxVzg3OSv31hNMPz95UspEw1L53dX-UDrE4&" +
                            "room_id="+roomId+"&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1668829732299&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1668829732&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
//                .method("GET" ,body) //method GET must not have a request body
                    .addHeader("Host", "lianmengapi.snssdk.com")
                    .addHeader("Cookie", "install_id=2159885451009367; ttreq=1$3467ff2806f428be9e6a32cad42a356cc2179a5d; odin_tt=0bf1c3e90a06bfe67e44c475dbcc02bbaac2c3baeac6141878ab5080015f7e33ee337cce5bb04b007cc1c45c10a332f19962f51dbe75eeb5edf67766eb760e268e8647659a296c1f7053125dce632503; passport_csrf_token=3130e223ac037a55a731da09b022ce20; passport_csrf_token_default=3130e223ac037a55a731da09b022ce20")
                    .addHeader("x-tt-dt", "AAAZCTKTD4DCH25IGMIZDDLQ6F6MNNG6RRK5FSDPMEA65AISV2VIDFMFCBUVAXT4374WGLISTMS4RPQS7UDRKWNS72J42RDY3MIHGOF6JBPSPINTKUKRMRDZJHQ4RZVQ5XVQKQISKAVKYMFY6WYYGBQ")
                    .addHeader("activity_now_client", "1668829733067")
                    .addHeader("x-ss-req-ticket", "1668829732304")
                    .addHeader("passport-sdk-version", "20372")
                    .addHeader("sdk-version", "2")
                    .addHeader("x-vc-bdturing-sdk-version", "2.2.1.cn")
                    .addHeader("user-agent", "com.ss.android.ugc.aweme/210601 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.10.0.2)")
                    .addHeader("x-ladon", "dkOpITiLqtis9qeShwTQuFNZoaMJp2zB2lMsI2DvHTrpYbrJ")
                    .addHeader("x-gorgon", "0404e05c0000f8dedfd0acdd09415225f3d5bcb6dedece8dfd28")
                    .addHeader("x-khronos", "1668829732")
                    .addHeader("x-argus", "K/jqaWVXaZ++aYi9V1ltkev7+l3YIZof7whGyAr/HymHF486QJqZY77LTcJqrt9o/GBL1hJHFKKPCg2/CdcMHSOrZ4plQxER2sIzLOzaCGe63ujGMuvw0IAUHL7OxhHqia6ipGmLKftPmaHcssHQloWoVmKowtKep8b6iFwYpMgz5tf99wKQgxHJXObvWohJsV/7o5QiBADHANtyIdKPEboWS+mOHLVysYppsqCTZHX3BpOgeEfo9qG1QaAWFZbK+8hhgNYt5EKYmOMZLr5NmCqLmG7ydEsYrtqS7od87JxGQg==")
                    .build();
            Response response;
            ProductResultBean bean=new ProductResultBean();
            //每10秒发送一次请求
            while (!isInterrupt){
                response = client.newCall(request).execute();
                while (response.code() != 200) {
                    logger.error("Product请求失败，正在重试");
                    response = client.newCall(request).execute();
                }
//                assert response.body() != null;
                String result=response.body().string();
                System.out.println(result);
                JSONObject object=JSON.parseObject(result);
                if (null==object || null==object.getJSONArray("promotions")             //有概率不获得商品信息
                        ||null==object.getJSONArray("promotions").getJSONObject(0)
                        || object.getJSONArray("promotions").getJSONObject(0).size()==0) {
                    continue;
                }
                //product_id
                String productId=object.getJSONArray("promotions").getJSONObject(0).getString("product_id");
                //promotion_id
                String promotionId=object.getJSONArray("promotions").getJSONObject(0).getString("promotion_id");
                //商品标题
                String title = object.getJSONArray("promotions").getJSONObject(0).getString("title");
                //商品单价-当前活动价格
                Double price=object.getJSONArray("promotions").getJSONObject(0).getDouble("min_price");
                //商品单价-当前活动价格
                Double regularPrice=object.getJSONArray("promotions").getJSONObject(0).getDouble("regular_price");
                //商品销量
                Long sale=object.getJSONArray("promotions").getJSONObject(0).getJSONObject("hot_atmosphere").getLong("sale_num");

                //LocalDateTime
                bean.setTime(LocalDateTime.now());
                bean.setProductId(productId);
                bean.setPromotionId(promotionId);
                bean.setTitle(title);
                bean.setPrice(price/100.0f);
                bean.setRegularPrice(regularPrice/100.0f);
                bean.setSaleNum(sale);
                //写入bean到csv
                String[] content = {bean.getTime().toString(),bean.getProductId(),bean.getPromotionId(),bean.getTitle(),String.valueOf(bean.getPrice()),String.valueOf(bean.getRegularPrice()),String.valueOf(sale)};
                writer.write(content);
                logger.info("Product请求成功");
                Thread.sleep(10000);
            }
            logger.info("ProductCatcher end....");
        }catch (Exception e){
            logger.error("获取商品信息失败",e);
        }finally {
            writer.close();
        }

    }

    @Override
    public void run() {
        getProduct(this.info.getLiveUrl(),this.info.getRoomId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl());
    }
}
