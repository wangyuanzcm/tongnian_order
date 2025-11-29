package org.jeecg.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.cart.entity.TnCart;
import org.jeecg.cart.vo.TnCartVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2024-01-16
 * @Version: V1.0
 */
public interface TnCartMapper extends BaseMapper<TnCart> {

    /**
     * 查询购物车列表
     * @param params
     * @return
     */
    List<TnCartVO> queryCartList(@Param("params") Map<String, Object> params);

    /**
     * 查询购物车数量
     * @param userId
     * @return
     */
    Integer queryCartCount(@Param("userId") String userId);

    /**
     * 根据商品ID和规格类型查询购物车项
     * @param goodsId
     * @param specType
     * @param userId
     * @return
     */
    TnCart queryByGoodsIdAndSpecType(@Param("goodsId") String goodsId, @Param("specType") String specType, @Param("userId") String userId);

    /**
     * 清空购物车
     * @param userId
     */
    void clearCart(@Param("userId") String userId);
}
