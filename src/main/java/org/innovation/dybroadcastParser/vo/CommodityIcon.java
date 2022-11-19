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
public class CommodityIcon {

    @JSONField(name="bottom_icon")
    private BottomIcon bottomIcon;
    @JSONField(name="bottom_icon_wide")
    private int bottomIconWide;
    @JSONField(name="live_room_type")
    private int liveRoomType;
    @JSONField(name="product_type")
    private int productType;
    @JSONField(name="rotation_time")
    private int rotationTime;
    @JSONField(name="up_icon")
    private String upIcon;
    @JSONField(name="up_icon_rotation_whitelist")
    private List<String> upIconRotationWhitelist;
    @JSONField(name="up_icon_rotations")
    private List<String> upIconRotations;
    public void setBottomIcon(BottomIcon bottomIcon) {
         this.bottomIcon = bottomIcon;
     }
     public BottomIcon getBottomIcon() {
         return bottomIcon;
     }

    public void setBottomIconWide(int bottomIconWide) {
         this.bottomIconWide = bottomIconWide;
     }
     public int getBottomIconWide() {
         return bottomIconWide;
     }

    public void setLiveRoomType(int liveRoomType) {
         this.liveRoomType = liveRoomType;
     }
     public int getLiveRoomType() {
         return liveRoomType;
     }

    public void setProductType(int productType) {
         this.productType = productType;
     }
     public int getProductType() {
         return productType;
     }

    public void setRotationTime(int rotationTime) {
         this.rotationTime = rotationTime;
     }
     public int getRotationTime() {
         return rotationTime;
     }

    public void setUpIcon(String upIcon) {
         this.upIcon = upIcon;
     }
     public String getUpIcon() {
         return upIcon;
     }

    public void setUpIconRotationWhitelist(List<String> upIconRotationWhitelist) {
         this.upIconRotationWhitelist = upIconRotationWhitelist;
     }
     public List<String> getUpIconRotationWhitelist() {
         return upIconRotationWhitelist;
     }

    public void setUpIconRotations(List<String> upIconRotations) {
         this.upIconRotations = upIconRotations;
     }
     public List<String> getUpIconRotations() {
         return upIconRotations;
     }

}