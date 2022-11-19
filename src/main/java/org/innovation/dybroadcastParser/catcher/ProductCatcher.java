package org.innovation.dybroadcastParser.catcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.innovation.dybroadcastParser.vo.ProductInfo;

import java.io.IOException;

public class ProductCatcher {

    public static void testProduct() throws IOException {
        //模拟手机发包
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?ecom_sdk_version=2580&author_id=58544496104&sec_author_id=MS4wLjABAAAAzzmS2TgIEvxGftMpWD13Ty8k5HmsjlGsLJ1yBUEm2Ew&room_id=7167693946695535398&entrance_info=%257B%2522search_id%2522%253A%2522202211192032390102112230323C783532%2522%252C%2522request_id%2522%253A%2522202211192032390102112230323C783532%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%252258544496104%2522%252C%2522follow_status%2522%253A%25220%2522%252C%2522device_id%2522%253A%25223408930656496894%2522%252C%2522sdk_version%2522%253A%25222570%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227167693946695535398%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522search_keyword%255C%255C%255C%2522%253A%255C%255C%255C%2522%25E7%2596%25AF%25E7%258B%2582%25E5%25B0%258F%25E6%259D%25A8%25E5%2593%25A5%25E7%259B%25B4%25E6%2592%25AD%255C%255C%255C%2522%252C%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%2522202211192032390102112230323C783532%255C%255C%255C%2522%252C%255C%255C%255C%2522token_type%255C%255C%255C%2522%253A%255C%255C%255C%2522live_ing%255C%255C%255C%2522%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25227167693959840484640%255C%255C%255C%2522%252C%255C%255C%255C%2522enter_from%255C%255C%255C%2522%253A%255C%255C%255C%2522general_search%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522live_cell%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_live_cell%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%2522202211192032390102112230323C783532%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1668861218939&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1668861218&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
////                .method("GET", body)
//                .addHeader("Host", "lianmengapi.snssdk.com")
//                .addHeader("Cookie", "install_id=2159885451009367; ttreq=1$3467ff2806f428be9e6a32cad42a356cc2179a5d; odin_tt=0bf1c3e90a06bfe67e44c475dbcc02bbaac2c3baeac6141878ab5080015f7e33ee337cce5bb04b007cc1c45c10a332f19962f51dbe75eeb5edf67766eb760e268e8647659a296c1f7053125dce632503; passport_csrf_token=3130e223ac037a55a731da09b022ce20; passport_csrf_token_default=3130e223ac037a55a731da09b022ce20; msToken=UZJ0DhO5Q0UPRa3GyO3OKhHztAhcbO4aLn_BOhIe91_Q8JkzDMlaG6S9y1LmEcbQSJL-oAIsaT0PDXSkPEBkUNCCxnrS66HZVH5G9hFJYg4=")
//                .addHeader("x-tt-dt", "AAAXRLBYTEWVXXSRW3MRBL3KLN57T5FH3AZKMBUUSWLJZUDBSEQKGDTEBXH2EXTXNOKOKBIL75SB44FA2MYVWLAUKX7WR6ARL7D6QQTHP3HZQOHDZBLREXFR6EZ7PNR7LHFITWKPIGCF6B2ADJOYKTA")
//                .addHeader("activity_now_client", "1668861220423")
//                .addHeader("x-ss-req-ticket", "1668861218941")
//                .addHeader("passport-sdk-version", "20372")
//                .addHeader("sdk-version", "2")
//                .addHeader("x-vc-bdturing-sdk-version", "2.2.1.cn")
//                .addHeader("user-agent", "com.ss.android.ugc.aweme/210601 (Linux; U; Android 7.1.2; zh_CN; SM-G973N; Build/PPR1.190810.011;tt-ok/3.10.0.2)")
//                .addHeader("x-ladon", "ja/we6wk3f1dz98i68Ov9Qe9XL26yPjotRL1LpLgmHVfKQ/X")
//                .addHeader("x-gorgon", "0404405e4005614cc3c4772243d7c93e83ae7390f68ef9a28eae")
//                .addHeader("x-khronos", "1668861218")
//                .addHeader("x-argus", "LQqQV/Ds2PwzxiHN0hdxWQdCftfIeVgDa9teZeQMkqO+TiipBnOKo1lVHQ3fkZlqzlspnTKzJPD5v7JJXruedA/AYbFMNafGPD7V5qrzHhBEeEQKZw/FpBGzlFrp0XvbOaBkaG+G73+EDDaf+U4y835HaP7KI3CspE7mn2fZ7jNg0xH2ziQLiXzDtsEucxDzPhaY54vDVpYYAKtd/YA292ndvGkPig1dFl7fHx0r89Tbl0iSwV3/sVhn9Xgx7cpH92ZYyOD4Z0s0qQM9lkq0AhOP")
//                .build();
//        Response response = client.newCall(request).execute();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://lianmengapi.snssdk.com/live/promotions/pop/v3/?ecom_sdk_version=2580&author_id=2384194153219051&sec_author_id=MS4wLjABAAAAcud_llwUN1kpfpzeb3Xqbq8nsRwU7lxVzg3OSv31hNMPz95UspEw1L53dX-UDrE4&room_id=7167505955482045222&entrance_info=%257B%2522search_id%2522%253A%25222022111911484801021208621505428AA5%2522%252C%2522request_id%2522%253A%2522null%2522%252C%2522action_type%2522%253A%2522click%2522%252C%2522_param_live_platform%2522%253A%2522live%2522%252C%2522anchor_id%2522%253A%25222384194153219051%2522%252C%2522follow_status%2522%253A%25220%2522%252C%2522device_id%2522%253A%25223408930656496894%2522%252C%2522sdk_version%2522%253A%25222570%2522%252C%2522search_type%2522%253A%2522general%2522%252C%2522room_id%2522%253A%25227167505955482045222%2522%252C%2522live_tracker_params%2522%253A%2522%257B%255C%2522search_params%255C%2522%253A%255C%2522%257B%255C%255C%255C%2522search_id%255C%255C%255C%2522%253A%255C%255C%255C%25222022111911484801021208621505428AA5%255C%255C%255C%2522%252C%255C%255C%255C%2522search_result_id%255C%255C%255C%2522%253A%255C%255C%255C%25222384194153219051%255C%255C%255C%2522%252C%255C%255C%255C%2522list_item_id%255C%255C%255C%2522%253A%255C%255C%255C%25222384194153219051%255C%255C%255C%2522%252C%255C%255C%255C%2522token_type%255C%255C%255C%2522%253A%255C%255C%255C%2522person_v2%255C%255C%255C%2522%252C%255C%255C%255C%2522enter_from%255C%255C%255C%2522%253A%255C%255C%255C%2522general_search%255C%255C%255C%2522%252C%255C%255C%255C%2522search_keyword%255C%255C%255C%2522%253A%255C%255C%255C%2522%25E4%25B8%259C%25E6%2596%25B9%25E7%2594%2584%25E9%2580%2589%25E7%259B%25B4%25E6%2592%25AD%25E9%2597%25B4%255C%255C%255C%2522%257D%255C%2522%257D%2522%252C%2522enter_from_merge%2522%253A%2522general_search%2522%252C%2522enter_method%2522%253A%2522others_photo%2522%252C%2522enter_from%2522%253A%2522live%2522%252C%2522category_name%2522%253A%2522general_search_temai_others_photo%2522%252C%2522live_ad_business_extra_params%2522%253A%2522%257B%255C%2522request_id%255C%2522%253A%255C%2522null%255C%2522%257D%2522%252C%2522carrier_type%2522%253A%2522live_popup_card%2522%252C%2522ecom_scene_id%2522%253A%25221001%2522%257D&op_type=3&use_new_price=1&iid=2159885451009367&device_id=3408930656496894&ac=wifi&channel=shenmasem_ls_dy_017&aid=1128&app_name=aweme&version_code=210600&version_name=21.6.0&device_platform=android&os=android&ssmix=a&device_type=SM-G973N&device_brand=samsung&language=zh&os_api=25&os_version=7.1.2&openudid=404978669ac5db65&manifest_version_code=210601&resolution=720*1280&dpi=240&update_version_code=21609900&_rticket=1668829732299&package=com.ss.android.ugc.aweme&mcc_mnc=46007&cpu_support64=false&host_abi=armeabi-v7a&ts=1668829732&is_guest_mode=0&app_type=normal&appTheme=light&need_personal_recommend=1&minor_status=0&is_android_pad=0&cdid=85f20741-c857-4b64-9e8e-64cde6aab63c&uuid=351564445751613")
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
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String result=response.body().string();
        System.out.println(result);
        JSONObject object=JSON.parseObject(result);
        if (null==object || null==object.getJSONArray("promotions")             //有概率不获得商品信息
                ||null==object.getJSONArray("promotions").getJSONObject(0)
                || object.getJSONArray("promotions").getJSONObject(0).size()==0) return;
        //product_id
        String productId=object.getJSONArray("promotions").getJSONObject(0).getString("product_id");
        //商品标题
        String title = object.getJSONArray("promotions").getJSONObject(0).getString("title");
        //商品单价-当前活动价格
        Double price=object.getJSONArray("promotions").getJSONObject(0).getDouble("min_price");
        //商品单价-当前活动价格
        Double regularPrice=object.getJSONArray("promotions").getJSONObject(0).getDouble("regular_price");
        //商品销量
        Long sale=object.getJSONArray("promotions").getJSONObject(0).getJSONObject("hot_atmosphere").getLong("sale_num");
        System.out.println(productId);
        System.out.println(title);
        System.out.println(price/100.0f);
        System.out.println(regularPrice/100.0f);
        System.out.println(sale);
    }
}
