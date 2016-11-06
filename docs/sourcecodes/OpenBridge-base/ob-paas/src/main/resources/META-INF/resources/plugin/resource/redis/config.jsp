<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 		prefix="c"%>
<c:choose>
	<c:when test="${ param['scene'] eq 'edit' }"> 
	
<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">存储容量</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="容量">
			<option value="1G" ${ resourceConfig['容量'] eq '1G' ? 'selected' : '' }>1G</option>
			<option value="3G" ${ resourceConfig['容量'] eq '3G' ? 'selected' : '' }>3G</option>
			<option value="6G" ${ resourceConfig['容量'] eq '6G' ? 'selected' : '' }>6G</option>
			<option value="10G" ${ resourceConfig['容量'] eq '10G' ? 'selected' : '' }>10G</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">存储类型</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="类型">
			<option value="BTREE" ${ resourceConfig['类型'] eq 'BTREE' ? 'selected' : '' }>BTREE</option>
			<option value="HASH" ${ resourceConfig['类型'] eq 'HASH' ? 'selected' : '' }>HASH</option>
		</select>
	</div>
</div>

	</c:when>
	
	<c:when test="${ param['scene'] eq 'show' }">

<dl class="">
    <dt>容量</dt>
    <dd>${ resourceConfig['容量'] }</dd>
</dl> 
<dl class="">
    <dt>存储类型</dt>
    <dd>${ resourceConfig['类型'] }</dd>
</dl> 

	</c:when>
</c:choose>
