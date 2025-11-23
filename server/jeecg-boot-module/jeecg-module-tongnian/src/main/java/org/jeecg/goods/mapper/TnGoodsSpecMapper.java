package org.jeecg.goods.mapper;

import java.util.List;
import org.jeecg.goods.entity.TnGoodsSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 商品规格/价格明细
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
public interface TnGoodsSpecMapper extends BaseMapper<TnGoodsSpec> {

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
    * @return List<TnGoodsSpec>
    */
	public List<TnGoodsSpec> selectByMainId(@Param("mainId") String mainId);

}
