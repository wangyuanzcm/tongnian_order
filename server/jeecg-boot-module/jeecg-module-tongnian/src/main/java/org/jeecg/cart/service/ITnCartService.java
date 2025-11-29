package org.jeecg.cart.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
public interface ITnCartService extends IService<TnCart> {

    /**
     * 查询购物车列表
     * @param params
     * @return
     */
    List<TnCartVO> queryCartList(Map<String, Object> params);

    /**
     * 查询购物车数量
     * @param userId
     * @return
     */
    Integer queryCartCount(String userId);

    /**
     * 添加商品到购物车
     * @param cart
     * @return
     */
    boolean addToCart(TnCart cart);

    /**
     * 更新购物车商品数量
     * @param id
     * @param quantity
     * @return
     */
    boolean updateCartItemQuantity(String id, Integer quantity);

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    boolean deleteCartItem(String id);

    /**
     * 批量删除购物车商品
     * @param ids
     * @return
     */
    boolean batchDeleteCartItems(List<String> ids);

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    boolean clearCart(String userId);
    
    /**
     * 从购物车结算创建订单
     * @param cartIds 购物车商品ID列表
     * @param userId 用户ID
     */
    void checkoutFromCart(List<String> cartIds, String userId);
}
