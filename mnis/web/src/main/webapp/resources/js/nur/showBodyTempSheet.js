var editIndex = undefined,		//编辑行索引
	fields    = '',				//编辑单元格field名
	cacheData = null,			//缓存所有数据
	updateIndex = null,
    config    = $.parseJSON(sessionStorage.getItem("config")),  //获取配置文件
/*	masterRecordId = null,*/
/*	cooledStatus = [],*/
/*	isChecked = false,*/
	currentDate = null;			//当前日期
var LODOP;
/*var bodyTempObj = {};*/
var dataGridColumns = null;		//数据表格渲染项

var dataGridObj = {
	dataGridTime : ['01:00','03:00','05:00','07:00','09:00','11:00','13:00','15:00','17:00','19:00','21:00','23:00'],
//	dataGridField : ['recordTime','temperature','cooled','pulse','heartRate','breath','bloodPress','otherOutput','totalInput','urine','stool','height','abdominalCir','weight','skinTestInfo','event','copyFlag', 'OtherOne', 'OtherTwo'],
	dataGridField : ['recordTime','temperature','cooled','pulse','heartRate','breath','bloodPress','otherOutput','totalInput','urine','stool','weight','skinTestInfo','event','copyFlag', 'OtherOne', 'OtherTwo'],
	dataGridFieldName: {
		"temperature": {
			"name": "体温",
			"unit": "℃",
			"type": 'validatebox',
			"precision": 1,
			"valid": "numRange['temperatureType',35,42]",
			"invalidMsg": ''
		},
		"cooled": {
			"name": "降温体温",
			"unit": ""
		},
		"pulse": {
			"name": "脉搏",
			"unit": "次/分",
			"type": 'validatebox',
			"valid": "isNumber['pulseType']",
			"invalidMsg": ''
		},
		"breath": {
			"name": "呼吸",
			"unit": "次/分",
			"type": 'validatebox',
			"valid": "isNumber['breathType']",
			"invalidMsg": ''
		},
		"heartRate": {
			"name": "心率",
			"unit": "次/分",
			"type": 'validatebox',
			"valid": "isNumber['heartRateType']",
			"invalidMsg": ''
		},
		"bloodPress": {
			"name": "血压",
			"unit": "mmHg",
			"type": 'validatebox',
			"valid": "numRange['bloodPressType']",
			"invalidMsg": ''
		},
		"otherOutput": {
			"name": "其他出量",
			"unit": "ml",
			"type": 'validatebox',
			"valid": "isNumber['otherOutputType']",
			"invalidMsg": ''
		},
		"totalInput": {
			"name": "总入量(24h)",
			"unit": "ml",
			"type": 'validatebox',
			"valid": "isNumber['totalInputType']",
			"invalidMsg": ''
		},
		"totalOutput": {
			"name": "总出量(ml)",
			"unit": "ml",
			"type": 'validatebox',
			"valid": "isNumber['totalInputType']",
			"invalidMsg": ''
		},
		"urine": {
			"name": "尿量",
			"unit": "ml",
			"type": 'validatebox',
			"valid": "isNumber['urineType']",
			"invalidMsg": ''
		},
		"stool": {
			"name": "大便",
			"unit": "次/天",
			"type": 'validatebox',
			"valid": "isNumber['stoolType']",
			"invalidMsg": ''
		},
		"urineTimes": {
			"name": "小便(次/日)",
			"unit": "次/天",
			"type": 'validatebox',
			"valid": "isNumber['stoolType']",
			"invalidMsg": ''
		},
		"OtherOne": {
			"name": "其它①",
			"unit": "ml|cm",
			"type": '',
			"valid": "",
			"invalidMsg": ''
		},
		"OtherTwo": {
			"name": "其它②",
			"unit": "ml|cm",
			"type": '',
			"valid": "",
			"invalidMsg": ''
		},
		"height":{
		 "name":"身高",
		 "unit":"cm",
		 "type":'validatebox',
		 "valid":"specialNum['heightType']",
		 "invalidMsg":''
		 },
		"weight": {
			"name": "体重",
			"unit": "kg",
			"type": 'validatebox',
			"valid": "specialNum['weightType']",
			"invalidMsg": ''
		},
		"event": {
			"name": "事件名",
			"unit": ""
		},
		"skinTestInfo": {
			"name": "皮试信息",
			"unit": ""
		},
		"abdominalCir": {
		 "name": "腹围",
		 "unit": "cm",
		 "type": 'validatebox',
		 "valid": "isNumber['abdominalCirType']",
		 "invalidMsg": ''
		 },
		"coolway": {
			"name": "降温方式",
			"unit": ""
		},
		"oxygenSaturation": {
			"name": "血氧",
			"unit": "%"
		},
		"PPD": {
			"name": "PPD",
			"unit": ""
		},
		"pain": {
			"name": "疼痛",
			"unit": "等级"
		}
	},
	dataGridOptions: {
		"temperature": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "yw", "text": "腋温", "editFlag": 1},
				{"val": "gw", "text": "肛温", "editFlag": 1},
				{"val": "kw", "text": "口温", "editFlag": 1},
				{"val": "bs", "text": "不升", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"pulse": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "cbc", "text": "测不出", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0}]
		},
		"breath": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "hxj", "text": "呼吸机", "editFlag": 1},
				/*{ "val":"zjhx","text":"自主呼吸","editFlag":1},*/
				{"val": "cbc", "text": "测不出", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"heartRate": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "cbc", "text": "测不出", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"bloodPress": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "sz", "text": "上肢", "editFlag": 1},
				{"val": "xz", "text": "下肢", "editFlag": 1},
				{"val": "cbc", "text": "测不出", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"otherOutput": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "otw", "text": "呕吐物", "editFlag": 1},
				{"val": "fs", "text": "腹水", "editFlag": 1},
				{"val": "phlegm", "text": "痰液", "editFlag": 1},
				{"val": "sweat", "text": "汗水", "editFlag": 1},
				{"val": "hydrothorax", "text": "胸水", "editFlag": 1},
				{"val": "csf", "text": "脑脊液", "editFlag": 1},
				{"val": "ylw", "text": "引流物", "editFlag": 1},
				{"val": "dz", "text": "胆汁", "editFlag": 1},
				{"val": "ox", "text": "呕血", "editFlag": 1},
				{"val": "kx", "text": "咯血", "editFlag": 1},
				/*{ "val":"shit","text":"大便","editFlag":1},
				 { "val":"urine","text":"尿","editFlag":1},
				 { "val":"ylw","text":"引流物","editFlag":1},
				 { "val":"ts","text":"特殊值","editFlag":1}*/
			]
		},
		"totalInput": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				/*{ "val":"otw","text":"呕吐物","editFlag":1},
				 { "val":"shit","text":"大便","editFlag":1},
				 { "val":"urine","text":"尿","editFlag":1},
				 { "val":"ylw","text":"引流物","editFlag":1},*/
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"totalOutput": {
			"opts": [
			]
		},
		"urine": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				/*{ "val":"zjpn","text":"自主排尿","editFlag":1},*/
				{"val": "dn", "text": "导尿", "editFlag": 1},
				{"val": "sj", "text": "失禁", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"stool": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "zxpb", "text": "自主排便", "editFlag": 1},
				{"val": "zxpb0", "text": "0", "editFlag": 0},
				{"val": "gch1", "text": "1/E", "editFlag": 0},
				{"val": "gch2", "text": "2/E", "editFlag": 0},
				{"val": "gch3", "text": "3/E", "editFlag": 0},
				/*{ "val":"gch","text":"灌肠后","editFlag":1},*/
				{"val": "sj", "text": "失禁(※)", "editFlag": 0},
				{"val": "rggm", "text": "人工肛门(☆)", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"urineTimes": {
			"opts": [
			]
		},
		"height": {
		 "opts": [
		 {"val": "", "text": "无", "editFlag": 0},
		 {"val": "moren", "text": "默认", "editFlag": 1},
		 {"val": "wc", "text": "卧床", "editFlag": 0},
		 {"val": "ts", "text": "特殊值", "editFlag": 1}
		 ]
		 },
		"weight": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "wc", "text": "卧床", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"skinTestInfo": {
			"opts": [
				{"val": "", "text": "无"},
				{"val": "qms", "text": "青霉素"},
				{"val": "xlo", "text": "新朗欧"},
				{"val": "tbmdz", "text": "头孢孟多酯"},
				{"val": "tzx", "text": "特治星"},
				{"val": "lsf", "text": "罗氏芬"},
				{"val": "tbfx", "text": "头孢呋辛"},
				{"val": "tbtd", "text": "头孢他啶"},
				{"val": "sps", "text": "舒普深"},
				{"val": "lms", "text": "链霉素"},
				{"val": "ts", "text": "特殊值"}
			]
		},
		"event": {
			"opts": [
				{"val": "", "text": "空"},
				{"val": "ry", "text": "入院"},
				{"val": "cy", "text": "出院"},
				{"val": "zr", "text": "转入"},
				/*{ "val":"zc","text":"转出"},*/
				{"val": "ss", "text": "手术"},
				{"val": "fm", "text": "分娩"},
				//{ "val":"cs","text":"出生"},
				/*{ "val":"bw","text":"病危"},
				 { "val":"bz","text":"病重"},*/
				{"val": "sw", "text": "死亡"}
			]
		},
		"coolway": {
			"opts": [
				{"val": "", "text": "无"},
				{"val": "ice", "text": "冰敷"},
				{"val": "alcohoswab", "text": "酒精擦洗"},
				{"val": "waterswab", "text": "温水擦洗"}
			]
		},
		"oxygenSaturation": {
			"opts": [
				{"val": "", "text": "无", "editFlag": 0},
				{"val": "moren", "text": "默认", "editFlag": 1},
				{"val": "cbc", "text": "测不出", "editFlag": 0},
				{"val": "out", "text": "外出", "editFlag": 0},
				{"val": "qj", "text": "请假", "editFlag": 0},
				{"val": "jc", "text": "拒测", "editFlag": 0},
				{"val": "ts", "text": "特殊值", "editFlag": 1}
			]
		},
		"PPD": {
			"opts": [
				{"val": "", "text": "无"},
				{"val": "oneadd", "text": "+"},
				{"val": "twoadd", "text": "++"},
				{"val": "threeadd", "text": "+++"},
				{"val": "onesub", "text": "-"}
			]
		},
		"pain": {
			"opts": [
				{"val": "yjtt", "text": "1"},
				{"val": "ejtt", "text": "2"},
				{"val": "sjtt", "text": "3"},
				{"val": "fjtt", "text": "4"},
				{"val": "wjtt", "text": "5"},
				{"val": "ljtt", "text": "6"},
				{"val": "qjtt", "text": "7"},
				{"val": "bjtt", "text": "8"},
				{"val": "jjtt", "text": "9"},
				{"val": "shijtt", "text": "10"}
			]
		},
		"abdominalCir": {
			"opts": [
			]
		},
		/*
		 * <select id="OtherOneType" name="OtherOne"
		 onchange="handleSelectChange('OtherOne', this)" style="width:75px;">
		 <option value="">无</option>
		 <option value="tlone">24h痰量</option>
		 <option value="otlone">24h呕吐</option>
		 <option value="sgone">身高</option>
		 <option value="fwone">腹围</option>
		 <option value="tszlone">特殊治疗</option>
		 </select>
		 * */
		"OtherOne": {
			"opts": [
				{"val": "", "text": "无"},
				{"val": "tlone", "text": "24h痰量"},
				{"val": "otlone", "text": "24h呕吐"},
				{"val": "sgone", "text": "身高"},
				{"val": "fwone", "text": "腹围"},
				{"val": "otw", "text": "呕吐物"},
				{"val": "fs", "text": "腹水"},
				{"val": "phlegm", "text": "痰液"},
				{"val": "sweat", "text": "汗水"},
				{"val": "hydrothorax", "text": "胸水"},
				{"val": "csf", "text": "脑脊液"},
				{"val": "ylw", "text": "引流物"},
				{"val": "dz", "text": "胆汁"},
				{"val": "ox", "text": "呕血"},
				{"val": "kx", "text": "咯血"},
				{"val": "tszlone", "text": "特殊治疗"}
			]
		},

		"OtherTwo": {
			"opts": [
				{"val": "", "text": "无"},
				{"val": "tltwo", "text": "24h痰量"},
				{"val": "otltwo", "text": "24h呕吐"},
				{"val": "sgtwo", "text": "身高"},
				{"val": "fwtwo", "text": "腹围"},
				{"val": "otw", "text": "呕吐物"},
				{"val": "fs", "text": "腹水"},
				{"val": "phlegm", "text": "痰液"},
				{"val": "sweat", "text": "汗水"},
				{"val": "hydrothorax", "text": "胸水"},
				{"val": "csf", "text": "脑脊液"},
				{"val": "ylw", "text": "引流物"},
				{"val": "dz", "text": "胆汁"},
				{"val": "ox", "text": "呕血"},
				{"val": "kx", "text": "咯血"},
				{"val": "tszltwo", "text": "特殊治疗"}
			]
		}
	}
};
$.extend($.fn.datagrid.defaults.editors, {
	timespinner: {
		init: function(container, options){
			var input = $('<input class="easyuitimespinner">').appendTo(container);
			return input.timespinner(options);
		},
		destroy: function(target){
			$(target).timespinner('destroy');
		},
		getValue: function(target){
			return $(target).timespinner('getValue');
		},
		setValue: function(target, value){
			$(target).timespinner('setValue',value);
		},
		resize: function(target, width){
			$(target).timespinner('resize',width);
		}
	}
});
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});
function editorCheck(index,field){
	var opts = dataGridObj.dataGridOptions[field].opts;
	var rows  = $('#info-tab').datagrid('getRows');
	var val = rows[index][field+'Type'] || false;
	
	if(!val){
		//设置默认选项
		if(fields == 'cooledTemperature'){
			$("#info-tab").datagrid('getRows')[editIndex]['cooled'] = '是';
			if(opts){
				rows[index][field+'Type'] = opts[1].val;
			}
		}
		else if(fields == 'cooled'){
			$('#info-tab').datagrid('endEdit', editIndex);
			//ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'cooled'});
			if($("#info-tab").datagrid('getRows')[editIndex]['cooled'] == '否'){
				$("#info-tab").datagrid('getRows')[editIndex]['cooledTemperature'] = '';
			}
			if(opts){
				rows[index][field+'Type'] = opts[1].val;
			}
		}
		else{
			rows[index][field+'Type'] = opts[1].val;
		}
		
		//修复combobox提前关闭回调的hidepanel方法无法调用
		/*ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:field+'Type'});
		if(ed.type == 'combobox'){ 
			$(ed.target).combo('hidePanel');
		}
		console.log(ed);*/

		
		$('#info-tab').datagrid('endEdit', editIndex);

		//判断是否输入值，若为空将Type内的已选项清除
		//TODO
		/*if(!rows[index][field]){
			rows[index][field+'Type'] = '';
		}*/

		//刷新 没有结束editor状态的值无法应用到datagrid上
		$('#info-tab').datagrid('refreshRow',index);
		//$('#info-tab').datagrid('endEdit', index);
		return 1; 
	}
	for(var i=0;i<opts.length;i++){
		if(opts[i].val == val){
			return opts[i].editFlag;
		}
	}
}
function endEditing(){
    if (editIndex == undefined){return true}
    if ($('#info-tab').datagrid('validateRow', editIndex)){
    	var ed = null;
    	if(fields == 'drugName'){
    		ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'drugName'});
    		if(ed){
    			var drugName = $(ed.target).combobox('getText');
		        $(ed.target).combobox('setValue',drugName);
		        $('#info-tab').datagrid('getRows')[editIndex]['drugName'] = drugName;
    		}
	        
    	}
    	/*if(fields == 'cooledTemperature'){
    		ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'cooledTemperature'});
    		var cooledTemp = $(ed.target).textbox('getValue');
    		if(cooledTemp){
    			$('#info-tab').datagrid('endEdit', editIndex);
    			
    			$('#info-tab').datagrid('refreshRow',editIndex);
    			//ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'cooled'});
    		}
    	}*/
        $('#info-tab').datagrid('endEdit', editIndex);
        updateIndex = editIndex;
        editIndex = undefined;
        fields    = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickCell(index,field,params){
	//缓存当前单击的cell的field
	var cacheField = field;

	//过滤数组，
	var filterArray = [];
	if( field.toString().indexOf('Type') > 0 || 
		field == 'cooled' || 
		field == 'cooledTemperature' ||
		field == 'testResult' || 
		field == 'drugName' ||
		field == 'eventCode' ||
		field == 'coolwayCode' ||
		field == 'PPDCode' ||
		field == 'painCode' ||
		field == 'recordDate' ||
		field == 'copyFlag' ||
		field == 'remark' ||
		field == 'other'){
		if(endEditing()){
			$('#info-tab').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
			editIndex = index;
			var ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:field});

			// 特殊情况的判断
			if( field.toString().indexOf('Type') > 0 && $(ed.target).combobox('getValue') == ""){
				//增加下拉框默认选项
				var dCombobox = $(ed.target).combobox('getData');
				$(ed.target).combobox('setValue',dCombobox[1].val);
			}
			if( field == 'remark'){
				$(ed.target).focus();
			}
			if( field == 'other'){
				$(ed.target).focus();
			}
			if( field == 'recordDate'){
				var ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'recordDate'});
	        	if(!$(ed.target).spinner('getValue')){
	        		$(ed.target).spinner('setValue',new Date().format("hh:mm")); 
	        	}
			}
			if( field == 'cooledTemperature' ){
				$(ed.target).focus();
			}
			//记录fields全局
			fields = field;
		}
		return ;
	}
	if(editorCheck(index,field) == 0 && typeof params != 'object'){
		return ;
	}
	if (endEditing()){
        $('#info-tab').datagrid('selectRow', index)
                .datagrid('editCell', {index:index,field:field});
        editIndex = index;
        fields    = field;
        var ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:field});
        if(params && typeof params != 'string'){
        	//该选项存在val时  disable:true
        	if(params.val){
        		//非选择无时
        		if(params.val != '无'){
        			$(ed.target).val(params.val);
        		}
        		else{
        			//无时清空
        			$(ed.target).val('');
        		}
        	}
        	else{
        		//属于可录入时 非数字并且不属于范围清空
        		//增加血压数据格式判断
        		var t = $(ed.target).val();
        		if( /^[0-9]+$/.test(t) || /^[0-9]+\/[0-9]+$/.test(t) ){
        			//是否还有特殊处理
        		}
        		else{
        			$(ed.target).val('');
        		}
        	}
        }
        $(ed.target).focus();
        if(params && params.disabled){
        	$('#info-tab').datagrid('endEdit', editIndex);
        	updateIndex = editIndex;
        	editIndex = undefined;
        	fields = undefined;
        }
    }
}
function onClickRow(index){
    if (editIndex != index){
        if (endEditing()){
            $('#info-tab').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#info-tab').datagrid('selectRow', editIndex);
        }
    }
}
function valueFormatterFn(type){
    var typeName = type;
    /*return function(value,row){
        if(!value && row[typeName]){
            return ay.eventStringObj[row[typeName]];
        }
    }*/
}


