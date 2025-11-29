<template>
  <div class="cart-container">
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:delete-outlined" @click="handleClearCart" :disabled="selectedRowKeys.length === 0">
          清空购物车
        </a-button>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell="{ column, record, index, text }">
        <!-- 数量调整 -->
        <template v-if="column.dataIndex === 'quantity'">
          <div class="quantity-control">
            <a-button 
              size="small" 
              :disabled="record.quantity <= 1"
              @click="updateQuantity(record, record.quantity - 1)"
            >
              -
            </a-button>
            <input 
              type="number" 
              v-model.number="record.quantity" 
              min="1" 
              class="quantity-input"
              @change="updateQuantity(record, record.quantity)"
            />
            <a-button size="small" @click="updateQuantity(record, record.quantity + 1)">
              +
            </a-button>
          </div>
        </template>
        <!-- 图片显示 -->
        <template v-else-if="column.dataIndex === 'imageUrl'">
          <a-image
            :width="60"
            :src="record.imageUrl"
            :preview="{ visible: false }"
            @click="previewImage(record.imageUrl)"
          >
            <template #placeholder>
              <div class="image-placeholder">
                <LoadingOutlined />
              </div>
            </template>
          </a-image>
        </template>
        <!-- 总价计算 -->
        <template v-else-if="column.dataIndex === 'totalPrice'">
          ¥{{ (record.price * record.quantity).toFixed(2) }}
        </template>
      </template>
      <!-- 底部统计信息 -->
      <template #footer>
        <div class="cart-footer">
          <div class="total-info">
            <span>已选 {{ selectedRowKeys.length }} 件商品</span>
            <span class="total-price">合计：¥{{ calculateTotalPrice() }}</span>
          </div>
          <a-button type="primary" size="large" :disabled="selectedRowKeys.length === 0" @click="handleCheckout">
            结算
          </a-button>
        </div>
      </template>
    </BasicTable>

    <!-- 图片预览弹窗 -->
    <a-modal
      :visible="previewVisible"
      :footer="null"
      @cancel="previewVisible = false"
      width="80%"
    >
      <img :src="previewImageUrl" style="width: 100%" alt="预览图片" />
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, unref } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage'
import { cartList, updateCart, deleteCartItem, batchDeleteCartItem, clearCart, checkoutFromCart } from './Cart.api';
import { useMessage } from '/@/hooks/web/useMessage';
import { Image as AImage } from 'ant-design-vue';
import { Modal as AModal } from 'ant-design-vue';
import { Drawer as ADrawer } from 'ant-design-vue';
import { LoadingOutlined } from '@ant-design/icons-vue';

// 提示弹窗
const $message = useMessage()
// 图片预览相关
const previewVisible = ref(false);
const previewImageUrl = ref('');

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  tableProps: {
    api: cartList,
    columns: cartColumns,
    canResize: true,
    useSearchForm: false,
    actionColumn: {
      width: 80,
      fixed: 'right'
    },
    pagination: {
      current: 1,
      pageSize: 10,
      pageSizeOptions: ['10', '20', '50'],
    },
  },
});

// 注册table数据
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

// 预览图片方法
function previewImage(url) {
  previewImageUrl.value = url;
  previewVisible.value = true;
}

/**
 * 更新数量
 */
async function updateQuantity(record, quantity) {
  if (quantity < 1) quantity = 1;
  try {
    await updateCart({
      id: record.id,
      quantity: quantity
    });
    record.quantity = quantity;
    reload();
  } catch (error) {
    $message.error('更新数量失败');
  }
}

/**
 * 删除事件
 */
async function handleDelete(record) {
  await deleteCartItem({ id: record.id }, handleSuccess);
}

/**
 * 批量删除事件
 */
async function batchHandleDelete() {
  await batchDeleteCartItem({ ids: selectedRowKeys.value }, handleSuccess)
}

/**
 * 清空购物车
 */
async function handleClearCart() {
  await clearCart({}, handleSuccess);
}

/**
 * 成功回调
 */
function handleSuccess() {
  (selectedRowKeys.value = []) && reload();
}

/**
 * 计算总价
 */
function calculateTotalPrice() {
  let total = 0;
  const selectedRows = tableContext[2].selectedRows.value || [];
  selectedRows.forEach(row => {
    total += row.price * row.quantity;
  });
  return total.toFixed(2);
}

/**
 * 结算功能
 */
async function handleCheckout() {
  const selectedRows = tableContext[2].selectedRows.value || [];
  if (selectedRows.length === 0) {
    $message.warning('请选择要结算的商品');
    return;
  }
  
  try {
    const result = await checkoutFromCart({ ids: selectedRowKeys.value });
    if (result) {
      $message.success('订单创建成功');
      // 清空选择并刷新购物车列表
      selectedRowKeys.value = [];
      reload();
    } else {
      $message.error('订单创建失败');
    }
  } catch (error) {
    console.error('结算失败:', error);
    $message.error('结算失败，请稍后重试');
  }
}

/**
 * 操作栏
 */
function getTableAction(record) {
  return [
    {
      label: '删除',
      popConfirm: {
        title: '是否确认删除',
        confirm: handleDelete.bind(null, record),
        placement: 'topLeft',
      },
    }
  ]
}

// 购物车列定义
const cartColumns = [
  {
    title: '商品图片',
    align: 'center',
    dataIndex: 'imageUrl',
    width: 80
  },
  {
    title: '商品名称',
    align: 'center',
    dataIndex: 'goodsName',
  },
  {
    title: '商品规格',
    align: 'center',
    dataIndex: 'specType',
  },
  {
    title: '单价',
    align: 'center',
    dataIndex: 'price',
    customRender: ({ text }) => {
      return `¥${text}`;
    }
  },
  {
    title: '数量',
    align: 'center',
    dataIndex: 'quantity',
    width: 120
  },
  {
    title: '总价',
    align: 'center',
    dataIndex: 'totalPrice',
    customRender: ({ record }) => {
      return `¥${(record.price * record.quantity).toFixed(2)}`;
    }
  }
];
</script>

<style scoped>
.cart-container {
  padding: 20px;
}

.quantity-control {
  display: flex;
  align-items: center;
}

.quantity-input {
  width: 60px;
  text-align: center;
  margin: 0 8px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 4px;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background-color: #fafafa;
  border-top: 1px solid #f0f0f0;
}

.total-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.total-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4d4f;
}

.image-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  background-color: #f5f5f5;
  color: #999;
}
</style>
