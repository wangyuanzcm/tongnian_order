package org.jeecg.receiver.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 收件人信息
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Data
@TableName("tn_recipient")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="收件人信息")
public class TnRecipient implements Serializable {
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
	/**收件人姓名*/
	@Excel(name = "收件人姓名", width = 15)
    @Schema(description = "收件人姓名")
    private java.lang.String name;
	/**收件人手机号*/
	@Excel(name = "收件人手机号", width = 15)
    @Schema(description = "收件人手机号")
    private java.lang.String phone;
	/**所在地区*/
    @Excel(name = "所在地区", width = 15,exportConvert=true,importConvert = true )
    @Schema(description = "所在地区")
    private java.lang.String address;

    public String convertisAddress() {
        return SpringContextUtils.getBean(ProvinceCityArea.class).getText(address);
    }

    public void convertsetAddress(String text) {
        this.address = SpringContextUtils.getBean(ProvinceCityArea.class).getCode(text);
    }
	/**地址详情*/
	@Excel(name = "地址详情", width = 15)
    @Schema(description = "地址详情")
    private java.lang.String addressDetail;
	/**收件人昵称*/
	@Excel(name = "收件人昵称", width = 15)
    @Schema(description = "收件人昵称")
    private java.lang.String nickName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
}
