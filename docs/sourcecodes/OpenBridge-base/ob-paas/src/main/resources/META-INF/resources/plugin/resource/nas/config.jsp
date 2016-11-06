<%@ page language="java" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 		prefix="c"%>
<c:choose>
	<c:when test="${ param['scene'] eq 'edit' }"> 
	
<div class="form-group">
	<label class="col-sm-3 col-md-3 col-xs-3 col-lg-3">存储容量</label>
	<div class="col-sm-9 col-md-9 col-xs-9 col-lg-9">
		<select name="容量">
			<option value="10" ${ resourceConfig['容量'] eq '10' ? 'selected' : '' }>10G</option>
			<option value="30" ${ resourceConfig['容量'] eq '30' ? 'selected' : '' }>30G</option>
			<option value="60" ${ resourceConfig['容量'] eq '40' ? 'selected' : '' }>60G</option>
			<option value="100" ${ resourceConfig['容量'] eq '100' ? 'selected' : '' }>100G</option>
		</select>
	</div>
</div>

	</c:when>
	
	<c:when test="${ param['scene'] eq 'show' }">

<dl class="">
    <dt>容量</dt>
    <dd>${ resourceConfig['容量'] }G</dd>
</dl> 

	</c:when>
</c:choose>
