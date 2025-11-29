<template>
 <div>
     <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :searchInfo="searchInfo">
      <!--插槽:table标题-->
       <template #tableTitle>
           <a-button type="primary" @click="handleCreate" preIcon="ant-design:plus-outlined" v-if="mainId!=''"> 新增</a-button>
           <a-button  type="primary" preIcon="ant-design:export-outlined" @click="onExportXls" v-if="mainId!=''"> 导出</a-button>
           <j-upload-button  type="primary" preIcon="ant-design:import-outlined" @click="onImportXls" v-if="mainId!=''">导入</j-upload-button>
           <a-dropdown v-if="selectedRowKeys.length > 0">
               <template #overlay>
                 <a-menu>
                   <a-menu-item key="1" @click="batchHandleDelete">
                     <Icon icon="ant-design:delete-outlined"></Icon>
                     删除
                   </a-menu-item>
                 </a-menu>
               </template>
               <a-button>批量操作
                 <Icon icon="mdi:chevron-down"></Icon>
               </a-button>
         </a-dropdown>
       </template>
        <!--操作栏-->
       <template #action="{ record }">
         <TableAction :actions="getTableAction(record)"/>
       </template>
       <!--字段回显插槽-->       
       <template v-slot:bodyCell="{ column, record, index, text }">
       </template>
       <!-- 图片列表插槽 -->
       <template #imageList="{ record }">
         <div v-if="record.imageList && Array.isArray(record.imageList) && record.imageList.length > 0">
           <div class="image-preview-list">
             <div v-for="(item, index) in record.imageList" :key="item.id || index" class="image-preview-item">
               <a-image
                 :width="80"
                 :src="item.url"
                 :preview="{ visible: false }"
                 @click="previewImage(item.url)"
               >
                 <template #placeholder>
                   <div class="image-placeholder">
                     <LoadingOutlined />
                   </div>
                 </template>
               </a-image>
             </div>
           </div>
         </div>
         <div v-else-if="record.imageUrlString && typeof record.imageUrlString === 'string' && record.imageUrlString.trim() !== ''">
           <div class="image-preview-list">
             <div v-for="(url, index) in record.imageUrlString.split(',')" :key="index" class="image-preview-item">
               <a-image
                 :width="80"
                 :src="url"
                 :preview="{ visible: false }"
                 @click="previewImage(url)"
               >
                 <template #placeholder>
                   <div class="image-placeholder">
                     <LoadingOutlined />
                   </div>
                 </template>
               </a-image>
             </div>
           </div>
         </div>
         <span v-else>--</span>
         <!-- 图片预览弹窗 -->
         <a-modal
           :visible="previewVisible"
           :footer="null"
           @cancel="previewVisible = false"
           width="80%"
         >
           <img :src="previewImageUrl" style="width: 100%" alt="预览图片" />
         </a-modal>
       </template>
     </BasicTable>

     <!-- 购买数量选择抽屉 -->
     <a-drawer
       title="购买商品"
       :width="400"
       :visible="buyDrawerVisible"
       :footer="null"
       @close="handleCloseDrawer"
     >
       <div class="buy-drawer-content">
         <div class="goods-info">
           <div v-if="selectedGoods.imageList && Array.isArray(selectedGoods.imageList) && selectedGoods.imageList.length > 0" class="goods-image">
             <a-image
               :width="100"
               :src="selectedGoods.imageList[0].url"
               :preview="{ visible: false }"
             />
           </div>
           <div v-else-if="selectedGoods.imageUrlString" class="goods-image">
             <a-image
               :width="100"
               :src="selectedGoods.imageUrlString.split(',')[0]"
               :preview="{ visible: false }"
             />
           </div>
           <div class="goods-details">
             <div class="goods-spec">{{ selectedGoods.specType }}</div>
             <div class="goods-price">¥{{ selectedGoods.price }}</div>
           </div>
         </div>
         
         <div class="quantity-section">
           <span class="quantity-label">购买数量：</span>
           <div class="quantity-control">
             <a-button 
               size="small" 
               :disabled="buyQuantity <= 1"
               @click="buyQuantity--"
             >
               -
             </a-button>
             <input 
               type="number" 
               v-model.number="buyQuantity" 
               min="1" 
               class="quantity-input"
             />
             <a-button size="small" @click="buyQuantity++">
               +
             </a-button>
           </div>
         </div>
         
         <div class="drawer-actions">
           <a-button @click="handleCloseDrawer">取消</a-button>
           <a-button type="primary" @click="handleAddToCart">加入购物车</a-button>
         </div>
       </div>
     </a-drawer>

      <TnGoodsSpecModal @register="registerModal" @success="handleSuccess"/>
   </div>
</template>

