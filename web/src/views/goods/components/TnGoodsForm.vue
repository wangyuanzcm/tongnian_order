<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
  <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs">
      <a-tab-pane tab="商品规格/价格明细" key="tnGoodsSpec" :forceRender="true">
        <JVxeTable
          v-if="tnGoodsSpecTable.show"      
          keep-source
          resizable
          ref="tnGoodsSpec"
          :loading="tnGoodsSpecTable.loading"
          :columns="tnGoodsSpecTable.columns"
          :dataSource="tnGoodsSpecTable.dataSource"
          :height="340"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
        >
          <!-- 图片列表插槽 -->
          <template #imageList="{ row, value }">
            <div v-if="value && value.length > 0">
              <div v-for="(item, index) in value" :key="item.id || index" :style="{ marginBottom: '5px' }">
                <JImageUpload
                  :value="[{ id: item.id, url: item.url, name: item.name || item.url?.split('/')?.pop() || '', sort: item.sort || index + 1, status: item.status || 'Y' }]"
                  :token="true"
                  :multiple="false"
                  :limit="1"
                  :dynamicDisabled="() => true"
                />
              </div>
            </div>
            <span v-else>--</span>
          </template>
        </JVxeTable>
      </a-tab-pane>
    </a-tabs>
    <div style="width: 100%;text-align: center;margin-top: 10px;" v-if="showFlowSubmitButton">
      <a-button preIcon="ant-design:check-outlined" style="width: 126px" type="primary" @click="handleSubmit">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
    import { defHttp } from '/@/utils/http/axios';
    import {ref, computed, unref,reactive, onMounted, defineProps } from 'vue';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import { JVxeTable } from '/@/components/jeecg/JVxeTable'
    import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts'
    import {formSchema,tnGoodsSpecJVxeColumns} from '../TnGoods.data';
    import {saveOrUpdate,queryTnGoodsSpec} from '../TnGoods.api';
    import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils'
    const isUpdate = ref(true);
    
    const refKeys = ref(['tnGoodsSpec', ]);
    const activeKey = ref('tnGoodsSpec');
    const tnGoodsSpec = ref();
    const tableRefs = {tnGoodsSpec, };
    const tnGoodsSpecTable = reactive({
          loading: false,
          dataSource: [],
          columns:tnGoodsSpecJVxeColumns,
          show: false
    })

    const props = defineProps({
      formData: { type: Object, default: ()=>{} },
      formBpm: { type: Boolean, default: true }
    });
    const formDisabled = computed(()=>{
      if(props.formBpm === true){
        if(props.formData.disabled === false){
          return false;
        }
      }
      return true;
    });
    // 是否显示提交按钮
    const showFlowSubmitButton = computed(()=>{
      if(props.formBpm === true){
        if(props.formData.disabled === false){
          return true
        }
      }
      return false
    });
    
    //表单配置
    const [registerForm, {setProps,resetFields, setFieldsValue, validate}] = useForm({
        labelWidth: 150,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: {span: 24}
    });

    onMounted(()=>{
      initFormData();
    });
    //渲染流程表单数据
    const queryByIdUrl = '/goods/tnGoods/queryById';
    async function initFormData(){
      if(props.formBpm === true){
        await reset();
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //表单赋值
        await setFieldsValue({
          ...data
        });
        requestSubTableData(queryTnGoodsSpec, {id: data.id}, tnGoodsSpecTable, ()=>{
          tnGoodsSpecTable.show = true;
        });
        // 隐藏底部时禁用整个表单
        setProps({ disabled: formDisabled.value })
      }
    }
    
    //方法配置
    const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys);

    async function reset(){
      await resetFields();
      activeKey.value = 'tnGoodsSpec';
      tnGoodsSpecTable.dataSource = [];
    }
    function classifyIntoFormData(allValues) {
         let main = Object.assign({}, allValues.formValue)
         // 处理商品规格列表中的图片数据
         const tnGoodsSpecList = allValues.tablesValue[0].tableData.map(spec => {
           // 处理图片数据格式转换
           if (spec.imageUrlString && Array.isArray(spec.imageUrlString)) {
             // 从JImageUpload组件获取的对象数组中提取需要的字段
             spec.imageList = spec.imageUrlString.map((item) => ({
               id: item.uid, // 使用uid作为图片ID
               name: item.name || '',
               url: item.url,
               sort: 0,
               status: 'Y' // 图片状态为有效
             }));
             // 保持兼容，将图片URL也保存到imageUrlString字段
             spec.imageUrlString = spec.imageUrlString.map((item) => item.url).join(',');
           } else if (spec.imageList && Array.isArray(spec.imageList)) {
             // 如果已经是imageList格式，确保每个项目都有必要的字段
             spec.imageList = spec.imageList.map(item => ({
               id: item.id || item.uid,
               name: item.name || '',
               url: item.url,
               sort: item.sort || 0,
               status: item.status || 'Y'
             }));
           } else if (spec.imageUrlString && typeof spec.imageUrlString === 'string') {
             // 如果是逗号分隔的URL字符串，转换为imageList格式
             const urls = spec.imageUrlString.split(',');
             spec.imageList = urls.map((url, index) => ({
               id: index + 1,
               name: `image_${index + 1}`,
               url: url,
               sort: index,
               status: 'Y'
             }));
           } else {
             // 如果没有图片数据，确保imageList是空数组
             spec.imageList = [];
             spec.imageUrlString = '';
           }
           return spec;
         });
         return {
           ...main, // 展开
           tnGoodsSpecList: tnGoodsSpecList,
         }
       }
    //表单提交事件
    async function requestAddOrEdit(values) {
      //提交表单
      await saveOrUpdate(values, true);
    }
</script>

<style lang="less" scoped>
	/** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-calendar-picker) {
    width: 100%;
  }
</style>