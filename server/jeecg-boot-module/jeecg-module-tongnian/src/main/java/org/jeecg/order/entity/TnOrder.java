package org.jeecg.order.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Schema(description="订单主表")
@Data
@TableName("tn_order")
public class TnOrder implements Serializable {
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
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @Schema(description = "订单编号")
    private java.lang.String orderNo;
	/**应付金额*/
	@Excel(name = "应付金额", width = 15)
    @Schema(description = "应付金额")
    private java.math.BigDecimal payableAmount;
	/**实付金额*/
	@Excel(name = "实付金额", width = 15)
    @Schema(description = "实付金额")
    private java.math.BigDecimal paidAmount;
	/**代收金额*/
	@Excel(name = "代收金额", width = 15)
    @Schema(description = "代收金额")
    private java.math.BigDecimal collectAmount;
	/**收件人*/
	@Excel(name = "收件人", width = 15)
    @Schema(description = "收件人")
    private java.lang.String recipientId;
	/**订单状态*/
    @Excel(name = "订单状态", width = 15,replace = {"是_Y","否_N"} )
    @Schema(description = "订单状态")
    private java.lang.Integer orderStatus;
	/**是否新客户*/
    @Excel(name = "是否新客户", width = 15,replace = {"是_Y","否_N"} )
    @Schema(description = "是否新客户")
    private java.lang.Integer isNewCustomer;
	/**订单和商品映射id*/
	@Excel(name = "订单和商品映射id", width = 15)
    @Schema(description = "订单和商品映射id")
    private java.lang.String goodsSpecId;
}
