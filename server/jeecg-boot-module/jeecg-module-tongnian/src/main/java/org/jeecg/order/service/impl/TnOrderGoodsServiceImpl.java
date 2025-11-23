package org.jeecg.order.service.impl;

import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.order.mapper.TnOrderGoodsMapper;
import org.jeecg.order.service.ITnOrderGoodsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单商品关联表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Service
public class TnOrderGoodsServiceImpl extends ServiceImpl<TnOrderGoodsMapper, TnOrderGoods> implements ITnOrderGoodsService {
	
	@Autowired
	private TnOrderGoodsMapper tnOrderGoodsMapper;
	
	@Override
	public List<TnOrderGoods> selectByMainId(String mainId) {
		return tnOrderGoodsMapper.selectByMainId(mainId);
	}
}
