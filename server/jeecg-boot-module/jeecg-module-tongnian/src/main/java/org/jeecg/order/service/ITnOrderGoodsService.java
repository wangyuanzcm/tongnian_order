package org.jeecg.order.service;

import org.jeecg.order.entity.TnOrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单商品关联表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
public interface ITnOrderGoodsService extends IService<TnOrderGoods> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TnOrderGoods>
	 */
	public List<TnOrderGoods> selectByMainId(String mainId);
}
