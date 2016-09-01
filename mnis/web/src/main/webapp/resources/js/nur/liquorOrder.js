var _data = {
    "rows":[
        {"mId":"12",
        "prestype":"左氧氟沙星注射液 10mg</br>50%葡萄糖注射液50ml",
        "prescate":"杜鹃",
        "planexedate":"2014-08-15 07:00",
        "presinfo":"李芳",
        "norms":"2014-08-15 08:00",
        "dosage":"2014-08-15 12:00",
        "usage":"杜鹃",
        "hz":"2014-08-15 08:20",
        "createdate":"2014-04-23 13:40",
        "createdoc":"李医生",
        "comment":"皮试",
        "exedoc":"王丹丹",
        "exedate":"2014-04-24 17:20"},
        {"mId":"13",
        	"prestype":"注射用重组人促红素注射液 20mg</br>50%葡萄糖注射液50ml",
            "prescate":"周安",
            "planexedate":"2014-08-15 07:00",
            "presinfo":"李芳",
            "norms":"2014-08-15 08:00",
            "dosage":"2014-08-15 12:00",
            "usage":"杜鹃",
            "hz":"2014-08-15 08:20",
            "createdate":"2014-04-23 13:40",
            "createdoc":"李医生",
            "comment":"皮试",
            "exedoc":"王丹丹",
            "exedate":"2014-04-24 17:20"},
           
                
    ]
}
var _data2 = {
    "rows":[
        {"mId":"12",
        "prestype":"注射用头孢曲松钠 250mg<br/>氯化钠注射液50ml",
        "prescate":"王思奥",
        "planexedate":"2014-08-15 08:10",
        "presinfo":"彭璐",
        "norms":"2014-08-15 08:32",
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
function hideSpanCheckBox(){
    $('.long-medi p span.circle').each(function(){
        $(this).removeClass('circle2');
    });
}
function loadMediData(d){
    $('#medicalOrderTable').datagrid({
        data:d,
        fit:false
    });
}
function loadMediData2(d){
    $('#deptTable').datagrid({
        data:d,
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
        $('#medicalOrderTable').datagrid({
            data:_data2,
            fit:true
        });
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
                loadMediData(_data);
                loadMediData2(_data2);
                // 添加数据加载   

            }
        });
    });
    //$('#mid-show-box').height( $(window).height() - $('.top-tools').height() );
	var pid = $('#pid').val();
    $('input[name=inspectType]').click(function(){
        searchLabTestRecords();
    });
    loadMediData(_data);
    loadMediData2(_data2);
});
function loadRecordSuccess( data ) {
	//$('#labTestRecordTable').datagrid('loadData', data);
}
function setCheckBox(value,row,index){
    return '<input type="checkbox" value='+ value +'>';
}


