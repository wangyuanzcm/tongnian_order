package org.jeecg.goods.service.impl;

import org.jeecg.goods.entity.TnGoodsSpec;
import org.jeecg.goods.mapper.TnGoodsSpecMapper;
import org.jeecg.goods.service.ITnGoodsSpecService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 商品规格/价格明细
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Service
public class TnGoodsSpecServiceImpl extends ServiceImpl<TnGoodsSpecMapper, TnGoodsSpec> implements ITnGoodsSpecService {
	
	@Autowired
	private TnGoodsSpecMapper tnGoodsSpecMapper;
	
	@Override
	public List<TnGoodsSpec> selectByMainId(String mainId) {
		return tnGoodsSpecMapper.selectByMainId(mainId);
	}
}
