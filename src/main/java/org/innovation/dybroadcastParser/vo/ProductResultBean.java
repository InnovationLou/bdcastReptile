package org.innovation.dybroadcastParser.vo;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Innovation
 */
@Data
public class ProductResultBean {
    @Alias("时间戳")
    private LocalDateTime time;
    private String productId;
    private String promotionId;
    private String title;
    private Double price;
    private Double regularPrice;
    private Long saleNum;
}
