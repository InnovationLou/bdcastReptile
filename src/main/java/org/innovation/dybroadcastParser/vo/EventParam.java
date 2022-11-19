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
public class EventParam {

    @JSONField(name="discount_type")
    private String discountType;
    @JSONField(name="enter_from_merge")
    private String enterFromMerge;
    @JSONField(name="enter_method")
    private String enterMethod;
    @JSONField(name="insurance_commodity_flag")
    private String insuranceCommodityFlag;
    @JSONField(name="membership_prd")
    private String membershipPrd;
    @JSONField(name="presell_type")
    private String presellType;
    @JSONField(name="source_page")
    private String sourcePage;
    @JSONField(name="warm_up_status")
    private String warmUpStatus;
    public void setDiscountType(String discountType) {
         this.discountType = discountType;
     }
     public String getDiscountType() {
         return discountType;
     }

    public void setEnterFromMerge(String enterFromMerge) {
         this.enterFromMerge = enterFromMerge;
     }
     public String getEnterFromMerge() {
         return enterFromMerge;
     }

    public void setEnterMethod(String enterMethod) {
         this.enterMethod = enterMethod;
     }
     public String getEnterMethod() {
         return enterMethod;
     }

    public void setInsuranceCommodityFlag(String insuranceCommodityFlag) {
         this.insuranceCommodityFlag = insuranceCommodityFlag;
     }
     public String getInsuranceCommodityFlag() {
         return insuranceCommodityFlag;
     }

    public void setMembershipPrd(String membershipPrd) {
         this.membershipPrd = membershipPrd;
     }
     public String getMembershipPrd() {
         return membershipPrd;
     }

    public void setPresellType(String presellType) {
         this.presellType = presellType;
     }
     public String getPresellType() {
         return presellType;
     }

    public void setSourcePage(String sourcePage) {
         this.sourcePage = sourcePage;
     }
     public String getSourcePage() {
         return sourcePage;
     }

    public void setWarmUpStatus(String warmUpStatus) {
         this.warmUpStatus = warmUpStatus;
     }
     public String getWarmUpStatus() {
         return warmUpStatus;
     }

}