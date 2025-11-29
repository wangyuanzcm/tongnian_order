package org.jeecg.goods.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.UnsupportedEncodingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * @Description: 商品规格/价格明细
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Data
@TableName("tn_goods_spec")
@Schema(description="商品规格/价格明细")
public class TnGoodsSpec implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**商品ID*/
    @Schema(description = "商品ID")
    private java.lang.String goodsId;
	/**商品细分规格*/
	@Excel(name = "商品细分规格", width = 15)
    @Schema(description = "商品细分规格")
    private java.lang.String specType;
	/**商品价格*/
	@Excel(name = "商品价格", width = 15)
    @Schema(description = "商品价格")
    private java.math.BigDecimal price;
	/**商品图片*/
	@Excel(name = "商品图片", width = 15)
    private transient java.lang.String imageUrlString;

    private transient byte[] imageUrl;

    public byte[] getImageUrl(){
        if(imageUrlString==null){
            return null;
        }
        try {
            return imageUrlString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageUrlString(){
        if(imageUrl==null || imageUrl.length==0){
            return "";
        }
        try {
            return new String(imageUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**状态*/
    @Excel(name = "状态", width = 15,replace = {"是_Y","否_N"} )
    @Schema(description = "状态")
    private java.lang.String status;
    
    /**
     * 图片列表（非数据库字段）
     */
    @Schema(description = "图片列表")
    @JsonDeserialize(using = ImageListDeserializer.class)
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private List<TnImage> imageList;
    
    public List<TnImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<TnImage> imageList) {
        this.imageList = imageList;
    }
    
    // 自定义反序列化器，处理字符串数组和对象数组
    public static class ImageListDeserializer extends JsonDeserializer<List<TnImage>> {
        @Override
        public List<TnImage> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            List<TnImage> result = new java.util.ArrayList<>();
            
            if (node.isArray()) {
                for (JsonNode item : node) {
                    if (item.isTextual()) {
                        // 如果是字符串，创建TnImage对象
                        String imageId = item.asText();
                        TnImage image = new TnImage();
                        image.setId(imageId);
                        result.add(image);
                    } else if (item.isObject()) {
                        // 如果是对象，使用默认反序列化
                        TnImage image = p.getCodec().treeToValue(item, TnImage.class);
                        result.add(image);
                    }
                }
            }
            
            return result;
        }
    }
}
