<template>
  <div>
      <!--引用表格-->
     <BasicTable bordered size="middle" :loading="loading" rowKey="id" :canResize="true" :columns="tnOrderGoodsColumns" :dataSource="dataSource" :pagination="false">
        <!--字段回显插槽-->
        <template v-slot:bodyCell="{ column, record, index, text }">
          <!--商品规格展示-->
          <template v-if="column.dataIndex === 'goodsSpec'">
            {{ record.goodsSpec || '-' }}
          </template>
          <!--商品小计（单价*数量）-->
          <template v-if="column.dataIndex === 'subtotal'">
            ¥{{ (record.unitPrice * record.quantity).toFixed(2) }}
          </template>
        </template>
      </BasicTable>
    </div>
</template>

<script lang="ts" setup>
  import {ref,watchEffect,computed} from 'vue';
  import {BasicTable} from '/@/components/Table';
  import {tnOrderGoodsColumns} from '../TnOrder.data';
  import {tnOrderGoodsList} from '../TnOrder.api';
  import { downloadFile } from '/@/utils/common/renderUtils';

  const props = defineProps({
    id: {
       type: String,
       default: '',
     },
    // 直接从主表传递的订单商品列表数据
    tnOrderGoodsList: {
      type: Array,
      default: () => [],
    },
  })

  const loading = ref(false);
  const dataSource = ref([]);

  // 优先使用主表传递的数据，如果没有则通过API获取
  watchEffect(() => {
    if (props.tnOrderGoodsList && props.tnOrderGoodsList.length > 0) {
      // 使用主表传递的数据
      dataSource.value = props.tnOrderGoodsList;
    } else if (props.id) {
      // 兼容处理：如果没有传递数据，则通过API获取
      loadData(props.id);
    } else {
      dataSource.value = [];
    }
  });

   function loadData(id) {
         dataSource.value = []
         loading.value = true
          tnOrderGoodsList({id}).then((res) => {
           if (res.success) {
              dataSource.value = res.result.records
           }
         }).finally(() => {
           loading.value = false
         })
    }
</script>
