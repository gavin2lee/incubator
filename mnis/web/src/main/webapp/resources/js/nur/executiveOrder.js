var orderDetail = [],
    _state      = 0,
    tempArr = [];


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
            var planDate = getPlanDate(tempArr[i].orderExecInfoList);
            tempArr[i].orderBaseGroup._standard = standard;
            tempArr[i].orderBaseGroup._dosage = dosage;
            tempArr[i].orderBaseGroup._planDate = planDate;
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


