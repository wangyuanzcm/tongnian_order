package org.jeecg.goods.service;

import org.jeecg.goods.entity.TnGoodsSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 商品规格/价格明细
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
public interface ITnGoodsSpecService extends IService<TnGoodsSpec> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<TnGoodsSpec>
   */
	public List<TnGoodsSpec> selectByMainId(String mainId);
  
  /**
   * 保存商品规格及图片信息
   *
   * @param tnGoodsSpec 商品规格对象
   * @return boolean
   */
  @Override
  boolean save(TnGoodsSpec tnGoodsSpec);
  
  /**
   * 更新商品规格及图片信息
   *
   * @param tnGoodsSpec 商品规格对象
   * @return boolean
   */
  @Override
  boolean updateById(TnGoodsSpec tnGoodsSpec);
}
