package org.jeecg.goods.service.impl;

import org.jeecg.goods.entity.TnGoodsImages;
import org.jeecg.goods.mapper.TnGoodsImagesMapper;
import org.jeecg.goods.service.ITnGoodsImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.jeecg.goods.entity.TnImage;
import org.jeecg.goods.mapper.TnImageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;

/**
 * @Description: 商品图片映射表
 * @Author: jeecg-boot
 * @Date:   2025-11-25
 * @Version: V1.0
 */
@Service
public class TnGoodsImagesServiceImpl extends ServiceImpl<TnGoodsImagesMapper, TnGoodsImages> implements ITnGoodsImagesService {

    @Autowired
    private TnGoodsImagesMapper tnGoodsImagesMapper;

    @Autowired
    private TnImageMapper tnImageMapper;

    /**
     * 通过商品规格ID查询图片列表
     * 
     * @param goodsSpecId 商品规格ID
     * @return List<TnImage> 图片列表
     */
    @Override
    public List<TnImage> selectImagesByGoodsSpecId(String goodsSpecId) {
        // 先查询商品规格关联的图片ID列表
        List<String> imageIds = tnGoodsImagesMapper.selectImageIdsByGoodsSpecId(goodsSpecId);
        
        if (imageIds == null || imageIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 根据图片ID列表查询图片信息
        QueryWrapper<TnImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", imageIds);
        queryWrapper.orderByAsc("sort");
        
        return tnImageMapper.selectList(queryWrapper);
    }

    /**
     * 保存商品规格与图片的关联关系
     * 
     * @param goodsSpecId 商品规格ID
     * @param imageList 图片列表
     * @return boolean
     */
    @Override
    public boolean saveGoodsSpecImages(String goodsSpecId, List<TnImage> imageList) {
        // 先删除旧的关联关系
        deleteByGoodsSpecId(goodsSpecId);
        
        if (imageList == null || imageList.isEmpty()) {
            return true;
        }
        
        // 保存新的关联关系
        List<TnGoodsImages> tnGoodsImagesList = new ArrayList<>();
        for (TnImage image : imageList) {
            // 首先保存图片信息
            if (image.getId() == null || "".equals(image.getId())) {
                tnImageMapper.insert(image);
            }
            
            // 然后创建关联关系
            TnGoodsImages tnGoodsImages = new TnGoodsImages();
            tnGoodsImages.setGoodsSpecId(goodsSpecId);
            tnGoodsImages.setImageId(image.getId());
            tnGoodsImagesList.add(tnGoodsImages);
        }
        
        return saveBatch(tnGoodsImagesList);
    }

    /**
     * 通过商品规格ID删除关联关系
     * 
     * @param goodsSpecId 商品规格ID
     * @return boolean
     */
    @Override
    public boolean deleteByGoodsSpecId(String goodsSpecId) {
        return tnGoodsImagesMapper.deleteByGoodsSpecId(goodsSpecId);
    }
}
