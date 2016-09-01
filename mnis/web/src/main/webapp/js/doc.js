$(function($) {
	$.ajaxSetup({
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		complete : function(XMLHttpRequest, textStatus) {
			var sessionstatus = XMLHttpRequest
					.getResponseHeader("sessionstatus");
			if (sessionstatus == "timeout") {
				$.messager.alert("超时", "会话超时，请重新登录!", "info", function() {
					top.window.location.reload();
				});
			}
		}
	});
});
function text2Html(str) {
	if (str) {
		return str.replace(/ /g, '&nbsp;').replace(/\n/g, '<br>');
	}
}
function html2Text(html) {
	if (html) {
		return html.replace(/&nbsp;/g, ' ').replace(/<br>/g, '\n');
	}
}
function htmlspecialchars(str) {
	str = str.replace("&", "&amp;");
	str = str.replace("<", "&lt;");
	str = str.replace(">", "&gt;");
	str = str.replace("\"", "&quot;");
	return str;
}

$.extend(true, $.fn.datagrid.defaults.editors, {
    textarea: {
		getValue : function(target) {
			return text2Html($(target).val());
		},
		setValue : function(target, value) {
			$(target).val(html2Text(value));
		}
    }
});
$.extend(true, $.fn.datagrid.defaults.editors, {
	bloodPressure: {
		init: function(container, options){
            var input = $('<input type="text" class="easyui-textbox">/<input type="text" class="easyui-textbox">').appendTo(container);
            options = options || {};
            var target = input.get();
            $(target[0]).textbox(options);
            $(target[2]).textbox(options);
            return target;
        },
		getValue : function(target) {
			if ($(target[0]).val() || $(target[2]).val()) {
				return $(target[0]).val() + '/' + $(target[2]).val();
			}
			return '';
		},
		setValue : function(target, value) {
			if (value) {
				var vals = value.split('/');
				$(target[0]).textbox('setValue', vals[0]);
				$(target[2]).textbox('setValue', vals[1]);
			}
		},
        resize: function(target, width){
        	var fontSize = $(target).css('font-size');
        	var size = parseInt(fontSize.substring(0, fontSize.length - 2));
        	var boxWidth = (width - size/2)/2;
        	$(target[0]).textbox('resize', boxWidth);
        	$(target[2]).textbox('resize', boxWidth);
        }
	},
	timespinner: {
        init: function(container, options){
            var input = $('<input type="text">').appendTo(container);
            options = options || {};
            return input.timespinner(options);
        },
        getValue: function(target){
            return $(target).timespinner('getValue');
        },
        setValue: function(target, value){
            $(target).timespinner('setValue', value);
        },
        resize: function(target, width){
            $(target).timespinner('resize', width);
        }
    }
});
function dateToString(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + "." + (m < 10 ? ("0" + m) : m) + "." + (d < 10 ? ("0" + d) : d);
}
function datetimeToString(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	var h = date.getHours();
	var mi = date.getMinutes();
	return y + "." + (m < 10 ? ("0" + m) : m) + "." + (d < 10 ? ("0" + d) : d)
			+ " " + h + ":" + mi;
}
(function($) {
	// 修改datebox显示格式
	$.fn.datebox.defaults.formatter = function(date) {
		return dateToString(date);
	};
	$.fn.datebox.defaults.parser = function(s) {
		if (!s) {
			return new Date();
		}
		var ss = s.split(".");
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	};
	// change the default config
	$.extend(true, $.fn.combobox.defaults, {
		filter : function(q, row) {
			var opts = $(this).combobox("options");
			return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
		}
	});
})(jQuery);
function formatDate(dateStr, isFirstRow, lastDateStr) {
	if (!dateStr) {
		return '';
	}
	var strs = dateStr.split(' ');
	if (isFirstRow || !lastDateStr) {
		return dateStr;
	}
	if (new Date(dateStr).toDateString() == new Date(lastDateStr).toDateString()) {
		return strs[1];
	}
	return dateStr;
}
function getDate(dateStr, isFirstRow, lastDateStr) {
	if (!dateStr) {
		return '';
	}
	var strs = dateStr.split(' ');
	if (isFirstRow || !lastDateStr) {
		return strs[0];
	}
	if (new Date(dateStr).toDateString() == new Date(lastDateStr).toDateString()) {
		return '';
	}
	return strs[0];
}
function getDate2(dateStr, isFirstRow, lastDateStr) {
	if (!dateStr) {
		return '';
	}
	var strs = dateStr.split(' ');
	if (isFirstRow || !lastDateStr) {
		return strs[0];
	}
	var s = strs[0].split('.');
	var lastStrs = lastDateStr.split(' ');
	var l = lastStrs[0].split('.');
	if (s[0] == l[0] && s[1] == l[1] && s[2] == l[2]) {
		return '';
	}
	if (s[0] == l[0]) {
		return s[1] + '.' + s[2];
	}
	return strs[0];
}
function getTime(dateStr) {
	if (!dateStr) {
		return '';
	}
	var strs = dateStr.split(' ');
	if (strs && strs.length > 1) {
		return strs[1];
	}
	return '';
}

