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
        this.info = new BaseInfo(info.getLiveUrl(), info.getLiveId(), info.getRoomId(), info.getLiveName(), info.getUserUrl(),info.getAuthorId(),info.getSecAuthorId());
    }


    public static void getProduct(String liveUrl, String liveId, String roomId, String liveName, String userUrl,String authorId,String secAuthorId) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        //指定路径和编码
        CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\Product-output" + liveName
                +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))+".csv", CharsetUtil.CHARSET_GBK);
        //按行写出
        writer.writeHeaderLine("时间戳","productId","promotionId","商品标题","商品活动价格","商品常规价格","商品销量","直播间id","主播名称");
        try{
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
//            Request request = new Request.Builder()
//                    .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?author_id=" + authorId +
//                            "&sec_author_id=" + secAuthorId +
//                            "&room_id="+roomId+"&op_type=3&use_new_price=1&aid=1128&entrance_info=%257B%2522search_id%2522%253A%252220221123211406010208037089210D9D69%2522%252C%2522request_id%2522%253A%252220221123211406010208037089210D9D69%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%25222384194153219051%2522%252C%2522follow_status%2522%253A%25221%2522%252C%2522device_id%2522%253A%2522493005801012839%2522%252C%2522sdk_version%2522%253A%25222700%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227168990216811121438%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522pipeline_version%255C%255C%255C%2522%253A1%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25222384194153219051%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%252220221123211406010208037089210D9D69%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25227168990359320612127%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%252220221123211406010208037089210D9D69%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2353398884097752&device_id=493005801012839&ac=wifi&channel=aweGW&aid=1128&app_name=aweme&version_code=220900&version_name=22.9.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&manifest_version_code=220901&resolution=720*1280&dpi=240&update_version_code=22909900&_rticket=1669209284980&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1669209283&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=cb2b840d-7ec8-4687-b673-1e0c91534e80&md=0")
////                .method("GET" ,body) //method GET must not have a request body
//                    .addHeader("Host", "lianmengapi.snssdk.com")
//                    .addHeader("Cookie", "passport_csrf_token=64ca82ff9b8e73db6fb74c6a57d28d14; passport_csrf_token_default=64ca82ff9b8e73db6fb74c6a57d28d14; d_ticket=df26923499a3f424b39cb0fca9bb29301351c; multi_sids=102495582093%3A709a6dcbc04081c46a695de09ffec54e; odin_tt=87b11de3720c7cb7ae8b9ac64fdc0b68f92ff30c2847c5078d3ac1d45dccb693e99e86bbd2513b90f1c97741a4c27b7d0cfd122f2c61d6622942e2cd7d10bdef3b20e2a447dce74d1dc21b8613749356; n_mh=Tx_fYJeTgyE_fsOR-zd_ogqeK51DxxRlWMGjTIq5Te8; passport_assist_user=Cj2nyHV4JWBbUtmx0Grhu5PhBE3ohi1s432ihuH4qnKvLuA5Bk7hC9sWz8S-y_s0NzYQsfAkQQ0A3NGASPcKGkgKPMU49fuP9A2zr2bQFeLtQzzty6bxQV0zoPVYxHdbPTknITsydZUiCKx1n329Mf1CWFwie0zIT5BR4YweNhDuy6ENGImv1lQiAQP-eC3e; uid_tt=625b7c3b7cb73cf68964aeb79e083a91; uid_tt_ss=625b7c3b7cb73cf68964aeb79e083a91; sid_tt=709a6dcbc04081c46a695de09ffec54e; sessionid=709a6dcbc04081c46a695de09ffec54e; sessionid_ss=709a6dcbc04081c46a695de09ffec54e; install_id=2353398884097752; ttreq=1$132c0ac9af43890477bbdbf4780678d903790802; store-region-src=did; sid_guard=709a6dcbc04081c46a695de09ffec54e%7C1670919027%7C5184000%7CSat%2C+11-Feb-2023+08%3A10%3A27+GMT; store-region=cn-gd")
//                    .addHeader("x-tt-dt", "AAA23ATEUZM67E2TGYNPHRLKESBA6C2HFL7OIZ5VRQM5IJABR5DZQGJGXWFOQMOENER2KQMJETU37OV5G6PACFNKM37V7AWVGV5QSICFP7U4FC65TDILBSVYELZFF57JWGVIUE34GNVI7PYYQH4XV4A")
//                    .addHeader("activity_now_client", "1670919139688")
//                    .addHeader("x-ss-req-ticket", "1670919138824")
//                    .addHeader("x-vc-bdturing-sdk-version", "3.1.0.cn")
//                    .addHeader("passport-sdk-version", "20374")
//                    .addHeader("x-tt-token", "00709a6dcbc04081c46a695de09ffec54e005ee9353438edebab5efb7ef431b223126d5b72a994a9da8ef3cb356b2f6ea561a843160e45c181451d77b758cb0b8e9edf0332c3a1a7fcbb0bcfc3632a41c3a771857d51a251d2b50a61036e0b87f027d-1.0.1")
//                    .addHeader("sdk-version", "2")
//                    .addHeader("user-agent", "com.ss.android.ugc.aweme/220901 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.12.13.1)")
//                    .addHeader("x-ladon", "wWF8VzC0hWnadoZKOqKC+LOojwMzDiyNoBzJrcvShuvKzxTh")
//                    .addHeader("x-gorgon", "0404c0f20000a098d5d7f7be0e110e117f066203458b8bf7856f")
//                    .addHeader("x-khronos", "1670919138")
//                    .addHeader("x-argus", "N5aKlztU0PC+QyDyZcrWP0HCC0KVm0fNvZd2HKr7eYON9CfU9ivOBJH6MOZ9+v8shGOR+cKKS3IddsM7myqrv3iEilj+UeTqwunROC/1P+wYF214YLqX4Iidu82NjuVha6DCootlrrHNZmRI8mRvCOOADgDgPgDDyCGSvFQwqmIV80HXjX0tQlTQ+KZwswM+nuKD4ugySLXqXG+sx9uRKTnHuJI8qI/XyW2n5IIlcss4Qsf4zdzQYsRqpAZNADSM94FGaIw50sPN6TJk2V+9Isk4AxHtp0YSuiN/6g0VFMB91Q==")
//                    .build();
            Request request = new Request.Builder()
                    .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?" +
                            "ecom_sdk_version=27100" +
                            "&author_id=" + authorId +
                            "&sec_author_id=" + secAuthorId +
                            "&room_id=" + roomId +
                            "&entrance_info=%257B%2522search_id%2522%253A%2522202212131621510102122000882C034D33%2522%252C%2522request_id%2522%253A%2522202212131621510102122000882C034D33%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%25224195355415549012%2522%252C%2522follow_status%2522%253A%25221%2522%252C%2522device_id%2522%253A%2522493005801012839%2522%252C%2522sdk_version%2522%253A%25222700%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227176479292711324475%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522pipeline_version%255C%255C%255C%2522%253A1%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25224195355415549012%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%2522202212131621510102122000882C034D33%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25227176479296188468535%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%2522202212131621510102122000882C034D33%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2353398884097752&device_id=493005801012839&ac=wifi&channel=aweGW&aid=1128&app_name=aweme&version_code=220900&version_name=22.9.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=e35eaf454044e370&manifest_version_code=220901&resolution=720*1280&dpi=240&update_version_code=22909900&_rticket=1670919716555&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1670919716&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=cb2b840d-7ec8-4687-b673-1e0c91534e80&uuid=351564992851618&md=0")
