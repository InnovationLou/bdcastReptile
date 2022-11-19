/**
  * Copyright 2022 jb51.net 
  */
package org.innovation.dybroadcastParser.vo;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Auto-generated: 2022-11-19 16:5:5
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class LiveRoomPopBizUnder {

    @JSONField(name="label_name")
    private String labelName;
    @JSONField(name="tag_id")
    private String tagId;
    private List<String> text;
    @JSONField(name="track_tag")
    private String trackTag;
    private int type;
    public void setLabelName(String labelName) {
         this.labelName = labelName;
     }
     public String getLabelName() {
         return labelName;
     }

    public void setTagId(String tagId) {
         this.tagId = tagId;
     }
     public String getTagId() {
         return tagId;
     }

    public void setText(List<String> text) {
         this.text = text;
     }
     public List<String> getText() {
         return text;
     }

    public void setTrackTag(String trackTag) {
         this.trackTag = trackTag;
     }
     public String getTrackTag() {
         return trackTag;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

}