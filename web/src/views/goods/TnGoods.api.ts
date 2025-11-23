import {defHttp} from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/goods/tnGoods/list',
  save='/goods/tnGoods/add',
  edit='/goods/tnGoods/edit',
  deleteOne = '/goods/tnGoods/delete',
  deleteBatch = '/goods/tnGoods/deleteBatch',
  importExcel = '/goods/tnGoods/importExcel',
  exportXls = '/goods/tnGoods/exportXls',
  tnGoodsSpecList = '/goods/tnGoods/listTnGoodsSpecByMainId',
  tnGoodsSpecSave='/goods/tnGoods/addTnGoodsSpec',
  tnGoodsSpecEdit='/goods/tnGoods/editTnGoodsSpec',
  tnGoodsSpecDelete = '/goods/tnGoods/deleteTnGoodsSpec',
  tnGoodsSpecDeleteBatch = '/goods/tnGoods/deleteBatchTnGoodsSpec',
}
/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;
/**
 * 列表接口
 * @param params
 */
export const list = (params) =>
  defHttp.get({url: Api.list, params});

/**
 * 删除单个
 */
export const deleteOne = (params,handleSuccess) => {
  return defHttp.delete({url: Api.deleteOne, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.deleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({url: url, params});
}
/**
 * 列表接口
 * @param params
 */
export const tnGoodsSpecList = (params) => {
  if(params['goodsId']){
    return defHttp.get({url: Api.tnGoodsSpecList, params});
  }
  return Promise.resolve({});
}


/**
 * 删除单个
 */
export const tnGoodsSpecDelete = (params,handleSuccess) => {
  return defHttp.delete({url: Api.tnGoodsSpecDelete, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const tnGoodsSpecDeleteBatch = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.tnGoodsSpecDeleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const  tnGoodsSpecSaveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.tnGoodsSpecEdit : Api.tnGoodsSpecSave;
  return defHttp.post({url: url, params});
}
/**
 * 导入
 */
export const tnGoodsSpecImportUrl = '/goods/tnGoods/importTnGoodsSpec'

/**
 * 导出
 */
export const tnGoodsSpecExportXlsUrl = '/goods/tnGoods/exportTnGoodsSpec'