//                    .method("GET", body)
                    .addHeader("Host", "lianmengapi.snssdk.com")
                    .addHeader("Cookie", "passport_csrf_token=64ca82ff9b8e73db6fb74c6a57d28d14; passport_csrf_token_default=64ca82ff9b8e73db6fb74c6a57d28d14; d_ticket=df26923499a3f424b39cb0fca9bb29301351c; multi_sids=102495582093%3A709a6dcbc04081c46a695de09ffec54e; odin_tt=87b11de3720c7cb7ae8b9ac64fdc0b68f92ff30c2847c5078d3ac1d45dccb693e99e86bbd2513b90f1c97741a4c27b7d0cfd122f2c61d6622942e2cd7d10bdef3b20e2a447dce74d1dc21b8613749356; n_mh=Tx_fYJeTgyE_fsOR-zd_ogqeK51DxxRlWMGjTIq5Te8; passport_assist_user=Cj2nyHV4JWBbUtmx0Grhu5PhBE3ohi1s432ihuH4qnKvLuA5Bk7hC9sWz8S-y_s0NzYQsfAkQQ0A3NGASPcKGkgKPMU49fuP9A2zr2bQFeLtQzzty6bxQV0zoPVYxHdbPTknITsydZUiCKx1n329Mf1CWFwie0zIT5BR4YweNhDuy6ENGImv1lQiAQP-eC3e; uid_tt=625b7c3b7cb73cf68964aeb79e083a91; uid_tt_ss=625b7c3b7cb73cf68964aeb79e083a91; sid_tt=709a6dcbc04081c46a695de09ffec54e; sessionid=709a6dcbc04081c46a695de09ffec54e; sessionid_ss=709a6dcbc04081c46a695de09ffec54e; install_id=2353398884097752; ttreq=1$132c0ac9af43890477bbdbf4780678d903790802; store-region-src=did; sid_guard=709a6dcbc04081c46a695de09ffec54e%7C1670919027%7C5184000%7CSat%2C+11-Feb-2023+08%3A10%3A27+GMT; store-region=cn-gd")
                    .addHeader("x-tt-dt", "AAA23ATEUZM67E2TGYNPHRLKESBA6C2HFL7OIZ5VRQM5IJABR5DZQGJGXWFOQMOENER2KQMJETU37OV5G6PACFNKM37V7AWVGV5QSICFP7U4FC65TDILBSVYELZFF57JWGVIUE34GNVI7PYYQH4XV4A")
                    .addHeader("activity_now_client", "1670919717307")
                    .addHeader("x-ss-req-ticket", "1670919716558")
                    .addHeader("x-vc-bdturing-sdk-version", "3.1.0.cn")
                    .addHeader("passport-sdk-version", "20374")
                    .addHeader("x-tt-token", "00709a6dcbc04081c46a695de09ffec54e005ee9353438edebab5efb7ef431b223126d5b72a994a9da8ef3cb356b2f6ea561a843160e45c181451d77b758cb0b8e9edf0332c3a1a7fcbb0bcfc3632a41c3a771857d51a251d2b50a61036e0b87f027d-1.0.1")
                    .addHeader("sdk-version", "2")
                    .addHeader("user-agent", "com.ss.android.ugc.aweme/220901 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.12.13.1)")
                    .addHeader("x-ladon", "SzpfelkbbMRVTF96CxOApL+U2zzwbuERoONEyi+lhwnoRRc8")
                    .addHeader("x-gorgon", "0404a0900000ab6e361bc7c017a5093db85f2e16f1d0a248eca8")
                    .addHeader("x-khronos", "1670919716")
                    .addHeader("x-argus", "3veIZHZOnz7X1CZCSg2LuuJGs7+uxJwPkbPDtQqdRZzK1PHUOKTFVUXwE2NQIXUlE/0AKGktDTqG9yNyOYUeCuAm8Yy4jCZitHVg2RB1avcwZkVQwJJF+QSLR93ND8PPgq4JqWs5gfcM0LW0ocZCq5Nq1ixLXqM1gYGR3hgNfmpZk5YgNu4gDMAr7NnX43Mu+vzYdDw9UhB6sfLppjWrAqmDfaIFGys4tqjmSmia2HAIntsmqLYRE29Qq3juYGbCtgSeVGrMiJDicsO6jDuUeN3SS9veaMkqwrRdhNlovqP8mQ==")
                    .build();
            Response response;
            ProductResultBean bean=new ProductResultBean();
            //每10秒发送一次请求
            while (!isInterrupt){
                response = client.newCall(request).execute();
                while (response.code() != 200) {
                    logger.error("Product请求失败，正在重试");
                    response = client.newCall(request).execute();
                    Thread.sleep(3000);
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
        getProduct(this.info.getLiveUrl(),this.info.getLiveId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl(),this.info.getAuthorId(),this.info.getSecAuthorId());
    }
}
