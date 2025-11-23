<template>
    <div style="min-height: 400px">
        <div v-if="!formDisabled" style="margin-bottom: 12px; display: flex; gap: 8px; align-items: flex-start">
            <a-textarea v-model:value="addressRaw" rows="3" placeholder="粘贴包含姓名、电话、省市区、详细地址的完整文本，点击智能识别自动填充" />
            <a-button type="default" @click="handleSmartParse" pre-icon="ant-design:scan-outline">智能识别</a-button>
        </div>
        <BasicForm @register="registerForm"></BasicForm>
        <div style="width: 100%;text-align: center" v-if="!formDisabled">
            <a-button @click="submitForm" pre-icon="ant-design:check" type="primary">提 交</a-button>
        </div>
    </div>
</template>

<script lang="ts">
    import {BasicForm, useForm} from '/@/components/Form/index';
    import {computed, defineComponent, ref} from 'vue';
    import {defHttp} from '/@/utils/http/axios';
    import { propTypes } from '/@/utils/propTypes';
    import {getBpmFormSchema} from '../TnRecipient.data';
    import {saveOrUpdate} from '../TnRecipient.api';
    import AddressParse from 'zh-address-parse';
    import { pcaa } from '/@/utils/areaData/pcaUtils';
    
    export default defineComponent({
        name: "TnRecipientForm",
        components:{
            BasicForm
        },
        props:{
            formData: propTypes.object.def({}),
            formBpm: propTypes.bool.def(true),
        },
        setup(props){
            const [registerForm, { setFieldsValue, setProps, getFieldsValue }] = useForm({
                labelWidth: 100,
                labelAlign: 'left',
                baseRowStyle: { padding: '0 20px' },
                schemas: getBpmFormSchema(props.formData),
                showActionButtonGroup: false,
                baseColProps: {span: 24}
            });
            const addressRaw = ref('');

            const formDisabled = computed(()=>{
                if(props.formData.disabled === false){
                    return false;
                }
                return true;
            });

            let formData = {};
            const queryByIdUrl = '/receiver/tnRecipient/queryById';
            async function initFormData(){
                let params = {id: props.formData.dataId};
                const data = await defHttp.get({url: queryByIdUrl, params});
                formData = {...data}
                //设置表单的值
                await setFieldsValue(formData);
                //默认是禁用
                await setProps({disabled: formDisabled.value})
            }

            function findProvinceCodeByName(name: string): string | undefined {
                const provinces = pcaa['86'];
                for (const code in provinces) {
                    if (provinces[code] === name) return code;
                }
            }
            function findCityCodeByName(provinceCode: string, name: string): string | undefined {
                const cities = pcaa[provinceCode] || {};
                for (const code in cities) {
                    if (cities[code] === name) return code;
                }
            }
            function findAreaCodeByName(cityCode: string, name: string): string | undefined {
                const areas = pcaa[cityCode] || {};
                for (const code in areas) {
                    if (areas[code] === name) return code;
                }
            }

    async function handleSmartParse(){
                if (!addressRaw.value) return;
                const options = { type: 0, textFilter: ['电話','電話','联系人','聯系人'], nameMaxLength: 4 } as any;
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

            async function submitForm() {
                let data = getFieldsValue();
                let params = Object.assign({}, formData, data);
                console.log('表单数据', params)
                await saveOrUpdate(params, true)
            }

            initFormData();
            
            return {
                registerForm,
                formDisabled,
                addressRaw,
                handleSmartParse,
                submitForm,
            }
        }
    });
</script>