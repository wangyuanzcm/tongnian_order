package org.jeecg.order.vo;

import java.util.List;
import org.jeecg.order.entity.TnOrder;
import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.receiver.entity.TnRecipient;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Data
@Schema(description="订单主表")
public class TnOrderPage {

	/**主键*/
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
	/**商品名称*/
	@Excel(name = "商品名称", width = 15, dicCode = "tongnian_goods_category")
    @Dict(dicCode = "tongnian_goods_category")
	@Schema(description = "商品名称")
    private java.lang.String goodId;
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
	@Schema(description = "订单状态")
    @Excel(name = "订单状态", width = 15,replace = {"是_Y","否_N"} )
    private java.lang.Integer orderStatus;
	/**是否新客户*/
	@Schema(description = "是否新客户")
    @Excel(name = "是否新客户", width = 15,replace = {"是_Y","否_N"} )
    private java.lang.Integer isNewCustomer;
	/**订单和商品映射id*/
	@Excel(name = "订单和商品映射id", width = 15)
	@Schema(description = "订单和商品映射id")
    private java.lang.String goodsSpecId;
	
	@ExcelCollection(name="订单商品关联表")
	@Schema(description = "订单商品关联表")
	private List<TnOrderGoods> tnOrderGoodsList;
	
	/**收件人详细信息*/
	@Schema(description = "收件人详细信息")
	private TnRecipient recipient;
	
}
