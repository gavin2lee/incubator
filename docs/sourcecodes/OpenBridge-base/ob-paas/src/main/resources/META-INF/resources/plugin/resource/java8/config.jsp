<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 		prefix="c"%>
<c:choose>
	<c:when test="${ param['scene'] eq 'edit' }"> 

<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">CPU</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="cpu">
			<option value="1" ${ resourceConfig['cpu'] eq '1' ? 'selected' : '' }>1核</option>
			<option value="2" ${ resourceConfig['cpu'] eq '2' ? 'selected' : '' }>2核</option>
		</select> 
	</div>
</div>  
<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">内存</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="内存">
			<option value="1" ${ resourceConfig['内存'] eq '1' ? 'selected' : '' }>1GB</option>
			<option value="2" ${ resourceConfig['内存'] eq '2' ? 'selected' : '' }>2GB</option>
			<option value="3" ${ resourceConfig['内存'] eq '3' ? 'selected' : '' }>3GB</option>
			<option value="4" ${ resourceConfig['内存'] eq '4' ? 'selected' : '' }>4GB</option>
		</select>
	</div>
</div>  
<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">容器个数</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="容器个数">
			<option value="1" ${ resourceConfig['容器个数'] eq '1' ? 'selected' : '' }>1</option>
			<option value="2" ${ resourceConfig['容器个数'] eq '2' ? 'selected' : '' }>2</option>
			<option value="3" ${ resourceConfig['容器个数'] eq '3' ? 'selected' : '' }>3</option>
			<option value="4" ${ resourceConfig['容器个数'] eq '4' ? 'selected' : '' }>4</option>
		</select>
	</div>
</div>
 
	</c:when>
	
	<c:when test="${ param['scene'] eq 'show' }">

<dl class="">
    <dt>CPU</dt>
    <dd>${ resourceConfig['cpu'] }核</dd>
</dl>
<dl class="">
    <dt>内存</dt>
    <dd>${ resourceConfig['内存'] }GB</dd>
</dl>
<dl class="">
    <dt>容器数</dt>
    <dd>${ resourceConfig['容器个数'] }个</dd>
</dl>

	</c:when>
</c:choose>

