<template>
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :maxHeight="500" :width="800" @ok="handleSubmit">
      <BasicForm @register="registerForm" name="TnGoodsSpecForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
    import {ref, computed, unref,inject, reactive} from 'vue';
    import {BasicModal, useModalInner} from '/@/components/Modal';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import {tnGoodsSpecFormSchema} from '../TnGoods.data';
    import {tnGoodsSpecSaveOrUpdate} from '../TnGoods.api';
    import { getDateByPicker } from '/@/utils';
    //日期个性化选择
    const fieldPickers = reactive({
    });

    //接收主表id
    const mainId = inject('mainId');
    // Emits声明
    const emit = defineEmits(['register','success']);
    const isUpdate = ref(true);
    const isDetail = ref(false);
    //表单配置
    const [registerForm, { setProps,resetFields, setFieldsValue, validate, scrollToField }] = useForm({
        labelWidth: 150,
        schemas: tnGoodsSpecFormSchema,
        showActionButtonGroup: false,
        baseColProps: {span: 24},
        baseRowStyle: { padding: "0 20px" }
    });

    //表单赋值
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
        //重置表单
        await resetFields();
        setModalProps({confirmLoading: false,showCancelBtn:!!data?.showFooter,showOkBtn:!!data?.showFooter});
        isUpdate.value = !!data?.isUpdate;
        isDetail.value = !!data?.showFooter;
        if (unref(isUpdate)) {
            //表单赋值
            const record = { ...data.record };
            // 处理图片数据回显
            if (record.imageList && Array.isArray(record.imageList)) {
                // 直接将imageList转换为JImageUpload组件需要的对象数组格式
                record.imageList = record.imageList.map((item) => ({
                    uid: item.id || item.uid, // 使用图片ID作为uid
                    name: item.name || `image_${item.id || item.uid}`,
                    url: item.url,
                }));
            } else if (record.imageUrlString && typeof record.imageUrlString === 'string') {
                // 如果是逗号分隔的URL字符串，转换为对象数组格式
                const urls = record.imageUrlString.split(',');
                record.imageList = urls.map((url, index) => ({
                    uid: index + 1,
                    name: `image_${index + 1}`,
                    url: url,
                }));
            }
            await setFieldsValue(record);
        }
        // 隐藏底部时禁用整个表单
       setProps({ disabled: !data?.showFooter })
    });
    //设置标题
    const title = computed(() => (!unref(isUpdate) ? '新增' : !unref(isDetail) ? '详情' : '编辑'));
    //表单提交事件
    async function handleSubmit(v) {
        try {
            let values = await validate();
            // 预处理日期数据
            changeDateValue(values);
            console.log(values,'===')
            // 处理图片数据格式转换
            if (values.imageList) {
                // 确保imageList是字符串数组
                if (Array.isArray(values.imageList)) {
                    // 从图片对象数组中提取uid属性，形成字符串数组
                    values.imageList = values.imageList.map((item) => {
                        // 确保item是对象且有uid属性
                        if (item && typeof item === 'object' && item.uid) {
                            return item.uid;
                        }
                        return null;
                    }).filter(Boolean); // 过滤掉null值
                } else if (typeof values.imageList === 'string') {
                    // 如果是字符串，尝试解析
                    try {
                        const parsed = JSON.parse(values.imageList);
                        if (Array.isArray(parsed)) {
                            // 如果解析后是数组，提取uid
                            values.imageList = parsed.map((item) => {
                                if (item && typeof item === 'object' && item.uid) {
                                    return item.uid;
                                }
                                return null;
                            }).filter(Boolean);
                        } else {
                            // 否则设为空数组
                            values.imageList = [];
                        }
                    } catch (e) {
                        console.error('解析图片JSON字符串失败:', e);
                        values.imageList = [];
                    }
                } else {
                    // 其他情况设为空数组
                    values.imageList = [];
                }
            } else {
                // 如果没有imageList，设为空数组
                values.imageList = [];
            }
            
            setModalProps({confirmLoading: true});
            if (unref(mainId)) {
                values['goodsId'] = unref(mainId);
             }
            //提交表单
            await tnGoodsSpecSaveOrUpdate(values, isUpdate.value);
            //关闭弹窗
            closeModal();
            //刷新列表
            emit('success');
        } catch ({ errorFields }) {
          if (errorFields) {
            const firstField = errorFields[0];
            if (firstField) {
              scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
            }
          }
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
</style>
