package org.jeecg.cart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.cart.entity.TnCart;
import org.jeecg.cart.mapper.TnCartMapper;
import org.jeecg.cart.service.ITnCartService;
import org.jeecg.cart.vo.TnCartVO;
import org.jeecg.order.entity.TnOrder;
import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.order.service.ITnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2024-01-16
 * @Version: V1.0
 */
@Service
public class TnCartServiceImpl extends ServiceImpl<TnCartMapper, TnCart> implements ITnCartService {

    @Autowired
    private TnCartMapper tnCartMapper;
    @Autowired
    private ITnOrderService tnOrderService;

    /**
     * 查询购物车列表
     * @param params
     * @return
     */
    @Override
    public List<TnCartVO> queryCartList(Map<String, Object> params) {
        List<TnCartVO> cartList = tnCartMapper.queryCartList(params);
        // 计算每个商品的总价
        for (TnCartVO cart : cartList) {
            cart.calculateTotalPrice();
        }
        return cartList;
    }

    /**
     * 查询购物车数量
     * @param userId
     * @return
     */
    @Override
    public Integer queryCartCount(String userId) {
        return tnCartMapper.queryCartCount(userId);
    }

    /**
     * 添加商品到购物车
     * @param cart
     * @return
     */
    @Override
    public boolean addToCart(TnCart cart) {
        // 检查是否已存在相同商品和规格的购物车项
        TnCart existingCart = tnCartMapper.queryByGoodsIdAndSpecType(
                cart.getGoodsId(), cart.getSpecType(), cart.getUserId());

        if (existingCart != null) {
            // 如果已存在，更新数量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            return updateById(existingCart);
        } else {
            // 如果不存在，添加新记录
            return save(cart);
        }
    }

    /**
     * 更新购物车商品数量
     * @param id
     * @param quantity
     * @return
     */
    @Override
    public boolean updateCartItemQuantity(String id, Integer quantity) {
        TnCart cart = getById(id);
        if (cart != null) {
            cart.setQuantity(quantity);
            return updateById(cart);
        }
        return false;
    }

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    @Override
    public boolean deleteCartItem(String id) {
        return removeById(id);
    }

    /**
     * 批量删除购物车商品
     * @param ids
     * @return
     */
    @Override
    public boolean batchDeleteCartItems(List<String> ids) {
        return removeByIds(ids);
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @Override
    public boolean clearCart(String userId) {
        tnCartMapper.clearCart(userId);
        return true;
    }
    
    /**
     * 从购物车结算创建订单
     * @param cartIds 购物车商品ID列表
     * @param userId 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkoutFromCart(List<String> cartIds, String userId) {
        // 1. 获取购物车商品列表
        List<TnCart> cartList = this.listByIds(cartIds);
        if (cartList == null || cartList.isEmpty()) {
            throw new RuntimeException("购物车商品不存在");
        }
        
        // 2. 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TnOrderGoods> orderGoodsList = new ArrayList<>();
        
        // 3. 准备订单商品数据
        for (TnCart cart : cartList) {
            // 计算总金额
            totalAmount = totalAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            
            // 创建订单商品记录
            TnOrderGoods orderGoods = new TnOrderGoods();
            orderGoods.setGoodsSpec(cart.getGoodsId());
            orderGoods.setGoodsName(cart.getGoodsName());
            orderGoods.setUnitPrice(cart.getPrice());
            orderGoods.setQuantity(cart.getQuantity());
            orderGoods.setCreateBy(userId);
            orderGoods.setCreateTime(new Date());
            orderGoods.setUpdateBy(userId);
            orderGoods.setUpdateTime(new Date());
            
            orderGoodsList.add(orderGoods);
        }
        
        // 4. 创建订单主表记录
        TnOrder order = new TnOrder();
        order.setOrderNo(generateOrderNo());
        order.setCreateBy(userId);
        order.setCreateTime(new Date());
        order.setUpdateBy(userId);
        order.setUpdateTime(new Date());
        order.setPayableAmount(totalAmount);
        order.setPaidAmount(totalAmount); // 假设直接支付
        order.setOrderStatus(1); // 订单状态：1-待处理
        order.setIsNewCustomer(0); // 假设不是新客户
        
        // 5. 保存订单和订单商品
        tnOrderService.saveMain(order, orderGoodsList);
        
        // 6. 删除已结算的购物车商品
        this.batchDeleteCartItems(cartIds);
    }
    
    /**
     * 生成订单编号
     * @return
     */
    private String generateOrderNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}
