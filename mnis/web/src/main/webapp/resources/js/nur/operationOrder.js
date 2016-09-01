var _data = {
    "rows":[
        {"mId":"已手术",
        "prestype":"2014-07-15 10:00",
        "prescate":"阑尾炎切除术",
        "planexedate":"阑尾",
        "presinfo":"刘琪",
        "norms":"李强",
        "dosage":"王思施",
        "usage":"2014-07-15 9:30",
        "hz":"艾迪",
        "createdate":"2014-07-15 11:30",
        "createdoc":"李强",
        "comment":"术前禁食8小时",
        "exedoc":"王丹丹",
        "exedate":"2014-07-15 19:28"},
             {"mId":"已手术",
                "prestype":"2014-07-15 10:00",
                "prescate":"阑尾炎切除术",
                "planexedate":"阑尾",
                "presinfo":"刘琪",
                "norms":"李强",
                "dosage":"王思施",
                "usage":"2014-07-15 9:30",
                "hz":"艾迪",
                "createdate":"2014-07-15 11:30",
                "createdoc":"李强",
                "comment":"笨巴比妥注射液 0.1g im 术前30分",
                "exedoc":"王丹丹",
                "exedate":"2014-07-15 9:28"},
                {"mId":"已手术",
                    "prestype":"2014-07-15 10:00",
                    "prescate":"阑尾炎切除术",
                    "planexedate":"阑尾",
                    "presinfo":"刘琪",
                    "norms":"李强",
                    "dosage":"王思施",
                    "usage":"2014-07-15 9:30",
                    "hz":"艾迪",
                    "createdate":"2014-07-15 11:30",
                    "createdoc":"李强",
                    "comment":"地西洋注射液 1mg im术前30分",
                    "exedoc":"王丹丹",
                    "exedate":"2014-07-15 9:28"},
                    {"mId":"已手术",
                        "prestype":"2014-07-15 10:00",
                        "prescate":"阑尾炎切除术",
                        "planexedate":"阑尾",
                        "presinfo":"刘琪",
                        "norms":"李强",
                        "dosage":"王思施",
                        "usage":"2014-07-15 9:30",
                        "hz":"艾迪",
                        "createdate":"2014-07-15 11:30",
                        "createdoc":"李强",
                        "comment":"术前备皮",
                        "exedoc":"廖文琪",
                        "exedate":"2014-07-15 9:38"},
   ]
}
var _data2 = {
}
function hideSpanCheckBox(){
    $('.long-medi p span.circle').each(function(){
        $(this).removeClass('circle2');
    });
}
function loadMediData(d){
    $('#medicalOrderTable').datagrid({
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
                // 添加数据加载   

            }
        });
    });
    $('#mid-show-box').height( $(window).height() - $('.top-tools').height() );
	var pid = $('#pid').val();
    $('input[name=inspectType]').click(function(){
        searchLabTestRecords();
    });
    $('#medicalOrderTable').datagrid({
        data:_data,
        fit:true
    });
});
function loadRecordSuccess( data ) {
	//$('#labTestRecordTable').datagrid('loadData', data);
}
function setCheckBox(value,row,index){
    return '<input type="checkbox" value='+ value +'>';
}


