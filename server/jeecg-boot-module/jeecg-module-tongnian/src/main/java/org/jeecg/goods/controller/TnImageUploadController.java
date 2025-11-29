package org.jeecg.goods.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.goods.entity.TnImage;
import org.jeecg.goods.service.ITnImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品图片上传控制器
 * @Author: jeecg-boot
 * @Date: 2024-05-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/api/goods/image")
@Tag(name="商品图片上传接口")
@Slf4j
public class TnImageUploadController {

    @Autowired
    private ITnImageService tnImageService;

    /**
     * 上传商品图片
     * @param file 上传的文件
     * @return 包含图片信息的结果对象
     */
    @Operation(summary = "上传商品图片")
    @PostMapping(value = "/upload")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件是否为空
            if (file == null || file.isEmpty()) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "上传文件为空");
                return errorResult;
            }

            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (!validFileType(fileName)) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "文件类型不支持，仅支持图片文件");
                return errorResult;
            }

            // 验证文件大小（限制为10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "文件大小不能超过10MB");
                return errorResult;
            }

            // 保存文件到minio
            String url = MinioUtil.upload(file, "goods");
            if (oConvertUtils.isEmpty(url)) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "上传失败");
                return errorResult;
            }

            // 保存图片信息到tn_image表
            TnImage tnImage = new TnImage();
            tnImage.setName(fileName);
            tnImage.setUrl(url);
            tnImage.setStatus("1"); // 设置状态为已上传
            tnImageService.save(tnImage);

            // 返回包含success、message、uid、name、url和status的图片数据，符合前端JImageUpload组件要求
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "上传成功");
            res.put("uid", tnImage.getId());
            res.put("name", fileName);
            res.put("url", url);
            res.put("status", "done");

            return res;
        } catch (Exception e) {
            log.error("图片上传失败：", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "上传失败：" + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 验证文件类型是否为图片
     * @param fileName 文件名
     * @return 是否为有效的图片文件
     */
    private boolean validFileType(String fileName) {
        if (oConvertUtils.isEmpty(fileName)) {
            return false;
        }
        // 获取文件扩展名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 定义允许的图片文件类型
        List<String> validExts = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
        return validExts.contains(ext);
    }
}