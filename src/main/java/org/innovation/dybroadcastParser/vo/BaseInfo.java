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

    public BaseInfo(String liveUrl, String liveId, String roomId, String liveName, String userUrl) {
    }
}
