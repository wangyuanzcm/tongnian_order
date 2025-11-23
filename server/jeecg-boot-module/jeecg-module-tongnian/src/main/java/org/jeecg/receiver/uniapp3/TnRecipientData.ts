import { render } from '@/common/renderUtils';
//列表数据
export const columns = [
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