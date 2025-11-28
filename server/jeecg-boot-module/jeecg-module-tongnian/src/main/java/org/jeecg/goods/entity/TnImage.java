package org.jeecg.goods.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 图片信息表
 * @Author: jeecg-boot
 * @Date:   2025-11-25
 * @Version: V1.0
 */
@Data
@TableName("tn_image")
@Schema(description="图片信息表")
public class TnImage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
    
    /**图片URL*/
    @Schema(description = "图片URL")
    private java.lang.String url;
    
    /**图片名称*/
    @Schema(description = "图片名称")
    private java.lang.String name;
    
    /**排序*/
    @Schema(description = "排序")
    private java.lang.Integer sort;
    
    /**状态*/
    @Schema(description = "状态")
    private java.lang.String status;
}
