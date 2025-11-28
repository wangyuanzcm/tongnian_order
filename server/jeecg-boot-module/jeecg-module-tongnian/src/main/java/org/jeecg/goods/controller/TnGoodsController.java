package org.jeecg.goods.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.system.query.QueryRuleEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import java.util.HashMap;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.goods.entity.TnGoodsSpec;
import org.jeecg.goods.entity.TnGoods;
import org.jeecg.goods.service.ITnGoodsService;
import org.jeecg.goods.service.ITnGoodsSpecService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
 /**
 * @Description: 商品列表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Tag(name="商品列表")
@RestController
@RequestMapping("/goods/tnGoods")
@Slf4j
public class TnGoodsController extends JeecgController<TnGoods, ITnGoodsService> {

	@Autowired
	private ITnGoodsService tnGoodsService;

	@Autowired
	private ITnGoodsSpecService tnGoodsSpecService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param tnGoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "商品列表-分页列表查询")
	@Operation(summary="商品列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TnGoods>> queryPageList(TnGoods tnGoods,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("name", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("isCustom", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<TnGoods> queryWrapper = QueryGenerator.initQueryWrapper(tnGoods, req.getParameterMap(),customeRuleMap);
		Page<TnGoods> page = new Page<TnGoods>(pageNo, pageSize);
		IPage<TnGoods> pageList = tnGoodsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param tnGoods
     * @return
     */
    @AutoLog(value = "商品列表-添加")
    @Operation(summary="商品列表-添加")
    @RequiresPermissions("goods:tn_goods:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TnGoods tnGoods) {
        tnGoodsService.save(tnGoods);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param tnGoods
     * @return
     */
    @AutoLog(value = "商品列表-编辑")
    @Operation(summary="商品列表-编辑")
    @RequiresPermissions("goods:tn_goods:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody TnGoods tnGoods) {
        tnGoodsService.updateById(tnGoods);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "商品列表-通过id删除")
    @Operation(summary="商品列表-通过id删除")
    @RequiresPermissions("goods:tn_goods:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        tnGoodsService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "商品列表-批量删除")
    @Operation(summary="商品列表-批量删除")
    @RequiresPermissions("goods:tn_goods:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.tnGoodsService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequiresPermissions("goods:tn_goods:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TnGoods tnGoods) {
        return super.exportXls(request, tnGoods, TnGoods.class, "商品列表");
    }

    /**
     * 导入
     * @return
     */
    @RequiresPermissions("goods:tn_goods:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TnGoods.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-商品规格/价格明细-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "商品规格/价格明细-通过主表ID查询")
	@Operation(summary="商品规格/价格明细-通过主表ID查询")
	@GetMapping(value = "/listTnGoodsSpecByMainId")
    public Result<IPage<TnGoodsSpec>> listTnGoodsSpecByMainId(TnGoodsSpec tnGoodsSpec,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        // 获取商品ID
        String goodsId = tnGoodsSpec.getGoodsId();
        if (goodsId != null && !"".equals(goodsId)) {
            // 使用selectByMainId方法获取包含图片列表的商品规格
            List<TnGoodsSpec> tnGoodsSpecList = tnGoodsSpecService.selectByMainId(goodsId);
            
            // 构建分页结果
            Page<TnGoodsSpec> page = new Page<TnGoodsSpec>(pageNo, pageSize);
            int total = tnGoodsSpecList.size();
            int start = (int) ((pageNo - 1) * pageSize);
            int end = Math.min(start + pageSize, total);
            
            if (start < total) {
                page.setRecords(tnGoodsSpecList.subList(start, end));
            }
            page.setTotal(total);
            
            return Result.OK(page);
        } else {
            // 如果没有提供商品ID，使用普通分页查询
            QueryWrapper<TnGoodsSpec> queryWrapper = QueryGenerator.initQueryWrapper(tnGoodsSpec, req.getParameterMap());
            Page<TnGoodsSpec> page = new Page<TnGoodsSpec>(pageNo, pageSize);
            IPage<TnGoodsSpec> pageList = tnGoodsSpecService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

	/**
	 * 添加
	 * @param tnGoodsSpec
	 * @return
	 */
	@AutoLog(value = "商品规格/价格明细-添加")
	@Operation(summary="商品规格/价格明细-添加")
	@PostMapping(value = "/addTnGoodsSpec")
	public Result<String> addTnGoodsSpec(@RequestBody TnGoodsSpec tnGoodsSpec) {
		tnGoodsSpecService.save(tnGoodsSpec);
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 * @param tnGoodsSpec
	 * @return
	 */
	@AutoLog(value = "商品规格/价格明细-编辑")
	@Operation(summary="商品规格/价格明细-编辑")
	@RequestMapping(value = "/editTnGoodsSpec", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editTnGoodsSpec(@RequestBody TnGoodsSpec tnGoodsSpec) {
		tnGoodsSpecService.updateById(tnGoodsSpec);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品规格/价格明细-通过id删除")
	@Operation(summary="商品规格/价格明细-通过id删除")
	@DeleteMapping(value = "/deleteTnGoodsSpec")
	public Result<String> deleteTnGoodsSpec(@RequestParam(name="id",required=true) String id) {
		tnGoodsSpecService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品规格/价格明细-批量删除")
	@Operation(summary="商品规格/价格明细-批量删除")
	@DeleteMapping(value = "/deleteBatchTnGoodsSpec")
	public Result<String> deleteBatchTnGoodsSpec(@RequestParam(name="ids",required=true) String ids) {
	    this.tnGoodsSpecService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportTnGoodsSpec")
    public ModelAndView exportTnGoodsSpec(HttpServletRequest request, TnGoodsSpec tnGoodsSpec) {
		 // Step.1 组装查询条件
		 QueryWrapper<TnGoodsSpec> queryWrapper = QueryGenerator.initQueryWrapper(tnGoodsSpec, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<TnGoodsSpec> pageList = tnGoodsSpecService.list(queryWrapper);
		 List<TnGoodsSpec> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "商品规格/价格明细");
		 mv.addObject(NormalExcelConstants.CLASS, TnGoodsSpec.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品规格/价格明细报表", "导出人:" + sysUser.getRealname(), "商品规格/价格明细"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importTnGoodsSpec/{mainId}")
    public Result<?> importTnGoodsSpec(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<TnGoodsSpec> list = ExcelImportUtil.importExcel(file.getInputStream(), TnGoodsSpec.class, params);
				 for (TnGoodsSpec temp : list) {
                    temp.setGoodsId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 tnGoodsSpecService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-商品规格/价格明细-end----------------------------------------------*/




}
