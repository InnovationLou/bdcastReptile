package org.innovation.dybroadcastParser.vo;

import lombok.Data;

/**
 * @author Innovation
 */
@Data
public class BaseInfo {
    private String liveUrl;
    private String liveId;
    private String roomId;
    private String liveName;
    private String userUrl;
    private String authorId;
    private String secAuthorId;
    private String liveStatus;

    public BaseInfo(String liveUrl, String liveId, String roomId, String liveName, String userUrl,String authorId,String secAuthorId) {
        this.liveUrl = liveUrl;
        this.liveId = liveId;
        this.roomId = roomId;
        this.liveName = liveName;
        this.userUrl = userUrl;
        this.authorId = authorId;
        this.secAuthorId = secAuthorId;
    }
}
