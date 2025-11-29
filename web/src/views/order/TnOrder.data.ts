import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '订单编号',
    align:"center",
    dataIndex: 'orderNo'
   },
   {
    title: '商品名称',
    align:"center",
    dataIndex: 'goodId',
    customRender: ({text}) => {
      // 使用render.renderDict函数通过字典接口查询商品名称
      return render.renderDict(text, 'tongnian_goods_category');
    }
   },
   {
    title: '应付金额',
    align:"center",
    dataIndex: 'payableAmount'
   },
   {
    title: '实付金额',
    align:"center",
    dataIndex: 'paidAmount'
   },
   {
    title: '代收金额',
    align:"center",
    dataIndex: 'collectAmount'
   },
   {
    title: '收件人',
    align:"center",
    dataIndex: 'recipientId',
    customRender: ({text}) => {
      // 使用render.renderDict函数通过字典接口查询收件人名称
      // 这里假设收件人的字典编码为'tn_recipient'
      return render.renderDict(text, 'tn_recipient');
    }
   },
   {
    title: '订单状态',
    align:"center",
    dataIndex: 'orderStatus',
    customRender:({text}) => {
       // 根据后端返回的数字值显示对应的状态文本
       const statusMap = {
         50: '已完成',
         // 可以根据实际需求添加更多状态映射
       };
       return statusMap[text] || `状态${text}`;
     },
   },
   {
    title: '是否新客户',
    align:"center",
    dataIndex: 'isNewCustomer',
    customRender:({text}) => {
       // 根据后端返回的数字值显示对应的客户类型
       const customerMap = {
         1: '是',
         2: '否',
         null: '-'
       };
       return customerMap[text] || text;
     },
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '订单编号',
    field: 'orderNo',
    component: 'Input',
  },
  {
    label: '商品名称',
    field: 'goodId',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"tongnian_goods_category"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入商品名称!'},
          ];
     },
  },
  {
    label: '应付金额',
    field: 'payableAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入应付金额!'},
          ];
     },
  },
  {
    label: '实付金额',
    field: 'paidAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入实付金额!'},
          ];
     },
  },
  {
    label: '代收金额',
    field: 'collectAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入代收金额!'},
          ];
     },
  },
  {
    label: '收件人',
    field: 'recipientId',
    component: 'JSearchSelect',
    componentProps:{
       dict:""
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入收件人!'},
          ];
     },
  },
  {
    label: '订单状态',
    field: 'orderStatus',
     component: 'JSwitch',
     componentProps:{
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入订单状态!'},
          ];
     },
  },
  {
    label: '是否新客户',
    field: 'isNewCustomer',
     component: 'JSwitch',
     componentProps:{
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入是否新客户!'},
          ];
     },
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表单数据
//子表列表数据
export const tnOrderGoodsColumns: BasicColumn[] = [
   {
    title: '商品名称',
    align:"center",
    dataIndex: 'goodsName',
    width: 200
   },
   {
    title: '商品规格',
    align:"center",
    dataIndex: 'goodsSpec',
    width: 100
   },
   {
    title: '商品单价',
    align:"center",
    dataIndex: 'unitPrice',
    width: 100,
    customRender: ({ text }) => {
      return `¥${Number(text).toFixed(2)}`;
    }
   },
   {
    title: '商品数量',
    align:"center",
    dataIndex: 'quantity',
    width: 100
   },
   {
    title: '商品小计',
    align:"center",
    dataIndex: 'subtotal',
    width: 100,
    customRender: ({ record }) => {
      return `¥${(record.unitPrice * record.quantity).toFixed(2)}`;
    }
   },
];
//子表表格配置
export const tnOrderGoodsJVxeColumns: JVxeColumn[] = [
    {
      title: '商品名称',
      key: 'goodsName',
      type: JVxeTypes.select,
      options:[],
      dictCode:"",
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '商品单价',
      key: 'unitPrice',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '商品数量',
      key: 'quantity',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
  ]

// 高级查询数据
export const superQuerySchema = {
  orderNo: {title: '订单编号',order: 0,view: 'text', type: 'string',},
  goodId: {title: '商品名称',order: 1,view: 'list', type: 'string',dictCode: 'tongnian_goods_category',},
  payableAmount: {title: '应付金额',order: 2,view: 'number', type: 'number',},
  paidAmount: {title: '实付金额',order: 3,view: 'number', type: 'number',},
  collectAmount: {title: '代收金额',order: 4,view: 'number', type: 'number',},
  recipientId: {title: '收件人',order: 5,view: 'sel_search', type: 'string',dictCode: '',},
  orderStatus: {title: '订单状态',order: 6,view: 'number', type: 'number',},
  isNewCustomer: {title: '是否新客户',order: 7,view: 'number', type: 'number',},
  //子表高级查询
  tnOrderGoods: {
    title: '订单商品关联表',
    view: 'table',
    fields: {
        goodsName: {title: '商品名称',order: 0,view: 'list', type: 'string',dictCode: '',},
        unitPrice: {title: '商品单价',order: 1,view: 'number', type: 'number',},
        quantity: {title: '商品数量',order: 2,view: 'number', type: 'number',},
    }
  },
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}