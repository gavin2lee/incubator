    
/**
* @author yanhui.wang
* 使panel和datagrid在加载时提示
*/
$.fn.panel.defaults.loadingMessage = ' ';
$.fn.datagrid.defaults.loadMsg = ' ';

function checkIsPassVerify(key){
    if(key){
        var node = $('#'+key);
        //判断是否是下拉选择
        if( $("#info-tab").length == 0){
            var type = node.find('option:selected').val();
        }
        else{
            var type = $("#info-tab").datagrid('getSelected')[key];
        }
        
        if('bs,out,qj,jc,ts,cbc,dn,sj,rggm,wc,zxpb0,gch1,gch2,gch3'.indexOf(type) >= 0 && type!='zxpb'){ return true; }
    }
    return false;
}

$(function() {
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        complete: function (XMLHttpRequest, textStatus) {
            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
            if (sessionstatus == "timeout") {
               /* if(self != top){
                    $.messager.alert("超时", "会话超时，请重新登录!", "info", function () {
                        parent.window.location.reload();
                    });
                }
                else{
                    $.messager.alert("超时", "会话超时，请重新登录!", "info", function () {
                        top.window.location.reload();
                    });
                }*/
                $.messager.alert("超时", "会话超时，请重新登录!", "info", function () {
                    top.window.location.reload();
                });
            }
        }
    });
});

/**
* panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
*/
$.fn.panel.defaults.onBeforeDestroy = function () {
    var frame = $('iframe', this);
    try {
        if (frame.length > 0) {
            for (var i = 0; i < frame.length; i++) {
                frame[i].contentWindow.document.write('');
                frame[i].contentWindow.close();
            }
            frame.remove();
            if ($.browser.msie) {
                CollectGarbage();
            }
        }
    } catch (e) {
    }
};

/**
* 创建一个模式化的dialog
*/
$.modalDialog = function (options) {
    var opts = $.extend({
        title: '',
        width: 840,
        height: 680,
        modal: true,
        onClose: function () {
            $(this).dialog('destroy');
        }
    }, options);
    opts.modal = true;
    return $.modalDialog.handler = $('<div/>').dialog(opts);
};

