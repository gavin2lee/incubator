$(function() {
	var editIndex = undefined;
	function onClickCell(index, field) {
		if (field == 'd1') {
			return;
		}
		if (endEditing()) {
			$('#info-tab').datagrid('selectRow', index).datagrid('editCell', {
				index : index,
				field : field
			});
			editIndex = index;
		}
	}
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#info-tab').datagrid('validateRow', editIndex)) {
			$('#info-tab').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	$.extend($.fn.datagrid.methods, {
		editCell : function(jq, param) {
			return jq.each(function() {
				var opts = $(this).datagrid('options');
				var fields = $(this).datagrid('getColumnFields', true).concat(
						$(this).datagrid('getColumnFields'));
				for ( var i = 0; i < fields.length; i++) {
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor1 = col.editor;
					if (fields[i] != param.field) {
						col.editor = null;
					}
				}
				$(this).datagrid('beginEdit', param.index);
				for ( var i = 0; i < fields.length; i++) {
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor = col.editor1;
				}
			});
		}
	});
	_data = {
		"total" : 0,
		"rows" : []
	};
	if (document.body.childNodes[0].nodeType == 3) {
		document.body.removeChild(document.body.childNodes[0]);
	}
	$('.content').height($(window).height() - $('.top-tools').height());
	$("#info-tab").datagrid({
		data : _data,
		fit : true,
		fitColumns : true,
		onClickCell : onClickCell,
		columns : [[{
			field : 'patientBedCode',
			title : '床号',
			width : 100,
			editor : 'text'
		}, {
			field : 'patientName',
			title : '患者',
			width : 100,
			editor : 'text'
		}, {
			field : 'patientId',
			title : '住院号',
			width : 60,
			editor : 'text'
		}, {
			field : 'orderTypeName',
			title : '时间',
			width : 100,
			editor : 'text'
		}, {
			field : 'createDate',
			title : '脉搏',
			width : 100,
			editor : 'text'
		}, {
			field : 'createDoctorName',
			title : '呼吸',
			width : 100,
			editor : 'text'
		}, {
			field : 'presInfo',
			title : '心率',
			width : 100,
			editor : 'text'
		}, {
			field : 'standard',
			title : '血压',
			width : 100,
			editor : 'text'
		}, {
			field : 'dosage',
			title : '血氧饱和度',
			width : 100,
			editor : 'text'
		}, {
			field : 'd1',
			title : '操作',
			width : 100,
			formatter : function(value, row, index) {
				var node = '', p1 = '', p2 = '';
				p1 = '<input type="checkbox" name="a1" style="position:relative;top:2px;">同步到护理记录<br>';
				p2 = '<input type="checkbox" name="a1" style="position:relative;top:2px;">同步到体温单';
				return '<div>' + p1 + p2 + '</div>';
			}
		}]]
	});

	$('.tab-btn').each(function() {
		$(this).click(function() {
			if (!$(this).hasClass('checked')) {
				clearAllChecked();
				$(this).addClass('checked');

			}
		});
	});
	function clearAllChecked() {
		$('.tab-btn').each(function() {
			if ($(this).hasClass('checked')) {
				$(this).removeClass('checked');
			}
		});
	}
	function showItemsEdit() {
		$("#itemsEdit").show();
	}
	function closeItemsEdit() {
		$("#itemsEdit").hide();
	}
});