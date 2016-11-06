<%@ page language="java" pageEncoding="UTF-8"%> 
<div>
	<table class="table table-condensed table-bordered table-hover" style="margin-left:auto;margin-right:auto;">
		<thead>
			<tr>
				<th style="width: 180px;">资源名称</th>
				<th style="width: 180px;">资源值</th>
				<th style="width: 50px;text-align: center;">
					<a href="javascript:void(0)" title="添加资源键值对" onclick="addNewRow({})">
						<i class=" " style="font-size: 120%;">+</i> 
					</a>
				</th>
			</tr>
		</thead>
		<tbody id="userDefinedResTbl">
		</tbody>
	</table>
</div>
<script>
	function showTables(sources){
		if(typeof sources != 'undefined'){
			for(var key in sources){
				var resource ={};
				resource.key = key;
				resource.value =sources[key];
				addNewRow(resource);
			}
		}
	}

	function addNewRow(resource){
		var table = $("#userDefinedResTbl");
		var rowIndex = table.find(">tr").length;
		var row =$("<tr class='tr"+rowIndex+"'></tr>");
		var cell1 = $("<td></td>");
		var resourceKey = $("<input type='text' name='resourceKey'/>");
		cell1.append("define.").append(resourceKey);
		if(typeof resource != 'undefined'){
			resourceKey.val(resource.key);
		}
		row.append(cell1);
		var cell2 = $("<td></td>");
		var resourceValue = $("<input type='text' name='resourceValue'/>");
		cell2.append(resourceValue);
		if(typeof resource != 'undefined'){
			resourceValue.val(resource.value);
		}
		row.append(cell2);
		var cell3 = $("<td class='centerAlign'></td>");
		cell3.append("<a href='javascript:void(0)' onclick=\"deleteRow("+rowIndex+")\"><i class=' '>-</i></a>");
		row.append(cell3);
		table.append(row);
	}
	
	function deleteRow(index){
		var table =$("#userDefinedResTbl");
		var rowIndex = table.find(">tr").length;
		var source = [];
		for(var i=0;i<rowIndex;i++){
			var row = $(table.find(">tr")[i]);
			var key = $.trim(row.find("input[name='resourceKey']").val());
			var value = $.trim(row.find("input[name='resourceValue']").val());
			var resource ={};
			resource.key= key;
			resource.value = value;
			source.push(resource);
		}
		table.empty();
		source.splice(index,1);
		for(var j=0;j<source.length;j++){
			addNewRow(source[j]);
		}
	}
	
	var jsonRes= '${resourceConfig}';
	if(jsonRes){
		var obj = JSON.parse(jsonRes);
		showTables(obj);
	}
</script>