/**
* 
* 扩展validatebox，
*/
$.extend($.fn.validatebox.defaults.rules, {
    //添加验证两次密码功能
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '输入内容前后不一致！'
    },
    minLength: {    
        validator: function(value, param){    
            return value.length >= param[0];    
        },    
        message: 'Please enter at least {0} characters.'   
    },
    specialNum:{
        validator:function(value, param){
            if(checkIsPassVerify(param[0])){return true;}
            var reg = '';
            switch(param[0]){
                case 'weightType':
                    reg = /^\d+\.?\d*$/;
                    break;
                case 'heightType':
                    reg = /^\d+\.?\d*$/;
                    break;
                default:
                    break;
            }
            if(reg.test(value)){
                if(param[0] == 'heightType'){
                    $.fn.validatebox.defaults.rules.specialNum.message = '身高范围是1~300';
                    if(parseFloat(value) >= 1 && parseFloat(value) <= 300){
                        return true;
                    }
                    return false;
                }
                if(param[0] == 'weightType'){
                    $.fn.validatebox.defaults.rules.specialNum.message = '体重范围是1~1000';
                    if(parseFloat(value) >= 1 && parseFloat(value) <= 1000){
                        return true;
                    }
                    return false;
                }
            }
            return false; 
        },
        message:'请输入数字'
    },
    remark:{
        validator:function(value,param){

            if(ay.getStrActualLen(value) > 50){
                $.fn.validatebox.defaults.rules.remark.message = '其他项长度不能超过50个字';
                return false;
            }
            return true;
        }
    },
    isNumber:{
        validator:function(value, param){
            if(checkIsPassVerify(param[0])){ return true;}
            if((param[0] == 'pulseType' || param[0] == 'heartRateType') && /^\d+$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '脉搏/心率 范围是0~180';
                if(parseInt(value) >= 0 && parseInt(value) <= 180){
                    return true;
                }
                
                return false;
            }
            if(param[0] == 'breathType' && /^\d+$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '呼吸范围是0~99';
                if(parseInt(value) >= 0 && parseInt(value) <= 99){
                    return true;
                }
                return false;
            }
            if(param[0] == 'otherOutputType' && /^\d+(\.\d+)?$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '其他出量范围是0~15000'; 
                if(parseInt(value) >= 0 && parseInt(value) <= 15000){
                    return true;
                }
                return false;
            }
            if(param[0] == 'totalInputType' && /^\d+(\.\d+)?$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '总入量范围是0~15000';
                if(parseInt(value) >= 0 && parseInt(value) <= 15000){
                    return true;
                }
                return false;
            }
            if(param[0] == 'urineType' && /^\d+(\.\d+)?$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '尿量范围是0~15000';
                if(parseInt(value) >= 0 && parseInt(value) <= 15000){
                    return true;
                }
                return false;
            }
            if(param[0] == 'stoolType' && /^\d+(\.\d+)?$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '大便次数是0~1000';
                if(parseInt(value) >= 0 && parseInt(value) <= 1000){
                    return true;
                }
                return false;
            }
            if(param[0] == 'abdominalCirType' && /^\d+(\.\d+)?$/.test(value)){
                $.fn.validatebox.defaults.rules.isNumber.message = '腹围范围是1~200';
                if(parseInt(value) >= 1 && parseInt(value) <= 200){
                    return true;
                }
                return false;
            }
            if(param[0] == 'oxygenSaturationType' && /^\d+(\.\d+)?$/.test(value)){
                return true;
            }
            return false; 
        },
        message:'请输入数字'
    },
    skinTest:{
        validator:function(value, param){
            console.log(1);
        },
        message:'请输入34~42范围内的温度'
    },
    cooledTemp:{
        validator:function(value, param){
            var row = $("#info-tab").datagrid('getSelected');
            var index = $("#info-tab").datagrid('getRowIndex',row);

            //若未录入体温提示错误
            //暂不强制该情况
            /*$.fn.validatebox.defaults.rules.cooledTemp.message = '体温未录入，请先输入体温';
            if( row.temperature && row.temperatureType ){
                if( 'yw,kw,gw'.indexOf(row.temperatureType) < 0){
                     $.fn.validatebox.defaults.rules.cooledTemp.message = '不允许录入降温后体温';
                    return false;
                }
                if( row.temperature < 35 || row.temperature > 42 ){
                    return false;
                }
            }
            else{
                return false;
            }*/

            if(/^[0-9]+\.?[0-9]*$/.test(value)){
                var temp1 = parseInt(param[0]);
                var temp2 = parseInt(param[1]);
                if( (temp1 <= value && value <= temp2)){
                    return true;
                }
            }
            $.fn.validatebox.defaults.rules.cooledTemp.message = '请输入35~42范围内的温度';
            return false; 
        }
    },
    numRange:{
        validator:function(value, param){
            var that = this,
                thisId = $(that).attr('id');
            if(checkIsPassVerify(param[0])){ return true;}
            if(/^[0-9]+\/{1}[0-9]+$/.test(value) && (param[0] =='bloodPressType' || param[0] =='bloodPress1Type' || param[0] =='bloodPress2Type')){
                var arr = value.split('/');
                $.fn.validatebox.defaults.rules.numRange.message = '血压范围值:0~300';
                if(parseInt(arr[0]) >= 0 && parseInt(arr[0]) <= 300 && parseInt(arr[1]) >= 0 && parseInt(arr[1]) <= 300){
                    return true;
                }
                return false;
            }
            else if(param[0] =='bloodPressType' || param[0] =='bloodPress1Type' || param[0] =='bloodPress2Type'){
                $.fn.validatebox.defaults.rules.numRange.message = '血压格式不正确';
                if( /^\d+$/.test(value) &&  value > 30){
                    
                    if($('#info-tab').length == 0){
                        var node = $("#" + thisId);
                        node.val(value+'/');

                    }
                    else{
                    	if(param[0] =='bloodPressType'){
                    		ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'bloodPress'});
                    	}else if(param[0] =='bloodPressType1'){
                    		ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'bloodPress1'});
                    	}else if(param[0] =='bloodPressType2'){
                    		ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'bloodPress2'});
                    	}
                        $(ed.target).val(value+'/');    
                    }
                    
                    
                }
                return false;
            }
            $.fn.validatebox.defaults.rules.numRange.message = '请输入数字';
            if(/[0-9]+\.?[0-9]*/.test(value)){
                var temp1 = parseInt(param[1]);
                var temp2 = parseInt(param[2]);
                if(temp1 <= value && value <= temp2){
                    return true;
                }
                $.fn.validatebox.defaults.rules.numRange.message = '温度范围35~42';
                return false;
            }
            return false; 
        },
        message:''
    }
});


