<template>
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :maxHeight="500" :width="800" @ok="handleSubmit">
      <BasicForm @register="registerForm" ref="formRef" name="TnOrderForm" />
      <!-- 子表单区域 -->
      <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs" class="jeecg-tab">
        <a-tab-pane tab="订单商品关联表" key="tnOrderGoods" :forceRender="true">
          <JVxeTable
            keep-source
            resizable
            ref="tnOrderGoods"
            :loading="tnOrderGoodsTable.loading"
            :columns="tnOrderGoodsTable.columns"
            :dataSource="tnOrderGoodsTable.dataSource"
            :height="340"
            :disabled="formDisabled"
            :rowNumber="true"
            :rowSelection="true"
            :toolbar="true"
            />
        </a-tab-pane>
      </a-tabs>

  </BasicModal>
</template>

<script lang="ts" setup>
    import {ref, computed, unref,reactive} from 'vue';
    import {BasicModal, useModalInner} from '/@/components/Modal';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import { JVxeTable } from '/@/components/jeecg/JVxeTable'
    import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts'
    import {formSchema,tnOrderGoodsJVxeColumns} from '../TnOrder.data';
    import {saveOrUpdate,queryTnOrderGoods} from '../TnOrder.api';
    import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils'
    import { useMessage } from '/@/hooks/web/useMessage';
    import { getDateByPicker } from '/@/utils';
    //日期个性化选择
    const fieldPickers = reactive({
    });
      const tnOrderGoodsFieldPickers = reactive({
      });
    const { createMessage } = useMessage();
    // Emits声明
    const emit = defineEmits(['register','success']);
    const isUpdate = ref(true);
    const formDisabled = ref(false);
    const refKeys = ref(['tnOrderGoods', ]);
    const activeKey = ref('tnOrderGoods');
    const tnOrderGoods = ref();
    const tableRefs = {tnOrderGoods, };
    const tnOrderGoodsTable = reactive({
          loading: false,
          dataSource: [],
          columns:tnOrderGoodsJVxeColumns
    })
    //表单配置
    const [registerForm, {setProps,resetFields, setFieldsValue, validate}] = useForm({
        labelWidth: 150,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: {span: 24},
        baseRowStyle: { padding: "0 20px"}
    });
     //表单赋值
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
        //重置表单
        await reset();
        setModalProps({confirmLoading: false,showCancelBtn:data?.showFooter,showOkBtn:data?.showFooter});
        isUpdate.value = !!data?.isUpdate;
        formDisabled.value = !data?.showFooter;
        if (unref(isUpdate)) {
            //表单赋值
            await setFieldsValue({
                ...data.record,
            });
             requestSubTableData(queryTnOrderGoods, {id:data?.record?.id}, tnOrderGoodsTable)
        }
        // 隐藏底部时禁用整个表单
       setProps({ disabled: !data?.showFooter })
    });
    //方法配置
    const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys);

    //设置标题
    const title = computed(() => (!unref(isUpdate) ? '新增' : !unref(formDisabled) ? '编辑' : '详情'));

    async function reset(){
      await resetFields();
      activeKey.value = 'tnOrderGoods';
      tnOrderGoodsTable.dataSource = [];
    }
    function classifyIntoFormData(allValues) {
         let main = Object.assign({}, allValues.formValue)
         return {
           ...main, // 展开
           tnOrderGoodsList: allValues.tablesValue[0].tableData,
         }
       }
    //表单提交事件
    async function requestAddOrEdit(values) {
        try {
            // 预处理日期数据
            changeDateValue(values);
            setModalProps({confirmLoading: true});
            //提交表单
            await saveOrUpdate(values, isUpdate.value);
            //关闭弹窗
            closeModal();
            //刷新列表
            emit('success');
        } finally {
            setModalProps({confirmLoading: false});
        }
    }

    /**
     * 处理日期值
     * @param formData 表单数据
     */
    const changeDateValue = (formData) => {
      if (formData && fieldPickers) {
          for (let key in fieldPickers) {
              if (formData[key]) {
                  formData[key] = getDateByPicker(formData[key], fieldPickers[key]);
              }
          }
      }
      if(formData && formData.tnOrderGoodsList && formData.tnOrderGoodsList.length > 0){
          formData.tnOrderGoodsList.forEach(subFormData=>{
              for (let key in tnOrderGoodsFieldPickers) {
                  if (subFormData[key]) {
                      subFormData[key] = getDateByPicker(subFormData[key], tnOrderGoodsFieldPickers[key]);
                  }
              }
          })
      }
    };
</script>

<style lang="less" scoped>
	/** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-calendar-picker) {
    width: 100%;
  }
  
  .jeecg-tab {
    padding: 0 20px;
  }
</style>