function formatCheck(value) {
	if (value) {
		return '√';
	}
}
/**
 * 设置页码
 */
function setPageNumber() {
	$('span.pageNumber').each(function(index, element) {
		$(this).text(index + 1);
	});
}
// 设置每页的诊断
function setDiagnosis($docHead, diagnosises, date) {
	if (!$docHead || !diagnosises) {
		return;
	}
	var diag = '';
	for (var m = 0; m < diagnosises.length; m++) {
		var diagnosis  = diagnosises[m];
		if (new Date(diagnosis['modifyDate']).getTime() > new Date(date).getTime()) {
			break;
		} else {
			diag = diagnosis['diagnosis'];
		}
	}
//	if (diag) {
		$docHead.find('#pDiagnosis').text(diag);
//	}
}
/**
 * 填充表格文书中的数据。<br>
 * 换科分页，诊断变更不分页；每页只显示第一条记录的时间对应的诊断。
 */
function fillTable(url, cureNo, columnNumber, getRowDatas, isBreakRow) {
	$.ajax({
		url: url,
		type: 'GET',
		data: {
			cureNo: cureNo
		},
    	success: function(result, status) {
    		var $paperContainer = $('.paper-container');
//    		var paperContentHeight = $('.paper-content').height() - 10;
    		var paperContentHeight = $('.paper').height() - $('.page-foot').outerHeight(true);
    		$('.paper-content').height(paperContentHeight);
    		var headHeight = $('.doc-head').outerHeight(true);
    		var commentHeight = $('.doc-comment').outerHeight(true);
    		var contentHeight1 = paperContentHeight - headHeight - commentHeight;
    		var contentHeight2 = paperContentHeight - headHeight;
//    		$('.doc-content').outerHeight(contentHeight1);
    		var $page = $('.paper').clone();
    		$page.find('.doc-comment').remove();
    		var firstPage = true;
    		var $docHead = $('.doc-head');
   			var $docTable = $('.doc-table');
   			var $tbody = $(".doc-table tbody");
   			var diagnosises = result['diagnosises'];
   			var data = result['datas'];
    		if (data && (data instanceof Array)) {
	    		var isFirstRow = true;
	    		var lastDate = undefined;
    			for (var i = 0; i < data.length; i++) {
    				var row = data[i];
    				var $row = $('<tr>');
    				var date = row['date'];
    				// each table doc should provide a function to resolve row's data
    				var rowDatas = getRowDatas(row, isFirstRow, lastDate);
    				for (var j = 0; j < rowDatas.length; j++) {
    					$row.append($('<td>').html(rowDatas[j]));
    				}
    				// append row
    				$tbody.append($row);
    				// 检查和设置每页的诊断为第一条记录对应的诊断
    				if (isFirstRow) {
    					setDiagnosis($docHead, diagnosises, date); // first
    				}
//    				if (i == data.length - 1) {
//    					setDiagnosis($docHead, diagnosises, date); // last
//    				}
    				isFirstRow = false;
    				lastDate = date;
    				// if page is full then create new page
    				if ($docTable.outerHeight(true) > (firstPage ? contentHeight1 : contentHeight2)) {
	    				isFirstRow = true;
	    				// 针对护理记录单的病情里的大段文字进行截取分页显示
	    				if (isBreakRow) {
	    					while ($docTable.outerHeight(true) > (firstPage ? contentHeight1 : contentHeight2)) {
	    						var $record = $row.find("td:eq(23)");
	    						var record = html2Text($record.html());
	    						if (record) {
	    							var at = 0; // 从后面算起的位移数
//	    						$record.html(text2Html(record.substring(0, record.length / 2)));
	    							while ($docTable.outerHeight(true) > (firstPage ? contentHeight1 : contentHeight2) && at < record.length) {
	    								at++;
	    								$record.html(text2Html(record.substring(0, record.length - at)));
	    							}
	    							if ($docTable.outerHeight(true) > (firstPage ? contentHeight1 : contentHeight2)) {
	    								$row.remove();
	    								$row = $('<tr>');
	    								var rowDatas = getRowDatas(row, isFirstRow, lastDate);
	    								for (var j = 0; j < rowDatas.length; j++) {
	    									$row.append($('<td>').html(rowDatas[j]));
	    								}
	    							} else {
	    								var $sign = $row.find("td:eq(24)");
	    								var sign = $sign.html();
	    								$sign.html('');
	    								// 生成空白行，并设置病情列
	    								$row = $('<tr>');
	    								for (var k = 0; k < columnNumber; k++) {
	    									$row.append($('<td>'));
	    								}
	    								$row.find("td:eq(23)").html(text2Html(record.substring(record.length - at, record.length)));
	    								$row.find("td:eq(24)").html(sign);
	    							}
	    						} else {
	    							$row.remove();
	    							$row = $('<tr>');
	    							var rowDatas = getRowDatas(row, isFirstRow, lastDate);
	    							for (var j = 0; j < rowDatas.length; j++) {
	    								$row.append($('<td>').html(rowDatas[j]));
	    							}
	    						}
//	    						setDiagnosis($docHead, diagnosises, date); // last
	    						// break page
	    						$paperContainer.append($('<hr class="break-page"/>'));
	    						// create new page
	    						firstPage = false;
	    						var $newPage = $page.clone();
	    						$paperContainer.append($newPage);
//	    						$newPage.find('.doc-content').outerHeight(contentHeight2);
	    						$docHead = $newPage.find('.doc-head');
	    						setDiagnosis($docHead, diagnosises, date); // first
	    						$docTable = $newPage.find('.doc-table');
	    						$tbody = $docTable.find('tbody');
	    						$tbody.append($row);
	    						isFirstRow = false;
	    					}
	    				} else {
	    					$row.remove();
//    					$row.detach();
	    					$row = $('<tr>');
	    					var rowDatas = getRowDatas(row, isFirstRow, lastDate);
	    					for (var j = 0; j < rowDatas.length; j++) {
	    						$row.append($('<td>').html(rowDatas[j]));
	    					}
//	    					setDiagnosis($docHead, diagnosises, date); // last
	    					// break page
	    					$paperContainer.append($('<hr class="break-page"/>'));
	    					// create new page
	    					firstPage = false;
	    					var $newPage = $page.clone();
	    					$paperContainer.append($newPage);
//	    					$newPage.find('.doc-content').outerHeight(contentHeight2);
	    					$docHead = $newPage.find('.doc-head');
	    					setDiagnosis($docHead, diagnosises, date); // first
	    					$docTable = $newPage.find('.doc-table');
	    					$tbody = $docTable.find('tbody');
	    					$tbody.append($row);
	    					isFirstRow = false;
	    				}
    				}
    			}
    		}
   			var $row = undefined;
    		while ($docTable.outerHeight(true) < (firstPage ? contentHeight1 : contentHeight2)) {
    			$row = $('<tr>');
    			for (var i = 0; i < columnNumber; i++) {
    				$row.append($('<td>'));
    			}
   				$tbody.append($row);
    		}
    		if ($row) {
    			$row.remove();
    		}
    		setPageNumber();
    	},
    	error: function(xhr, status, err) {
    		$.messager.alert('加载数据', '加载数据失败', 'error');
    		setPageNumber();
    	}
	});
}
/**
 * 填充表格文书中的数据,分2栏
 * @param url 获取数据URL
 * @param cureNo 病人治疗号（每次入院不同）
 * @param columnNumber 数据列数
 * @param getRowDatas 格式化数据的函数，function getRowDatas(row, isFirstRow, lastDate)
 */
