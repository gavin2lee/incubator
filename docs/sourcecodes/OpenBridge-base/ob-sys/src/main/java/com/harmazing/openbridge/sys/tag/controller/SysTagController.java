package com.harmazing.openbridge.sys.tag.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.openbridge.sys.tag.model.SysTag;
import com.harmazing.openbridge.sys.tag.service.ISysTagService;
import com.harmazing.framework.util.StringUtil;

@Controller
@RequestMapping("/sys/tag")
public class SysTagController extends AbstractController{
	private static final Log logger = LogFactory
			.getLog(SysTagController.class);
	
	@Autowired
	private ISysTagService tagService;
	
	/**
	 * 列表显示标签
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/page")
	public String pageTag(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String keyWord = request.getParameter("keyWords");
			if(keyWord!=null && !keyWord.trim().equals("")){
				params.put("keyword", keyWord);
				request.setAttribute("keyWords", keyWord);
			}
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 10));

			List<Map<String, Object>> pageData = tagService.tagPage(params);
			List<String> hotTags = tagService.getAllHotTags();
			if(hotTags!=null && hotTags.size()>0){
				StringBuilder sb = new StringBuilder();
				for(String tag : hotTags){
					sb.append(tag);
					sb.append(" ,");
				}
				String hottag = sb.toString();
				request.setAttribute("hottag", hottag.substring(0, hottag.length()-1));
			}
			request.setAttribute("pageData", pageData);
			return getUrlPrefix() + "/tag_page";
		} catch (Exception e) {
			logger.error("标签列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 * 删除标签
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteTag(HttpServletRequest request,
			HttpServletResponse response){
		try{
			String tagId = request.getParameter("tagId");
			String batchDel = request.getParameter("batchDel");
			if(batchDel!=null && batchDel.equals("T")){
				String [] tagids = tagId.split(",");
				tagService.batchDeleteTag(tagids);
			}else
			{
				tagService.deleteTag(tagId);
			}
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("删除标签保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 改变标签是否属于热门状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public JsonResponse changeTagStatus(HttpServletRequest request,
			HttpServletResponse response){
		try{
			String tagId = request.getParameter("tagId");
			String currentStatus = request.getParameter("currentStatus");
			Boolean status =false;
			if(currentStatus!=null && currentStatus.equals("true")){
				status=false;
			}else
			{
				status = true;
			}
			System.out.println(tagId);
			tagService.updateTagStatus(tagId,status);
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("修改标签保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 显示新增或修改标签的表单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/dialog")
	public String dialogTag(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String tagId = request.getParameter("tagId");
			if(tagId!=null && !tagId.trim().equals("")){
				SysTag tag = tagService.getTagById(tagId);
				if(tag!=null){
					request.setAttribute("tagId", tag.getTagId());
					request.setAttribute("tagName", tag.getTagText());
					request.setAttribute("hot",tag.getHot());
				}
			}
			return getUrlPrefix() + "/tag_dialog";
		} catch (Exception e) {
			logger.error("标签列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 * 新增或修改标签
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public JsonResponse saveOrUpdateTag(HttpServletRequest request,
			HttpServletResponse response){
		try{
			String tagId = request.getParameter("tagId");
			String tagName = request.getParameter("tagName");
			String tagStatus = request.getParameter("hot");
			SysTag tag = new SysTag();
			if(tagId !=null && !tagId.equals("")){
				tag.setTagId(tagId);
			}
			
			tag.setTagText(tagName);
			if(tagStatus!=null && tagStatus.equals("true"))
				tag.setHot(true);
			else
				tag.setHot(false);
			int ret=tagService.saveOrUpdateTag(tag);
			if(ret==-1){
				return JsonResponse.failure(500, "标签名已存在!");
			}
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("修改标签保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
