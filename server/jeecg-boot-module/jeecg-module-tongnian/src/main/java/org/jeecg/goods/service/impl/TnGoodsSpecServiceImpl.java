package org.jeecg.goods.service.impl;

import org.jeecg.goods.entity.TnGoodsSpec;
import org.jeecg.goods.mapper.TnGoodsSpecMapper;
import org.jeecg.goods.service.ITnGoodsSpecService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeecg.goods.service.ITnGoodsImagesService;
import org.jeecg.goods.entity.TnImage;
import org.jeecg.goods.entity.TnGoodsImages;
import java.util.ArrayList;

/**
 * @Description: 商品规格/价格明细
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Service
public class TnGoodsSpecServiceImpl extends ServiceImpl<TnGoodsSpecMapper, TnGoodsSpec> implements ITnGoodsSpecService {
	
	@Autowired
	private TnGoodsSpecMapper tnGoodsSpecMapper;
	
	@Autowired
	private ITnGoodsImagesService tnGoodsImagesService;
	
	@Override
	public List<TnGoodsSpec> selectByMainId(String mainId) {
		// 查询商品规格列表
		List<TnGoodsSpec> tnGoodsSpecList = tnGoodsSpecMapper.selectByMainId(mainId);
		
		// 为每个商品规格添加图片列表信息
		if (tnGoodsSpecList != null && !tnGoodsSpecList.isEmpty()) {
			for (TnGoodsSpec tnGoodsSpec : tnGoodsSpecList) {
				// 查询当前商品规格的图片列表
				tnGoodsSpec.setImageList(tnGoodsImagesService.selectImagesByGoodsSpecId(tnGoodsSpec.getId()));
			}
		}
		
		return tnGoodsSpecList;
	}
	
	@Override
	public boolean save(TnGoodsSpec tnGoodsSpec) {
		// 保存商品规格数据
		boolean result = super.save(tnGoodsSpec);
		
		// 处理图片数据
		if (result && tnGoodsSpec.getImageList() != null) {
			// 先删除旧的关联关系
			tnGoodsImagesService.deleteByGoodsSpecId(tnGoodsSpec.getId());
			
			// 检查imageList的实际类型
			Object imageListObj = tnGoodsSpec.getImageList();
			
			// 如果是字符串数组，直接处理
			if (imageListObj instanceof List) {
				List<?> list = (List<?>) imageListObj;
				if (!list.isEmpty()) {
					Object firstItem = list.get(0);
					
					// 如果是字符串数组
					if (firstItem instanceof String) {
						// 直接创建商品规格与图片的关联关系
						List<TnGoodsImages> tnGoodsImagesList = new ArrayList<>();
						for (Object item : list) {
							String imageId = (String) item;
							if (imageId != null && !imageId.isEmpty()) {
								TnGoodsImages tnGoodsImages = new TnGoodsImages();
								tnGoodsImages.setGoodsSpecId(tnGoodsSpec.getId());
								tnGoodsImages.setImageId(imageId);
								tnGoodsImagesList.add(tnGoodsImages);
							}
						}
						
						if (!tnGoodsImagesList.isEmpty()) {
							tnGoodsImagesService.saveBatch(tnGoodsImagesList);
						}
					} 
					// 如果是TnImage对象数组，使用原有逻辑
					else if (firstItem instanceof TnImage) {
						tnGoodsImagesService.saveGoodsSpecImages(tnGoodsSpec.getId(), (List<TnImage>) list);
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public boolean updateById(TnGoodsSpec tnGoodsSpec) {
		// 更新商品规格数据
		boolean result = super.updateById(tnGoodsSpec);
		
		// 处理图片数据
		if (result) {
			// 先删除原有图片关联
			tnGoodsImagesService.deleteByGoodsSpecId(tnGoodsSpec.getId());
			
			// 检查imageList的实际类型
			if (tnGoodsSpec.getImageList() != null) {
				Object imageListObj = tnGoodsSpec.getImageList();
				
				// 如果是字符串数组，直接处理
				if (imageListObj instanceof List) {
					List<?> list = (List<?>) imageListObj;
					if (!list.isEmpty()) {
						Object firstItem = list.get(0);
						
						// 如果是字符串数组
						if (firstItem instanceof String) {
							// 直接创建商品规格与图片的关联关系
							List<TnGoodsImages> tnGoodsImagesList = new ArrayList<>();
							for (Object item : list) {
								String imageId = (String) item;
								if (imageId != null && !imageId.isEmpty()) {
									TnGoodsImages tnGoodsImages = new TnGoodsImages();
									tnGoodsImages.setGoodsSpecId(tnGoodsSpec.getId());
									tnGoodsImages.setImageId(imageId);
									tnGoodsImagesList.add(tnGoodsImages);
								}
							}
							
							if (!tnGoodsImagesList.isEmpty()) {
								tnGoodsImagesService.saveBatch(tnGoodsImagesList);
							}
						} 
						// 如果是TnImage对象数组，使用原有逻辑
						else if (firstItem instanceof TnImage) {
							tnGoodsImagesService.saveGoodsSpecImages(tnGoodsSpec.getId(), (List<TnImage>) list);
						}
					}
				}
			}
		}
		
		return result;
	}
}
