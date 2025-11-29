package org.jeecg.order.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.order.entity.TnOrder;
import org.jeecg.order.entity.TnOrderGoods;
import org.jeecg.order.service.ITnOrderService;
import org.jeecg.order.service.ITnOrderGoodsService;
import org.jeecg.receiver.entity.TnRecipient;
import org.jeecg.receiver.service.ITnRecipientService;
import org.jeecg.order.vo.TnOrderPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
 /**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2025-11-23
 * @Version: V1.0
 */
@Tag(name="订单主表")
@RestController
@RequestMapping("/order/tnOrder")
@Slf4j
public class TnOrderController {
	@Autowired
private ITnOrderService tnOrderService;
@Autowired
private ITnOrderGoodsService tnOrderGoodsService;
@Autowired
private ITnRecipientService recipientService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tnOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订单主表-分页列表查询")
	@Operation(summary="订单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TnOrderPage>> queryPageList(TnOrder tnOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<TnOrder> queryWrapper = QueryGenerator.initQueryWrapper(tnOrder, req.getParameterMap());
		Page<TnOrder> page = new Page<TnOrder>(pageNo, pageSize);
		IPage<TnOrder> pageList = tnOrderService.page(page, queryWrapper);
		
		// 转换为包含商品信息和收件人信息的Page对象
		IPage<TnOrderPage> resultPage = pageList.convert(order -> {
			TnOrderPage orderPage = new TnOrderPage();
			// 复制基本信息
			BeanUtils.copyProperties(order, orderPage);
			
			// 查询关联的商品信息
			List<TnOrderGoods> goodsList = tnOrderGoodsService.selectByMainId(order.getId());
			orderPage.setTnOrderGoodsList(goodsList);
			
			// 查询关联的收件人信息
			if (order.getRecipientId() != null) {
				TnRecipient recipient = recipientService.getById(order.getRecipientId());
				orderPage.setRecipient(recipient);
			}
			
			return orderPage;
		});
		
		return Result.OK(resultPage);
	}
	
	/**
	 *   添加
	 *
	 * @param tnOrderPage
	 * @return
	 */
	@AutoLog(value = "订单主表-添加")
	@Operation(summary="订单主表-添加")
    @RequiresPermissions("order:tn_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TnOrderPage tnOrderPage) {
		TnOrder tnOrder = new TnOrder();
		BeanUtils.copyProperties(tnOrderPage, tnOrder);
		tnOrderService.saveMain(tnOrder, tnOrderPage.getTnOrderGoodsList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tnOrderPage
	 * @return
	 */
	@AutoLog(value = "订单主表-编辑")
	@Operation(summary="订单主表-编辑")
    @RequiresPermissions("order:tn_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TnOrderPage tnOrderPage) {
		TnOrder tnOrder = new TnOrder();
		BeanUtils.copyProperties(tnOrderPage, tnOrder);
		TnOrder tnOrderEntity = tnOrderService.getById(tnOrder.getId());
		if(tnOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		tnOrderService.updateMain(tnOrder, tnOrderPage.getTnOrderGoodsList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单主表-通过id删除")
	@Operation(summary="订单主表-通过id删除")
    @RequiresPermissions("order:tn_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		tnOrderService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单主表-批量删除")
	@Operation(summary="订单主表-批量删除")
    @RequiresPermissions("order:tn_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tnOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "订单主表-通过id查询")
	@Operation(summary="订单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TnOrder> queryById(@RequestParam(name="id",required=true) String id) {
		TnOrder tnOrder = tnOrderService.getById(id);
		if(tnOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tnOrder);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "订单商品关联表-通过主表ID查询")
	@Operation(summary="订单商品关联表-通过主表ID查询")
	@GetMapping(value = "/queryTnOrderGoodsByMainId")
	public Result<IPage<TnOrderGoods>> queryTnOrderGoodsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TnOrderGoods> tnOrderGoodsList = tnOrderGoodsService.selectByMainId(id);
		IPage <TnOrderGoods> page = new Page<>();
		page.setRecords(tnOrderGoodsList);
		page.setTotal(tnOrderGoodsList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tnOrder
    */
    @RequiresPermissions("order:tn_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TnOrder tnOrder) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<TnOrder> queryWrapper = QueryGenerator.initQueryWrapper(tnOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<TnOrder>  tnOrderList = tnOrderService.list(queryWrapper);

      // Step.3 组装pageList
      List<TnOrderPage> pageList = new ArrayList<TnOrderPage>();
      for (TnOrder main : tnOrderList) {
          TnOrderPage vo = new TnOrderPage();
          BeanUtils.copyProperties(main, vo);
          List<TnOrderGoods> tnOrderGoodsList = tnOrderGoodsService.selectByMainId(main.getId());
          vo.setTnOrderGoodsList(tnOrderGoodsList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单主表列表");
      mv.addObject(NormalExcelConstants.CLASS, TnOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单主表数据", "导出人:"+sysUser.getRealname(), "订单主表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("order:tn_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
              List<TnOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TnOrderPage.class, params);
              for (TnOrderPage page : list) {
                  TnOrder po = new TnOrder();
                  BeanUtils.copyProperties(page, po);
                  tnOrderService.saveMain(po, page.getTnOrderGoodsList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
