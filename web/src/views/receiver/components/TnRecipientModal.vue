<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" destroyOnClose :title="title" :width="600" :showFooter="showFooter" @ok="handleSubmit">
      <div v-if="showFooter" style="padding: 0 20px; margin-bottom: 12px; display: flex; flex-direction: column; gap: 8px; align-items: flex-start">
        <a-textarea v-model:value="addressRaw" rows="3" style="width: 100%" placeholder="粘贴包含姓名、电话、省市区、详细地址的完整文本，点击智能识别自动填充" />
        <a-button type="default" @click="handleSmartParse" pre-icon="ant-design:scan-outline">智能识别</a-button>
      </div>
      <BasicForm @register="registerForm" name="TnRecipientForm" />
  </BasicDrawer>
</template>

<script lang="ts" setup>
    import {ref, computed, unref, reactive} from 'vue';
    import {BasicDrawer, useDrawerInner} from '/@/components/Drawer';
    import AddressParse from 'zh-address-parse';
    import { pcaa } from '/@/utils/areaData/pcaUtils';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import {formSchema} from '../TnRecipient.data';
    import {saveOrUpdate} from '../TnRecipient.api';
    import { useMessage } from '/@/hooks/web/useMessage';
    import { getDateByPicker } from '/@/utils';
    const { createMessage } = useMessage();
    // Emits声明
    const emit = defineEmits(['register','success']);
    const isUpdate = ref(true);
    const isDetail = ref(false);
    //表单配置
    const [registerForm, { setProps,resetFields, setFieldsValue, validate, scrollToField }] = useForm({
        labelWidth: 120,
        labelAlign: 'left',
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: {span: 24},
        baseRowStyle: { padding: "0 20px" }
    });
    const showFooter = ref(true);
    const addressRaw = ref('');
    //表单赋值
    const [registerDrawer, {setDrawerProps, closeDrawer}] = useDrawerInner(async (data) => {
        //重置表单
        await resetFields();
        showFooter.value = !!data?.showFooter;
        setDrawerProps({confirmLoading: false, showFooter: showFooter.value});
        isUpdate.value = !!data?.isUpdate;
        isDetail.value = !!data?.showFooter;
        if (unref(isUpdate)) {
            //表单赋值
            await setFieldsValue({
                ...data.record,
            });
        }
        // 隐藏底部时禁用整个表单
       setProps({ disabled: !data?.showFooter })
    });
    //日期个性化选择
    const fieldPickers = reactive({
    });
    //设置标题
    const title = computed(() => (!unref(isUpdate) ? '新增' : !unref(isDetail) ? '详情' : '编辑'));
    //表单提交事件
    async function handleSubmit(v) {
        try {
            let values = await validate();
            // 预处理日期数据
            changeDateValue(values);
            setDrawerProps({confirmLoading: true});
            //提交表单
            await saveOrUpdate(values, isUpdate.value);
            //关闭弹窗
            closeDrawer();
            //刷新列表
            emit('success');
        } catch ({ errorFields }) {
           if (errorFields) {
             const firstField = errorFields[0];
             if (firstField) {
               scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
             }
           }
           return Promise.reject(errorFields);
        } finally {
            setDrawerProps({confirmLoading: false});
        }
    }

    function findProvinceCodeByName(name) {
        const provinces = pcaa['86'];
        for (const code in provinces) {
            if (provinces[code] === name) return code;
        }
    }
    function findCityCodeByName(provinceCode, name) {
        const cities = pcaa[provinceCode] || {};
        for (const code in cities) {
            if (cities[code] === name) return code;
        }
    }
    function findAreaCodeByName(cityCode, name) {
        const areas = pcaa[cityCode] || {};
        for (const code in areas) {
            if (areas[code] === name) return code;
        }
    }

    async function handleSmartParse(){
        if (!addressRaw.value) return;
        const options = { type: 0, textFilter: ['电話','電話','联系人','聯系人'], nameMaxLength: 4 };
        const parsed = AddressParse(addressRaw.value, options);
        const provinceName = parsed.province || '';
        const cityName = parsed.city || '';
        const areaName = parsed.area || '';
        const detail = parsed.detail || '';
        const phone = parsed.phone || '';
        const name = parsed.name || '';

        let provinceCode = provinceName ? findProvinceCodeByName(provinceName) : undefined;
        let cityCode = provinceCode && cityName ? findCityCodeByName(provinceCode, cityName) : undefined;
        let areaCode = cityCode && areaName ? findAreaCodeByName(cityCode, areaName) : undefined;
        let regionCode = '';
        if (provinceCode && cityCode && areaCode) {
            regionCode = `${provinceCode},${cityCode},${areaCode}`;
        } else if (provinceCode && cityCode) {
            regionCode = `${provinceCode},${cityCode}`;
        } else if (provinceCode) {
            regionCode = `${provinceCode}`;
        }

        const regionText = getAreaTextByCode(regionCode);
        const region = (regionText || '').replace(/\//g, '');
        const phoneLast4 = (phone || '').toString().slice(-4);
        const nickName = `${region}-${name || ''}-${phoneLast4 || ''}`;

        await setFieldsValue({
            name,
            phone,
            address: regionCode,
            addressDetail: detail,
            nickName
        });
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