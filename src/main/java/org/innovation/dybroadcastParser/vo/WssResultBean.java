package org.innovation.dybroadcastParser.vo;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Innovation
 */
@Data
public class WssResultBean {
    @Alias("时间戳")
    private LocalDateTime time;
    /**
     * 0-弹幕 1-点赞 2-礼物 3-用户入场信息 4-直播间人数
     */
    private String type;

    private String userName;
    private String userid;
    private String content;

    private String totalLikes;

    private String like;
    private String likeCount;

    private String giftid;
    private String giftDesc;
    private String giftCount;

    private String liveWatchCount;

}
