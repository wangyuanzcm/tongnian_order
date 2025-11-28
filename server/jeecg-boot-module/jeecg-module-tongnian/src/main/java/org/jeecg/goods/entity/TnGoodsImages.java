package org.jeecg.goods.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 商品图片映射表
 * @Author: jeecg-boot
 * @Date:   2025-11-25
 * @Version: V1.0
 */
@Data
@TableName("tn_goods_images")
@Schema(description="商品图片映射表")
public class TnGoodsImages implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
    
    /**商品规格ID*/
    @Schema(description = "商品规格ID")
    private java.lang.String goodsSpecId;
    
    /**图片ID*/
    @Schema(description = "图片ID")
    private java.lang.String imageId;
}
