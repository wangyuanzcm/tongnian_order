package org.jeecg.goods.service.impl;

import org.jeecg.goods.entity.TnGoods;
import org.jeecg.goods.entity.TnGoodsSpec;
import org.jeecg.goods.mapper.TnGoodsSpecMapper;
import org.jeecg.goods.mapper.TnGoodsMapper;
import org.jeecg.goods.service.ITnGoodsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Service
public class TnGoodsServiceImpl extends ServiceImpl<TnGoodsMapper, TnGoods> implements ITnGoodsService {

	@Autowired
	private TnGoodsMapper tnGoodsMapper;
	@Autowired
	private TnGoodsSpecMapper tnGoodsSpecMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		tnGoodsSpecMapper.deleteByMainId(id);
		tnGoodsMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			tnGoodsSpecMapper.deleteByMainId(id.toString());
			tnGoodsMapper.deleteById(id);
		}
	}
	
}