/**
* 防止panel/window/dialog组件超出浏览器边界
* @param left
* @param top
*/
var easyuiPanelOnMove = function (left, top) {
    var l = left;
    var t = top;
    if (l < 1) {
        l = 1;
    }
    if (t < 1) {
        t = 1;
    }
    var width = parseInt($(this).parent().css('width')) + 14;
    var height = parseInt($(this).parent().css('height')) + 14;
    var right = l + width;
    var buttom = t + height;
    var browserWidth = $(window).width();
    var browserHeight = $(window).height();
    if (right > browserWidth) {
        l = browserWidth - width;
    }
    if (buttom > browserHeight) {
        t = browserHeight - height;
    }
    $(this).parent().css({/* 修正面板位置 */
        left: l,
        top: t
    });
};

$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
* 扩展treegrid，使其支持平滑数据格式
*/
$.fn.treegrid.defaults.loadFilter = function (data, parentId) {
    var opt = $(this).data().treegrid.options;
    var idField, treeField, parentField;
    if (opt.parentField) {
        idField = opt.idField || 'id';
        treeField = opt.treeField || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [], tmpMap = [];
        for (i = 0, l = data.length; i < l; i++) {
            tmpMap[data[i][idField]] = data[i];
        }
        for (i = 0, l = data.length; i < l; i++) {
            if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children'])
                    tmpMap[data[i][parentField]]['children'] = [];
                data[i]['text'] = data[i][treeField];
                tmpMap[data[i][parentField]]['children'].push(data[i]);
            } else {
                data[i]['text'] = data[i][treeField];
                treeData.push(data[i]);
            }
        }
        return treeData;
    }
    return data;
};

/**
* 扩展combotree，使其支持平滑数据格式
*/
$.fn.combotree.defaults.loadFilter = function (data, parent) {
    var opt = $(this).data().tree.options;
    var idFiled, textField, parentField;
    if (opt.parentField) {
        idField = opt.idFiled || 'id';
        textField = opt.textField || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [], tmpMap = [];

        for (i = 0, l = data.length; i < l; i++) {
            tmpMap[data[i][idField]] = data[i];
        }
        for (i = 0, l = data.length; i < l; i++) {
            if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children'])
                    tmpMap[data[i][parentField]]['children'] = [];
                data[i]['text'] = data[i][textField];
                tmpMap[data[i][parentField]]['children'].push(data[i]);
            } else {
                data[i]['text'] = data[i][textField];
                treeData.push(data[i]);
            }
        }
        return treeData;
    }
    return data;
};

$.fn.tree.defaults.loadFilter = $.fn.combotree.defaults.loadFilter;

/**
* 显示当前系统时间
*/
$.fn.extend({
    showtime: function () {
        var today = new Date();
        var y = today.getFullYear();
        var m = today.getMonth() + 1;
        var d = today.getDate();
        var h = today.getHours();
        var ms = today.getMinutes();
        var s = today.getSeconds();
        if (ms.toString().length == 1) ms = "0" + ms.toString(); else ms = ms.toString();
        if (s.toString().length == 1) s = "0" + s.toString(); else s = s.toString();
        $('#time').html(h + ':' + ms + ':' + s);
        $('#date').html(y + '/' + m + '/' + d);
        setTimeout('$(this).showtime()', 1000);
    }
});


