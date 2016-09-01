$(function(){

	$.when(
            $.get(ay.contextPath + "/nur/doc/queryComWard"),
            $.get(ay.contextPath + "/nur/doc/getDocType")
        ).then(function(dept,type){

        	var deptList = dept[0].data,
        		typeList = type[0].data,
        		i,
        		dept_code = $("#dept_code"),
        		doc_type  = $("#doc_type");
        	for(i=0;i<deptList.length;i++){
        		dept_code.append("<option value='"+deptList[i].code+"'>"+deptList[i].name+"</option>");
        	}
        	for(i=0;i<typeList.length;i++){
        		doc_type.append("<option value='"+typeList[i].doc_type_id+"'>"+typeList[i].doc_type_name+"</option>")
        	}

        });

});