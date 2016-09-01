$(function(){

	//设置content区域高
	$(".content").height($(window).height()-$('.top-tools').height());
	$('.block-info').height($(".content").height()-180);
	$('.info-content').each(function(){
		$(this).height($(this).parent().height()-$(this).prev().height());		
	});

	//载入手术列表

	$.post(ay.contextPath+'/nur/nursingMemo/getSurgeryMan.do',{},function(data){
		_surDataList = data;
		addSurList(_surDataList);
		updateSurInfo(_surDataList[0]);
		$('.block-list ul').width($('.block-list ul').find('li').length*242);
		/* 手术切换 */ 
		$('.block-list ul li').each(function(){
			$(this).click(function(){
				clearSurChecked();
				$(this).addClass('checked');
				updateSurInfo(_surDataList[$(this).attr('data-id')]);
			})
			
		})

	});

	//手术列表宽度设置
	var titleBox = $('.title'),
		dateBox  = $('.title div p');


	titleBox.click(function(){
		if( $(this).css('overflow') == 'hidden' ){
			$(this).css('overflow','visible');
		}
		else{
			$(this).css('overflow','hidden');
		}
	});
	dateBox.each(function(){
		$(this).click(function(){
			var spanCheckbox = $(this).find('span');
			if( spanCheckbox.hasClass('circle2') ){
				return false;
			}
			else{
				hideSpanCheckBox();
				spanCheckbox.addClass('circle2');
				$('.title > span').text($(this).text());
				// 添加数据加载	

			}
		});
	});
	
	

	/* block/list 切换 */
	var bv = $("#block-view"),
		lv = $("#list-view");
	bv.click(function(){
		if(!$(this).hasClass('checked')){
			$(this).addClass('checked');
			lv.removeClass('checked');
			// add some action  切换页面的动作
			$('.sur-show-block').show();
			$('.sur-show-list').hide();
		}
	});
	lv.click(function(){
		if(!$(this).hasClass('checked')){
			$(this).addClass('checked');
			bv.removeClass('checked');
			// add some action
			$('.sur-show-block').hide();
			$('.sur-show-list').show();
			$('#sur-table-info').datagrid({
		        /*data:_data,*/
		        url:ay.contextPath+'/nur/nursingMemo/getSurgeryMan.do',
		        fit:true,
		        fitColumns:true,
		        singleSelect:true,
		        columns:[[    
		            {field:'bedId',title:'床号',width:100},    
		            {field:'patientName',title:'姓名',width:70},    
		            {field:'status',title:'手术状态',width:100},
		            {field:'surgeryTime',title:'手术时间',width:120},
		            {field:'surgeryName',title:'手术名称',width:100},
		            {field:'surgeryPart',title:'手术部位',width:70},
		            {field:'firDocName',title:'主刀医生',width:100},
		            {field:'secDocName',title:'助手医生',width:80},
		            {field:'sendNurName',title:'送病人护士',width:70},
		            {field:'sendTime',title:'送病人时间',width:120},
		            {field:'receNurName',title:'接病人护士',width:80},
		            {field:'receTime',title:'接病人时间',width:120},
		            {field:'a',title:'',width:80}
		        ]],
		        onDblClickRow:function(rowIndex,rowData){
		        	$("#prescriptionBox").dialog({
		        		title:'医嘱信息',
		        		width:700,
		        		height:300,
		        		modal:true
		        	});
		        	$("#prescription").datagrid({
		        		fit:true,
				        fitColumns:true,
				        columns:[[    
					        {field:'bedId',title:'床号',width:100},    
					        {field:'patientName',title:'患者姓名',width:100},    
					        {field:'patientId',title:'住院号',width:100},
					        {field:'patientId1',title:'开立时间',width:100},
					        {field:'patientId2',title:'医嘱名称',width:100},
					        {field:'patientId3',title:'频次',width:100},
					        {field:'patientId4',title:'用法',width:100},    
					        {field:'patientId5',title:'执行状态'},
					        {field:'patientId6',title:'执行护士'},
					        {field:'patientId7',title:'执行时间',width:100}
					    ]]    

		        	})
		        }    
		    });
		}
	});
	
	function updateSurInfo(d){
		updateSurAreaOne(d);
		updateSurAreaTwo(d);
		updateSurAreaThree(d);
		updateSurAreaFour(d);
	}

	function updateSurAreaOne(d){
		var str = '<p><span>手术计划时间：'+ d.surgeryInfo.surgeryTime +'</span></p>' +
				  '<p><span>手术名称：'+ d.surgeryInfo.surgeryName +'</span><span>手术部位：'+ d.surgeryInfo.surgeryPart +'</span></p>' +
				  '<p><span>主刀医生：'+ d.firDocName +'</span><span>第一助手：'+ d.surgeryInfo.firNurName +'</span><span>第二助手：'+ d.surgeryInfo.secNurName +'</span></p>';
		$('.sur-info .info-content').html(str);
	}
	function updateSurAreaTwo(d){
		
		var str = '<p><span>送病人：'+ d.sendNurName +' '+ d.sendTime +'</span></p><p><span>接病人：'+ d.receNurName +' '+ d.receTime +'</span></p>';
		$('.recv-msg .info-content').html(str);
	}
	function updateSurAreaThree(d){
		
		var str = '<p><span>医嘱开立时间：'+ d.surgeryInfo.creatTime +'</span><span>医嘱开立医生：'+ d.surgeryInfo.adviceDoc +'</span></p>' +
				  '<p><span>医嘱内容：'+ d.surgeryInfo.adviceContent +'</span><span>执行时间：'+ d.surgeryInfo.performTime +'</span></p>' +
				  '<p><span>执行护士：'+ d.surgeryInfo.performNurName +'</span></p>';
		$('.pres-info .info-content').html(str);
	}
	function updateSurAreaFour(d){
		var str = '<p>术前：<br><span>体温：'+ d.surgeryInfo.temperatureBefor +'</span><span>脉搏：'+ d.surgeryInfo.pulseBefor +'</span><span>呼吸：'+ d.surgeryInfo.breathBefor +'</span><span>血压：'+ d.surgeryInfo.pressureBefor +'</span>' +
				  '<p>术后：<br><span>体温：'+ d.surgeryInfo.temperatureAfter +'</span><span>脉搏：'+ d.surgeryInfo.pulseAfter +'</span><span>呼吸：'+ d.surgeryInfo.breathAfter +'</span><span>血压：'+ d.surgeryInfo.pressureAfter +'</span>';
		$('.vital-signs .info-content').html(str);
	}
	function addSurList(d){
		var len = d.length,
			age = ['31岁','25岁','53岁','2岁'],
			sex = ['女','女','女','女'],
			hosNum = ['000008','000001','000002','000003'],
			bl  = $('.block-list ul'),
			status = '',
			str = '',
			nodeStr = ''
		for(var i=0;i<len;i++){
			if( i==0 ){
				str = 'checked';
			}
			else{
				str = '';
			}
			if( '待手术' == d[i].status ){ status = 'waitsur' }
			if( '已手术' == d[i].status ){ status = 'sured' }
			if( '手术中' == d[i].status ){ status = 'suring' }
			nodeStr += '<li data-id="'+ i +'" class="'+ status +' '+ str +'"><span>'+ d[i].status +'</span><div><p><span>'+ d[i].bedId +'床</span><span>'+ d[i].patientName +'</span><span>'+ age[i] +'</span></p><p><span>'+ sex[i] +'</span><span>'+ hosNum[i] +'</span></p></div></li>'
		}
		bl.html(nodeStr);
	}

	function hideSpanCheckBox(){
		$('.title p span.circle').each(function(){
			$(this).removeClass('circle2');
		})
	}
	function clearSurChecked(){
		$('.block-list ul li').each(function(){
			if( $(this).hasClass('checked') ){
				$(this).removeClass('checked');
			}
		})
	}
})