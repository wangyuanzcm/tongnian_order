import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '商品名称',
    align: "center",
    dataIndex: 'name_dictText'
  },
  {
    title: '是否定制商品',
    align: "center",
    dataIndex: 'isCustom_dictText'
  },
  {
    title: '商品说明',
    align: "center",
    dataIndex: 'remark'
  },
  {
    title: '状态',
    align: "center",
    dataIndex: 'status_dictText'
  },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "商品名称",
    field: "name",
    component: 'JSelectMultiple',
    componentProps: {
      dictCode: "tongnian_goods_category"
    },
    //colProps: {span: 6},
  },
  {
    label: "是否定制商品",
    field: "isCustom",
    component: 'JSelectMultiple',
    componentProps: {
      dictCode: "tongnian_is_custom"
    },
    //colProps: {span: 6},
  },
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '商品名称',
    field: 'name',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "tongnian_goods_category"
    },
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入商品名称!' },
      ];
    },
  },
  {
    label: '是否定制商品',
    field: 'isCustom',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "tongnian_is_custom"
    },
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入是否定制商品!' },
      ];
    },
  },
  {
    label: '商品说明',
    field: 'remark',
    component: 'InputTextArea',//TODO 注意string转换问题
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "tongnian_goods_status"
    },
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入状态!' },
      ];
    },
  },	// TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
];

//子表列表数据
export const tnGoodsSpecColumns: BasicColumn[] = [
  {
    title: '商品细分规格',
    align: "center",
    dataIndex: 'specType'
  },
  {
    title: '商品价格',
    align: "center",
    dataIndex: 'price'
  },
  {
    title: '商品图片',
    align: 'center',
    dataIndex: 'imageList',
    customRender: ({ record }) => {
      return render.renderImageList({ record });
    }
  },
  {
    title: '状态',
    align: "center",
    dataIndex: 'status',
    customRender: ({ text }) => {
      return render.renderSwitch(text, [{ text: '是', value: 'Y' }, { text: '否', value: 'N' }])
    }
  }
];
//子表表单数据
export const tnGoodsSpecFormSchema: FormSchema[] = [
  // TODO 子表隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
  {
    label: '商品细分规格',
    field: 'specType',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入商品细分规格!' },
      ];
    },
  },
  {
    label: '商品价格',
    field: 'price',
    component: 'InputNumber',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入商品价格!' },
      ];
    },
  },
  { label: '商品图片', field: 'imageList', component: 'JImageUpload', componentProps: { token: true, multiple: true, fileMax: 5 }, dynamicRules: ({ model, schema }) => { return [{ required: true, message: '请输入商品图片!' },]; }, dynamicDisabled: ({ values }) => { return values.specType === ''; }, },
  {
    label: '状态',
    field: 'status',
    component: 'JSwitch',
    componentProps: {
    },
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入状态!' },
      ];
    },
  },
];

// 高级查询数据
export const superQuerySchema = {
  name: { title: '商品名称', order: 0, view: 'list', type: 'string', dictCode: 'tongnian_goods_category', },
  isCustom: { title: '是否定制商品', order: 1, view: 'list', type: 'string', dictCode: 'tongnian_is_custom', },
  remark: { title: '商品说明', order: 2, view: 'textarea', type: 'string', },
  status: { title: '状态', order: 3, view: 'list', type: 'string', dictCode: 'tongnian_goods_status', },
};

// 导出JVxeTable列配置别名，保持向后兼容
export const tnGoodsSpecJVxeColumns = tnGoodsSpecColumns;