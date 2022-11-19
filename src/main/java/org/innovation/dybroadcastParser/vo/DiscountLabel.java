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
public class DiscountLabel {

    private String id;
    @JSONField(name="is_show")
    private int isShow;
    @JSONField(name="is_third_coupon")
    private boolean isThirdCoupon;
    @JSONField(name="kol_user_tag")
    private int kolUserTag;
    private String tag;
    @JSONField(name="tag_header")
    private String tagHeader;
    private int type;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setIsShow(int isShow) {
         this.isShow = isShow;
     }
     public int getIsShow() {
         return isShow;
     }

    public void setIsThirdCoupon(boolean isThirdCoupon) {
         this.isThirdCoupon = isThirdCoupon;
     }
     public boolean getIsThirdCoupon() {
         return isThirdCoupon;
     }

    public void setKolUserTag(int kolUserTag) {
         this.kolUserTag = kolUserTag;
     }
     public int getKolUserTag() {
         return kolUserTag;
     }

    public void setTag(String tag) {
         this.tag = tag;
     }
     public String getTag() {
         return tag;
     }

    public void setTagHeader(String tagHeader) {
         this.tagHeader = tagHeader;
     }
     public String getTagHeader() {
         return tagHeader;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

}