function comboFomatterFunc(n){
	var idx = n;
	return function(value){
    	if (value == '') {
	        return;
	    }
	    for (var i = 0; i < dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts.length; i++) {
	        if (dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].val == value) {
	            return dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].text;
	        }
	    }
    }
}
function selectFomatterFunc(n){
	var idx = n;
	return function(record){
    	if(record.editFlag == 1){
    		var row = $("#info-tab").datagrid('getSelected');
    		var rindex = $("#info-tab").datagrid('getRowIndex', row);
            onClickCell(rindex,dataGridObj.dataGridField[idx],{"disabled":false});
    	}
    	else{
    		var row = $("#info-tab").datagrid('getSelected');
    		var rindex = $("#info-tab").datagrid('getRowIndex', row);
			onClickCell(rindex,dataGridObj.dataGridField[idx],{"disabled":true,"val":record.text});
    	}
    }
}

dataGridObj.dataGridField = (config.data.system.tempInputItemsBase+','+config.data.system.tempInputItemsOther).split(',');

function initDataGridColumns(){
	if( !dataGridColumns ){
		dataGridColumns = [[],[]];
		for( var idx=0;idx<dataGridObj.dataGridField.length;idx++){
	        if( ['date'].indexOf(dataGridObj.dataGridField[idx]) >= 0 ){
	            continue;
	        };
			if( dataGridObj.dataGridField[idx] == 'time'){
				dataGridColumns[0].push({field:'recordTime',align:'center',title:'时间点',rowspan:'2',width:'60'});
			}
			else if( dataGridObj.dataGridField[idx] == 'cooledTemperature'){
				dataGridColumns[0].push({align:'center',title:'降温体温',width:'40',editor:{type:'checkbox',options:{on:'是',off:'否'}}});
				dataGridColumns[1].push({field:'cooledTemperature',align:'center',title:'℃',editor:{type:'validatebox',options:{validType:'cooledTemp[35,42]'}}});
			}
			else if(dataGridObj.dataGridField[idx] == 'copyFlag'){
				dataGridColumns[0].push({field:'copyFlag',align:'center',title:'是否转抄',rowspan:'2',width:'60',editor:{type:'checkbox',options:{on:'是',off:'否'}}});
			}
			else if(dataGridObj.dataGridField[idx] == 'skinTest'){
				dataGridColumns[0].push({colspan:2,align:'center',title:'皮试'});
				dataGridColumns[1].push({field:'drugName',align:'center',width:'55',title:'药物名',
					formatter:function(value,row){
	                    return row.drugName;
	                },
					editor:{
	                    type:'combobox',
	                    options:{
	                    	valueField:'val',
		                    textField:'text',
	                    	data:dataGridObj.dataGridOptions['skinTestInfo'].opts
	                    }
	        	    }
			});
				dataGridColumns[1].push({field:'testResult',align:'center',width:'55',title:'结果',
					formatter:function(value){
						var str = '';
						str = value;
						if( value == '阳'){
							str = '<font color=red>'+value+'</font>';
						}
						return str;
					},
					editor:{type:'checkbox',options:{on:'阳',off:'阴'}}});
			}
	        else if(dataGridObj.dataGridField[idx] == 'other'){
	            dataGridColumns[0].push({field:'other',rowspan:2,align:'center',width:'80',title:'其他',editor:{type:'validatebox',options:{validType:"remark['remark']",invalidMessage:""}}});
	        }
	        else if(dataGridObj.dataGridField[idx] == 'remark'){
	            dataGridColumns[0].push({field:'remark',rowspan:2,align:'center',width:'80',title:'备注',editor:{type:'validatebox',options:{validType:"remark['remark']",invalidMessage:""}}});
	        }
	        else if(dataGridObj.dataGridField[idx] == 'coolway'){
				dataGridColumns[0].push({
					colspan :1,
					align : 'center',
					title : '降温方式'
				});
				dataGridColumns[1]
					.push({
						field : 'coolwayCode',
						align : 'center',
						width : '55',
						title : '',
						formatter : function(value, row) {
							if (value == '') {
								return '';
							}
							for (var i = 0; i < dataGridObj.dataGridOptions['coolway'].opts.length; i++) {
								if (dataGridObj.dataGridOptions['coolway'].opts[i].val == value) {
									return dataGridObj.dataGridOptions['coolway'].opts[i].text;
								}
							}
						},
						editor : {
							type : 'combobox',
							options : {
								valueField : 'val',
								textField : 'text',
								editable : false,
								onSelect : function(value) {
									if (value.text != '空') {
										onClickCell(editIndex, 'recordDate')
									} else {
										if (infoTab.datagrid('getSelected').recordDate) {
											infoTab.datagrid('getSelected').recordDate = '';
											$('#info-tab').datagrid(
													'endEdit', editIndex);
											$('#info-tab')
													.datagrid('refreshRow',
															editIndex);
										}
									}
								},
								data : dataGridObj.dataGridOptions['coolway'].opts
							}
						}
					});
			}
			else if (dataGridObj.dataGridField[idx] == 'pain') {
				// 事件
				dataGridColumns[0].push({
					colspan : 1,
					align : 'center',
					title : '疼痛'
				});
				dataGridColumns[1]
					.push({
						field : 'painCode',
						align : 'center',
						width : '55',
						title : '',
						formatter : function(value, row) {
							if (value == '') {
								return '';
							}
							for (var i = 0; i < dataGridObj.dataGridOptions['pain'].opts.length; i++) {
								if (dataGridObj.dataGridOptions['pain'].opts[i].val == value) {
									return dataGridObj.dataGridOptions['pain'].opts[i].text;
								}
							}
						},
						editor : {
							type : 'combobox',
							options : {
								valueField : 'val',
								textField : 'text',
								editable : false,
								onSelect : function(value) {
									if (value.text != '空') {
										onClickCell(editIndex, 'recordDate')
									} else {
										if (infoTab.datagrid('getSelected').recordDate) {
											infoTab.datagrid('getSelected').recordDate = '';
											$('#info-tab').datagrid(
													'endEdit', editIndex);
											$('#info-tab')
													.datagrid('refreshRow',
															editIndex);
										}
									}
								},
								data : dataGridObj.dataGridOptions['pain'].opts
							}
						}
					});
				// 其他
			}
			else if (dataGridObj.dataGridField[idx] == 'PPD') {
				// 事件
				dataGridColumns[0].push({
					colspan : 1,
					align : 'center',
					title : 'PPD'
				});
				dataGridColumns[1]
					.push({
						field : 'PPDCode',
						align : 'center',
						width : '55',
						title : '',
						formatter : function(value, row) {
							if (value == '') {
								return '';
							}
							for (var i = 0; i < dataGridObj.dataGridOptions['PPD'].opts.length; i++) {
								if (dataGridObj.dataGridOptions['PPD'].opts[i].val == value) {
									return dataGridObj.dataGridOptions['PPD'].opts[i].text;
								}
							}
						},
						editor : {
							type : 'combobox',
							options : {
								valueField : 'val',
								textField : 'text',
								editable : false,
								onSelect : function(value) {
									if (value.text != '空') {
										onClickCell(editIndex, 'recordDate')
									} else {
										if (infoTab.datagrid('getSelected').recordDate) {
											infoTab.datagrid('getSelected').recordDate = '';
											$('#info-tab').datagrid(
													'endEdit', editIndex);
											$('#info-tab')
													.datagrid('refreshRow',
															editIndex);
										}
									}
								},
								data : dataGridObj.dataGridOptions['PPD'].opts
							}
						}
					});
				// 其他
			}
			else if(dataGridObj.dataGridField[idx] == 'event'){
				//事件
				dataGridColumns[0].push({colspan:2,align:'center',title:'事件'});
				dataGridColumns[1].push({field:'eventCode',align:'center',width:'55',title:'事件名',
					formatter:function(value,row){
	                    if (value == '') {
						        return '';
						}
					    for (var i = 0; i < dataGridObj.dataGridOptions['event'].opts.length; i++) {
					        if (dataGridObj.dataGridOptions['event'].opts[i].val == value) {
					            return dataGridObj.dataGridOptions['event'].opts[i].text;
					        }
					    }
	                },
					editor:{
	                    type:'combobox',
	                    options:{
	                    	valueField:'val',
		                    textField:'text',
		                    editable:false,
		                    onSelect:function(value){
		                    	if(value.text != '空'){
		                    		onClickCell(editIndex,'recordDate')
		                    	}
		                    	else{
		                    		if($("#info-tab").datagrid('getSelected').recordDate){
		                    			$("#info-tab").datagrid('getSelected').recordDate = '';
		                    			$('#info-tab').datagrid('endEdit', editIndex);
										$('#info-tab').datagrid('refreshRow',editIndex);
		                    		}
		                    	}
		                    },
	                    	data:dataGridObj.dataGridOptions['event'].opts
	                    }
	        	    }
				});
				dataGridColumns[1].push({field:'recordDate',align:'center',width:'55',title:'时间',
					editor:{
						type:'timespinner'
					}
				});
			}
			else{
				console.log(idx);
				console.log(dataGridObj.dataGridField[idx]);
				console.log(dataGridObj);
				dataGridColumns[0].push({align:'center',colspan:2,title:dataGridObj.dataGridFieldName[ dataGridObj.dataGridField[idx] ].name});
				dataGridColumns[1].push({field:dataGridObj.dataGridField[idx],align:'center',width:'55',
	                formatter:valueFormatterFn(dataGridObj.dataGridField[idx]+'Type'),
	                title:dataGridObj.dataGridFieldName[ dataGridObj.dataGridField[idx] ].unit,editor:{type:'validatebox',options:{validType:dataGridObj.dataGridFieldName[ dataGridObj.dataGridField[idx] ].valid,invalidMessage:dataGridObj.dataGridFieldName[ dataGridObj.dataGridField[idx] ].invalidMsg}}});
				dataGridColumns[1].push({field:dataGridObj.dataGridField[idx]+'Type',align:'center',width:'55',title:'类型',
					formatter:comboFomatterFunc(idx),
					editor:{
	                    type:'combobox',
	                    options:{
	                    	editable:false,
	                    	valueField:'val',
		                    textField:'text',
		                    onSelect:selectFomatterFunc(idx),
	                    	data:dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts
	                    }
	        	    }
	        	});
			}
		}
	}
}
function getPatBedId(s,isNotCheckBox) {
	if(s){
		patientIdList = s;
	}
	if(parent.peopleList){
		patientIdList = parent.peopleList.toString();
	}
	if(patientIdList.length == 0){
		if(isNotCheckBox)
			$.messager.alert('提示', "您没有选择患者，请先选择患者！");
		$('#info-tab').datagrid('loadData',{total:0,rows:[]});
		return;
	}
	var startDate = $("#startDate").val();
	var json = null;
	$.post(ay.contextPath+"/nur/bodySign/getBodySignRecord?id="+patientIdList+"&date="+startDate,{},function(data){
		cacheData = data.data ? data.data.lst : [];
		json = "{\"total\":10,\"rows\":[";
		var bodySignRecordList = cacheData;
		if(!bodySignRecordList || bodySignRecordList.length==0){
			$('#info-tab').datagrid('loadData',{total:0,rows:[]}); 
			return;
		}
		for(var x=0;x<bodySignRecordList.length;x++){
			json += "{\"recordTime\":\""+bodySignRecordList[x].recordTime+"\",\"patientId\":\""+bodySignRecordList[x].patientId+"\",\"masterRecordId\":\""+bodySignRecordList[x].masterRecordId+"\",";
			var bodySignItemList=bodySignRecordList[x].bodySignItemList;
			if(bodySignItemList)
				for(var y=0;y<bodySignItemList.length;y++){
					//console.log(bodySignItemList[y])
					if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="temperature" && bodySignItemList[y].measureNoteCode !="cooledTemperature"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"temperature\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"temperatureType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="pulse"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"pulse\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"pulseType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="breath"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"breath\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"breathType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="totalInput"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"totalInput\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"totalInputType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="heartRate"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"heartRate\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"heartRateType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="bloodPress"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"bloodPress\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"bloodPressType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="otherOutput"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"otherOutput\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"otherOutputType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="bloodGlu"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"bloodGlu\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"bloodGluType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="abdominalCir"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"abdominalCir\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"abdominalCirType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="bloodGlu"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"bloodGlu\":\""+bodySignItemList[y].itemValue+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="urine"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"urine\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"urineType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="stool"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"stool\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"stoolType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="height"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"height\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"heightType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="weight"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"weight\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"weightType\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="other"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"other\":\""+bodySignItemList[y].itemValue+"\",";
							json += "\"otherType\":\"\",";
						}
					}else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="remark"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"remark\":\""+bodySignItemList[y].itemValue+"\",";
						}
					}
					else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="PPD"){
						if(bodySignItemList[y].measureNoteCode!='undefined'){
							json += "\"PPDCode\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}
					else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="coolway"){
						if(bodySignItemList[y].measureNoteCode!='undefined'){
							json += "\"coolwayCode\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}
					else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="oxygenSaturation"){
						if(bodySignItemList[y].itemValue!='undefined'){
							json += "\"oxygenSaturation\":\""+bodySignItemList[y].itemValue+"\",";
						}
					}
					else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="pain"){
						if(bodySignItemList[y].measureNoteCode!='undefined'){
							json += "\"painCode\":\""+bodySignItemList[y].measureNoteCode+"\",";
						}
					}
					else if(bodySignItemList[y].bodySignDict && bodySignItemList[y].bodySignDict.itemCode=="cooledTemperature"){
						if(bodySignItemList[y].itemValue){
							json += "\"cooledTemperature\":\""+bodySignItemList[y].itemValue+"\",";
						}
					}
					json += "\"mId\":\""+bodySignItemList[y].masterRecordId+"\",";
				}
			json += "\"recordDay\":\""+bodySignRecordList[x].recordDay+"\",";
			var recordTime = bodySignRecordList[x].recordTime;
			recordTime = recordTime.substring(0,recordTime.lastIndexOf(':')) ;
			json += "\"recordTime\":\""+recordTime+"\",";
			if(bodySignRecordList[x].copyFlag == 'Y'){
				json += "\"copyFlag\":\"是\",";
			}else{
				json += "\"copyFlag\":\"否\",";
			}
			if(bodySignRecordList[x].skinTestInfo && bodySignRecordList[x].skinTestInfo.drugName){
				json += "\"drugName\":\""+bodySignRecordList[x].skinTestInfo.drugName+"\",";
				if(bodySignRecordList[x].skinTestInfo.testResult == 'p'){
					json += "\"testResult\":\"阳\",";
				}
				else{
					json += "\"testResult\":\"阴\",";
				}
				
				/*var skinResult = bodySignRecordList[x].skinTestInfo.testResult,
				skindrugName   = bodySignRecordList[x].skinTestInfo.drugName;
				if(skinResult!='undefined'){
					if(skinResult=="N"){
						skinResult=skindrugName+"-阴";
					}else{
						skinResult=skindrugName+"-阳";
					}
					skinResult += "-"+bodySignRecordList[x].skinTestInfo.drugBatchNo;
					skinResult += "-"+bodySignRecordList[x].skinTestInfo.drugCode;
				}else{
					skinResult="";
				}*/
			}
			if(bodySignRecordList[x].event && bodySignRecordList[x].event.eventCode){
				var eventRecordDate = bodySignRecordList[x].event.recordDate;
				json += "\"eventCode\":\""+bodySignRecordList[x].event.eventCode+"\",";
				if(eventRecordDate){
					eventRecordDate = eventRecordDate.substring(eventRecordDate.indexOf(' '),eventRecordDate.lastIndexOf(':'));
					json += "\"recordDate\":\""+eventRecordDate+"\",";
				}
				//problem = bodySignRecordList[x].event.problem+"-"+bodySignRecordList[x].event.intervention+"-"+bodySignRecordList[x].event.recordDate;
			}
			if(bodySignRecordList[x].remark){
				json += "\"remark\":\""+bodySignRecordList[x].remark+"\",";
			}
			json = json.substring(0,json.length-1);
			json += "},";
		}
		json = json.substring(0,json.length-1);
		json = json + "]}";
		json = $.parseJSON(json);
		if(json==null){		
			$('#info-tab').datagrid('loadData',{total:0,rows:[]}); 
		}
		else{
			$("#info-tab").datagrid({
				data : json.rows
			});
		}


	});
}
function comboFomatterFunc(n){
	var idx = n;
	return function(value,row,index){
    	if (value == '') {
	        return;
	    }
	    for (var i = 0; i < dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts.length; i++) {
	        if (dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].val == value) {
	            return dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].text;
	        }
	    }
    }
}

