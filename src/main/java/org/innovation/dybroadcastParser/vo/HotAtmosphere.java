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
public class HotAtmosphere {

    @JSONField(name="bg_color")
    private String bgColor;
    @JSONField(name="bg_colors")
    private List<String> bgColors;
    @JSONField(name="border_color")
    private String borderColor;
    @JSONField(name="border_colors")
    private List<String> borderColors;
    private String img;
    @JSONField(name="left_margin")
    private int leftMargin;
    private String num;
    @JSONField(name="sale_num")
    private String saleNum;
    @JSONField(name="top_img")
    private String topImg;
    private int type;
    @JSONField(name="ui_type")
    private int uiType;
    public void setBgColor(String bgColor) {
         this.bgColor = bgColor;
     }
     public String getBgColor() {
         return bgColor;
     }

    public void setBgColors(List<String> bgColors) {
         this.bgColors = bgColors;
     }
     public List<String> getBgColors() {
         return bgColors;
     }

    public void setBorderColor(String borderColor) {
         this.borderColor = borderColor;
     }
     public String getBorderColor() {
         return borderColor;
     }

    public void setBorderColors(List<String> borderColors) {
         this.borderColors = borderColors;
     }
     public List<String> getBorderColors() {
         return borderColors;
     }

    public void setImg(String img) {
         this.img = img;
     }
     public String getImg() {
         return img;
     }

    public void setLeftMargin(int leftMargin) {
         this.leftMargin = leftMargin;
     }
     public int getLeftMargin() {
         return leftMargin;
     }

    public void setNum(String num) {
         this.num = num;
     }
     public String getNum() {
         return num;
     }

    public void setSaleNum(String saleNum) {
         this.saleNum = saleNum;
     }
     public String getSaleNum() {
         return saleNum;
     }

    public void setTopImg(String topImg) {
         this.topImg = topImg;
     }
     public String getTopImg() {
         return topImg;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setUiType(int uiType) {
         this.uiType = uiType;
     }
     public int getUiType() {
         return uiType;
     }

}