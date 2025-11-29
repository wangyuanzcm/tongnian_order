import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';

const { createConfirm } = useMessage();

// 获取购物车列表
export const cartList = () => {
  return defHttp.get<Array<any>>({ url: '/cart/tnCart/list' });
};

// 获取购物车数量
export const getCartCount = () => {
  return defHttp.get<number>({ url: '/cart/tnCart/count' });
};

// 添加到购物车
export const addToCart = (data: any) => {
  return defHttp.post<boolean>({ url: '/cart/tnCart/add', data });
};

// 更新购物车商品
export const updateCart = (data: any) => {
  return defHttp.put<boolean>({ url: `/cart/tnCart/updateQuantity/${data.id}/${data.quantity}` });
};

// 删除购物车商品
export const deleteCartItem = (params: { id: string }, successCallback?: Function) => {
  return createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '确定要删除该商品吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      const result = await defHttp.delete<boolean>({ url: `/cart/tnCart/delete/${params.id}` });
      if (successCallback) {
        successCallback();
      }
      return result;
    },
  });
};

// 批量删除购物车商品
export const batchDeleteCartItem = (params: { ids: Array<string> }, successCallback?: Function) => {
  return createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '确定要删除选中的商品吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      const result = await defHttp.delete<boolean>({ url: '/cart/tnCart/batchDelete', data: params.ids });
      if (successCallback) {
        successCallback();
      }
      return result;
    },
  });
};

// 清空购物车
export const clearCart = (params: any, successCallback?: Function) => {
  return createConfirm({
    iconType: 'warning',
    title: '确认清空',
    content: '确定要清空购物车吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      const result = await defHttp.delete<boolean>({ url: '/cart/tnCart/clear' });
      if (successCallback) {
        successCallback();
      }
      return result;
    },
  });
};

// 从购物车结算
export const checkoutFromCart = (params: { ids: Array<string> }) => {
  return createConfirm({
    iconType: 'info',
    title: '确认结算',
    content: `确定要结算选中的 ${params.ids.length} 件商品吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      return defHttp.post<boolean>({ url: '/cart/tnCart/checkout', data: params.ids });
    },
  });
};

/**
 * 购物车API
 */
export const CartApi = {
  // 获取购物车列表
  getCartList: cartList,

  // 获取购物车数量
  getCartCount: getCartCount,

  // 添加到购物车
  addToCart: addToCart,

  // 更新购物车商品数量
  updateCartItemQuantity: (id: string, quantity: number) => {
    return updateCart({ id, quantity });
  },

  // 删除购物车商品
  deleteCartItem: (id: string) => {
    return deleteCartItem({ id });
  },

  // 批量删除购物车商品
  batchDeleteCartItems: (ids: Array<string>) => {
    return batchDeleteCartItem({ ids });
  },

  // 清空购物车
  clearCart: () => {
    return clearCart({});
  },

  // 从购物车结算
  checkoutFromCart: (ids: Array<string>) => {
    return checkoutFromCart({ ids });
  },
};
