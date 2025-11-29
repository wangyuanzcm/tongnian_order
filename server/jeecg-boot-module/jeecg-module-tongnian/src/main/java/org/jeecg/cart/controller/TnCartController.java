package org.jeecg.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.cart.entity.TnCart;
import org.jeecg.cart.service.ITnCartService;
import org.jeecg.cart.vo.TnCartVO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date: 2024-01-16
 * @Version: V1.0
 */
@Tag(name = "购物车")
@RestController
@RequestMapping("/cart/tnCart")
@Slf4j
public class TnCartController {

    @Autowired
    private ITnCartService tnCartService;

    /**
     * 查询购物车列表
     * @param request
     * @return
     */
    @Operation(summary="查询购物车列表", description="查询购物车列表")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(HttpServletRequest request) {
        try {
            // 获取当前用户ID
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String userId = loginUser.getUsername();
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);

            List<TnCartVO> cartList = tnCartService.queryCartList(params);
            return Result.OK(cartList);
        } catch (Exception e) {
            log.error("查询购物车列表失败", e);
            return Result.error("查询购物车列表失败");
        }
    }

    /**
     * 获取购物车数量
     * @return
     */
    @Operation(summary="获取购物车数量", description="获取购物车数量")
    @GetMapping(value = "/count")
    public Result<?> queryCartCount() {
        try {
            // 获取当前用户ID
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String userId = loginUser.getUsername();
            Integer count = tnCartService.queryCartCount(userId);
            return Result.OK(count);
        } catch (Exception e) {
            log.error("获取购物车数量失败", e);
            return Result.error("获取购物车数量失败");
        }
    }

    /**
     * 添加商品到购物车
     * @param cart
     * @return
     */
    @Operation(summary="添加商品到购物车", description="添加商品到购物车")
    @PostMapping(value = "/add")
    public Result<?> addToCart(@RequestBody TnCart cart) {
        try {
            // 设置当前用户ID
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String userId = loginUser.getUsername();
            cart.setUserId(userId);
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());

            boolean result = tnCartService.addToCart(cart);
            if (result) {
                return Result.OK("添加到购物车成功");
            } else {
                return Result.error("添加到购物车失败");
            }
        } catch (Exception e) {
            log.error("添加到购物车失败", e);
            return Result.error("添加到购物车失败");
        }
    }

    /**
     * 更新购物车商品数量
     * @param id
     * @param quantity
     * @return
     */
    @Operation(summary="更新购物车商品数量", description="更新购物车商品数量")
    @PutMapping(value = "/updateQuantity/{id}/{quantity}")
    public Result<?> updateCartItemQuantity(@PathVariable String id, @PathVariable Integer quantity) {
        try {
            boolean result = tnCartService.updateCartItemQuantity(id, quantity);
            if (result) {
                return Result.OK("更新购物车商品数量成功");
            } else {
                return Result.error("更新购物车商品数量失败");
            }
        } catch (Exception e) {
            log.error("更新购物车商品数量失败", e);
            return Result.error("更新购物车商品数量失败");
        }
    }

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    @Operation(summary="删除购物车商品", description="删除购物车商品")
    @DeleteMapping(value = "/delete/{id}")
    public Result<?> deleteCartItem(@PathVariable String id) {
        try {
            boolean result = tnCartService.deleteCartItem(id);
            if (result) {
                return Result.OK("删除购物车商品成功");
            } else {
                return Result.error("删除购物车商品失败");
            }
        } catch (Exception e) {
            log.error("删除购物车商品失败", e);
            return Result.error("删除购物车商品失败");
        }
    }

    /**
     * 批量删除购物车商品
     * @param ids
     * @return
     */
    @Operation(summary="批量删除购物车商品", description="批量删除购物车商品")
    @DeleteMapping(value = "/batchDelete")
    public Result<?> batchDeleteCartItems(@RequestBody List<String> ids) {
        try {
            boolean result = tnCartService.batchDeleteCartItems(ids);
            if (result) {
                return Result.OK("批量删除购物车商品成功");
            } else {
                return Result.error("批量删除购物车商品失败");
            }
        } catch (Exception e) {
            log.error("批量删除购物车商品失败", e);
            return Result.error("批量删除购物车商品失败");
        }
    }

    /**
     * 清空购物车
     * @return
     */
    @Operation(summary="清空购物车", description="清空购物车")
    @DeleteMapping(value = "/clear")
    public Result<?> clearCart() {
        try {
            // 获取当前用户ID
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String userId = loginUser.getUsername();
            boolean result = tnCartService.clearCart(userId);
            if (result) {
                return Result.OK("清空购物车成功");
            } else {
                return Result.error("清空购物车失败");
            }
        } catch (Exception e) {
            log.error("清空购物车失败", e);
            return Result.error("清空购物车失败");
        }
    }

    @Operation(summary="从购物车结算")
    @PostMapping("/checkout")
    public Result<?> checkout(@RequestBody List<String> cartIds) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        tnCartService.checkoutFromCart(cartIds, loginUser.getUsername());
        return Result.OK();
    }
}
