var orderDetail = [],
    _state      = 0,
    tempArr = [];
var _allData1 = {
    "1":[
        {"mId":"12",
        "prestype":"长期",
        "prescate":"口服液",
        "planexedate":"2014-08-08 16:00",
        "presinfo":"维生素C片",
        "norms":"50.00mg",
        "dosage":"0.3",
        "usage":"P.O",
        "hz":"TID",
        "createdate":"2014-08-07 11:31",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"周安",
        "exedate":"2014-08-08 16:09"},
        {"mId":"13",
        "prestype":"长期",
        "prescate":"注射液",
        "planexedate":"2014-08-08 16:10",
        "presinfo":"注射用重组人促红素注射液<br>50%葡萄糖注射液20ml",
        "norms":"3000.00u<br>10.00g",
        "dosage":"0.3",
        "usage":"iv.drip",
        "hz":"BID",
        "createdate":"2014-08-07 11:30",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"周安",
        "exedate":"2014-08-08 16:09"}],
    "2":[{"mId":"14",
        "prestype":"长期",
        "prescate":"注射液",
        "planexedate":"",
        "presinfo":"氨茶碱0.25g",
        "norms":"10.00ml",
        "dosage":"0.3",
        "usage":"",
        "hz":"24H",
        "createdate":"2014-08-07 11:39",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"",
        "exedate":""},
        {"mId":"14",
        "prestype":"长期",
        "prescate":"注射液",
        "planexedate":"",
        "presinfo":"碳酸氢钠注射液250ml、50%葡萄糖注射液250ml",
        "norms":"250.00ml、12.50g",
        "dosage":"0.3",
        "usage":"",
        "hz":"QD(12)",
        "createdate":"2014-08-07 11:37",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"",
        "exedate":""}],
    "3":[{"mId":"14",
        "prestype":"长期",
        "prescate":"检查",
        "planexedate":"",
        "presinfo":"双肾及肾血管彩色多普勒超声",
        "norms":"1次",
        "dosage":"0.3",
        "usage":"",
        "hz":"24H",
        "createdate":"2014-08-08 14:24",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"",
        "exedate":""},
        {"mId":"14",
        "prestype":"长期",
        "prescate":"检查",
        "planexedate":"",
        "presinfo":"心电图",
        "norms":"1次",
        "dosage":"0.3",
        "usage":"iv.drip",
        "hz":"QID",
        "createdate":"2014-08-08 14:21",
        "createdoc":"冯秀丽",
        "comment":"",
        "exedoc":"",
        "exedate":""}]
}
var _allData2 = {
    "rows":[
        {"mId":"12",
        "prestype":"长期",
        "prescate":"口服液",
        "planexedate":"2014-04-24 16:00",
        "presinfo":"左氧氟沙星注射液",
        "norms":"250ml*1",
        "dosage":"0.3",
        "usage":"ivgtt",
        "hz":"bid",
        "createdate":"2014-04-23 13:40",
        "createdoc":"李医生",
        "comment":"皮试",
        "exedoc":"王丹丹",
        "exedate":"2014-04-24 17:20"}
    ]
}

function getBedCode(val,row){
    var x = val.indexOf('_');
    return parseInt(val.substring(x+1));
}

function getOrderBaseInfo(val,row){
    var len = val.length,
        str = '';
    if( 1 == len){
        str =  val[0].orderName;
        return str;
    }
    else{
        for(var i=0;i<len;i++){
            if(i == (len-1) ){
                str += val[i].orderName;
                return str;
            }
            str += val[i].orderName +'<br>';        
        }
        return str;
    }
}
function getStandard(val){
    var len = val.length,
        str = '';
    if( 1 == len){
        str =  val[0].dosageUnit;
        return str;
    }
    else{
        for(var i=0;i<len;i++){
            if(i == (len-1) ){
                str += val[i].dosageUnit;
                return str;
            }
            str += val[i].dosageUnit +'<br>';       
        }
        return str;
    }
}
function getDosage(val){
    var len = val.length,
        str = '';
    if( 1 == len){
        str =  val[0].dosage;
        return str;
    }
    else{
        for(var i=0;i<len;i++){
            if(i == (len-1) ){
                str += val[i].dosage;
                return str;
            }
            str += val[i].dosage +'<br>';       
        }
        return str;
    }
}
function getPlanDate(val){
    var len = val.length,
        str = '';
    if( 1 == len){
        str =  val[0].planDate;
        return str;
    }
    else{
        for(var i=0;i<len;i++){
            if(i == (len-1) ){
                str += val[i].planDate;
                return str;
            }
            str += val[i].planDate +'<br>';     
        }
        return str;
    }
}

function hideSpanCheckBox(){
    $('.long-medi p span.circle').each(function(){
        $(this).removeClass('circle2');
    });
}
function loadMediData(){
    $('#medicalOrderTable').datagrid({
        fit:true
    });
}


$(function(){
    $('.long-medi').click(function(){
        if( $(this).css('overflow') == 'hidden' ){
            $(this).css('overflow','visible');
        }
        else{
            $(this).css('overflow','hidden');
        }
    });
    $('.tmp-medi').click(function(){
        hideSpanCheckBox();
        /*$('#medicalOrderTable').datagrid({
            data:_data2,
            fit:true
        });*/
    });
    $('.long-medi div p').each(function(){
        $(this).click(function(){
            var spanCheckbox = $(this).find('span');
            if( spanCheckbox.hasClass('circle2') ){
                return false;
            }
            else{
                hideSpanCheckBox();
                spanCheckbox.addClass('circle2');
                $('.long-medi > span').text($(this).text());
                //loadMediData();
                // 添加数据加载   

            }
        });
    });
    $('#mid-show-box').height( $(window).height() - $('.top-tools').height() );
	var pid = $('#pid').val();
    $('input[name=inspectType]').click(function(){
        searchLabTestRecords();
    });
    /*$.post('')
    $('#medicalOrderTable').datagrid({
        data:_data,
        url:ay.contextPath+'/nur/task/getManyPatient.do?patientIdList='+patientId+'&execDate=2014-08-22',
        fit:true
    });*/
    $.post(ay.contextPath+'/nur/task/getManyPatient.do?patientIdList='+patientId+'&execDate=2014-08-22',{},function(data){
        tempArr = data.lst;
        for(var i=0;i<tempArr.length;i++){
            var standard = getStandard(tempArr[i].orderBaseGroup.orderBaseInfoList);
            var dosage = getDosage(tempArr[i].orderBaseGroup.orderBaseInfoList);
            tempArr[i].orderBaseGroup._standard = standard;
            tempArr[i].orderBaseGroup._dosage = dosage;
            orderDetail.push(tempArr[i].orderBaseGroup);
        }
        $("#medicalOrderTable").datagrid({
            data : orderDetail,
            fit : true
        });
    })
});
function loadRecordSuccess( data ) {
	//$('#labTestRecordTable').datagrid('loadData', data);
}
function setCheckBox(value,row,index){
    return '<input type="checkbox" value='+ value +'>';
}


