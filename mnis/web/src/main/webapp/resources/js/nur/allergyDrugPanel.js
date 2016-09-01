var allergyDrugPanel   = "allergyDrugDialog",
	currCheckedAllergy = null,
	skinBoxIndex = null,
	isAllergyBox       = "isAllergy";


function _extend(child,parent,proto){
	if (!proto) {
        proto = parent;
        parent = null;
    }
    var childProto;
    if (parent) {
        var fn = function () { };
        fn.prototype = parent.prototype;
        childProto = new fn();
        _each(proto, function (key, val) {
            childProto[key] = val;
        });
    } else {
        childProto = proto;
    }
    childProto.constructor = child;
    child.prototype = childProto;
    child.parent = parent ? parent.prototype : null;
}

function AllergyDrugDialog(){

}

_extend(AllergyDrugDialog,{
	init:function(params){
		var self = this;
		self.callBackObj = params.callBackObj;
		self.loadLeftList = params.loadLeftList;
		self.loadRightList = params.loadRightList;		
		self.saveOperate = params.saveOperate;
		self.closeCallback = params.closeCallback;
		$("#"+allergyDrugPanel)[0].dialogObj = self;
	},
	refresh:function(isAllergyDrugDialog,index){
		var self = this;
		skinBoxIndex = index;
		$(".allergyDrug-list").detach();
		$("#checkedAllergyDrug ul").empty();
		if(self.loadLeftList){
			self.loadLeftList(self.callBackObj,self.loadLeftCb);
		}
		if(isAllergyDrugDialog){
			self.open();
			
		}
	},
	reload:function(){
		var refid,cateid,textStrArr=[],bno,title,sName,sResult,sClass,	
			adt = $(".allergyDrug-title"),
			adi = $(".allergyDrug-item ul li"),
			cad = $("#checkedAllergyDrug ul");
		$("#allergyDrugList"+skinBoxIndex).find('li').each(function(){
			if($(this).text()=='无'){
				return ;
			}
			refid  =  $(this).attr('refid');
			cateid =  $(this).attr('cateid');
			bno    =  $(this).attr('data-bno');
			title  =  $(this).attr('title');
			sName  =  $(this).find('span:eq(0)').html().split('-')[0];
			sClass =  $(this).find('span:eq(1)').attr('class');
			sResult=  $(this).find('span:eq(1)').html();
			if( refid != cateid){
				adi.find('input[refid='+refid+']').attr('checked',true).parent().attr('data-checked','1');
				cad.append('<li class="'+sClass+'" refid='+refid+' cateid='+cateid+' data-bno='+bno+' title='+title+'>'+sName+'-'+sResult+'</li>')
			}
			else{
				adt.find('input[refid='+refid+']').attr('checked',true).parent().attr('data-checked','1');
				adt.find('input[refid='+refid+']').parent().next().find('ul div').removeClass('hide');
				cad.append('<li class="'+sClass+'" refid='+refid+' cateid='+cateid+' data-bno='+bno+' title='+title+'>'+sName+'-'+sResult+'</li>');
			}
		});
	},
	save:function(){
		var self = this;
		var allergyList = $("#checkedAllergyDrug ul li");
		var allergyArr = [];
		var isBlack = '';
		if(allergyList.length==0){
			$("#allergyDrugList"+skinBoxIndex).html('<li>无</li>');
		}
		else{
			$("#allergyDrugList"+skinBoxIndex).html('');
			for(var idx=0;idx<allergyList.length;idx++){
				isBlack = 'green';
				allergyArr = allergyList[idx].innerHTML.split('-');
				if(allergyArr[1] == '阳'){
					isBlack = ' red ';
				}
				$("#allergyDrugList"+skinBoxIndex).append('<li title="批号：'+$(allergyList[idx]).attr('data-bno')+'" refid="'+$(allergyList[idx]).attr('refid')+'" cateid="'+$(allergyList[idx]).attr('cateid')+'" data-bno="'+$(allergyList[idx]).attr('data-bno')+'"><span>'+allergyArr[0]+'-'+$(allergyList[idx]).attr('data-bno')+'</span><span class="'+isBlack+'">'+allergyArr[1]+'</span></li>')
			}
			$("#skinBox"+skinBoxIndex).find('a').text('修改');
		}

		self.close();
	},
	loadLeftCb:function(items){
		var self = $("#"+allergyDrugPanel)[0].dialogObj;
		var nodeStr='';
		var cacheArr = [];
		
		for(var idx=0;idx<items.length;idx++){
			nodeStr = '<div class="allergyDrug-list"><div class="allergyDrug-title"><input class="allergyDrug-title-cb" type="checkbox" refid="'+items[idx].refid+'">'+items[idx].categoryName+'</div></div>';
			$("#allergyDrugBox").append(nodeStr);
			cacheArr.push(items[idx].refid);
		}
		$(".allergyDrug-title").bind('click',{self:self},leftItemEvent);
		if(self.loadRightList){
			self.loadRightList(self.callBackObj,self.loadRightCb,cacheArr);
		}
	},
	loadRightCb:function(items){
		var self = $("#"+allergyDrugPanel)[0].dialogObj;;
		var lists = $(".allergyDrug-list");
		var nodeStr = '';
		for(var idx=0;idx<items.length;idx++){
			nodeStr ='<div class="allergyDrug-item"><ul>';
			for(var ndx=0;ndx<items[idx].length;ndx++){
				nodeStr += '<li cateId="'+items[idx][ndx].categoryRefid+'"><input type="checkbox" refid="'+items[idx][ndx].refid+'" cateid="'+items[idx][ndx].categoryRefid+'"><label for="'+items[idx][ndx].refid+'">'+items[idx][ndx].drugName+'</label></li>'
			}
			nodeStr +='<div id="cover'+items[idx][0].categoryRefid+'" class="hide"></div></ul></div>';
			lists.eq(idx).append(nodeStr);
		}
		self.reload();
		$(".allergyDrug-item ul li").bind('click',{self:self},rightItemEvent);
	},
	loadAllergyDrug:function(){

	},
	open:function(){
		var self = this;
		if($("#skinBox"+skinBoxIndex+" > span").html() != '无'){
			self.loadAllergyDrug();
		}
		$("#"+allergyDrugPanel).dialog("open");
	},
	close:function(){
		$("#"+allergyDrugPanel).dialog("close");
	},
	openIsAllergy:function(text,rId,cateId){
		$("#allergyDrug").html(text);
		$("#refid").val(rId);
		if(cateId){
			$("#cateid").val(cateId);
		}
		$("#"+isAllergyBox).dialog('open');
	},
	closeIsAllergy:function(){
		$("#"+isAllergyBox).dialog('close');
	}
});


