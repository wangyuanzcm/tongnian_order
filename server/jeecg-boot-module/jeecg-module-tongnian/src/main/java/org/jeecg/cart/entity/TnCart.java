package org.jeecg.cart.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2024-01-16
 * @Version: V1.0
 */
@Data
@TableName("tn_cart")
public class TnCart {

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**商品ID*/
	@Excel(name = "商品ID", width = 15)
	private String goodsId;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
	private String goodsName;
	/**规格类型*/
	@Excel(name = "规格类型", width = 15)
	private String specType;
	/**价格*/
	@Excel(name = "价格", width = 15)
	private BigDecimal price;
	/**数量*/
	@Excel(name = "数量", width = 15)
	private Integer quantity;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
	private String imageUrl;
	/**用户ID*/
	@Excel(name = "用户ID", width = 15)
	private String userId;
}
