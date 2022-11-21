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
public class GroupData {

    @JSONField(name="avatar_list")
    private List<String> avatarList;
    @JSONField(name="button_text")
    private String buttonText;
    @JSONField(name="group_id")
    private String groupId;
    @JSONField(name="group_label")
    private String groupLabel;
    @JSONField(name="group_size")
    private int groupSize;
    private int joined;
    private int persent;
    public void setAvatarList(List<String> avatarList) {
         this.avatarList = avatarList;
     }
     public List<String> getAvatarList() {
         return avatarList;
     }

    public void setButtonText(String buttonText) {
         this.buttonText = buttonText;
     }
     public String getButtonText() {
         return buttonText;
     }

    public void setGroupId(String groupId) {
         this.groupId = groupId;
     }
     public String getGroupId() {
         return groupId;
     }

    public void setGroupLabel(String groupLabel) {
         this.groupLabel = groupLabel;
     }
     public String getGroupLabel() {
         return groupLabel;
     }

    public void setGroupSize(int groupSize) {
         this.groupSize = groupSize;
     }
     public int getGroupSize() {
         return groupSize;
     }

    public void setJoined(int joined) {
         this.joined = joined;
     }
     public int getJoined() {
         return joined;
     }

    public void setPersent(int persent) {
         this.persent = persent;
     }
     public int getPersent() {
         return persent;
     }

}