function showResEvent(podName, namespace){
	var load = common.loading('获取pod事件日志信息......');
	$.post(WEB_APP_PATH+'/project/env/rest/getPodEvent.do',{"podName":podName,"namespace":namespace},function(json){
		load.close();
		if(json.code==0){
			var cd = top.common.dialog({
    			title:'事件日志信息',
    			type: 1,
    			area: ['75%', '400px'],
   			    maxmin: false,
   			    btn:[],
   			    content: render(json)
    		});
		}else{
			top.common.alert(json.msg);
		}
	});
}

function render(json){
	var html = '<div style="padding:10px;"><table class="table_ob details_nr" style="width:900px;table-layout:fixed;">\
	<thead>\
		<tr>\
			<td style="width:150px;">FirstSeen</td>\
			<td style="width:150px;">LastSeen</td>\
			<td style="width:50px;">Count</td>\
			<td style="width:130px;">From</td>\
			<td style="width:150px;">SubobjectPath</td>\
			<td style="width:100px;">Reason</td>\
			<td style="width:170px;">Message</td>\
		</tr>\
	</thead>\
	<tbody>';
	var data = json.data;
	for(var i=0;i<data.items.length;i++){
		var tr = '<tr><td>';
		var d0 = new Date(data.items[i].firstTimestamp);
		d0 = 	d0.format('yyyy-MM-dd hh:mm:ss');
		tr = tr + d0 +"</td><td>";
		var d1 = new Date(data.items[i].firstTimestamp);
		d1 = 	d1.format('yyyy-MM-dd hh:mm:ss');
		tr = tr + d1 +"</td><td>";
		tr = tr + data.items[i].count +"</td><td>";
		
		if(data.items[i].source.component=='kubelet'){
			tr = tr+"<div>kubelet"+  data.items[i].source.host+" </div></td>";
		}else{
			tr = tr+"<div>"+ data.items[i].source.component +"</div></td>";
		}
		tr=tr+'<td><div style="word-break:break-all;white-space:normal; ">'+ data.items[i].involvedObject.fieldPath +'</div></td>';
		tr=tr+'<td>'+data.items[i].reason+'</td>';
		tr=tr+'<td>'+data.items[i].reason+'</td></tr>';
		html = html + tr;
	}
	html = html +'</tbody></table></div>';	
	return html;	
}