<script lang="ts" setup>
  import {ref, computed, unref,inject,watch, onMounted, onUnmounted} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage'
  import {useModal} from '/@/components/Modal';
  import TnGoodsSpecModal from './components/TnGoodsSpecModal.vue'
  import {tnGoodsSpecColumns} from './TnGoods.data';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
  import {tnGoodsSpecList, tnGoodsSpecDelete, tnGoodsSpecDeleteBatch, tnGoodsSpecExportXlsUrl, tnGoodsSpecImportUrl } from './TnGoods.api';
  import {isEmpty} from "/@/utils/is";
  import {useMessage} from '/@/hooks/web/useMessage';
  import {downloadFile} from '/@/utils/common/renderUtils';
  import { Image as AImage } from 'ant-design-vue';
  import { Modal as AModal } from 'ant-design-vue';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { addToCart } from '../cart/Cart.api';

    //接收主表id
    const mainId = inject('mainId') || '';
    //提示弹窗
    const $message = useMessage()
    //弹窗model
    const [registerModal, {openModal}] = useModal();
    const searchInfo = {};
    
    // 图片预览相关
    const previewVisible = ref(false);
    const previewImageUrl = ref('');
    
    // 预览图片方法
    function previewImage(url) {
      previewImageUrl.value = url;
      previewVisible.value = true;
    }
    
    // 购买相关状态
    const buyDrawerVisible = ref(false);
    const selectedGoods = ref({});
    const buyQuantity = ref(1);
    
    // 监听购买事件
    function handleBuyGoods(event) {
      const goods = event.detail;
      selectedGoods.value = goods;
      buyQuantity.value = 1;
      buyDrawerVisible.value = true;
    }
    
    // 关闭购买抽屉
    function handleCloseDrawer() {
      buyDrawerVisible.value = false;
    }
    
    // 添加到购物车
    async function handleAddToCart() {
      try {
        const goods = selectedGoods.value;
        const data = {
          goodsId: goods.id,
          specId: goods.id,
          quantity: buyQuantity.value,
          goodsName: goods.specType, // 使用规格类型作为商品名称
          price: goods.price,
          specType: goods.specType
        };
        
        const result = await addToCart(data);
        if (result) {
          $message.success('已成功加入购物车');
          handleCloseDrawer();
        } else {
          $message.error('加入购物车失败');
        }
      } catch (error) {
        console.error('加入购物车失败:', error);
        $message.error('加入购物车失败，请稍后重试');
      }
    }
    
    // 组件挂载时添加事件监听
    onMounted(() => {
      window.addEventListener('buyGoods', handleBuyGoods);
    });
    
    // 组件卸载时移除事件监听
    onUnmounted(() => {
      window.removeEventListener('buyGoods', handleBuyGoods);
    });
    // 列表页面公共参数、方法
    const {prefixCls, tableContext, onImportXls, onExportXls} = useListPage({
        tableProps: {
            api: tnGoodsSpecList,
            columns: tnGoodsSpecColumns,
            canResize: true,
            useSearchForm: false,
            actionColumn: {
                width: 180,
                fixed:'right'
            },
            pagination:{
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['5', '10', '20'],
            },
            // 添加排序配置，按照创建时间和更新时间倒序排列
            sorter: {
                defaultSortOrder: 'desc',
                field: 'createTime', // 主要排序字段
            },
            // 在API参数中添加排序信息
            beforeFetch: (params) => {
                // 确保排序参数被正确添加到API请求中
                params.orderByColumn = 'create_time'; // 后端字段名通常使用下划线格式
                params.isAsc = 'desc'; // 倒序排列
                return params;
            }
        },
        exportConfig: {
            name: '商品规格/价格明细',
            url: tnGoodsSpecExportXlsUrl,
            params: {
                'goodsId': mainId
            }
        },
        importConfig: {
            url: ()=>{
                return tnGoodsSpecImportUrl + '/' + unref(mainId)
            }
        }
    });

    //注册table数据
    const [registerTable, {reload}, {rowSelection, selectedRowKeys}] = tableContext;

    watch(mainId, () => {
         searchInfo['goodsId'] = unref(mainId);
         reload();
      }
    );

    /**
     * 新增事件
     */
    function handleCreate() {
        if (isEmpty(unref(mainId))) {
            $message.createMessage.warning('请选择一个主表信息')
            return;
        }
        openModal(true, {
            isUpdate: false,
            showFooter: true,
        });
    }

    /**
     * 编辑事件
     */
    async function handleEdit(record: Recordable) {
        openModal(true, {
            record,
            isUpdate: true,
            showFooter: true,
        });
    }

    /**
     * 删除事件
     */
    async function handleDelete(record) {
        await tnGoodsSpecDelete({id: record.id}, handleSuccess);
    }

    /**
     * 批量删除事件
     */
    async function batchHandleDelete() {
        await tnGoodsSpecDeleteBatch({ids: selectedRowKeys.value}, handleSuccess)
    }

    /**
     * 成功回调
     */
    function handleSuccess() {
        (selectedRowKeys.value = []) && reload();
    }

    /**
     * 操作栏
     */
    function getTableAction(record) {
        return [
            {
                label: '编辑',
                onClick: handleEdit.bind(null, record),
            }, {
                label: '删除',
                popConfirm: {
                    title: '是否确认删除',
                    confirm: handleDelete.bind(null, record),
                    placement: 'topLeft',
                },
            }
        ]
    }
</script>

<style scoped>
  .image-preview-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .image-preview-item {
    cursor: pointer;
  }
  
  .image-placeholder {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 80px;
    background-color: #f5f5f5;
    color: #999;
  }
  
  /* 购买抽屉样式 */
  .buy-drawer-content {
    padding: 20px;
  }
  
  .goods-info {
    display: flex;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .goods-image {
    margin-right: 20px;
  }
  
  .goods-details {
    flex: 1;
  }
  
  .goods-spec {
    font-size: 16px;
    margin-bottom: 10px;
  }
  
  .goods-price {
    font-size: 20px;
    font-weight: bold;
    color: #ff4d4f;
  }
  
  .quantity-section {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
  }
  
  .quantity-label {
    margin-right: 20px;
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
  
  .drawer-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 30px;
  }
</style>
