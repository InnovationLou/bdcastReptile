/**
  * Copyright 2022 jb51.net 
  */
package org.innovation.dybroadcastParser.vo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Auto-generated: 2022-11-19 16:5:5
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class RitTags {

    @JSONField(name="live_room_pop_biz_under")
    private List<LiveRoomPopBizUnder> liveRoomPopBizUnder;
    @JSONField(name="live_room_product_title_before")
    private List<String> liveRoomProductTitleBefore;
    public void setLiveRoomPopBizUnder(List<LiveRoomPopBizUnder> liveRoomPopBizUnder) {
         this.liveRoomPopBizUnder = liveRoomPopBizUnder;
     }
     public List<LiveRoomPopBizUnder> getLiveRoomPopBizUnder() {
         return liveRoomPopBizUnder;
     }

    public void setLiveRoomProductTitleBefore(List<String> liveRoomProductTitleBefore) {
         this.liveRoomProductTitleBefore = liveRoomProductTitleBefore;
     }
     public List<String> getLiveRoomProductTitleBefore() {
         return liveRoomProductTitleBefore;
     }

}