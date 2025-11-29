import type { AppRouteRecordRaw } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const cart: AppRouteRecordRaw = {
  path: '/cart',
  name: 'Cart',
  component: LAYOUT,
  redirect: '/cart/index',
  meta: {
    icon: 'ant-design:shopping-cart-outlined',
    title: t('routes.cart.cart'),
    orderNo: 10,
  },
  children: [
    {
      path: 'index',
      name: 'CartIndex',
      component: () => import('/@/views/cart/index.vue'),
      meta: {
        title: t('routes.cart.cartList'),
        icon: 'ant-design:shopping-cart-outlined',
      },
    },
  ],
};

export default cart;