function initAllergyDrugDialog(params){
	var dialog = new AllergyDrugDialog();
	dialog.init(params);
}
function closeAllergyDrugDialog(){
	$("#"+allergyDrugPanel)[0].dialogObj.close();
}
function saveAllergyDrug(){
	$("#"+allergyDrugPanel)[0].dialogObj.save();	
}
function refreshAllergyDrugDialog(isOpenDialog,index){
	$("#"+allergyDrugPanel)[0].dialogObj.refresh(isOpenDialog,index);
}

function confirmAllergy(){
	var isRed = '';
	var allergyDrug = $("#allergyDrug").html();
	var bNo         = $("#bNo").val();
	var refid         = $("#refid").val();
	var cateid         = $("#cateid").val();
	var isAllergy   = $("#isa:checked").val();
	if(isAllergy=='阳'){
		isRed =' color:#f00; ';
	}
	else{
		isRed =' color:green; ';
	}
	$(currCheckedAllergy).find('input').attr('checked',true);
	if(!cateid){
		cateid = $(currCheckedAllergy).find('input').attr('refid');
		removeAllergy(null,cateid);
		$(".allergyDrug-item ul li[cateid="+cateid+"]").find('input').each(function(){
			$(this).removeAttr('checked');
		})
	}
	currCheckedAllergy.attr('data-checked',1);
	$("#checkedAllergyDrug ul").append('<li style="'+isRed+'" title="批号：'+bNo+'" data-bNo="'+bNo+'" refid="'+refid+'" cateid="'+cateid+'">'+allergyDrug+'-'+isAllergy+'</li>');
	$("#isAllergy").dialog('close');
	resetIsAllergy();
}
function cancelAllergyDialog(){
	$("#allergyDrug").html('');
	$(currCheckedAllergy).find('input').removeAttr('checked');
	if($(currCheckedAllergy).attr('class') == 'allergyDrug-title'){
		var rid = $(currCheckedAllergy).find('input').attr('refid');
		$("#cover"+rid).addClass('hide');
	}
	$("#isAllergy").dialog('close');
	resetIsAllergy();
}
function clearCurrCheckedAllergy(){
	$(currCheckedAllergy).find('input').removeAttr('checked');
	resetIsAllergy();
}
function resetIsAllergy(){
	$("#bNo").val('');
	$("#cateid").val('');
	$("#refid").val('');
	$("input[name='isa']:eq(1)").attr('checked',true);
}

function rightItemEvent(event){
	currCheckedAllergy = $(this);
	var refid = $(this).find('input').attr('refid');
	var cateid = $(this).find('input').attr('cateid');
	if($(this).attr('data-checked')=='1'){
		removeAllergy(refid);
		$(this).find('input').removeAttr('checked');
		$(this).removeAttr('data-checked');
		return false;	
	}
	event.data.self.openIsAllergy($(this).find('label')[0].innerHTML,refid,cateid);
}

function leftItemEvent(event){
	currCheckedAllergy = $(this);
	var node = $(this).find('input[type="checkbox"]');
	var refid = node.attr('refid');
	if(!node.attr('checked')){
		$("#cover"+refid).removeClass('hide');
		$(".allergyDrug-item li").each(function(){
			if($(this).attr('refid') == refid){
				$(this).removeAttr('checked');
			}
		});		
		event.data.self.openIsAllergy($(this).text(),refid);
	}
	else{
		$("#cover"+refid).addClass('hide');
		removeAllergy(refid);
		node.removeAttr('checked');
	}
	
}

function removeAllergy(rid,cid){
	if(cid){
		$("#checkedAllergyDrug ul li").each(function(){
			if( $(this).attr('cateid') == cid ){
				$(this).remove();
			}
		});
	}
	if(rid){
		$("#checkedAllergyDrug ul li").each(function(){
			if( $(this).attr('refid') == rid ){
				$(this).remove();
			}
		})
	}
}

