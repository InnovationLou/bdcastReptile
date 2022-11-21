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
public class PreSaleData {

    @JSONField(name="deposit_price")
    private int depositPrice;
    @JSONField(name="discount_desc")
    private String discountDesc;
    @JSONField(name="origin_price")
    private int originPrice;
    @JSONField(name="shop_svalue")
    private int shopSvalue;
    public void setDepositPrice(int depositPrice) {
         this.depositPrice = depositPrice;
     }
     public int getDepositPrice() {
         return depositPrice;
     }

    public void setDiscountDesc(String discountDesc) {
         this.discountDesc = discountDesc;
     }
     public String getDiscountDesc() {
         return discountDesc;
     }

    public void setOriginPrice(int originPrice) {
         this.originPrice = originPrice;
     }
     public int getOriginPrice() {
         return originPrice;
     }

    public void setShopSvalue(int shopSvalue) {
         this.shopSvalue = shopSvalue;
     }
     public int getShopSvalue() {
         return shopSvalue;
     }

}