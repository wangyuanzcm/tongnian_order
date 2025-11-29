import { defHttp } from '/@/utils/http/axios';
import { goodsImageUploadUrl } from '../common/api';
import { UploadFileParams } from '/#/axios';

// 商品相关API接口
enum GoodsApi {
  // 商品管理
  goodsList = '/api/goods/goods/list',
  goodsAdd = '/api/goods/goods/add',
  goodsEdit = '/api/goods/goods/edit',
  goodsDelete = '/api/goods/goods/delete',
  goodsQueryById = '/api/goods/goods/queryById',
  
  // 商品规格管理
  goodsSpecList = '/api/goods/goodsSpec/list',
  goodsSpecAdd = '/api/goods/goodsSpec/add',
  goodsSpecEdit = '/api/goods/goodsSpec/edit',
  goodsSpecDelete = '/api/goods/goodsSpec/delete',
  goodsSpecQueryById = '/api/goods/goodsSpec/queryById',
  
  // 商品规格图片管理
  goodsSpecImageList = '/api/goods/goodsImages/list',
  goodsSpecImageAdd = '/api/goods/goodsImages/add',
  goodsSpecImageDelete = '/api/goods/goodsImages/delete',
}

/**
 * 上传商品图片
 * @param params 上传参数
 * @param onUploadProgress 上传进度回调
 */
export function uploadGoodsImage(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void
) {
  return defHttp.uploadFile<any>(
    {
      url: goodsImageUploadUrl,
      onUploadProgress,
    },
    params,
    { isReturnResponse: true }
  );
}

/**
 * 获取商品列表
 * @param params 查询参数
 */
export function getGoodsList(params: any) {
  return defHttp.get({ url: GoodsApi.goodsList, params });
}

/**
 * 添加商品
 * @param params 商品信息
 */
export function addGoods(params: any) {
  return defHttp.post({ url: GoodsApi.goodsAdd, params });
}

/**
 * 编辑商品
 * @param params 商品信息
 */
export function editGoods(params: any) {
  return defHttp.put({ url: GoodsApi.goodsEdit, params });
}

/**
 * 删除商品
 * @param params 删除参数
 */
export function deleteGoods(params: any) {
  return defHttp.delete({ url: GoodsApi.goodsDelete, params });
}

/**
 * 根据ID查询商品
 * @param id 商品ID
 */
export function getGoodsById(id: string) {
  return defHttp.get({ url: `${GoodsApi.goodsQueryById}/${id}` });
}

/**
 * 获取商品规格列表
 * @param params 查询参数
 */
export function getGoodsSpecList(params: any) {
  return defHttp.get({ url: GoodsApi.goodsSpecList, params });
}

/**
 * 添加商品规格
 * @param params 商品规格信息
 */
export function addGoodsSpec(params: any) {
  return defHttp.post({ url: GoodsApi.goodsSpecAdd, params });
}

/**
 * 编辑商品规格
 * @param params 商品规格信息
 */
export function editGoodsSpec(params: any) {
  return defHttp.put({ url: GoodsApi.goodsSpecEdit, params });
}

/**
 * 删除商品规格
 * @param params 删除参数
 */
export function deleteGoodsSpec(params: any) {
  return defHttp.delete({ url: GoodsApi.goodsSpecDelete, params });
}

/**
 * 根据ID查询商品规格
 * @param id 商品规格ID
 */
export function getGoodsSpecById(id: string) {
  return defHttp.get({ url: `${GoodsApi.goodsSpecQueryById}/${id}` });
}