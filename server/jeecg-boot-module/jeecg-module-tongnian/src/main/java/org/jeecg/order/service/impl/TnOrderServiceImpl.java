package org.jeecg.order.service.impl;

import org.jeecg.order.entity.TnOrder;
import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.order.mapper.TnOrderGoodsMapper;
import org.jeecg.order.mapper.TnOrderMapper;
import org.jeecg.order.service.ITnOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Service
public class TnOrderServiceImpl extends ServiceImpl<TnOrderMapper, TnOrder> implements ITnOrderService {

	@Autowired
	private TnOrderMapper tnOrderMapper;
	@Autowired
	private TnOrderGoodsMapper tnOrderGoodsMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(TnOrder tnOrder, List<TnOrderGoods> tnOrderGoodsList) {
		tnOrderMapper.insert(tnOrder);
		if(tnOrderGoodsList!=null && tnOrderGoodsList.size()>0) {
			for(TnOrderGoods entity:tnOrderGoodsList) {
				//外键设置
				entity.setOrderId(tnOrder.getId());
				tnOrderGoodsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(TnOrder tnOrder,List<TnOrderGoods> tnOrderGoodsList) {
		tnOrderMapper.updateById(tnOrder);
		
		//1.先删除子表数据
		tnOrderGoodsMapper.deleteByMainId(tnOrder.getId());
		
		//2.子表数据重新插入
		if(tnOrderGoodsList!=null && tnOrderGoodsList.size()>0) {
			for(TnOrderGoods entity:tnOrderGoodsList) {
				//外键设置
				entity.setOrderId(tnOrder.getId());
				tnOrderGoodsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		tnOrderGoodsMapper.deleteByMainId(id);
		tnOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			tnOrderGoodsMapper.deleteByMainId(id.toString());
			tnOrderMapper.deleteById(id);
		}
	}
	
}