function selectEventFocusTime(n){
	console.log(1);
}

function selectFomatterFunc(n){
	var idx = n;
	return function(record){
    	if(record.editFlag == 1){
    		var row = $("#info-tab").datagrid('getSelected');
    		var rindex = $("#info-tab").datagrid('getRowIndex', row);
            onClickCell(rindex,dataGridObj.dataGridField[idx],{"disabled":false});
    	}
    	else{
    		var row = $("#info-tab").datagrid('getSelected');
    		var rindex = $("#info-tab").datagrid('getRowIndex', row);
			onClickCell(rindex,dataGridObj.dataGridField[idx],{"disabled":true,"val":record.text});
    	}
    }
}

function setEnterTime(h){
	var timeArr = dataGridObj.dataGridTime;
	var str = '';
	var temp = 24;
	var index = 0;
	for(var i=0;i<timeArr.length;i++){
		if(temp > Math.abs(parseInt(timeArr[i])- h) ){
			temp = Math.abs(parseInt(timeArr[i])-h);
			index = i;
		}
		str += '<option value="'+timeArr[i]+'">'+timeArr[i]+'</option>';
	}
	$("#time").append(str);
	$("#time").find('option:eq('+index+')').attr('selected','selected');
}

function getOptName(type,val){
	var str = '';
	var opts = dataGridObj.dataGridOptions[type].opts;
	for(var i=0;i<opts.length;i++){
		if(opts[i].val == val){
			return opts[i].text;
		}
	}

}

