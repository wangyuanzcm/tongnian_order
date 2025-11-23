package org.jeecg.order.service;

import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.order.entity.TnOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
public interface ITnOrderService extends IService<TnOrder> {

	/**
	 * 添加一对多
	 *
	 * @param tnOrder
	 * @param tnOrderGoodsList
	 */
	public void saveMain(TnOrder tnOrder,List<TnOrderGoods> tnOrderGoodsList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param tnOrder
	 * @param tnOrderGoodsList
	 */
	public void updateMain(TnOrder tnOrder,List<TnOrderGoods> tnOrderGoodsList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
