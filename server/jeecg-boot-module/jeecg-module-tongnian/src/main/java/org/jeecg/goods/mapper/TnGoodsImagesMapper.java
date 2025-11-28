package org.jeecg.goods.mapper;

import org.jeecg.goods.entity.TnGoodsImages;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: 商品图片映射表
 * @Author: jeecg-boot
 * @Date:   2025-11-25
 * @Version: V1.0
 */
public interface TnGoodsImagesMapper extends BaseMapper<TnGoodsImages> {

    /**
     * 通过商品规格ID查询图片ID列表
     * 
     * @param goodsSpecId 商品规格ID
     * @return List<String> 图片ID列表
     */
    List<String> selectImageIdsByGoodsSpecId(@Param("goodsSpecId") String goodsSpecId);

    /**
     * 通过商品规格ID删除关联关系
     * 
     * @param goodsSpecId 商品规格ID
     * @return boolean
     */
    boolean deleteByGoodsSpecId(@Param("goodsSpecId") String goodsSpecId);
}
