package org.innovation.dybroadcastParser.vo;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Innovation
 */
@Data
public class BaseInfoResultBean {
    @Alias("时间戳")
    private LocalDateTime time;
    private String userName;
    private String userid;
    private Long fans;
    private Long likes;
}