/**
* 通用错误提示,用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
*/
var easyuiErrorFunction = function (XMLHttpRequest) {
    $.messager.progress('close');
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;

$.extend($.fn.datagrid.methods, {
    /**
    * 开打提示功能    
    * @param {} jq    
    * @param {} params 提示消息框的样式    
    * @return {}    
    */
    doCellTip: function (jq, params) {
        function showTip(showParams, td, e, dg) {
            //无文本，不提示。
            if($(td).find('select').length >0 ) return ;      
            if ($(td).text() == "") return;
            params = params || {};
            var options = dg.data('datagrid');
            $(td).tooltip('destroy');
            showParams.content = '<div class="tipcontent">' + ay.replaceHtmlTag(showParams.content) + '</div>';
            showParams.tipStyler = { 'filter': 'progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffffff,endColorstr=#e4e5f0,enabled=true,gradientType="1")', 'background-image': '-ms-linear-gradient(top,#ffffff,#e4e5f0)', 'background-image': '-webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#e4e5f0))', 'box-shadow': '5px 5px 3px #888888', '-moz-box-shadow': '5px 5px 3px #888888', 'color': '#575757', 'border-color': '#767676', 'max-width': '600px', 'overflow': 'hidden', 'word-break': 'break-word', 'word-wrap': 'break-word', 'white-space': '-moz-pre-wrap', 'white-space': '-hp-pre-wrap', 'white-space': '-o-pre-wrap', 'white-space': '-pre-wrap', 'white-space': 'pre', 'white-space': 'pre-wrap', 'white-space': 'pre-line' };
            $(td).tooltip({
                content: showParams.content,
                trackMouse: false,
                position: params.position,
                onHide: function () {
                    $(this).tooltip('destroy');
                },
                onUpdate: function () {
                    var tip = $(this).tooltip('tip');
                    if (showParams.tipStyler) {
                        tip.css(showParams.tipStyler);
                    }
                    if (showParams.contentStyler) {
                        tip.find('div.tipcontent').css(showParams.contentStyler);
                    }
                },
                onShow: function () {
                    var tip = $(this).tooltip('tip');
                    if (showParams.tipStyler) {
                        tip.css(showParams.tipStyler);
                    }
                    if (showParams.contentStyler) {
                        tip.find('div.tipcontent').css(showParams.contentStyler);
                    }
                }
            }).tooltip('show');

        };
        return jq.each(function () {
            var grid = $(this);
            var options = $(this).data('datagrid');
            if (!options.tooltip) {
                var panel = grid.datagrid('getPanel').panel('panel');
                panel.find('.datagrid-body').each(function () {
                    var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;
                    $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td[field]', {
                        'mouseover': function (e) {
                            //if($(this).attr('field')===undefined) return;      
                            var that = this;
                            var setField = null;
                            if (params.specialShowFields && params.specialShowFields.sort) {
                                for (var i = 0; i < params.specialShowFields.length; i++) {
                                    if (params.specialShowFields[i].field == $(this).attr('field')) {
                                        setField = params.specialShowFields[i];
                                    }
                                }
                            }
                            if (setField == null) {
                                if ($('#clonedTipContent')) {
                                    $('#clonedTipContent').remove();
                                }
                                options.factContent = $(this).find('>div').clone().attr('id', 'clonedTipContent').css({ 'margin-left': '-5000px', 'width': 'auto', 'display': 'inline', 'position': 'absolute' }).appendTo('body');
                                var balanceWidth = parseInt($(this).find('>div').css("padding-right").replaceAll("px", "")) + parseInt($(this).find('>div').css("padding-left").replaceAll("px", "")) + parseInt($(this).find('>div').css("margin-left").replaceAll("px", ""));
                                var factContentWidth = options.factContent.width() + balanceWidth;
                                params.content = $(this).text();
                                if (params.onlyShowInterrupt) {
                                    if (factContentWidth > $(this).width()) {
                                        showTip(params, this, e, grid);
                                    }
                                } else {
                                    showTip(params, this, e, grid);
                                }
                            } else {
                                panel.find('.datagrid-body').each(function () {
                                    var trs = $(this).find('tr[datagrid-row-index="' + $(that).parent().attr('datagrid-row-index') + '"]');
                                    trs.each(function () {
                                        var td = $(this).find('> td[field="' + setField.showField + '"]');
                                        if (td.length) {
                                            params.content = td.text();
                                        }
                                    });
                                });
                                showTip(params, this, e, grid);
                            }
                        },
                        'mouseout': function (e) {
                            if (options.factContent) {
                                options.factContent.remove();
                                options.factContent = null;
                            }
                        }
                    });
                });
            }
        });
    },
    /**
    * 关闭消息提示功能    
    * @param {} jq    
    * @return {}    
    */
    cancelCellTip: function (jq) {
        return jq.each(function () {
            var data = $(this).data('datagrid');
            if (data.factContent) {
                data.factContent.remove();
                data.factContent = null;
            }
            var panel = $(this).datagrid('getPanel').panel('panel');
            panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')
        });
    }
});

