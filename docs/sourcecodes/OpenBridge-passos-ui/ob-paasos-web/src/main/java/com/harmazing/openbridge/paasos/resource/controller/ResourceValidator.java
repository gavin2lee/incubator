package com.harmazing.openbridge.paasos.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IValidator;
import com.harmazing.framework.authorization.ValidatorContext;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.resource.model.PaasResource;
import com.harmazing.openbridge.paasos.resource.service.IPaasResourceService;


@Component("ResourceValidator")
public class ResourceValidator implements IValidator {

	
	@Autowired
	private IPaasResourceService paasResourceService;


	private PaasResource loadPaasResource(String resourceId, HttpServletRequest request) {
		PaasResource resource = null;
		if (request.getAttribute("resource") != null) {
			PaasResource resouceInReq = (PaasResource) request.getAttribute("resource");
			if (resouceInReq.getResId().equals(resourceId)) {
				resource = resouceInReq;
			} else {
				throw new RuntimeException("参数错误,request缓存资源与请求资源不匹配");
			}
		} else {
			resource = paasResourceService.getPaasResourceById(resourceId);
			request.setAttribute("resource", resource);
		}
		return resource;
	}

	/**
	 * check whether the tenantId of resource and user is equal
	 */
	@Override
	public boolean validate(ValidatorContext validatorContext) throws Exception {
		HttpServletRequest request = validatorContext.getRequest();
		IUser user = WebUtil.getUserByRequest(request);
		String resourceId = validatorContext.getParams().get("resId");
		if (StringUtil.isNull(resourceId)) {
			return false;
		}
		PaasResource resource = loadPaasResource(resourceId, request);
		if(null == resource){
			return false;
		}
		if (resource.getTenantId().equals(user.getTenantId())) {
			return true;
		}
		return false;
	}
}