function fillTable2(url, cureNo, columnNumber, getRowDatas) {
	$.ajax({
		url: url,
		type: 'GET',
		data: {
			cureNo: cureNo
		},
		success: function(result, status) {
			var $paperContainer = $('.paper-container');
//			var paperContentHeight = $('.paper-content').height() - 10;
			var paperContentHeight = $('.paper').height() - $('.page-foot').outerHeight(true);
			$('.paper-content').height(paperContentHeight);
			var headHeight = $('.doc-head').outerHeight(true);
			var commentHeight = $('.doc-comment').outerHeight(true);
			var contentHeight1 = paperContentHeight - headHeight - commentHeight;
			var contentHeight2 = paperContentHeight - headHeight;
//			$('.doc-content').height(contentHeight1);
			var $page = $('.paper').clone();
			$page.find('.doc-comment').remove();
			var firstPage = true;
			var $docContent = $('.doc-content');
			var $docHead = $('.doc-head');
			var $docTable = $('.doc-table');
//			$docTable.css('float', 'left');
//			$docTable.css('width', (100 / columns) + '%');
			var $table = $docTable.clone();
			var $tbody = $(".doc-table tbody");
			var columnIndex = 2;
			var diagnosises = result['diagnosises'];
   			var data = result['datas'];
			if (data && (data instanceof Array)) {
				var isFirstRow = true;
				var lastDate = undefined;
				for (var i = 0; i < data.length; i++) {
					var row = data[i];
					var $row = $('<tr>');
					var date = row['date'];
					// each table doc should provide a function to resolve row's data
					var rowDatas = getRowDatas(row, isFirstRow, lastDate);
					for (var j = 0; j < rowDatas.length; j++) {
						$row.append($('<td>').html(rowDatas[j]));
					}
					// append row
					$tbody.append($row);
					if (isFirstRow) {
    					setDiagnosis($docHead, diagnosises, date); // first
    				}
//					if (i == data.length - 1) {
//    					setDiagnosis($docHead, diagnosises, date); // last
//    				}
					isFirstRow = false;
					// if page is full then create new page
					if ($docTable.outerHeight(true) > (firstPage ? contentHeight1 : contentHeight2)) {
						// create new page
						firstPage = false;
						isFirstRow = true;
     					$row.remove();
//    					$row.detach();
        				$row = $('<tr>');
        				date = row['date'];
    					var rowDatas = getRowDatas(row, isFirstRow, lastDate);
    					for (var j = 0; j < rowDatas.length; j++) {
    						$row.append($('<td>').html(rowDatas[j]));
    					}
						var $lastRow = $docTable.find('tbody tr:last');
						$lastRow.height($lastRow.height() + ((firstPage ? contentHeight1 : contentHeight2) - $docTable.outerHeight(true)));
						columnIndex--;
						if (columnIndex > 0) {
							$docContent = $docTable.parent();
							$docTable = $table.clone();
							$docContent.append($docTable);
						} else {
							columnIndex = 2;
//							setDiagnosis($docHead, diagnosises, date); // last
							// break page
							$paperContainer.append($('<hr class="break-page"/>'));
							var $newPage = $page.clone();
							$paperContainer.append($newPage);
//							$newPage.find('.doc-content').outerHeight(contentHeight2);
							$docHead = $newPage.find('.doc-head');
							setDiagnosis($docHead, diagnosises, date); // first
							$docTable = $newPage.find('.doc-table');
						}
						$tbody = $docTable.find('tbody');
						$tbody.append($row);
						isFirstRow = false;
					}
					lastDate = date;
				}
			}
			while (columnIndex > 0) {
				var $row = undefined;
				while ($docTable.outerHeight(true) < (firstPage ? contentHeight1 : contentHeight2)) {
					$row = $('<tr>');
					for (var i = 0; i < columnNumber; i++) {
						$row.append($('<td>'));
					}
					$tbody.append($row);
				}
				if ($row) {
					$row.remove();
				}
				// 将最后一行的高度加大，以左右栏对齐
				var $lastRow = $docTable.find('tbody tr:last');
				$lastRow.height($lastRow.height() + ((firstPage ? contentHeight1 : contentHeight2) - $docTable.outerHeight(true)));
				// 加栏
				columnIndex--;
				if (columnIndex > 0) {
					$docContent = $docTable.parent();
					$docTable = $table.clone();
					$docContent.append($docTable);
					$tbody = $docTable.find('tbody');
				}
			}
			setPageNumber();
		},
		error: function(xhr, status, err) {
			$.messager.alert('加载数据', '加载数据失败', 'error');
			setPageNumber();
		}
	});
}