//扩展datagrid view,支持html标签显示
$.extend($.fn.datagrid.defaults.view, {
    renderRow: function (target, fields, frozen, rowIndex, rowData) {
        var opts = $.data(target, "datagrid").options;
        var cc = [];
        if (frozen && opts.rownumbers) {
            var rownumber = rowIndex + 1;
            if (opts.pagination) {
                rownumber += (opts.pageNumber - 1) * opts.pageSize;
            }
            cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rownumber + "</div></td>");
        }
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var col = $(target).datagrid("getColumnOption", field);
            if (col) {
                var value = jQuery.proxy(function () { try { return eval('this.' + field); } catch (e) { return ""; } }, rowData)();
                var styleValue = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";
                var style = col.hidden ? "style=\"display:none;" + styleValue + "\"" : (styleValue ? "style=\"" + styleValue + "\"" : "");
                cc.push("<td field=\"" + field + "\" " + style + ">");
                if (col.checkbox) {
                    var style = "";
                } else {
                    var style = styleValue;
                    if (col.align) {
                        style += ";text-align:" + col.align + ";";
                    }
                    if (!opts.nowrap) {
                        style += ";white-space:normal;height:auto;";
                    } else {
                        if (opts.autoRowHeight) {
                            style += ";height:auto;";
                        }
                    }
                }
                cc.push("<div style=\"" + style + "\" ");
                if (col.checkbox) {
                    cc.push("class=\"datagrid-cell-check ");
                } else {
                    cc.push("class=\"datagrid-cell " + col.cellClass);
                }
                cc.push("\">");
                if (col.checkbox) {
                    cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>");
                } else {
                	// fulai.zeng remove for running error
                    //value = replaceHtmlTag(value);
                    if (col.formatter) {
                        cc.push(col.formatter(value, rowData, rowIndex));
                    } else {
                        cc.push(value);
                    }
                }
                cc.push("</div>");
                cc.push("</td>");
            }
        }
        return cc.join("");
    }, onAfterRender: function (target) {
        $(target).datagrid('doCellTip', {
            onlyShowInterrupt: true,
            position: 'bottom'
        });
    }
});

