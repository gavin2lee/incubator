package com.lachesis.mnisqm.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.StringUtils;

/**
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 * 
 * 
 */
public class AnnotationHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
		BaseDataVo result = new BaseDataVo();
		//如果是已经处理的异常
		if(exception instanceof CommRuntimeException){
			CommRuntimeException e = (CommRuntimeException)exception;
			if(StringUtils.isEmpty(e.getCode())){
				result.setCode(Constants.FailureCode);
			}else{
				result.setCode(e.getCode());
			}
			result.setMsg(e.getMessage());
		}else{
			result.setCode(Constants.FailureCode);
			result.setMsg(exception.getMessage());
		}

		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(result.toJsonString());
			writer.close();
		} catch (IOException e) {
		}
		return null;
	}

}