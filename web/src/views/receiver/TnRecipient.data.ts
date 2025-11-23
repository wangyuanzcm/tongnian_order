import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '收件人姓名',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '收件人手机号',
    align:"center",
    dataIndex: 'phone'
   },
   {
    title: '所在地区',
    align:"center",
    dataIndex: 'address',
   },
   {
    title: '地址详情',
    align:"center",
    dataIndex: 'addressDetail'
   },
   {
    title: '收件人昵称',
    align:"center",
    dataIndex: 'nickName'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "收件人姓名",
      field: 'name',
      component: 'Input',
      //colProps: {span: 6},
 	},
	{
      label: "收件人手机号",
      field: 'phone',
      component: 'Input',
      //colProps: {span: 6},
 	},
	{
      label: "所在地区",
      field: 'address',
      component: 'JAreaLinkage',
      componentProps: {
        saveCode: 'region',
      },
      //colProps: {span: 6},
 	},
	{
      label: "收件人昵称",
      field: 'nickName',
      component: 'Input',
      //colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '收件人姓名',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入收件人姓名!'},
          ];
     },
  },
  {
    label: '收件人手机号',
    field: 'phone',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入收件人手机号!'},
          ];
     },
  },
  {
    label: '所在地区',
    field: 'address',
    component: 'JAreaLinkage',
    componentProps: {
      saveCode: 'region',
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入所在地区!'},
          ];
     },
  },
  {
    label: '地址详情',
    field: 'addressDetail',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入地址详情!'},
          ];
     },
  },
  {
    label: '收件人昵称',
    field: 'nickName',
    component: 'Input',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];

// 高级查询数据
export const superQuerySchema = {
  name: {title: '收件人姓名',order: 0,view: 'text', type: 'string',},
  phone: {title: '收件人手机号',order: 1,view: 'text', type: 'string',},
  address: {title: '所在地区',order: 2,view: 'pca', type: 'string',},
  addressDetail: {title: '地址详情',order: 3,view: 'text', type: 'string',},
  nickName: {title: '收件人昵称',order: 4,view: 'text', type: 'string',},
  remark: {title: '备注',order: 5,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}