function unitformatter(value, rowData, rowIndex) {
    if (value == 0) {
        return;
    }
    for (var i = 0; i < Address.length; i++) {
        if (Address[i].value == value) {
            return Address[i].text;
        }
    }
}


/*
* 更新体温单数据
* @params 行号
*/
function updateInfo(){
	var rowIndex = editIndex;
	var selectRow = $("#info-tab").datagrid('getSelected');
	//是否校验通过 通过了保存当前项 不通过获取焦点
	if (endEditing()) {
		$('#info-tab').datagrid('selectRow', editIndex).datagrid('editCell', {
			index : editIndex,
			field : fields
		});

	}
	else{
		var ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:fields});
		$(ed.target).focus();
		return ;
	}
	var skinArr;
	var eventArr;
	var recordTime = $("#time").val();//new Date().format("hh:mm:ss");
	//var recordDate = $("#recordDate").val();
	var recordDay = $("#date").val(); //new Date().format("yyyy-MM-dd");
	var patientName = $("#patientName").html();
	var recordNurseName = $("#userName", window.parent.document).val();
	var recordNurseCode = $("#userCode", window.parent.document).val();
	
	//return ;
	//var data = $("#info-tab").datagrid('getData').rows;
	//为空不保存
	if(!selectRow){
		$.messager.alert('提示', "请选择需要更新的项！");
		return;
	}
	var item = '';
	var bodySignItemList = "";
	var rowData = null;
	//for(var i=0;i<len;i++){
	rowData = selectRow;
	//rowData.recordTime = rowData.recordTime.substring(0,rowData.recordTime.lastIndexOf(':'));
	bodySignItemList = '"bodySignItemList":[';
	item += '{"patientId": "'+rowData.patientId+'","patientName": "'+patientName+'",' +
			'"recordDay": "'+rowData.recordDay+'","recordNurseCode": "'+recordNurseCode+'","masterRecordId":"'+rowData.masterRecordId+'",'+
			'"recordNurseName": "'+recordNurseName+'","recordTime": "'+rowData.recordTime+':00",';
	if(rowData.remark){
		item += '"remark": "'+rowData.remark+'",';
	}
	else{
		item += '"remark": "",';
	}
	if(rowData.copyFlag){
		if('是' == rowData.copyFlag){
			item += '"copyFlag": "Y",';
		}else{
			item += '"copyFlag": "N",';
		}
	}
	if(rowData.drugName){
		var testResult = rowData.testResult == '阳' ? 'p' : 'n';
		item += '"skinTestInfo": {"drugName": "'+rowData.drugName+'","orderGroupNo": "-1",'+
				'"patientId": "'+rowData.patientId+'","patientName": "'+patientName+'",'+
				'"testNurseId": "'+recordNurseCode+'","testNurseName": "'+recordNurseName+'",'+
				'"testResult": "'+testResult+'"},';
	}

	if(rowData.eventCode){
		item += '"event": {"confirmed": false,"eventCode": "'+rowData.eventCode+'",'+
				'"index": 0,"patientId": "'+rowData.patientId+'",'+
				'"patientName": "'+patientName+'","recordDate": "'+rowData.recordDay+' '+rowData.recordDate+':00"},';
	}
	if(rowData.painCode){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
				'"itemCode": "pain","itemName": "疼痛",'+
				'"itemUnit": ""},"index": 0,'+
				'"itemValue": "'+getOptName('pain',rowData.painCode)+'","measureNoteCode": "'+rowData.painCode+'"},';
	}
	if(rowData.PPDCode){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
				'"itemCode": "PPD","itemName": "PPD",'+
				'"itemUnit": ""},"index": 0,'+
				'"itemValue": "'+getOptName('PPD',rowData.PPDCode)+'","measureNoteCode": "'+rowData.PPDCode+'"},';
	}
	if(rowData.coolwayCode){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
				'"itemCode": "coolway","itemName": "coolway",'+
				'"itemUnit": ""},"index": 0,'+
				'"itemValue": "'+getOptName('coolway',rowData.coolwayCode)+'","measureNoteCode": "'+rowData.coolwayCode+'"},';
	}
	if(rowData.other){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "other","itemName": "其他",'+
							'"itemUnit": ""},"index": 0,'+
							'"itemValue": "'+rowData.other+'","measureNoteCode":"",'+
							'"measureNoteName": ""},'
	}
	if(rowData.temperature){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "temperature","itemName": "体温",'+
							'"itemUnit": "℃"},"index": 0,'+
							'"itemValue": "'+rowData.temperature+'","measureNoteCode": "'+rowData.temperatureType+'",'+
							'"measureNoteName": "'+getOptName('temperature',rowData.temperatureType)+'"},'
	}
	if(rowData.pulse){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "pulse","itemName": "脉 搏",'+
							'"itemUnit": "次/分"},"index": 0,'+
							'"itemValue": "'+rowData.pulse+'","measureNoteCode": "'+rowData.pulseType+'",'+
							'"measureNoteName": "'+getOptName('pulse',rowData.pulseType)+'"},'
	}
	if(rowData.heartRate){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "heartRate","itemName": "呼 吸",'+
							'"itemUnit": "次/分"},"index": 0,'+
							'"itemValue": "'+rowData.heartRate+'","measureNoteCode": "'+rowData.heartRateType+'",'+
							'"measureNoteName": "'+getOptName('heartRate',rowData.heartRateType)+'"},'
	}
	if(rowData.breath){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "breath","itemName": "心 率",'+
							'"itemUnit": "次/分"},"index": 0,'+
							'"itemValue": "'+rowData.breath+'","measureNoteCode": "'+rowData.breathType+'",'+
							'"measureNoteName": "'+getOptName('breath',rowData.breathType)+'"},'
	}
	if(rowData.bloodPress){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "bloodPress","itemName": "血 压",'+
							'"itemUnit": "mmHg"},"index": 0,'+
							'"itemValue": "'+rowData.bloodPress+'","measureNoteCode": "'+rowData.bloodPressType+'",'+
							'"measureNoteName": "'+getOptName('bloodPress',rowData.bloodPressType)+'"},'
	}
	if(rowData.otherOutput){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "otherOutput","itemName": "其他出量",'+
							'"itemUnit": "ml"},"index": 0,'+
							'"itemValue": "'+rowData.otherOutput+'","measureNoteCode": "'+rowData.otherOutputType+'",'+
							'"measureNoteName": "'+getOptName('otherOutput',rowData.otherOutputType)+'"},'
	}
	if(rowData.totalInput){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "totalInput","itemName": "总入液量",'+
							'"itemUnit": "ml"},"index": 0,'+
							'"itemValue": "'+rowData.totalInput+'","measureNoteCode": "'+rowData.totalInputType+'",'+
							'"measureNoteName": "'+getOptName('totalInput',rowData.totalInputType)+'"},'
	}
	if(rowData.urine){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "urine","itemName": "尿 量",'+
							'"itemUnit": "ml"},"index": 0,'+
							'"itemValue": "'+rowData.urine+'","measureNoteCode": "'+rowData.urineType+'",'+
							'"measureNoteName": "'+getOptName('urine',rowData.urineType)+'"},'
	}
	if(rowData.stool){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "stool","itemName": "大 便",'+
							'"itemUnit": "次"},"index": 0,'+
							'"itemValue": "'+rowData.stool+'","measureNoteCode": "'+rowData.stoolType+'",'+
							'"measureNoteName": "'+getOptName('stool',rowData.stoolType)+'"},'
	}
	if(rowData.height){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "height","itemName": "身 高",'+
							'"itemUnit": "cm"},"index": 0,'+
							'"itemValue": "'+rowData.height+'","measureNoteCode": "'+rowData.heightType+'",'+
							'"measureNoteName": "'+getOptName('height',rowData.heightType)+'"},'
	}
	if(rowData.weight){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "weight","itemName": "体 重",'+
							'"itemUnit": "Kg"},"index": 0,'+
							'"itemValue": "'+rowData.weight+'","measureNoteCode": "'+rowData.weightType+'",'+
							'"measureNoteName": "'+getOptName('weight',rowData.weightType)+'"},'
	}
	if(rowData.oxygenSaturation){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "oxygenSaturation","itemName": "腹 围",'+
							'"itemUnit": "cm"},"index": 0,'+
							'"itemValue": "'+rowData.oxygenSaturation+'","measureNoteCode": "'+rowData.oxygenSaturationType+'",'+
							'"measureNoteName": "'+getOptName('oxygenSaturation',rowData.oxygenSaturation)+'"},'
	}
	if(rowData.abdominalCir){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "abdominalCir","itemName": "腹 围",'+
							'"itemUnit": "cm"},"index": 0,'+
							'"itemValue": "'+rowData.abdominalCir+'","measureNoteCode": "'+rowData.abdominalCirType+'",'+
							'"measureNoteName": "'+getOptName('abdominalCir',rowData.abdominalCirType)+'"},'
	}
	if(rowData.cooledTemperature){
		bodySignItemList += '{"abnormalFlag": 0,"bodySignDict": {'+
							'"itemCode": "cooledTemperature","itemName": "降温后体温",'+
							'"itemUnit": "℃"},"index": 0,'+
							'"itemValue": "'+rowData.cooledTemperature+'","measureNoteCode": "yw",'+
							'"measureNoteName": "腋温"},';
		item += '"cooled": "1",';
	}
	else{
		item += '"cooled": "0",';
	}
	if( bodySignItemList.length <= 22 ){
		bodySignItemList += "]";
	}
	else{
		bodySignItemList = bodySignItemList.substring(0,bodySignItemList.length-1)+"]";
	}
	
	item += bodySignItemList+'},';
	//}
	item = item.substring(0,item.length-1);
	//console.log(item);
	var url = ay.contextPath+'/nur/bodySign/updateBodySignRecord.do';
	$.post(url,{"item":item},function(result){
        try {
            if(result.rslt ==0) {
                $.messager.alert('提示',result.msg);
                getPatBedId("",true);
            } else {
                $.messager.alert('错误',result.msg);
            }
            //clearData();
            //loadPdf({"currentWeek":$("#currentWeek")},null);
        } catch (e) {
            $.messager.alert('错误',e);
        }
    });
}

