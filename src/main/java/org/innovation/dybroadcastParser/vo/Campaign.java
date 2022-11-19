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
public class Campaign {

    @JSONField(name="auction_info")
    private AuctionInfo auctionInfo;
    @JSONField(name="campaign_id")
    private String campaignId;
    @JSONField(name="campaign_type")
    private int campaignType;
    @JSONField(name="end_time")
    private int endTime;
    @JSONField(name="fake_sec_kill")
    private boolean fakeSecKill;
    @JSONField(name="group_data")
    private GroupData groupData;
    @JSONField(name="is_preheat")
    private boolean isPreheat;
    private String label;
    @JSONField(name="left_stock")
    private int leftStock;
    @JSONField(name="max_price")
    private int maxPrice;
    private String pic;
    @JSONField(name="pre_sale_data")
    private PreSaleData preSaleData;
    private int price;
    @JSONField(name="price_header")
    private String priceHeader;
    @JSONField(name="price_str")
    private String priceStr;
    @JSONField(name="progress_text")
    private String progressText;
    @JSONField(name="promotion_id")
    private String promotionId;
    @JSONField(name="regular_price")
    private int regularPrice;
    @JSONField(name="start_time")
    private int startTime;
    private int stock;
    private int style;
    @JSONField(name="sub_type")
    private int subType;
    @JSONField(name="time_end_label")
    private String timeEndLabel;
    @JSONField(name="time_start_label")
    private String timeStartLabel;
    @JSONField(name="user_limit")
    private int userLimit;
    public void setAuctionInfo(AuctionInfo auctionInfo) {
         this.auctionInfo = auctionInfo;
     }
     public AuctionInfo getAuctionInfo() {
         return auctionInfo;
     }

    public void setCampaignId(String campaignId) {
         this.campaignId = campaignId;
     }
     public String getCampaignId() {
         return campaignId;
     }

    public void setCampaignType(int campaignType) {
         this.campaignType = campaignType;
     }
     public int getCampaignType() {
         return campaignType;
     }

    public void setEndTime(int endTime) {
         this.endTime = endTime;
     }
     public int getEndTime() {
         return endTime;
     }

    public void setFakeSecKill(boolean fakeSecKill) {
         this.fakeSecKill = fakeSecKill;
     }
     public boolean getFakeSecKill() {
         return fakeSecKill;
     }

    public void setGroupData(GroupData groupData) {
         this.groupData = groupData;
     }
     public GroupData getGroupData() {
         return groupData;
     }

    public void setIsPreheat(boolean isPreheat) {
         this.isPreheat = isPreheat;
     }
     public boolean getIsPreheat() {
         return isPreheat;
     }

    public void setLabel(String label) {
         this.label = label;
     }
     public String getLabel() {
         return label;
     }

    public void setLeftStock(int leftStock) {
         this.leftStock = leftStock;
     }
     public int getLeftStock() {
         return leftStock;
     }

    public void setMaxPrice(int maxPrice) {
         this.maxPrice = maxPrice;
     }
     public int getMaxPrice() {
         return maxPrice;
     }

    public void setPic(String pic) {
         this.pic = pic;
     }
     public String getPic() {
         return pic;
     }

    public void setPreSaleData(PreSaleData preSaleData) {
         this.preSaleData = preSaleData;
     }
     public PreSaleData getPreSaleData() {
         return preSaleData;
     }

    public void setPrice(int price) {
         this.price = price;
     }
     public int getPrice() {
         return price;
     }

    public void setPriceHeader(String priceHeader) {
         this.priceHeader = priceHeader;
     }
     public String getPriceHeader() {
         return priceHeader;
     }

    public void setPriceStr(String priceStr) {
         this.priceStr = priceStr;
     }
     public String getPriceStr() {
         return priceStr;
     }

    public void setProgressText(String progressText) {
         this.progressText = progressText;
     }
     public String getProgressText() {
         return progressText;
     }

    public void setPromotionId(String promotionId) {
         this.promotionId = promotionId;
     }
     public String getPromotionId() {
         return promotionId;
     }

    public void setRegularPrice(int regularPrice) {
         this.regularPrice = regularPrice;
     }
     public int getRegularPrice() {
         return regularPrice;
     }

    public void setStartTime(int startTime) {
         this.startTime = startTime;
     }
     public int getStartTime() {
         return startTime;
     }

    public void setStock(int stock) {
         this.stock = stock;
     }
     public int getStock() {
         return stock;
     }

    public void setStyle(int style) {
         this.style = style;
     }
     public int getStyle() {
         return style;
     }

    public void setSubType(int subType) {
         this.subType = subType;
     }
     public int getSubType() {
         return subType;
     }

    public void setTimeEndLabel(String timeEndLabel) {
         this.timeEndLabel = timeEndLabel;
     }
     public String getTimeEndLabel() {
         return timeEndLabel;
     }

    public void setTimeStartLabel(String timeStartLabel) {
         this.timeStartLabel = timeStartLabel;
     }
     public String getTimeStartLabel() {
         return timeStartLabel;
     }

    public void setUserLimit(int userLimit) {
         this.userLimit = userLimit;
     }
     public int getUserLimit() {
         return userLimit;
     }

}