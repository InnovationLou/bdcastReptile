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
public class ProductInfo {

    private Campaign campaign;
    @JSONField(name="carousel_time")
    private int carouselTime;
    @JSONField(name="commodity_icon")
    private CommodityIcon commodityIcon;
    @JSONField(name="logical_clock")
    private int logicalClock;
    private List<Promotions> promotions;
    @JSONField(name="server_time")
    private int serverTime;
    @JSONField(name="show_duration")
    private int showDuration;
    @JSONField(name="status_code")
    private int statusCode;
    @JSONField(name="status_msg")
    private String statusMsg;
    public void setCampaign(Campaign campaign) {
         this.campaign = campaign;
     }
     public Campaign getCampaign() {
         return campaign;
     }

    public void setCarouselTime(int carouselTime) {
         this.carouselTime = carouselTime;
     }
     public int getCarouselTime() {
         return carouselTime;
     }

    public void setCommodityIcon(CommodityIcon commodityIcon) {
         this.commodityIcon = commodityIcon;
     }
     public CommodityIcon getCommodityIcon() {
         return commodityIcon;
     }

    public void setLogicalClock(int logicalClock) {
         this.logicalClock = logicalClock;
     }
     public int getLogicalClock() {
         return logicalClock;
     }

    public void setPromotions(List<Promotions> promotions) {
         this.promotions = promotions;
     }
     public List<Promotions> getPromotions() {
         return promotions;
     }

    public void setServerTime(int serverTime) {
         this.serverTime = serverTime;
     }
     public int getServerTime() {
         return serverTime;
     }

    public void setShowDuration(int showDuration) {
         this.showDuration = showDuration;
     }
     public int getShowDuration() {
         return showDuration;
     }

    public void setStatusCode(int statusCode) {
         this.statusCode = statusCode;
     }
     public int getStatusCode() {
         return statusCode;
     }

    public void setStatusMsg(String statusMsg) {
         this.statusMsg = statusMsg;
     }
     public String getStatusMsg() {
         return statusMsg;
     }

}