var rowMenuData = [[{ text: "删除该行",func:function(){$(this).hide();} }]];

Date.prototype.format = function(format) {

	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

$("#print").click(function(){
	if(parser.isTableTemp){

		printTable();

		// $("#viewBox").show();
		// $("#editBox").hide();
		// $("#viewDoc").attr("src","http://localhost:8080/report/printpdf?template_id="+parser.tableTempId+"&inpatient_no=1");
	}
	else{
		printPage(printHtml);
	}
	
});

function printPage(cb){
    var win = $('#printFrame')[0].contentWindow;
    var html = $("#editBox").html();
    //html = html.replace(/zoom\:\s[0-9]*\.?[0-9]*/,'zoom:1');
    cb(html,win);
}

function printTable(){
	var w = $('#printFrame')[0].contentWindow;
    var html = $("#editBox").html();
	w.document.head.innerHTML ='<link rel="stylesheet" type="text/css" href="./resources/style/normalize.css"><link rel="stylesheet" type="text/css" href="./resources/style/demo.css">'
    w.document.body.innerHTML = html;
    var printWin = $(w.document.body)
    printWin.find(".editor").css({
    	"padding":"0",
    	"margin":"0"
    });
    //$("#dataTable").css("font-size","13px");
    $("#printFrame").show();
    printWin.find("table").css("font-size","12px");
    parser.dataToPrintView(printWin);
    setTimeout(function(){
    	w.print()
    },1000);
}

function printHtml(html,w) {
	w.document.head.innerHTML ='<link rel="stylesheet" type="text/css" href="./resources/style/demo.css">'
    w.document.body.innerHTML = html;
    var printWin = $(w.document.body);
    printWin.addClass('print');
	if (printWin.find('[itemname="出院记录"]').length !== 0) {}
    printWin.find('.editor').each(function(i){
    	$(this).css({
    		"font-weight":100
    	})
    	$(this).append("<div style='text-align:center;font-size:12px;'>第（"+(i+1)+"）页</div>");
    });
    //修改打印时checkbox样式
    printWin.find(".block.inputItem input[type='checkbox']").each(function(){
    	$(this).after("<label for='"+$(this).attr('id')+"'></label>"); 	
    });
	// TODO 在此修改打印时候的审核时间
	// 使用jQuery找到审核时间的input并清空其value
    printWin.find(".block.inputItem[data-type='DAT_TIM']").each(function(){
    	$(this).html("");
    	$(this).append("<input type='text'>");
    });
    //$.parser.parse();
    parser.dataToPrintView(printWin);

    setTimeout(function(){
    	w.print()
    },1000);
}

var parser = {
	state:'add',
	editIndex:null,
	page:{
		count:0,
		currenIndex:0,
		breakIndex:[]
	},
	isTableTemp:true,
	tableTempId:null,
	choosedTreeNode:null,
	config:null,
	pageIndex:0,
	containter:null,
	isEdit:{state:false,target:null,index:{tr:null,td:null},thIndex:null},
	bands:[],
	params:{},
	reset:function(){
		/*this.cacheData = {oldVal:{},newVal:{},dataElement:{}};
		this.config='';
		this.containter = null;
		this.isEdit = {s:false,target:null};
		this.bands = [];
		this.single = {list:[],}
		this.params = {};*/
		this.config=null;
		this.pageIndex = 0;
		this.editIndex = null;
		this.isTableTemp = true;
		this.tableTempId = null;
		this.detail = {};
		this.containter = null;
		Data.data("recordId",null);
		Data.data("data_list",[]);
		Data.data("data_item",{});
		Data.data("dyna_list",[]);
		$("#addNewLine").unbind('click');
		$("#editBox").html("");
	},
	getValFormOptions:function(key,options){
		var op = options;
		for(var i=0;i<op.length;i++){
			if(key == op[i].code){
				return op[i].value;
			}
		}
		return null;
	},
	/*
		保存数据到本地
	*/
	saveToData:function(params){
		var that = this;

		var inputItem = $(".inputItem");
		//获取所有非表格性质的录入框内容
		inputItem.each(function(i,n){
			var type = $(this).attr('data-type');
			var data_item = Data.data("data_item");
			//非表格形式数据获取
			var data = that.getDataFromInput($(this),type);
			if(data){
				if(data.val == '' && data_item[data.key] && data.list_item && data.list_item.length == 0 ){ data_item[data.key].record_value = '';data_item[data.key].list_item=[];}
				//if(data.val || (data.list_item && data.list_item.length>0) ){
				if(true){	
					if(!data_item[data.key]){
						data_item[data.key] = {
							"record_value":data.val,
							"record_item_id": data.val,
							"record_id": null
						}
						if(data.list_item && data.list_item.length>0){
							data_item[data.key]['list_item'] = data.list_item;
						}
					}
					else{
						data_item[data.key].record_value = data.val;
						if(data.list_item && data.list_item.length>0){
							data_item[data.key]['list_item'] = data.list_item;
						}
					}
				}
			}
		});
			//表格形式的数据获取
		var data_list = Data.data("data_list");
		var tableList = $("#dataTable");
		var trs = tableList.find("tr");
		trs.each(function(rowIndex){
			var tds = $(this).find('td');
			tds.each(function(cellIndex){
				var val = that.getTableListData($(this),rowIndex,cellIndex);
				//if(val != undefined){
					if(!data_list[rowIndex]){
						data_list.push({
							"record_id": null,
							"data_index": null
						});
					}
					that.setTableListData(val,rowIndex,cellIndex);
				//}
			});
		});
		/*
			动态表头数据
		*/
		var dynaList = [];
		var dynaListItems = $(".dynaTableHeader");
		var list_detail = [];
		dynaListItems.each(function(i){
			var dataKey = ["TITLE_ONE","TITLE_TWO","TITLE_THREE","TITLE_FOUR","TITLE_FIVE"][i];
			list_detail.push({
				"data_key":dataKey,
				"data_value":$(this).attr("data-value") || "null" 
			});
		});
		dynaList  = list_detail.length>0 ? list_detail : [];
		Data.data("dyna_list",dynaList);
		//将本地数据转换成保存到服务器的格式
		var recordStr = this.localDataToServerData();
		this.saveToServer(recordStr,params);
	},
	localDataToServerData:function(){
		var data_item = Data.data('data_item');
		var data_list = Data.data('data_list');
		var dyna_list = Data.data("dyna_list");

		var recordStr = {};
		if(!Data.data('recordId')){
			recordStr={
				"recordId"   : null,
				"createTime" : null,
				"modify_time": null,
				"inpatient_no": 1,
				"template_id": Data.data('template_id'),
				"data_list":[],
				"data_item":[]
			};
		}
		else{
			recordStr={
				"recordId"   : Data.data('recordId'),
				"createTime" : Data.data('createTime'),
				"modify_time": Data.data('modify_time'),
				"inpatient_no": Data.data('inpatient_no'),
				"template_id": Data.data('template_id'),
				"data_list":[],
				"data_item":[]
			};
		}
		for( x in data_item){
			recordStr.data_item.push({
				"template_item_id":x,
				"record_value": data_item[x].record_value,
				"record_item_id": data_item[x].record_item_id,
				"record_id": null
			});
			if(data_item[x].list_item){
				recordStr.data_item[recordStr.data_item.length-1]["list_item"] = data_item[x].list_item;
				//recordStr.data_item[recordStr.data_item.length-1].record_value = '';
			}
		}
		var list_detail = [];
		var index = parser.editIndex;
		for(var i=0;i<data_list.length;i++){
			if(index>=0 && index!=i) continue;
			list_detail = [];
			for(cell in data_list[i]){
				if(cell != "record_id" &&  cell != "data_index" ){
					list_detail.push({
						"data_key": cell,
						"data_value": data_list[i][cell].data_value
					});
				}
				
			}
			recordStr.data_list.push({
				"record_id": data_list[i].record_id,
				"data_index": null,
				"list_detail":list_detail	
			});

		}
		//暂时只能一条一条更新 
		//外部recordId存放的最新一条ID 
		//将内部record_id提取到外部
		if(recordStr.data_list.length>0){
			recordStr.recordId = recordStr.data_list[0].record_id;	
		}

		recordStr.dyna_list = dyna_list;

		return recordStr;
	},
	// 保存至服务器
	saveToServer:function(recordStr,params){
		var that = this;
		try{
			$.post("./saveData",{"recordStr":JSON.stringify(recordStr)},function(d){
				console.log(d);
				if(d.rslt == "0"){
					if(params.active){
						$.messager.alert("提示","保存成功");	
					}
					else{
						$.messager.show({
							title:'提示信息',
							msg:'保存成功',
							timeout:1500,
							showType:'slide',
							style:{
								right:"50%",
								top:0
							}
						})
					}
					// $("#save").unbind().bind('click',function(){
					// 	if(that.endEdit().state && that.endEdit().saveState){
					// 		that.saveToData({"active":true});	
					// 	}
					// });
					//更新左侧树
					$.get('./getTree',{'dept_code': 1,'valid': 'PC','inpatient_no': 1},function(d){
						var mt = d.data;
						var mtc = getLeftTreeData(mt);
						/*$("#statement").tree("update",{
							data:mtc
						});*/
					});
				}
			});
		}
		catch(e){
			console.log(e);
			$("#save").unbind().bind('click',function(){
				if(that.endEdit().state && that.endEdit().saveState){
					that.saveToData({"active":true});	
				}
			});
		}
	},
	/*
	*	获取表格列表录入数据
	*/
	getTableListData:function(target,rIndex,cIndex){
		var type = target.attr('data-type');
		var value = target.attr('data-value');
		if( type == 'SWT' ){
			if( value == 1  ){
				value = 'Y';
			}
			else{
				value = 'N';
			}
		}
		return value;
	},
	/*
	*	将table数据存入data_list
	*
	*/
	setTableListData:function(val,rIndex,cIndex){
		var data_list = Data.data("data_list");
		var cellObj = data_list[rIndex];
		var cell = parser.detail.cell[cIndex];
		var itemName = cell.params.item_name;
		if(!cellObj[itemName]){
			cellObj[itemName] = {
				"data_value":(val ? val : "")
			};
		}
		else{
			cellObj[itemName].data_value =(val ? val : "");	
		}
	},
	/*
	*	传入目标节点,类型
	*	返回{id:data}
	*/
	getDataFromInput:function(target,type){
		if(!type){return null;}
		var key    = target.attr('itemName'),
			val    = '',
			list_item = [],
			list_son = [],
			input  = null,
			cSpan  = null;
		if('STR' === type || 'NUM' === type || 'BP' === type){
			input = target.children();
			val = input.val();
		}
		else if('SYS_TIME' === type){
			input = target.children();
			val = input.datetimebox('getValue');	
		}
		else if('SEL' === type){
			//单选
			input = target.find("div > span > label > input:checked");
			if(input.length == 0){ 
				return {
					"key":key,
					"val":val,
					"list_item":[]
				}
			}
			val = input.val();
			cSpan = input.parent().parent().children("span");
			var cbc = this.getCheckBoxChild(cSpan);
			for(x in cbc){
				if(cbc[x] instanceof Array && cbc[x].length>=0){
					for(var i=0;i<cbc[x].length;i++){
						list_son.push({
							"template_item_id":x,
							"record_value":cbc[x][i]
						});
					}
				}
				else{
					list_son.push({
						"template_item_id":x,
						"record_value":cbc[x]
					});
				}
			}
			//if(list_son.length > 0){
			list_item.push({
				"template_item_id": input.val(),
				"record_value": input.val(),
				"list_item":list_son
			});
		}
		else if('MSEL' === type){
			var that = this;
			//多选
			//数据分隔符采用[\,,\#,]
			input = target.find("div > span > label > input:checked");
			if(input.length==0){
				return {
					"key":key,
					"val":val,
					"list_item":[]
				}
			}

			input.each(function(){
				val += $(this).val()+",";
				cSpan = $(this).parent().parent().children("span");
				var cbc = that.getCheckBoxChild(cSpan);
				for(x in cbc){
					if(typeof cbc[x] == 'array'){
						for(var i=0;i<cbc[x].length;i++){
							list_son.push({
								"template_item_id":x,
								"record_value":cbc[x][i]
							});
						}
					}
					else{
						list_son.push({
							"template_item_id":x,
							"record_value":cbc[x]
						});
					}
				}
				//if(list_son.length > 0){
				// for(var i=0;i<list_son.length;i++){
					
				// }
				list_item.push({
					"template_item_id": $(this).val(),
					"record_value": $(this).val(),
					"list_item":list_son
				});
				//}
				// if(cbc.check){
				// 	val += "#"+cbc.check;
				// }
				// if(cbc.text.length>0){
				// 	val += "&"+cbc.text;
				// }
				// val += ","
			});
			val = val.substring(0,val.length-1);			
		}
		else if('OPT' === type){
			input = target.children();
			val = input.val();
		}
		else{

		}
		return {
			"key":key,
			"val":val,
			"list_item":list_item
		}
	},
	/*
	*	获取checkbox节点下级节点的内容(checkbox,text)
	*/
	getCheckBoxChild:function(target){
		var cInputCheck = null,
			cInputText  = null,
			childVal = {};
			cCheck = '',cText = '';
		if(target){
			cInputCheck = target.find("label > input[type='checkbox']:checked");
			cInputText = target.find("label > input[type='text']");
		}
		if(cInputCheck.length>0){
			cInputCheck.each(function(){
				if(!childVal[$(this).val()]){
					childVal[$(this).val()] = [];
				}
				//cCheck += $(this).val()+"*";
				childVal[$(this).val()].push($(this).val());
			});
			if(cCheck.length > 0){
				//cCheck = cCheck.substring(0,cCheck.length-1);
			}
		}
		if(cInputText.length>0){
			cInputText.each(function(){
				//cText += ($(this).val() ? $(this).val() : null) + "*";
				childVal[$(this).attr('name')] = $(this).val();
			});
			//cText = cText.substring(0,cText.length-1);
		}
		return childVal/*{
			"check":cCheck,
			"text":cText
		}*/
	},
	/*
		params.edit  是否让第一个录入框进入编辑模式
	*/
	addNewLine:function(table,params){
		var that = this;
		if(!this.detail.rowWrap){
			this.detail.tbody = table.find("tbody");//$(".band[name='detail'] table tbody");
		}
		if(this.editIndex && !this.endEdit().state){
			return false;
		}
		var tbody = this.detail.tbody;
		var tr    = $("<tr></tr>");
		var tw    = tbody.width();
		tbody.append(tr);
		$.each(this.detail.cell,function(i,n){
			var td = $("<td></td>");
			td.attr('data-type',n.params.data_type);
			td.attr('itemname',n.params.item_name);
			td.css({
				"width":(n.params.width-1)+"px"
			});
			tr.append(td);
			td.append("<div style='width:"+(n.params.width-1)+"px;'></div>");
			//td.children().css('display','block').css('display','inline-block');
			tw -= n.params.width;
		});
		if(tw>0){
			tr.append("<td style='width:auto;'></td>");
		}
		//第一个元素进入编辑状态
		if(params.edit){
			this.beginEdit(tr.find("td:eq(0)"),this.detail.cell[0]);	
		}
	},
	//设置编辑的行索引，用以判断是否修改的不是当前行
	setEditIndex:function(target){
		//如果是动态表头则不设置编辑索引;
		if( target.attr("data-type") == "dynaTh" ){
			return;
		}
		var tr = target.parent();
		var index = parseInt(tr.attr("data-index"));
		if(typeof this.editIndex =='number' &&  this.editIndex != index){
			this.saveToData({"active":false});
			$("#dataTable").find("tr").removeClass("edit");
			tr.addClass("edit");
			parser.editIndex = index;
		}
		else if(index == this.editIndex){
			//操作的是同一行，不处理退出
			return ;
		}
		else{
			$("#dataTable").find("tr").removeClass("edit");
			tr.addClass("edit");
			parser.editIndex = index;
		}
	},
	//使某块进入编辑状态
	beginEdit:function(target,cell){
		var that = this;

		//进入编辑状态的控件结束编辑状态
		//若返回false则退出
		if(!this.endEdit().state) return false;

		//若原存在编辑索引
		//与当前索引不同则进行保存操作
		this.setEditIndex(target);

		var cellNode = $(cell.cellTemp),
			div      = $("<div></div>"),
			oValue   = target.attr('data-value'),
			oText    = target.attr('data-text');
		div.css({
			"width":(cell.params.width-1)+"px"
		})
		//cellNode.css('vertical-align','middle;');
		div.append(cellNode);
		target.html(div);
		this.isEdit.state  = true;
		this.isEdit.target = cellNode;
		this.isEdit.index.td  = target.index();
		this.isEdit.index.tr  = target.parent().index();

		if(cell.params.data_type == 'DATE'){
			cellNode.datebox({
				value:oValue,
				required:(cell.params.required ? true : false),
				invalidMessage:"",
				missingMessage:"请填入日期",
				showSeconds:false,
				formatter:function(date){
					return new Date(date).format('yyyy-MM-dd');
				}	
			});
		}
		else if(cell.params.data_type == 'TIME'){
			if(!oValue){
				oValue = new Date().format('hh:mm');
			}
			cellNode.timespinner({
				required:(cell.params.required ? true : false),
				invalidMessage:"",
				missingMessage:"请填入时间",
				value:oValue,
				showSeconds:false
			});
		}
		else if(cell.params.data_type == 'NUM'){
			if(oValue){
				cellNode.val(oValue);
			}
			cellNode.validatebox({
				required:(cell.params.required ? true : false),
				validType:'number['+cell.params.min_value+","+cell.params.max_value+","+cell.params.precision+"]",
				required:cell.params.required
			});
			cellNode.focus();
		}
		else if(cell.params.data_type == 'BP'){
			if(oValue){
				cellNode.val(oValue);
			}
			cellNode.validatebox({
				required:(cell.params.required ? true : false),
				validType:'bp['+cell.params.min_value+","+cell.params.max_value+",'"+cell.params.item_name+"']",
				required:cell.params.required
			});
			cellNode.focus();
		}
		else if(cell.params.data_type == 'STR'){
			cellNode.val(oValue);
			cellNode.validatebox({
				required:(cell.params.required ? true : false),
			});
			cellNode.focus();
		}
		else if(cell.params.data_type == 'OPT'){
			cellNode.attr("size",1);
			var options = cell.params.options,
				opStr   = '',
				flag    = '';
			opStr += '<option '+flag+' value=""></option>';
			for(var i=0;i<options.length;i++){
				flag = '';
				if(options[i].code==oValue){ flag = 'selected="selected"' }
				opStr += '<option '+flag+' value="'+options[i].code+'">'+options[i].value+'</option>';
			}
			cellNode.append(opStr);
		}
		else if(cell.params.data_type == 'SWT'){
			if(target.attr("data-value") == '1'){
				cellNode.attr('checked',true);
			}
		}
		else if(cell.params.data_type == "DYNA_TITLE"){
			var checkbox = cell.params.checkbox,
				cbStr    = '',
				flag     = '';

			for(var i=0;i<checkbox.length;i++){
				cbStr += '<span><label><input name="'+cell.params.itemValue+'" type="checkbox" value="'+checkbox[i].checkbox_code+'" data-text="'+checkbox[i].checkbox_name+'" data-type="'+checkbox[i].data_type+'"/>'+checkbox[i].checkbox_name+'</label></span>'
			}
			cbStr += '<div><button id="cfmCb">确定</button><button id="cancelCb">取消</button></div>';
			cellNode.tooltip({
				content: $('<div class="tab-checkbox">'+cbStr+'</div>'),
				showEvent: 'click',
				hideEvent:null,
				onShow: function(){
					var t = $(this);
					var span = t.tooltip('tip').find(".tab-checkbox");
					var input = t.tooltip('tip').find(".tab-checkbox span input");
					if( cell.params.data_type == "DYNA_TITLE" && !span.attr("data-type")){
						span.attr("data-type","SEL");
						input.each(function(){
							//设置单选
							$(this).bind("click",function(){
								var that = $(this);
								var currClickIndex = that.parent().parent().index();
								if(parseInt(span.attr("data-index")) ==  that.parent().parent().index() ){
									$(this).removeAttr("checked");
									span.attr("data-index",null);
									return ;
								}
								input.each(function(){
									$(this).removeAttr("checked");
									if(currClickIndex == $(this).parent().parent().index()){
										$(this).attr("checked","checked");
										span.attr("data-index",that.parent().parent().index());
									}
								});
							});
						})
					}
					var value=target.attr('data-value'),text=target.attr('data-text');
					if(value){ 
						t.tooltip('tip').find(".tab-checkbox span input").each(function(){
							if(value.indexOf($(this).val()) >=0 ){ $(this).attr('checked',true); }
						});
					}
					t.tooltip('tip').find(".tab-checkbox button#cfmCb").unbind().bind('click',function(){
						//确定操作  更新响应的数据源框，并清空数据
						var thIndex = that.isEdit.thIndex;
						that.isEdit.thIndex = null;
						var checkItem = t.tooltip('tip').find(".tab-checkbox span input:checked");
						var checkType = checkItem.attr("data-type");
						var dynaIndex = cell.params.dynaIndex;
						var dynaDetailCellName = ["DATA_ONE","DATA_TWO","DATA_THREE","DATA_FOUR","DATA_FIVE"][dynaIndex];
						if(that.endEdit().state){
							$.get('./getDynaItemData?checkbox_code='+checkItem.val()+"&data_type="+checkType,function(d){
								var cells = parser.detail.cell;
								$.each(cells,function(i,n){
									if(n.params.item_name == dynaDetailCellName){
										n.params.data_type = checkType;
										var cellTemp = that.getDetailCell(n.params);
										n.cellTemp = cellTemp;
										if(checkType == "SEL"){
											n.params.checkbox = d.data;
										}
										else if(checkType == "OPT"){
											n.params.options = d.data;
										}
									}
								});
								
							});
						}
						return false;
					});
					t.tooltip('tip').find(".tab-checkbox button#cancelCb").unbind().bind('click',function(){
						//取消操作恢复原来状态；
						var thIndex = that.isEdit.thIndex;
						if(parseInt(thIndex)>=0){
							var currTh = $(".dynaTableHeader:eq("+thIndex+")");
							var oldChoose = currTh.attr("data-index");
							span.attr("data-index",oldChoose);
							input.each(function(){
								$(this).removeAttr("checked");
							});
							t.tooltip('tip').find(".tab-checkbox span input:eq("+oldChoose+")").attr("checked","checked")
							that.isEdit.thIndex = null;
						}
						that.endEdit().state;		
						t.tooltip('hide');
						t.tooltip('destroy');
					});
					t.tooltip('tip').unbind().bind('mouseenter', function(){
						return true;
					}).bind('mouseleave', function(){
						return true;
					});
				}	
			});
			cellNode.tooltip("show");
		}
		else if(cell.params.data_type == 'SEL'){
			var checkbox = cell.params.checkbox,
				cbStr    = '',
				flag     = '';

			for(var i=0;i<checkbox.length;i++){
				cbStr += '<span><label><input name="'+cell.params.itemValue+'" type="checkbox" value="'+checkbox[i].checkbox_code+'" data-text="'+checkbox[i].checkbox_ord+'"/>'+checkbox[i].checkbox_ord+'.'+checkbox[i].checkbox_name+'</label></span>'
			}
			cbStr += '<div><button id="cfmCb">确定</button><button id="cancelCb">取消</button></div>';
			cellNode.tooltip({
				content: $('<div class="tab-checkbox">'+cbStr+'</div>'),
				showEvent: 'click',
				hideEvent:null,
				onShow: function(){
					var t = $(this);
					var span = t.tooltip('tip').find(".tab-checkbox");
					var input = t.tooltip('tip').find(".tab-checkbox span input");
					if(cell.params.data_type == 'SEL' && !span.attr("data-type")){
						span.attr("data-type","SEL");
						input.each(function(){
							$(this).bind("click",function(){
								var that = $(this);
								if(parseInt(span.attr("data-index")) ==  that.parent().parent().index() ){
									$(this).removeAttr("checked");
									return ;
								}
								input.each(function(){
									$(this).removeAttr("checked");
									if(that != $(this)){
										that.attr("checked","checked");
										span.attr("data-index",that.parent().parent().index());
									}
								});
							});
						})
					}
					var value=target.attr('data-value'),text=target.attr('data-text');
					if(value){ 
						t.tooltip('tip').find(".tab-checkbox span input").each(function(){
							if(value.indexOf($(this).val()) >=0 ){ $(this).attr('checked',true); }
						});
					}
					t.tooltip('tip').find(".tab-checkbox button#cfmCb").bind('click',function(){
						that.endEdit().state;
						return false;
					});
					t.tooltip('tip').find(".tab-checkbox button#cancelCb").bind('click',function(){
						that.endEdit().state;		
					});
					t.tooltip('tip').unbind().bind('mouseenter', function(){
						return true;
					}).bind('mouseleave', function(){
						return true;
					});
				}	
			});
			cellNode.tooltip("show");
		}
		else{
			cellNode.val(oValue);
			cellNode.focus();
		}
	},
	//结束编辑状态
	/*
		结束编辑状态
		@return {
			state 是否成功endedit
			saveState 是否可以进行保存操作
			addState  是否可以进行新增
		}
	*/
	endEdit:function(){
		var that = this;
		if(!this.isEdit.state && !this.isEdit.target){
			return {
				"state":true,
				"saveState":false,
				"addState":true
			}
		}
		if(this.isEdit.state){
			var target = this.isEdit.target,
				value  = '',text = '',
				className = target.attr('class');
			value = target.val();
			//当是 datebox 的时候无法去调用isValid
			//此处只处理能够使用validatebox来判断是否检验成功
			if(className && className.indexOf('validatebox-text') >= 0 && !target.validatebox('isValid')){
				target.focus();
				return false;
			}
			if( className && className.indexOf('swt') >=0){
				value = 0;
				text  = '';
				if(target.is(":checked")){
					value = 1;
					text  = '√';
				}
			}
			else if(className && className.indexOf('timespinner') >=0){
				//timespinner多了一层结构，需单独处理。
				value = target.timespinner('getValue');
				text  = value;
				target.parent().parent().parent().attr({
					"data-value":value,
					"data-text":text
				});
				target.parent().parent().html("<div>"+text+"</div>");
				this.isEdit.state = false;
				this.isEdit.target = null;
				this.isEdit.index.td  = null;
				this.isEdit.index.tr  = null;
				return {
					"state":true,
					"saveState":true,
					"addState":true
				}
			}
			else if(className && className.indexOf('validatebox-text') >=0){
				value = target.val();
				text  = value;
			}
			else if(className && className.indexOf('datebox') >=0){
				value = target.datebox('getValue');
				text  = value;
				if(target.attr("data-required")){
					if(!value){
						target.focus();
						return {
							"state":false
						}
					}
				}
			}
			
			else if(className && className.indexOf('tooltip') >=0){
				var checkedbox = target.tooltip('tip').find(".tab-checkbox span input");
				var num = 0;
				checkedbox.each(function(n){
					if( $(this).is(":checked")){
						value += ','+$(this).val();
						text  += ','+$(this).attr('data-text');
						if(num == 0){
							value = value.substring(1,value.length);
							text  = text.substring(1,text.length)
						}
						num++;
					}
				});
				target.tooltip('tip').find(".tab-checkbox button#cfmCb").unbind('click');
				target.tooltip('tip').find(".tab-checkbox button#cancelCb").unbind('click');
				target.tooltip('tip').unbind();
				target.tooltip('hide');
				target.tooltip('destroy');
			}
			else if(!className && $(target).is('input')){
				value = $(target).val();
				text  = value;
			}
			else if(!className && $(target).is('select')){
				value = $(target).val();
				text  = $(target).children(":checked").html();
			}
			target.parent().parent().attr({
				"data-value":value,
				"data-text":text
			});
			target.parent().html("<div>"+text+"</div>");
		}
		this.isEdit.state = false;
		this.isEdit.target = null;
		this.isEdit.index.td  = null;
		this.isEdit.index.tr  = null;
		return {
			"state":true,
			"saveState":true,
			"addState":true
		}
	},
	parseOption:function(op){
		var options = "<option value=''> ";
		for(var i=0;i<op.length;i++){
			options +="<option value='"+op[i].code+"'>"+op[i].value;
		}
		return options;
	},
	/*
		初始化模板和数据
		config 模板配置文件
		params {
			type 类型
			rId  记录ID
			tId  模板ID
			day  日期
			time 时间点
		}
	*/
	init:function(config,params){
		var that = this;
		//config = $.parseJSON(config);
		var cfg = config.data,i=0,n=0;
		this.config = cfg;
		this.isTableTemp = params.type == 1 ? false : true;
		if(this.isTableTemp) this.tableTempId = params.tId;
		$.each(cfg,function(i,n){
			that.parseBand(i,n);
		});
		$("#addNewLine").unbind().bind('click',function(){
			if(that.endEdit().state && that.endEdit().addState){
				var table = $(".editor:last-child").find("#dataTable");
				that.addNewLine(table,{"edit":true});
			}
		});
		$("#save").unbind().bind('click',function(){
			if(that.endEdit().state){
				that.saveToData({"active":true});	
				$("#save").unbind("click");
			}
		});
		$(document).unbind().bind('keypress',function(e){
			if(e.keyCode === 13){
				var tr = parser.isEdit.index.tr,td = parser.isEdit.index.td;
				if(that.endEdit().state && parseInt(tr)>=0 && parseInt(td)>=0 && parseInt(td)< $("#dataTable").find("tr:eq("+tr+")").find("td").length-1 ){
					that.beginEdit($("#dataTable").find("tr:eq("+tr+")").find("td:eq("+(td+1)+")"),that.detail.cell[td+1]);
				}
			}
		});
		parser.choosedTreeNode = $("#statement").tree('getSelected');
		$.parser.parse();
		if(parser.choosedTreeNode){
			$("#statement").tree('select',parser.choosedTreeNode.target);
			// $('#statement').tree('update', {
			// 	target: parser.choosedTreeNode.target,
			// 	checked:true
			// });
		}
		this.loadData(params,that.parseData);
	},

	/*
		加载数据
		params {
			type 类型
			rId  记录ID
			tId  模板ID
			day  日期
			time 时间点
		}
	*/
	loadData:function(params,cb){
		var that = this;
		var data = null;
		var rId  = params.rId;
		var day = new Date().format('yyyy-MM-dd');
		if(params.type){
			$.messager.progress(); 	
		}
		
		//try{
			if(params.type == 1){
				$.get('./loadData?inpatient_no=1&template_id='+params.tId+"&record_id="+rId,function(d){
					data = d;
					that.parseData(data.data);
					//that.parseData(d.data);
				});
			}
			else if(params.type == 2){
				$.get("./loadData?inpatient_no=1&template_id="+params.tId+"&data_value=",function(d){
					data = d;
					that.parseData(data.data);
				});
			}
			else if(params.type == "day"){
				$.get("./loadData?inpatient_no=1&template_id="+params.tId+"&data_value="+params.day,function(d){
					data = d;
					that.parseData(data.data);
				});
			}
			else if(params.type == "time"){
				$.get("./loadData?inpatient_no=1&record_id="+rId+"&template_id="+params.tId+"&data_value="+params.day+" "+params.time,function(d){
					data = d;
					that.parseData(data.data);
				});
			}
			else{

			}
		// return {
		// 	"type":type,
		// 	"rId" :rId,
		// 	"day" :day,

		// }
		//}
		/*catch(e){
			console.log(e);
		}*/

		// $.get('./loadData?record_id='+rId,function(d){
		// 	console.log(d);
		// 	data = d;
		// 	//that.parseData(d.data);
		// });
		//return data;
	},
	/*
		解析数据
		@param {object} 数据
	*/
	parseData:function(data){
		var that = this;
		$.messager.progress('close');
		if(data.length==0) return;
		Data.data('recordId',data[0].recordId);
		Data.data('createTime',data[0].createTime);
		Data.data('modify_time',data[0].modify_time);
		Data.data('template_id',data[0].template_id);
		Data.data('inpatient_no',data[0].inpatient_no);
		var i=0;
		var data_item = data[0].data_item;
		var data_item_obj = Data.data('data_item');
		for(i;i<data_item.length;i++){
			data_item_obj[data_item[i].template_item_id] = {
				"record_value": data_item[i].record_value,
				"record_item_id": data_item[i].record_item_id,
				"record_id": data_item[i].record_id
			}
			if(data_item[i].list_item){
				data_item_obj[data_item[i].template_item_id]['list_item'] = data_item[i].list_item;
			}
		}
		var index = -1;
		var data_list = data[0].data_list;
		var data_list_arr = Data.data('data_list');
		var list_detail =[];
		for(i=0;i<data_list.length;i++){
			data_list_arr.push({
				"record_id": data_list[i].record_id,
				"data_index": data_list[i].data_index
			});
			var list_detail = data_list[i].list_detail;
			for(var li=0;li<list_detail.length;li++){
				data_list_arr[data_list_arr.length-1][list_detail[li].data_key] ={
					"data_value":list_detail[li].data_value
				} 
			}
		}

		//新增动态表头读取过程
		//在获取数据的时候动态加载
		var dyna_list = data[0].dyna_list;
		Data.data('dyna_list',dyna_list);
		var dynaListItems = $(".dynaTableHeader");
		var dynaTableCell = parser.detail.changeableCell;
		for(i=0;i<dyna_list.length;i++){

			var dynaCheckbox = dynaTableCell[i].params.checkbox;
			var checkboxItem = getCheckbox(dynaCheckbox,dyna_list[i].data_value);
			var data_type    = checkboxItem ? checkboxItem.data_type : checkboxItem;
			if(!data_type){continue;}
			$(dynaListItems[i]).attr("data-value",dyna_list[i].data_value).attr("data-text",checkboxItem.checkbox_name).children("div").width($(dynaListItems[i]).width()).html("<div>"+checkboxItem.checkbox_name+"</div>");
			var cells =  parser.detail.cell;
			var dynaDetailCellName = ["DATA_ONE","DATA_TWO","DATA_THREE","DATA_FOUR","DATA_FIVE"][i];
			for(var j=0;j<cells.length;j++){
				if(cells[j].params.item_name == dynaDetailCellName){
					$.ajax({
						type: "get",
						async:false,
						url: "./getDynaItemData",
						data: "checkbox_code="+dyna_list[i].data_value+"&data_type="+data_type,
						success: function(d){
						    cells[j].params.data_type = data_type;
							var cellTemp = that.getDetailCell(cells[j].params);
							cells[j].cellTemp = cellTemp;
							if(data_type == "SEL"){
								cells[j].params.checkbox = d.data;
							}
							else if(data_type == "OPT"){
								cells[j].params.options = d.data;
							}
						}
					});

					// $.ajax('./getDynaItemData?checkbox_code='+dyna_list[i].data_value+"&data_type="+data_type,function(d){
					// 	cells[j].params.data_type = data_type;
					// 	cells[j].params.data_type = dyna_list[i].data_value;
					// 	var cellTemp = that.getDetailCell(cells[j].params);
					// 	cells[j].cellTemp = cellTemp;
					// 	if(checkType == "SEL"){
					// 		cells[j].params.checkbox = d.data;
					// 	}
					// 	else if(checkType == "OPT"){
					// 		cells[j].params.options = d.data;
					// 	}
					// });
				}
			}
		}

		//把数据展示到view
		this.DataToView(null,false,function(){});
	},
	/*
		containter 目标容器
		isToPrint  是否是面向打印
		fn         回调
	*/
	DataToView:function(containter,isToPrint,fn){
		var that = this;
		//非表格形式的
		var dataItem = Data.data('data_item');
		var inputItem = null,cInputItem = null;type = null,val = null,list_item = null,
			inputItems = $("body");
		if(containter){
			inputItems = containter;
		}
		for( x in dataItem){
			inputItem  = inputItems.find("div.inputItem[itemname='"+x+"']");
			type       = inputItem.attr('data-type');
			val 	   = dataItem[x].record_value;
			list_item  = dataItem[x].list_item
			if(type == 'STR' || type == 'NUM' || type == 'BP' || type == 'STR' || type == 'OPT'){
				cInputItem = inputItem.children();
				cInputItem.val(val);
			}
			if(type == 'SYS_TIME'){
				cInputItem = inputItem.find("input:eq(0)");
				if(isToPrint){
					cInputItem = $("<input type='text'>");
					inputItem.html("").append(cInputItem);
					cInputItem.val(val);
				}
				else{
					cInputItem.datetimebox({
						value:val
					});	
				}
			}
			if(type == 'SEL' || type == 'MSEL'){
				
				//var checkVal=this.parseCheckData(val);
				var cCheck = null,cInput = null,list_son=null;
				for(var i=0;i<list_item.length;i++){

					cInputItem = inputItem.find("div > span > label > input[value='"+list_item[i].template_item_id+"']");
					cInputItem.attr('checked','checked');
					if(type == 'SEL'){
						var checkindex = cInputItem.parent().parent().index();
						inputItem.children().attr("checkindex",checkindex);
					}
					list_son = list_item[i].list_item;
					cCheck = cInputItem.parent().siblings(".childCheck");
					//子节点大于0让
					if(cCheck.find("input").length>0){
						cCheck.find("input").each(function(){
							$(this).removeAttr('disabled');
						});
					}
					cText  = cInputItem.parent().siblings(".childText");
					for(var ls=0;ls<list_son.length;ls++){
						//相等时有子选择项
						if( list_son[ls].record_value == list_son[ls].template_item_id ){
							cCheck.find("input[value='"+list_son[ls].record_value+"']").attr("checked","checked");
						}
						else{
							cText.find("input[name='"+list_son[ls].template_item_id+"']").removeAttr("disabled").val(list_son[ls].record_value);
						}
					}
				}
			}
		}

		//表格形式的
		var dataList = Data.data('data_list');
		var table = $("#dataTableList"),tr=null,td=null;
		table.hide();
		//渲染所有的数据 该数据只用于计算分页 并隐藏
		//没加载一行就计算一次高度
		var tableHeight = 0;
		var detailHeight = this.detailHeight;
		var remainHeight = detailHeight-tableHeight;
		var breakPageIndex = 0,currenIndex=1;
		for(var i=0;i<dataList.length;i++){
			this.addNewLine(table,{"edit":false});
			tr = table.find("tr:eq("+(i-breakPageIndex)+")");
			for(x in dataList[i]){
				this.dataToCellView(tr,x,dataList[i]);
			}

			//加载一行计算高度
			/*
				加载一行计算一次高度
				当高度超过detail高度的时候对parser.page进行处理增加count、pageindex等
				处理完分页后再进行一次分页后的渲染（或节点复制）
			*/
			tableHeight = table.height();
			remainHeight = detailHeight-tableHeight;
			if(remainHeight <= 0){
				currenIndex += 1;
				this.page.count += 1;
				this.page.breakIndex.push(i);
				detailHeight = this.detailHeight*currenIndex;
				remainHeight = detailHeight-tableHeight;
				/*table.find("tr:last-child").remove();
				breakPageIndex = i;
				i--;
				tableHeight = 0;
				var newPage = $("<div>第 "+this.pageIndex+" 页</div>");
				$("#editor"+this.pageIndex).append(newPage);
				newPage.css({
					"position":"absolute",
					"bottom":"0px",
					"width":"100%",
					"line-height":"15px",
					"text-align":"center"
				});
				var newPageContainter = that.createContainter(that.config[0]);
				var detail = that.initNewTablePage(newPageContainter);
				table = detail.find("#dataTable");
				table.find("tr").remove();*/
			}
		}
		if(dataList.length>0){
			this.page.count += 1;
			this.page.breakIndex.push(dataList.length);
		}

		console.log(this.page);

		/*
			渲染可展示的view 只显示一页
			调用分页渲染方法渲染分页
		*/
		var table = $("#dataTable");

		//获取第一个分页索引 若不存在分页则获取 datalist.length
		var pageOneIndex = this.page.breakIndex.length >= 0 ? this.page.breakIndex[0] : dataList.length;
		this.page.currenIndex = 1;
		for(var i=0;i<pageOneIndex;i++){
			this.addNewLine(table,{"edit":false});
			tr = table.find("tr:eq("+i+")");
			tr.attr("data-index",i);
			for(x in dataList[i]){
				this.dataToCellView(tr,x,dataList[i]);
			}
		}

		if(dataList.length>0){

			//设置页数
			var newPage = $("<div id='pageIndex'>第 "+this.pageIndex+" 页</div>");
			$("#editor1").append(newPage);
			newPage.css({
				"position":"absolute",
				"left":"0px",
				"bottom":"0px",
				"width":"100%",
				"line-height":"15px",
				"text-align":"center"
			});

			// 调用分页
			this.showPagination($("#editor1"),this.page,table);	
		}
		

		// var newPage = $("<div>第 "+this.pageIndex+" 页</div>");
		// $("#editor"+this.pageIndex).append(newPage);
		// newPage.css({
		// 	"position":"absolute",
		// 	"bottom":"0px",
		// 	"width":"100%",
		// 	"line-height":"15px",
		// 	"text-align":"center"
		// });
	},
	showPagination:function(containt,page,table){
		var that = this;

		var dataList = Data.data('data_list'),
			currenPageIndex = this.page.currenIndex,
			currenIndex = 0,
			breakIndex  = this.page.breakIndex,
			count = this.page.count,
			start = 0,end = 0,rowIndex=0,i,
			pagination = $("<div id='pagination' class='pagination'></div>");

		var prev  = $("<div>上一页</div>");
		var mid   = $("<div class='p-mid'></div>");
		var next = $("<div>下一页</div>");
		pagination.append(prev).append(mid).append(next);
		
		if(currenIndex == 1){
			prev.addClass("disabled");
		}
		if(count == 1){
			prev.addClass("disabled");
			next.addClass("disabled");
		}
		var select = $("<select></select>");
		for(i=1;i<=count;i++){
			select.append("<option value='"+i+"'>"+i+"/"+count+"</option>");
		}
		mid.append(select);
		select.unbind().bind("change",function(){
			currenIndex = 0;
			table.find("tbody").html("");
			var val = parseInt($(this).val());
			if(val == 1){
				start = 0;
				end   = breakIndex[val-1];
			}
			else{
				start = breakIndex[val-2];
				end = breakIndex[val-1];
			}

			// TODO该方法可以提取出来
			for(start;start<end;start++){
				that.addNewLine(table,{"edit":false});
				tr = table.find("tr:eq("+currenIndex+")");
				tr.attr("data-index",start);
				for(x in dataList[start]){
					that.dataToCellView(tr,x,dataList[start]);
				}
				currenIndex += 1;
			}

			$("#pageIndex").html("第 "+val+" 页");

		});


		next.unbind().bind('click',function(){
			if(count == 1){
				start = 0;end = dataList.length;
			}
			else{

			}
		});
		prev.unbind().bind('click',function(){
			
		});
		

		containt.append(pagination);



	},
	/*
		数据到cell

	*/
	dataToCellView:function(tr,x,dl){
		td = tr.find("td[itemname='"+x+"']");
		type = td.attr('data-type');
		if(['data_index','record_id'].indexOf(x)>=0){
			return;
		}
		//增加了对 null，空 数据的处理
		if(!dl[x].data_value){
			td.attr({
				"data-value":"",
				"data-text":""
			});
			td.children().html("<div></div>");
			return;
		}
		if(type=='SWT'){
			if(dl[x].data_value == "Y"){
				td.attr({
					"data-value":1
				});
				td.children().html("<div>√</div>");
			}
			else{
				td.attr({
					"data-value":0
				});
				td.children().html("<div></div>");
			}
		}
		else if(type=='SEL'){
			var selIndex = '';
			var checkbox = parser.detail.cell[td.index()].params.checkbox;
			var data_value = dl[x].data_value;
			$.each(data_value.split(","),function(ci,n){
				for(var ci=0;ci<checkbox.length;ci++){
					if(checkbox[ci].checkbox_code == n){
						selIndex += (ci+1) + ",";
					}
				}
			});
			if(selIndex.length>0){
				selIndex = selIndex.substring(0,selIndex.length-1);
			}
			td.attr({
				"data-value":dl[x].data_value,
				"data-text":selIndex
			});
			td.children().html("<div>"+selIndex+"</div>");
		}
		else if(type=='OPT'){
			var optStr = this.getValFormOptions(dl[x].data_value,parser.detail.cell[td.index()].params.options);
			td.attr({
				"data-value":dl[x].data_value,
				"data-text":optStr
			});
			td.children().html("<div>"+optStr+"</div>");
		}
		else{
			td.attr({
				"data-value":dl[x].data_value,
				"data-text":dl[x].data_value
			});
			td.children().html("<div>"+dl[x].data_value+"</div>");
		}
	},	

	dataToPrintView:function(c){
		this.DataToView(c,true);
	},
	parseCheckData:function(val){

		var checkVal=[],childCheckVal=[],childTextVal=[];

		val = val.split(',');
		$.each(val,function(i,n){
			checkVal.push({});
			//n = n.split('#');

			if(n.indexOf('#') >=0 && n.indexOf('&') >=0){
				n=n.split("#");
				checkVal[i].val = n[0];
				n=n[1].split('&');
				checkVal[i].childCheckVal = n[0].split("*");
				checkVal[i].childTextVal  = n[1].split("*");
			}
			else if(n.indexOf('#') >=0){
				n=n.split("#");
				checkVal[i].val = n[0];
				checkVal[i].childCheckVal = n[1].split("*");
			}
			else if(n.indexOf('&') >=0){
				n=n.split("&");
				checkVal[i].val = n[0];
				checkVal[i].childTextVal = n[1].split("*");
			}
			else{
				checkVal[i].val = n;
				checkVal[i].childCheckVal = null;
				checkVal[i].childTextVal  = null;
			}
		});

		return  checkVal;
	},
	createContainter:function(cfg){
		this.pageIndex = this.pageIndex+1;
		if(this.pageIndex > 1){
			$("#editBox").append("<div style='page-break-after:always;'></div>")
		}
		var containter = $("<div class='editor' id='editor"+this.pageIndex+"'></div>");
		$("#editBox").append(containter);

		//分成两种生成方式 横纸 和竖纸 宽高不同
		if(cfg.docType == 1){
			containter.css({
				"width":cfg.width+"px",
				"height":"1000px",
				"position":"relative",
				"margin":"0 auto 10px"
			});
		}
		else if(cfg.docType >= 2){
			containter.css({
				"width":(cfg.width-40)+"px",
				"height":(cfg.height-30)+"px",
				"position":"relative",
				"margin":"10px auto 0",
				"paddingBottom":"10px"
			});
		}
		else{

		}
		this.currContainter = containter
		return containter;
	},
	parseBand:function(index,cfg){
		var that = this;	
		this.containter = this.createContainter(cfg);
		var containter = this.containter;
		
		//解析每个bands
		//一个band是一个区域（例如header,detail）
		var bands = cfg.bands;
		var isTable = cfg.docType == 2 ? true : false;
		var pageHeaderIndex,cloumnHeaderIndex,detailIndex,pageFooterIndex;
		for(i=0;i<bands.length;i++){
			this.bands[i] = $("<div class='band'></div>");
			containter.append(this.bands[i]);
			this.bands[i].attr('name',bands[i].bandName);
			this.bands[i].css({
				"height":bands[i].height+"px"
			});
			var items = bands[i].items;
			
			if(bands[i].bandName == 'page-header'){
				pageHeaderIndex = i;
			}
			else if(bands[i].bandName == 'cloumn-header'){
				cloumnHeaderIndex = i;
				this.bands[i].css({
					"border-right":"1px solid #000"
					// "width":(this.bands[i].width()-40)+"px",
					// "margin-left":"20px"
				});
				// this.parseTableHeader(items,this.bands[i],function(){
				// 	//增加表格头渲染完成后的回调
				// 	//处理第一个表格头元素内包含line元素时需处理多余的上边框线
				// 	var line = $(".band[name='cloumn-header'] > div[data-type='line']");
				// 	if(line.length>0){
				// 		var lt3sText = $(".band[name='cloumn-header'] > div:lt(3)[data-type!='line']");
				// 		lt3sText.each(function(){
				// 			if( $(this).css('top') != 0 ){
				// 				$(this).css('border-top','none');
				// 			}
				// 			if( line.attr('data-dir') && $(this).css('text-align') == 'right' ){
				// 				$(this).css('border-top','none');
				// 			}
				// 			else{}
				// 		});
				// 	}
				// });
			}
			else if(bands[i].bandName == 'page-footer'){
				pageFooterIndex = i;
			}
			else if(bands[i].bandName == 'detail'){
				detailIndex = i;
				if(isTable){
					this.bands[i].css({
						"height":"auto",
						"border-left":"1px solid",
						"border-right":"1px solid",
						"display":" table",
						"border-collapse":" collapse",
						"border-bottom":"1px solid #000"
					});
				}
				
				// if(isTable){
				// 	this.parseDetail(items,this.bands[i],function(){});	
				// }
				// else{
				// 	this.parseItems(items,this.bands[i]);
				// }
				
			}
			else{
				//this.parseItems(items,this.bands[i]);
			}

		}
		
		if(parseInt(cloumnHeaderIndex) >=0){
			var items = bands[cloumnHeaderIndex].items;
			this.parseTableHeader(items,this.bands[cloumnHeaderIndex],function(){
				//增加表格头渲染完成后的回调
				//处理第一个表格头元素内包含line元素时需处理多余的上边框线
				var line = $(".band[name='cloumn-header'] > div[data-type='line']");
				if(line.length>0){
					var lt3sText = $(".band[name='cloumn-header'] > div:lt(3)[data-type!='line']");
					lt3sText.each(function(){
						if( $(this).css('top') != 0 ){
							$(this).css('border-top','none');
						}
						if( line.attr('data-dir') && $(this).css('text-align') == 'right' ){
							$(this).css('border-top','none');
						}
						else{}
					});
				}
			});
		}
		if(parseInt(pageHeaderIndex) >=0){
			var items = bands[pageHeaderIndex].items;
			this.parseItems(items,this.bands[pageHeaderIndex]);
		}
		if(parseInt(pageFooterIndex) >=0){
			var items = bands[pageFooterIndex].items;
			this.parseItems(items,this.bands[pageFooterIndex]);
		}
		if(parseInt(detailIndex) >=0){
			var items = bands[detailIndex].items;
			if(!this.fixDetailHeight){
				//计算detail高度
				//默认为655 加上15像素的分页高度  总计670px
				var editorHight = 670;
				var allband = $(".band");
				//缓存pagefooter Y轴的值
				var footerY = 0;
				allband.each(function(){
					if($(this).attr("name") !== "detail"){
						editorHight = editorHight - $(this).height(); 
					}
					if($(this).attr("name") !== "page-footer" && $(this).attr("name") !== "detail"){
						footerY = footerY + $(this).height(); 
					}
				});
				editorHight -= 15;
				$(".band[name='detail']").css("max-height",editorHight+"px");
				footerY += parseInt($(".band[name='detail']").css("max-height"));
				if(parseInt(pageFooterIndex) >=0){
					$(".band[name='page-footer']").css({
						"top":footerY+"px",
						"position":"absolute"
					});
				}
				this.fixDetailHeight = true;
				this.detailHeight = editorHight;
			}
			if(isTable){
				this.parseDetail(items,this.bands[detailIndex],function(){});	
			}
			else{
				this.parseItems(items,this.bands[detailIndex]);
			}
		}

		


		// for(i=0;i<bands.length;i++){
			
		// 	if(bands[i].bandName == 'cloumn-header'){
		// 		this.parseTableHeader(items,this.bands[i],function(){
		// 			//增加表格头渲染完成后的回调
		// 			//处理第一个表格头元素内包含line元素时需处理多余的上边框线
		// 			var line = $(".band[name='cloumn-header'] > div[data-type='line']");
		// 			if(line.length>0){
		// 				var lt3sText = $(".band[name='cloumn-header'] > div:lt(3)[data-type!='line']");
		// 				lt3sText.each(function(){
		// 					if( $(this).css('top') != 0 ){
		// 						$(this).css('border-top','none');
		// 					}
		// 					if( line.attr('data-dir') && $(this).css('text-align') == 'right' ){
		// 						$(this).css('border-top','none');
		// 					}
		// 					else{}
		// 				});
		// 			}
		// 		});
		// 	}
		// 	else if(bands[i].bandName == 'detail'){

		// 		if(!this.fixDetailHeight){
		// 			//计算detail高度
		// 			var editorHight = 700;
		// 			var allband = $(".band");
		// 			allband.each(function(){
		// 				if($(this).attr("name") !== "detail"){
		// 					editorHight = editorHight - $(this).height(); 
		// 				}
		// 			});
		// 			$(".band[name='detail']").height(editorHight);
		// 			this.fixDetailHeight = true;
		// 			this.detailHeight = editorHight;
		// 		}
				


		// 		if(isTable){
		// 			this.parseDetail(items,this.bands[i],function(){});	
		// 		}
		// 		else{
		// 			this.parseItems(items,this.bands[i]);
		// 		}
		// 	}
		// 	else{
		// 		this.parseItems(items,this.bands[i]);
		// 	}
		// }
	},
	//组装节点 非表格类型的控件节点
	assembleNode:function(params,offsetT){
		var that = this;
		if(params.type == 'break'){
			return "break";
		}
		var tempNode = $("<div class='block' itemName='"+params.item_name+"'></div>"),node=null,required=false;
		tempNode.css({
				"height":params.height+"px",
				"line-height":params.height+"px",
				"left":(params.posX)+"px",
				"top":(params.posY-offsetT)+"px",
				"fontSize":params.size+"px",
				"fontWeight":params.isbold,
				"textAlign":params.align
			});
		if(params.margin == 'MIDDLE'){ tempNode.css('lineHeight',params.height+"px"); }

		if(params.type == 'line'){
			tempNode.attr('data-type','line');
			tempNode.css({
				"width":params.width+"px",
				"backgroundColor":'#000'
			});
		}
		
		if(params.type == 'Static'){
			tempNode.attr('data-type','sText');
			tempNode.html(params.item_name);
			tempNode.css({
				"width":params.width+"px"
			});
		}
		if(params.type == 'Param'){

			//标识类型
			tempNode.attr('data-type',params.data_type);
			//录入控件的标识
			tempNode.addClass('inputItem');
			tempNode.css({
				"width":params.width+"px"
			});
			if(params.data_type === 'STR'){
				tempNode.attr("data-type","STR");
				tempNode.append("<input type='text' data-name='"+params.item_name+"'>");
			}
			if(params.data_type == 'SYS_TIME'){
				tempNode.html("<input type='text' class='easyui-datetimebox' data-options=\"showSeconds:false,formatter:function(date){/*var y = date.getFullYear();var m = date.getMonth()+1;var d = date.getDate();var h = date.getHours();var M = date.getMinutes();*/return new Date(date).format('yyyy-MM-dd hh:mm');}\" data-name='"+params.item_name+"' style='height:18px;width:"+params.width+"px;'>");
			}
			if(params.data_type == 'BP'){
				tempNode.append("<input type='text' data-name='"+params.item_name+"' >");
				tempNode.children().validatebox({
					validType:'bp['+params.min_value+","+params.max_value+",'"+params.item_name+"']",
					required:params.required
				});
			}
			if(params.data_type == 'NUM'){
				tempNode.append("<input type='text' data-name='"+params.item_name+"' >");
				tempNode.children().validatebox({
					validType:'number['+params.min_value+","+params.max_value+","+params.precision+"]",
					required:params.required
				});
			}
			if(params.data_type == 'SEL'){
				tempNode.css({
					'lineHeight':"18px"
				});
				var CHECKBOX = $("<div data-name="+params.item_name+"></div>");
				var c_son_str = '',c_text_str = '';
				tempNode.append(CHECKBOX);
				if( params.checkbox.length >0 ){
					var checkbox = params.checkbox,
						span = null,
						hasChildNode = false;
					for(var cdx=0;cdx<checkbox.length;cdx++){

						span = $("<span></span>"),
						c_son_span = null,c_text_span = null,hasChildNode = false;;

						span.append("<label><input type='checkbox' id='"+(""+params.item_name+checkbox[cdx].checkbox_code)+"' name='"+params.item_name+"' value='"+checkbox[cdx].checkbox_code+"'>"+checkbox[cdx].checkbox_name+"</label>");	
						if(checkbox[cdx].checkbox_son.length>0){
							var c_son = checkbox[cdx].checkbox_son,c_son_text='';
							c_son_span = $("<span class='childCheck'></span>");
							for(var csdx=0;csdx<c_son.length;csdx++){
								c_son_text += "<label><input type='checkbox' disabled='disabled' name='"+checkbox[cdx].checkbox_code+"' value='"+c_son[csdx].checkbox_code_son+"'>"+c_son[csdx].checkbox_name_son+"</label>";
							}
							c_son_span.html("("+c_son_text+")");
							span.append(c_son_span);	
							hasChildNode = true;
						}
						if(checkbox[cdx].checkbox_text.length>0){
							var c_text = checkbox[cdx].checkbox_text,c_text_text='';
							c_text_span = $("<span class='childText'></span>");
							for(var csdx=0;csdx<c_text.length;csdx++){
								c_text_text += "<label><input style='width:"+(c_text[csdx].width ? c_text[csdx].width : 40)+"px;' type='text' disabled='disabled' name='"+c_text[csdx].checkbox_text_code+"'>"+(c_text[csdx].unit_text ? c_text[csdx].unit_text : '')+"</label>";
							}
							c_text_span.html(c_text_text);
							span.append(c_text_span);
							hasChildNode = true;
						}
						CHECKBOX.append(span);
						if(hasChildNode){
							span.attr('data-hasChild','true');
						}	
					}
				}
				var cCheckbox = CHECKBOX.children().children('label').children();
				CHECKBOX.attr("checkIndex","null");
				cCheckbox.bind('click',function(){
					cCheckbox.each(function(){
						$(this).removeAttr('checked');
						if( $(this).parent().parent().attr('data-haschild') ){
							$(this).parent().next().find("input").attr('disabled','disabled').removeAttr('checked');
						}
					});
            		$(this).attr("checked", "checked");
            		var index = $(this).parent().parent().index();
            		if(parseInt(CHECKBOX.attr("checkIndex")) == index){
            			$(this).removeAttr("checked");
            			CHECKBOX.attr("checkIndex",null);
            			return ;
            		}
            		//if(!checkIndex) checkIndex = index;
            		CHECKBOX.attr("checkIndex",index);

            		if($(this).parent().parent().attr('data-haschild') ){
            			if($(this).attr('checked') == 'checked'){
            				$(this).parent().next().find("input").removeAttr('disabled');	
            			}
            		}					
				});
			}
			if(params.data_type == 'MSEL'){
				tempNode.css({
					'lineHeight':"18px"
				});
				var CHECKBOX = $("<div data-name="+params.item_name+"></div>");
				var c_son_str = '',c_text_str = '';
				tempNode.append(CHECKBOX);
				if( params.checkbox.length >0 ){
					var checkbox = params.checkbox;
					for(var cdx=0;cdx<checkbox.length;cdx++){

						var span = $("<span></span>"),
							c_son_span = null,c_text_span = null;

						span.append("<label><input type='checkbox' name='"+params.item_name+"' value='"+checkbox[cdx].checkbox_code+"'>"+checkbox[cdx].checkbox_name+"</label>");	
						if(checkbox[cdx].checkbox_son.length>0){
							var c_son = checkbox[cdx].checkbox_son,c_son_text='';
							c_son_span = $("<span class='childCheck'></span>");
							for(var csdx=0;csdx<c_son.length;csdx++){
								c_son_text += "<label><input type='checkbox' name='"+checkbox[cdx].checkbox_code+"' value='"+c_son[csdx].checkbox_code_son+"'>"+c_son[csdx].checkbox_name_son+"</label>";
							}
							c_son_span.html("("+c_son_text+")");
							span.append(c_son_span);
						}
						if(checkbox[cdx].checkbox_text.length>0){
							var c_text = checkbox[cdx].checkbox_text,c_text_text='';
							c_text_span = $("<span class='childText'></span>");
							for(var csdx=0;csdx<c_text.length;csdx++){
								c_text_text += "<label>"+(c_text[csdx].text_name ? c_text[csdx].text_name : '')+"<input type='text' style='width:"+c_text[csdx].width+"px;'>"+(c_text[csdx].text_unit ? c_text[csdx].text_unit : '')+"</label>";
							}
							c_text_span.html(","+c_text_text+"");
							span.append(c_text_span);
							hasChildNode = true;
						}
						CHECKBOX.append(span);
					}
				}
			}
			if(params.data_type == 'OPT'){
				var SEL = $("<select data-name="+params.item_name+"></select>")
				tempNode.append(SEL);
				if( params.options.length >0 ){
					var options = params.options;
					SEL.append("<option value=''></option>");
					for(var odx=0;odx<options.length;odx++){
						SEL.append("<option value='"+options[odx].code+"'>"+options[odx].value+"</option>")
					}
				}
			}
		}
		return tempNode;
	},
	parseDetail:function(items,containter,fn){
		var that = this;
		//add table
		var table = $("<table id='dataTable'><tbody></tbody></table>");
		var tableList = $("<table id='dataTableList'><tbody></tbody></table>");
		containter.append(table);
		containter.append(tableList);
		//设置绑定事件
		table.delegate('td','click',function(){

			var tdIndex  = $(this).index(),
				trIndex  = $(this).parent().index(),
				ctdIndex = that.isEdit.index.td,
				ctrIndex = that.isEdit.index.tr;

			if(tdIndex == ctdIndex && trIndex == ctrIndex){
				console.log('同一个节点---return');
				return;
			}
			that.beginEdit($(this),that.detail.cell[tdIndex]);
		});

		if(items.length==0){ return; }
		this.detail.cell = [];
		$.each(items,function(i,n){
			that.detail.cell.push({});
			var cellTemp = '';
			cellTemp = that.getDetailCell(n);
			that.detail.cell[i].cellTemp = cellTemp;
			that.detail.cell[i].params = n;
		});
	},
	getDetailCell:function(item){
		var cellTemp = '';
		switch(item.data_type){
			case 'DATE':
			case 'TIME':
			case 'SYS_TIME':
				cellTemp = '<input type="text" data-required="'+item.required+'" style="width:'+(item.width-1)+'px;height:'+item.height+'px;"/>'
				break;
			case 'BP':
			case 'NUM':
			case 'STR':
				cellTemp = '<input type="text" data-required="'+item.required+'" data-name="'+item.item_name+'"  style="width:'+(item.width-1)+'px;height:'+(item.height-1)+'px;"/>';
				break;
			case 'OPT':
				cellTemp = '<select data-required="'+item.required+'" style="width:'+(item.width-1)+'px;height:'+(item.height-1)+'px;"></select>';
				break;
			case 'DYNA_TITLE':
			case 'MSEL':
			case 'SEL':
				cellTemp = '<div data-required="'+item.required+'" style="width:'+(item.width-1)+'px;height:'+(item.height-1)+'px;"></div>';
				break;
			case 'SWT':
				cellTemp = '<input data-required="'+item.required+'" class="swt" data-type="SWT" type="checkbox" />'
				break;
			default:
				cellTemp = '<input data-required="'+item.required+'" type="text" style="width:'+(item.width-1)+'px;height:'+(item.height-1)+'px;"/>';	
		}
		return cellTemp;
	},
	//解析表格头
	parseTableHeader:function(items,containter,fn){
		var that = this;
		var tempNode = null;
		$.each(items,function(i,n){
			if(n.type != 'line'){

				//增加自定义表头   （放在这里ok？）
				if( n.data_type == "DYNA_TITLE" ){
					var cellTemp = that.getDetailCell(n);
					var index    = ["TITLE_ONE","TITLE_TWO","TITLE_THREE","TITLE_FOUR","TITLE_FIVE"].indexOf(n.item_name);
					n.dynaIndex = index;
					if(!that.detail.changeableCell){
						that.detail.changeableCell = [];
					}
					that.detail.changeableCell.push({});
					that.detail.changeableCell[index].cellTemp = cellTemp;
					that.detail.changeableCell[index].params = n;
					var dynaTh = $("<div class='dynaTableHeader' data-type='dynaTh' style='position:absolute;left:"+n.posX+"px;top:"+n.posY+"px;width:"+n.width+"px;height:"+n.height+"px;'><div></div></div>");
					dynaTh.attr("data-index",index);
					containter.append(dynaTh);
					dynaTh.unbind().bind('click',function(){
						var thIndex = parseInt($(this).attr("data-index"));
						var currIndex = that.isEdit.thIndex;
						if(thIndex == currIndex){
							console.log('同一个节点---return');
							return;
						}
						that.isEdit.thIndex = thIndex;
						that.beginEdit($(this),that.detail.changeableCell[thIndex]);
					});
				}
				else{
					tempNode = that.assembleNode(n,0);
					tempNode.css({
						"line-height":"inherit"
					});
					tempNode.html("<span class='headrText'>"+tempNode.html()+"</span>")
					tempNode.addClass("hBefore");
					containter.append(tempNode);	
				}
				
			}
			else{
				tempNode = that.assembleNode(n,0);
				tempNode.css({
					"backgroundColor":"inherit"
				}).append('<svg  width="100%" height="100%" version="1.1"></svg>');
				containter.append(tempNode);
				var line = '';
				if(n.direction){
					tempNode.attr('data-dir',n.direction);
					line = '<line x1="0" y1="0" x2="'+n.width+'" y2="'+n.height+'" style="stroke:rgb(0,0,0);stroke-width:1"/>';
				}
				else{
					line = '<line x1="0" y1="'+n.height+'" x2="'+n.width+'" y2="0" style="stroke:rgb(0,0,0);stroke-width:1"/>';
				}
				tempNode.children().html(line);
			}
		});
		return fn();
	},
	/*
	*	初始化新非表格页面
	*/
	initNewPage:function(containter){
		var containterOne = $("#editor"+"1");
		containter.append(containterOne.find(".band[name='page-header']").clone());
		var detail = $("<div class='band' name='detail'>");
		containter.append(detail);
		detail.height((970-containter.children().height()));
		return detail;
	},
	/*
	*	初始化新非表格页面
	*/
	initNewTablePage:function(containter){
		var containterOne = $("#editor"+"1");
		containter.append(containterOne.find(".band[name='page-header']").clone());
		containter.append(containterOne.find(".band[name='cloumn-header']").clone());
		containter.append(containterOne.find(".band[name='page-footer']").clone());
		var detail = containterOne.find(".band[name='detail']").clone()
		containter.append(detail);
		return detail;
	},
	/*
	* i 上次解析的位置,items 解析的组件,containter band容器
	*/
	parseItemForBreak:function(i,items,containter){
		var nextPageItems = [];
		var offset = items[i].posY;
		$.each(items,function(index,n){
			if(index<=i){
				return true;
 			}
			nextPageItems.push(n);
		});
		console.log(nextPageItems);
		this.parseItems(nextPageItems,containter,offset);
	},
	//解析非表格类型控件
	parseItems:function(items,containter,offsetTop){
		var that = this;
		var tempNode = null;
		var offsetT = offsetTop ? offsetTop : 0;
		$.each(items,function(i,n){
			tempNode = that.assembleNode(n,offsetT);
			if(tempNode === "break"){
				//创建新的page
				var newPageContainter = that.createContainter(that.config[0]);
				//将page header中的部分提取到新页面中;
				var newPageDetail = that.initNewPage(newPageContainter);
				//解析新页面
				that.parseItemForBreak(i,items,newPageDetail);
				return false;
			}
			else{
				containter.append(tempNode);
			}
		});
	},
	// 树的操作
	removeTreeNode:function(){
		var node  = $("#statement").tree('getSelected'),
			type = node.type,
			rId  = node.recordId,
			id 	 = node.id;
		if( type == 1 && rId){
			$.messager.confirm("提示","确定要删除此记录吗?",function(r){
				if(r){
					//delete data
					try{
						$.get('./delete_data?recordId='+rId,function(d){
							if(d.rslt == "0"){
								$.messager.alert("提示","记录删除成功");
								$("#statement").tree('update',{
									target:node.target,
									// iconCls:"icon-emp",
									record_id:null
								})
							}
						});		
					}catch(e){

					}
				}
				else{
					//不操作
				}
			});
		}

		else if( type == 2){

			var cNode = node.children;
			console.log(cNode);
			if(cNode && cNode.length >= 1){
				$.messager.alert("提示","此模版存在已录入的模板数据,请先将已录入数据删除再进行次操作!");
			}
			else{
				$.messager.confirm("提示","确定要删除此模版吗?",function(r){
					if(r){
						//删除模版
						try{
							$.post('./delete_template?templateId='+id,function(d){
								if(d.rslt=="0"){
									$.messager.alert("提示","模板删除成功");
									$("#statement").tree('remove', node.target);
								}
							});
						}catch(e){
							console.log(e);
						}
					}	
					else{
						//不操作
					}
				});
			}
		}
		else{

		}
        
	},
	removeTemp:function(){

	}
};

function SYS_TIM(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var h = date.getHours();
	var M = date.getMinutes();
	return y+'-'+m+'-'+d+' '+h+':'+M;
}

function getCheckbox(checkboxs,val){
	var checkbox = null;
	for(var i=0;i<checkboxs.length;i++){
		if(checkboxs[i].checkbox_code == val){
			checkbox = checkboxs[i];
			return checkbox;
		}
	}
	return checkbox;
}

function uploadfile(e){
	var imgPath = $("#temFile").val();
    if (imgPath == "") {
        alert("请选择上传图片！");
        e.preventDefault();
        return false;
    }
    var fName = $("#fName").val();
    var dept_code = $("#dept_code").val();
    var doc_type  = $("#doc_type").val();
    $("#tempForm").ajaxForm();
    /*$.ajax({
        type: "POST",
        url: "./upload",
        data: {
        	imgPath: $("#temFile").val(),
        	fName:fName,
        	dept_code:dept_code,
        	doc_type:doc_type
    	},
        cache: false,
        success: function(data) {
            alert("上传成功");
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert("上传失败，请检查网络后重试");
        }
    });*/
    e.preventDefault();

}

function getLeftTreeData(mt){
	var mtc = [],i=0,data = [];
	for(i=0;i<mt.length;i++){
		mtc.push({});
		if(mt[i].type_id == 1){
			mtc[i].id=mt[i].type_id;
			mtc[i].text=mt[i].type_name;
			//mtc[i].iconCls="icon-save";  
			data = mt[i].data;
			if(data.length > 0){
				mtc[i].children=[]
			}
			for(var i1=0;i1<data.length;i1++){
				if(data[i1].record_id){
					mtc[i].children.push({
						"id":data[i1].template_id,
						"text":data[i1].template_name,
						"recordId":data[i1].record_id,
						"type":mt[i].type_id
					});
				}
				else{
					mtc[i].children.push({
						"id":data[i1].template_id,
						"text":data[i1].template_name,
						"recordId":data[i1].record_id,
						"type":mt[i].type_id/*,
						"iconCls":"icon-emp"*/
					});
				}
				
			}
		}
		else if(mt[i].type_id == 2){
			mtc[i].id=mt[i].type_id;
			mtc[i].text=mt[i].type_name;
			//mtc[i].iconCls="icon-save";
			data = mt[i].data;
			if(data.length > 0){
				mtc[i].children=[]
			}
			for(var i1=0;i1<data.length;i1++){
				mtc[i].children.push({
					"id":data[i1].template_id,
					"text":data[i1].template_name,
					"recordId":data[i1].record_id,
					"type":mt[i].type_id
				});
				if(data[i1].data.length>0){
					mtc[i].children[i1].children = [];
					for(var i2=0;i2<data[i1].data.length;i2++){
						mtc[i].children[i1].children.push({
							"id":data[i1].data[i2].template_id,
							"pId":data[i1].data[i2].pat_id,
							"text":data[i1].data[i2].data_value,
							"recordId":data[i1].data[i2].record_id,
							"type":"day"
						});
						if(data[i1].data[i2].data.length>0){
							mtc[i].children[i1].children[i2].children = [];
							for(var i3=0;i3<data[i1].data[i2].data.length;i3++){
								mtc[i].children[i1].children[i2].children.push({
									"id":data[i1].data[i2].data[i3].template_id,
									"pId":data[i1].data[i2].data[i3].pat_id,
									"text":data[i1].data[i2].data[i3].data_value,
									"recordId":data[i1].data[i2].data[i3].record_id,
									"type":"time"
								});
							}
						}
					}
				}
			}
		}
		else{

		}
	}
	return mtc;
}

$(function(){
	window.Data = $("#dataElm");
	Data.data("data_list",[]);
	Data.data("data_item",{});
	Data.data("dyna_list",[]);
	$.post('./getDocType',{},function(d){
		console.log(d);
	});
	$.get('./getTree',{'dept_code': 1,'valid': 'PC','inpatient_no': 1},function(d){
		var mt = d.data;
		var mtc = [];
		mtc = getLeftTreeData(mt);
		console.log(mtc);
		$("#statement").tree({
			data:mtc,
			onClick:function(node){
				console.log(node);
				var tId = node.id,
					recordId = node.recordId;
				if(node.type == 1){
					$("#viewBox").hide();
					$("#editBox").show();
					$.get('./list?template_id='+tId,function(d){
						Data.data("template_id",tId);
						parser.reset();
						parser.init(d,{type:1,rId:recordId,tId:tId});
					});
				}
				else if(node.type == 2){
					$("#viewBox").hide();
					$("#editBox").show();
					$.get('./list?template_id='+tId,function(d){
						Data.data("template_id",tId);
						parser.reset();
						parser.init(d,{type:2,tId:tId});
					});
				}
				else if(node.type === "day"){
					$("#viewBox").hide();
					$("#editBox").show();
					var day = node.text;
					$.get('./list?template_id='+tId,function(d){
						Data.data("template_id",tId);
						parser.reset();
						parser.init(d,{type:"day",tId:tId,day:day});
					});
				}
				else if(node.type === 'time'){
					$("#viewBox").hide();
					$("#editBox").show();
					var time = node.text;
					var parentNode = $("#statement").tree("getParent",node.target);
					var day = parentNode.text;
					$.get('./list?template_id='+tId,function(d){
						Data.data("template_id",tId);
						parser.reset();
						parser.init(d,{type:"time",day:day,time:time,tId:tId,rId:recordId});
					});
				}
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				// 查找节点
				$("#statement").tree('select', node.target);
				var node  = $("#statement").tree('getSelected'),
				type = node.type,
				rId  = node.recordId;
				//临床报告且有录入的记录
				if(type == 1 && rId){
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}
			}
		});
	});
});

