package org.jeecg.goods.vo;

import java.util.List;
import org.jeecg.goods.entity.TnGoods;
import org.jeecg.goods.entity.TnGoodsSpec;
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
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Data
@Schema(description="商品列表")
public class TnGoodsPage {

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
	/**商品名称*/
	@Excel(name = "商品名称", width = 15, dicCode = "tongnian_goods_category")
    @Dict(dicCode = "tongnian_goods_category")
	@Schema(description = "商品名称")
    private java.lang.String name;
	/**是否定制商品*/
	@Excel(name = "是否定制商品", width = 15, dicCode = "tongnian_is_custom")
    @Dict(dicCode = "tongnian_is_custom")
	@Schema(description = "是否定制商品")
    private java.lang.String isCustom;
	/**商品类别*/
	@Excel(name = "商品类别", width = 15)
	@Schema(description = "商品类别")
    private java.lang.String category;
	/**商品说明*/
	@Excel(name = "商品说明", width = 15)
	@Schema(description = "商品说明")
    private java.lang.String remark;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "tongnian_goods_status")
    @Dict(dicCode = "tongnian_goods_status")
	@Schema(description = "状态")
    private java.lang.String status;
	
	@ExcelCollection(name="商品规格/价格明细")
	@Schema(description = "商品规格/价格明细")
	private List<TnGoodsSpec> tnGoodsSpecList;
	
}