$.extend($.fn.treegrid.defaults.view, {
    renderRow: function (target, fields, frozen, rowIndex, rowData) {
        var ops = $.data(target, "treegrid").options;
        var cc = [];
        if (frozen && ops.rownumbers) {
            cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
        }
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var col = $(target).datagrid("getColumnOption", field);
            if (col) {
                var styleValue = col.styler ? (col.styler(rowData[field], rowData) || "") : "";
                var style = col.hidden ? "style=\"display:none;" + styleValue + "\"" : (styleValue ? "style=\"" + styleValue + "\"" : "");
                cc.push("<td field=\"" + field + "\" " + style + ">");
                if (col.checkbox) {
                    var style = "";
                } else {
                    var style = "";
                    if (col.align) {
                        style += "text-align:" + col.align + ";";
                    }
                    if (!ops.nowrap) {
                        style += "white-space:normal;height:auto;";
                    } else {
                        if (ops.autoRowHeight) {
                            style += "height:auto;";
                        }
                    }
                }
                cc.push("<div style=\"" + style + "\" ");
                if (col.checkbox) {
                    cc.push("class=\"datagrid-cell-check ");
                } else {
                    cc.push("class=\"datagrid-cell " + col.cellClass);
                }
                cc.push("\">");
                if (col.checkbox) {
                    if (rowData.checked) {
                        cc.push("<input type=\"checkbox\" checked=\"checked\"");
                    } else {
                        cc.push("<input type=\"checkbox\"");
                    }
                    cc.push(" name=\"" + field + "\" value=\"" + (rowData[field] != undefined ? rowData[field] : "") + "\"/>");
                } else {
                    var val = null;
                    if (col.formatter) {
                        val = col.formatter(ay.replaceHtmlTag(rowData[field]), rowData);
                    } else {
                        val = ay.replaceHtmlTag(rowData[field]);
                    }
                    if (field == ops.treeField) {
                        for (var j = 0; j < rowIndex; j++) {
                            cc.push("<span class=\"tree-indent\"></span>");
                        }
                        if (rowData.state == "closed") {
                            cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
                            cc.push("<span class=\"tree-icon tree-folder " + (rowData.iconCls ? rowData.iconCls : "") + "\"></span>");
                        } else {
                            if (rowData.children && rowData.children.length) {
                                cc.push("<span class=\"tree-hit tree-expanded\"></span>");
                                cc.push("<span class=\"tree-icon tree-folder tree-folder-open " + (rowData.iconCls ? rowData.iconCls : "") + "\"></span>");
                            } else {
                                cc.push("<span class=\"tree-indent\"></span>");
                                cc.push("<span class=\"tree-icon tree-file " + (rowData.iconCls ? rowData.iconCls : "") + "\"></span>");
                            }
                        }
                        cc.push("<span class=\"tree-title\">" + val + "</span>");
                    } else {
                        cc.push(val);
                    }
                }
                cc.push("</div>");
                cc.push("</td>");
            }
        }
        return cc.join("");
    }, onAfterRender: function (target) {
        $(target).datagrid('doCellTip', {
            onlyShowInterrupt: true,
            position: 'bottom'
        });
    }
});
/*
* client Pagination
*
*/
(function($){
function pagerFilter(data){
    if ($.isArray(data)){    // is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var state = dg.data('datagrid');
    var opts = dg.datagrid('options');
    if (!state.allRows){
        state.allRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = $.extend(true,[],state.allRows.slice(start, end));
    return data;
}

var loadDataMethod = $.fn.datagrid.methods.loadData;
$.extend($.fn.datagrid.methods, {
    clientPaging: function(jq){
        return jq.each(function(){
            var dg = $(this);
            var state = dg.data('datagrid');
            var opts = state.options;
            opts.loadFilter = pagerFilter;
            var onBeforeLoad = opts.onBeforeLoad;
            opts.onBeforeLoad = function(param){
                state.allRows = null;
                return onBeforeLoad.call(this, param);
            }
            dg.datagrid('getPager').pagination({
                onSelectPage:function(pageNum, pageSize){
                    opts.pageNumber = pageNum;
                    opts.pageSize = pageSize;
                    $(this).pagination('refresh',{
                        pageNumber:pageNum,
                        pageSize:pageSize
                    });
                    dg.datagrid('loadData',state.allRows);
                }
            });
            $(this).datagrid('loadData', state.data);
            if (opts.url){
                $(this).datagrid('reload');
            }
        });
    },
    loadData: function(jq, data){
        jq.each(function(){
            $(this).data('datagrid').allRows = null;
        });
        return loadDataMethod.call($.fn.datagrid.methods, jq, data);
    },
    getAllRows: function(jq){
        return jq.data('datagrid').allRows;
    }
})
})(jQuery);