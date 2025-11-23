package org.jeecg.order.mapper;

import java.util.List;
import org.jeecg.order.entity.TnOrderGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单商品关联表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
public interface TnOrderGoodsMapper extends BaseMapper<TnOrderGoods> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<TnOrderGoods>
   */
	public List<TnOrderGoods> selectByMainId(@Param("mainId") String mainId);
}
