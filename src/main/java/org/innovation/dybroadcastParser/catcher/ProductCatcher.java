package org.innovation.dybroadcastParser.catcher;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.innovation.dybroadcastParser.vo.BaseInfo;
import org.innovation.dybroadcastParser.vo.ProductResultBean;
import org.jetbrains.annotations.NotNull;

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
    public static void getProductNew(String liveUrl, String liveId, String roomId, String liveName, String userUrl,String authorId,String secAuthorId) {
        //live.douyin.com/live/promotions/pop/v3
        try (Playwright playwright = Playwright.create()) {
            try (Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setDevtools(false)
            )
            ) {
                //指定路径和编码
                CsvWriter writer = CsvUtil.getWriter(System.getProperty("user.dir")+"\\data\\Product-output" + liveName
                        +LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"))+".csv", CharsetUtil.CHARSET_GBK);
                //按行写出
                writer.writeHeaderLine("时间戳","productId","promotionId","商品标题","商品活动价格","商品常规价格","商品销量","直播间id","主播名称");

                Page page = browser.newPage();

                page.onResponse(response -> {
                    if (response.url().contains("live.douyin.com/live/promotions/pop/v3")){
                        try {
                            ProductResultBean bean=new ProductResultBean();
                            String result=response.text();
                            System.out.println(result);
                            JSONObject object=JSON.parseObject(result);
                            if (object!=null){
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
                                logger.info("Product请求成功"+liveName);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            logger.error("Product请求失败"+liveName);
                        }
                    }
                });

                page.navigate(liveUrl);
                //等待NetworkIdle
//                page.waitForLoadState(LoadState.NETWORKIDLE);
                while (!isInterrupt){
                    page.waitForTimeout(10000);
                    page.reload();
                }
                page.close();
                browser.close();
                playwright.close();
                logger.info("产品信息爬取完毕"+liveName);
            } catch (Exception e) {
                logger.error(e);
            }
        }
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
            Request request =null;

            if(liveId.equals("168465302284")){
                request = getRequest168465302284(roomId, authorId, secAuthorId);
            }else if(liveId.equals("726084074851")){
                request = getRequest726084074851(roomId, authorId, secAuthorId);
            }
            else {
                return;
            }
//            Request request = getRequest(roomId, authorId, secAuthorId);
            Response response;
            ProductResultBean bean=new ProductResultBean();
            //每10秒发送一次请求
            while (!isInterrupt){
                response = client.newCall(request).execute();
                while (response.code() != 200) {
                    logger.error("Product请求失败，正在重试--code:"+response.code());
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

    private static Request getRequest80017709309(String roomId, String authorId, String secAuthorId) {
        return null;
    }

    private static Request getRequest726084074851(String roomId, String authorId, String secAuthorId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?ecom_sdk_version=2580&author_id=104689026423&sec_author_id=MS4wLjABAAAAZYx4edFdJMIJAXkRQkSzk0U7y5RsNYEDiSb5ckLjx9U&room_id=7177219822478953271&entrance_info=%257B%2522search_id%2522%253A%25222022121513030301020917417349068361%2522%252C%2522request_id%2522%253A%25222022121513030301020917417349068361%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%2522104689026423%2522%252C%2522follow_status%2522%253A%25220%2522%252C%2522device_id%2522%253A%25223408930656496894%2522%252C%2522sdk_version%2522%253A%25222570%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227177219822478953271%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522pipeline_version%255C%255C%255C%2522%253A1%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%2522104689026423%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%25222022121513030301020917417349068361%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25228124256160443629681%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%25222022121513030301020917417349068361%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1671080624848&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1671080624&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
                //.method("GET", body)
                .addHeader("Host", "lianmengapi.snssdk.com")
                .addHeader("Cookie", "install_id=2159885451009367; ttreq=1$3467ff2806f428be9e6a32cad42a356cc2179a5d; odin_tt=0bf1c3e90a06bfe67e44c475dbcc02bbaac2c3baeac6141878ab5080015f7e33ee337cce5bb04b007cc1c45c10a332f19962f51dbe75eeb5edf67766eb760e268e8647659a296c1f7053125dce632503; passport_csrf_token=3130e223ac037a55a731da09b022ce20; passport_csrf_token_default=3130e223ac037a55a731da09b022ce20; store-region-src=did; store-region=cn-gd; msToken=rS-rdAchTpTD3EKFAktnZ3nZ2YlsI-ZLT4naGUYc6X9YQy2235YuB6VdYPoJ-wFl_xOSNaEnKuVhv0H9uvOcBceBRLMXGShF9_wh8TpqFV0=")
                .addHeader("x-tt-dt", "AAA7ERN2OPHQUOYLT6LUQKLRX36P5KUZFFT5LDAJ3W6ZQEHA7UACUWDWQWUKEYCNVX66J7GO2N3MMJKO4PYDABQMQJ73G76BDTJRN2OXAAJGCJPQUF7FITN7CEUKGEIF2JQKTHCA2HTBHWCSE3DLZDI")
                .addHeader("activity_now_client", "1671080624896")
                .addHeader("x-ss-req-ticket", "1671080624850")
                .addHeader("passport-sdk-version", "20372")
                .addHeader("sdk-version", "2")
                .addHeader("x-vc-bdturing-sdk-version", "2.2.1.cn")
                .addHeader("user-agent", "com.ss.android.ugc.aweme/210601 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.10.0.2)")
                .addHeader("x-ladon", "sLRhZAO4Y7sBg2DhIqK8T5MXtopA12wc4Gnrp5zyantCMRSm")
                .addHeader("x-gorgon", "0404c0244005c8dcf1a4eca3b54b679c9346399713e74a64bdd5")
                .addHeader("x-khronos", "1671080624")
                .addHeader("x-argus", "zi8kLj2w/tuZBMziW/40P0ZtynOCu9DfM+u5rEI5Kh5zpcGZS5JTXzS5qdHMJMQ7mGE6rb4H7eul2Jb9GBkI4Q8hfVG2GImI7i7dEWskpV18UGiR/qML15S0xitiNZs+kRPP7kZfqB+R3qbOHeI11VD6w+JDvso4oYts6NFF1QHjG5ibkVckdEtBexI6StBwqEUQNEgU36irLXfDBt/tMChJzuuPQCjzHxyF7XJomG4OCVhtFwPU3qaa7/sxH8nqxhb8ZqvQUw2oSuJyF42FD29T")
                .addHeader("Accept", "*/*")
                .addHeader("Postman-Token", "8d95615a-3522-4db7-8e21-fdfa0d9414af")
                .build();
        return request;
    }

    private static Request getRequest168465302284(String roomId, String authorId, String secAuthorId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?ecom_sdk_version=2580&author_id=4195355415549012&sec_author_id=MS4wLjABAAAAlwXCzzm7SmBfdZAsqQ_wVVUbpTvUSX1WC_x8HAjMa3gLb88-MwKL7s4OqlYntX4r&room_id=7177126250090089274&entrance_info=%257B%2522search_id%2522%253A%2522202212151232090102120810811203CE89%2522%252C%2522request_id%2522%253A%2522202212151232090102120810811203CE89%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%25224195355415549012%2522%252C%2522follow_status%2522%253A%25220%2522%252C%2522device_id%2522%253A%25223408930656496894%2522%252C%2522sdk_version%2522%253A%25222570%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227177126250090089274%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522pipeline_version%255C%255C%255C%2522%253A1%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25224195355415549012%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%2522202212151232090102120810811203CE89%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25223715452303323110008%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%2522202212151232090102120810811203CE89%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1671078740079&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1671078740&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
//                .method("GET", body)
                .addHeader("Host", "lianmengapi.snssdk.com")
                .addHeader("Cookie", "install_id=2159885451009367; ttreq=1$3467ff2806f428be9e6a32cad42a356cc2179a5d; odin_tt=0bf1c3e90a06bfe67e44c475dbcc02bbaac2c3baeac6141878ab5080015f7e33ee337cce5bb04b007cc1c45c10a332f19962f51dbe75eeb5edf67766eb760e268e8647659a296c1f7053125dce632503; passport_csrf_token=3130e223ac037a55a731da09b022ce20; passport_csrf_token_default=3130e223ac037a55a731da09b022ce20; store-region-src=did; store-region=cn-gd; msToken=1fcQo7ZISCHcapGsOppOlUsA0TG7JjnNAB_pC9s55HR8XQtXAQd2zMu8HsIjaI3C1INSWo4S_gCU590t_SsvS1aNcFPEDcmMDs-PJTpY1rY=")
                .addHeader("x-tt-dt", "AAA7ERN2OPHQUOYLT6LUQKLRX36P5KUZFFT5LDAJ3W6ZQEHA7UACUWDWQWUKEYCNVX66J7GO2N3MMJKO4PYDABQMQJ73G76BDTJRN2OXAAJGCJPQUF7FITN7CEUKGEIF2JQKTHCA2HTBHWCSE3DLZDI")
                .addHeader("activity_now_client", "1671078740127")
                .addHeader("x-ss-req-ticket", "1671078740081")
                .addHeader("passport-sdk-version", "20372")
                .addHeader("sdk-version", "2")
                .addHeader("x-vc-bdturing-sdk-version", "2.2.1.cn")
                .addHeader("user-agent", "com.ss.android.ugc.aweme/210601 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.10.0.2)")
                .addHeader("x-ladon", "L3l5W+OVtHx4pTmdFx5S3EH2OQWKLXptZTY/qAvQ9RSEirmj")
                .addHeader("x-gorgon", "0404c0fa400568eab3bf4595a005dc770234b80bc22f02b1e53d")
                .addHeader("x-khronos", "1671078740")
                .addHeader("x-argus", "wXnM0+35fC1C82iabJkAnFVtRO2p1/7y6ptpvuLxv6W69gKQ6CwZPqvlU5JfvXK68ix0f1TGIGnvqPbpxVZudZ0lePtHQ8GPHnttvLcXknpA7nXD6vfRoP1bzsxsK6C7vhrG7Lj7SxkHONLR2SsLQAJ7bL+4XtpsQOPNrQZVqNpHCS9sH/nPROwc0ijNsJMdRdKz/2Qo8q9e5ZOZGT7EKxdJ6rnJ4nB2gMgQlhBNpYdra1EFJtncPdwKjOXIPGpEG/sZ0SxvBRZMa10NdZFOqdS1")
                .build();
        return request;
    }

    @NotNull
    private static Request getRequest(String roomId, String authorId, String secAuthorId) {
        Request request = new Request.Builder()
                .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?" +
                        "ecom_sdk_version=27100" +
                        "&author_id=" + authorId +
                        "&sec_author_id=" + secAuthorId +
                        "&room_id=" + roomId +
                        "&entrance_info=%257B%2522search_id%2522%253A%2522202212142129440102121012331C161B9F%2522%252C%2522request_id%2522%253A%2522202212142129440102121012331C161B9F%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%25224195355415549012%2522%252C%2522follow_status%2522%253A%25220%2522%252C%2522device_id%2522%253A%25223408930656496894%2522%252C%2522sdk_version%2522%253A%25222570%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227176973783046114103%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522pipeline_version%255C%255C%255C%2522%253A1%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25224195355415549012%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%2522202212142129440102121012331C161B9F%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25224832541476774254283%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%2522202212142129440102121012331C161B9F%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1671024595583&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1671024595&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
//                    .method("GET", body)
                .addHeader("Host", "lianmengapi.snssdk.com")
                .addHeader("Cookie", "install_id=2159885451009367; ttreq=1$3467ff2806f428be9e6a32cad42a356cc2179a5d; odin_tt=0bf1c3e90a06bfe67e44c475dbcc02bbaac2c3baeac6141878ab5080015f7e33ee337cce5bb04b007cc1c45c10a332f19962f51dbe75eeb5edf67766eb760e268e8647659a296c1f7053125dce632503; passport_csrf_token=3130e223ac037a55a731da09b022ce20; passport_csrf_token_default=3130e223ac037a55a731da09b022ce20; store-region-src=did; store-region=cn-gd; msToken=px0XNAWcmFbXoe89WoN5wexd4AlaH_pQp00mLtxicDOHGISRp-DX3ezfWRSlTshlgU1nTnq0M3PwbclGdqA0bezrgLhJ3DhdJ6mYF5qFSm0=")
                .addHeader("x-tt-dt", "AAATA6LVGCL2KPRZTUL2ZZ6OKAJYRWL3OVUVB5ZSIJXDIQIIOH7GSG3V33FYRZVOH3S766EGBH5BPFGEX6GLDEREU3LZTKBBKTRQJYTK4ABRZA2LNTL54VQ4FF3WCJU2MBSVZAU2ERRQQS6JPFGM3PQ")
//                    .addHeader("activity_now_client", "1671024595618")
//                    .addHeader("x-ss-req-ticket", "1671024595585")
                .addHeader("passport-sdk-version", "20372")
                .addHeader("sdk-version", "2")
                .addHeader("x-vc-bdturing-sdk-version", "2.2.1.cn")
                .addHeader("user-agent", "com.ss.android.ugc.aweme/210601 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.10.0.2)")
//                    .addHeader("x-ladon", "ZjkIeoShPEHL3F/v9Hdpt6YV6GxCc4Ch0KaLJsz5AnNrKu9d")
//                    .addHeader("x-gorgon", "0404a0ce4005cb5412ac0be9214be5070a9c2d22d146daa0935a")
//                    .addHeader("x-khronos", "1671024595")
                .addHeader("x-argus", "Dp1kjX8V/Ay/e53owM8FWQZLctAa3HOdtp9lwcA/pj3FXi8x3PXLL8DmnvxvFXcA/C4A7UKTEJUiibYZ1sdja08HpbAHOG61mo3Dweu58K0KddqASrDmATQZrejbH2IFepktlQgXLVww1h2yWw24eR26sQ0quwC+cSW6p+wBSJRDfxZZtP52BLYxNeX/EJZo4qtSBI9s8VIA88fck0IPAcDhddl0VNoPtD51eaT9c8lPrLMxXxvG1heeG6B/+YblnncoCH9t35P+3WEGgl/vN4Yi")
                .build();
        return request;
    }

    @Override
    public void run() {
        getProductNew(this.info.getLiveUrl(),this.info.getLiveId(),this.info.getRoomId(),this.info.getLiveName(),this.info.getUserUrl(),this.info.getAuthorId(),this.info.getSecAuthorId());
    }
}
