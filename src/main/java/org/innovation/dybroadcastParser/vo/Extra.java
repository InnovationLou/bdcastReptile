/**
  * Copyright 2022 jb51.net 
  */
package org.innovation.dybroadcastParser.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Auto-generated: 2022-11-19 16:5:5
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class Extra {

    @JSONField(name="has_discount_price")
    private String hasDiscountPrice;
    @JSONField(name="origin_id")
    private String originId;
    @JSONField(name="origin_type")
    private String originType;
    @JSONField(name="start_time")
    private String startTime;
    public void setHasDiscountPrice(String hasDiscountPrice) {
         this.hasDiscountPrice = hasDiscountPrice;
     }
     public String getHasDiscountPrice() {
         return hasDiscountPrice;
     }

    public void setOriginId(String originId) {
         this.originId = originId;
     }
     public String getOriginId() {
         return originId;
     }

    public void setOriginType(String originType) {
         this.originType = originType;
     }
     public String getOriginType() {
         return originType;
     }

    public void setStartTime(String startTime) {
         this.startTime = startTime;
     }
     public String getStartTime() {
         return startTime;
     }

}