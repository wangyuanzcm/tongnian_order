package org.jeecg.goods.service;

import org.jeecg.goods.entity.TnGoodsImages;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.jeecg.goods.entity.TnImage;

/**
 * @Description: 商品图片映射表
 * @Author: jeecg-boot
 * @Date:   2025-11-25
 * @Version: V1.0
 */
public interface ITnGoodsImagesService extends IService<TnGoodsImages> {

    /**
     * 通过商品规格ID查询图片列表
     * 
     * @param goodsSpecId 商品规格ID
     * @return List<TnImage> 图片列表
     */
    List<TnImage> selectImagesByGoodsSpecId(String goodsSpecId);

    /**
     * 保存商品规格与图片的关联关系
     * 
     * @param goodsSpecId 商品规格ID
     * @param imageList 图片列表
     * @return boolean
     */
    boolean saveGoodsSpecImages(String goodsSpecId, List<TnImage> imageList);

    /**
     * 通过商品规格ID删除关联关系
     * 
     * @param goodsSpecId 商品规格ID
     * @return boolean
     */
    boolean deleteByGoodsSpecId(String goodsSpecId);
}
