$(function(){
	var pid = $('#pid').val();

    $('input[name=inspectType]').click(function(){
    	searchInspectionRecords();
    });
//url: '#url("/nur/inspectionRpt/")' + $id + '/getInspectionRdsByCond.do',
//queryParams: { id: '$id', startDate: '$currentDate', endDate: '$currentDate' },
    
    $("#inspectionRptTable").datagrid({
        fit:true,
        singleSelect : true,
        data:_data
    });

});
function loadRecordSuccess( data ) {
}
function relReportFormatter(value, row, index) {
    return '<a id="btn" href="#" class="easyui-linkbutton">对比</a>'
}

function searchInspectionRecords() {
    var inspectType = $('input[name=inspectType]:checked').val();
    var pid = $('#pid').val();
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    var url = ay.contextPath + '/nur/inspectionRpt/getInspectionRdsByCond.do';
    $.post( url,
        {startDate: startDate, endDate: endDate, status : inspectType},
        function (result) {
            try {
                $('#inspectionRptTable').datagrid('loadData', result);
            } catch (e) {
                $.messager.alert('提示', result);
            }
        }
    );
}