/**
 * 初始化文书中的数据
 */
function initDocData(data) {
	if (data) {
		$.each(data, function(index, obj) {
			var name = this['key'];
			var value = this['value'];
			if (value) {
				var $e = $('[name=' + name + ']');
//     				var tagName = $e[0].tagName;
				var type = $e.attr('type');
				if ('radio' == type) {
					$e.each(function(index,element) {
						var $ele = $(element);
						if (value == $ele.val()) {
							$ele.attr('checked', 'checked');
						}
					});
// 					$e.filter('[value=' + value + ']').attr('checked', 'checked'); // another way
				} else if ('checkbox' == type) {
					$e.attr('checked', value);
//     					if ('on' == value) {
// 	    					$e.attr('checked', 'checked');
//     					}
				} else {
					if ('bloodPressure' == name && value) {
						var values = value.split('/');
						$('#bloodPressure1').val(values[0]);
						$('#bloodPressure2').val(values[1]);
					}
					$e.val(value);
				}
			}
		});
	}
}
// Enter 模拟 Tab
function enterSimulateTab($list) {
	$list.each(function(index,target) {
    	$(target).keydown(function(e) {
    		var theEvent = window.event || e;
	    	var code = theEvent.keyCode || theEvent.which;
	    	if (code == 13 && !$(this).is('textarea')) {
	    		theEvent.preventDefault();
	    		var pre = index - 1;
	    		pre = pre < 0 ? 0 : pre;
	    		var last = $list.length - 1;
	    		var next = index + 1;
	    		next = next > last ? last : next;
	    		var idx = theEvent.shiftKey ? pre : next;
	    		var $target = $($list[idx]);
    			$target.focus();
    			var type = $target.attr('type');
    			if (type) {
    				type = type.toLowerCase();
    				if (type != 'checkbox' && type != 'radio') {
    					$target.click();
    				}
    			}
	    	}
    	});
    });
}
jQuery.fn.extend({
	insertAtCaret : function(myValue) {
		return this.each(function(i) {
			if (document.selection) {
				// For browsers like Internet Explorer
				this.focus();
				var sel = document.selection.createRange();
				sel.text = myValue;
				this.focus();
			} else if (this.selectionStart || this.selectionStart == '0') {
				// For browsers like Firefox and Webkit based
				var startPos = this.selectionStart;
				var endPos = this.selectionEnd;
				var scrollTop = this.scrollTop;
				this.value = this.value.substring(0, startPos) + myValue
						+ this.value.substring(endPos, this.value.length);
				this.focus();
				this.selectionStart = startPos + myValue.length;
				this.selectionEnd = startPos + myValue.length;
				this.scrollTop = scrollTop;
			} else {
				this.value += myValue;
				this.focus();
			}
		});
	}
});
function textarea($textarea) {
	var gap = '    ';
	$textarea.each(function(index,target) {
		var $target = $(target);
		if (!$target.val()) {
			$target.val(gap);
		}
		$target.keyup(function(e) {
			if (e.keyCode == 13) {
				$target.insertAtCaret(gap);
			}
		});
	});
}
function autosize() {
	$('textarea').autosize({
		append: '',
		callback: function() {
			$('#dg').datagrid('resize');
		}
	});
}