/*
* 删除行
*
*/
function deleteInfo(){
/*	var skinArr;
	var eventArr;*/
	var selectRow = $("#info-tab").datagrid('getSelected');
	if(!selectRow){ 
		$.messager.alert('提示','请选择要删除的数据行!');
		return; 
	}

	$.messager.confirm('提醒','确定要删除该行数据吗?',function(r){
		if(r){
		    var index = $("#info-tab").datagrid('getRowIndex',selectRow);
			$.post(ay.contextPath+'/nur/bodySign/deleteBodySignRecord.do',{recordId:selectRow.masterRecordId},function(data){
				console.log(data);
				try{
					if(data.rslt == 0){
						$('#info-tab').datagrid('deleteRow',index);
						$.messager.alert('提示','删除成功');
					}
				}catch(e){
					console.log("error:"+e);
				}
			});
		}
	});
}


//查看体温单
function showTempSheet(){
    if(parent.peopleList.length==0){
        $.messager.alert('提示', "您没有选择患者，请先选择患者！");
    }
	window.parent.window.showTempSheetPanel(null);
}



$(function(){
	initSkinTestDrugsData();
	initDataGridColumns();
	var recordDay = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDay);
	if(!currentDate){ currentDate = recordDay; }
	if(document.body.childNodes[0].nodeType == 3){
		document.body.removeChild(document.body.childNodes[0]);	
	}
	$('.content').height( $(window).height() - $('.top-tools').height() );
	$("#showTempSheetBtn").bind('click',function(){
		showTempSheet();
	});
	//设置患者信息容器宽度
	if(parent.setPatientInfoWrap){
		parent.setPatientInfoWrap();
	}

	$("#info-tab").datagrid({
		fit : true,
		singleSelect: true,
		onClickCell: onClickCell,
		columns:dataGridColumns
	});
});


/** 皮试药物初始化
 * 	"skinTestInfo":{
			"opts":[
				{ "val":"","text":"无"},
				{ "val":"青霉素","text":"青霉素"},
				{ "val":"链霉素","text":"链霉素"},
				{ "val":"破伤风抗毒素血清","text":"破伤风抗毒素血清"}
			]
		}
 * **/
function initSkinTestDrugsData(){
	var skinTestDrugs = config.data.system.comSkinTestDrugs ? config.data.system.comSkinTestDrugs.split(",") : [];
	var stDrugs = [];
	$(skinTestDrugs).each(function(index,value){
		var drug = {};
		if("无" == value){
			drug.val = "";
		}else{
			drug.val = value;
		}
		drug.text = value;
		stDrugs.push(drug);
	});
	
	dataGridObj.dataGridOptions['skinTestInfo'].opts = stDrugs;
}
