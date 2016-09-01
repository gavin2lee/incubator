var params = {
    bbw: 6,  //border-bottom-width
    row1: 1,
    row2: 1.4,
    row3: 2.8
};

//不允许输入的字符
notAllowSymbol = ['&', '*', '\''];

var deptCode;
var cacheData = {};
var cacheDate = null;
var st = null;
var t = 20000;
var textAreaArr = [
    '24hinout',
    '24hurine',
    'abdomaen',
    'weight',
    'bodysigh',
    'cinnamon',
    'other',
    'important',
    'message',
    'exchange_content',
    'hypoxemia_tid',
    'BP_tid',
    'pulverization_bid',
    'pulverization_qd',
    'pulverization_tid',
    'hypoxemia_bid'
];
function save() {
    var v = $("#inputBox").val();
    $("#inputBox").parent().html(v);
}

function setSize() {
    //获取基础参数
    deptCode = $("#deptCode").val();
    var dw = $(document).width();
    var dh = $(document).height();
    var abbw = params.bbw * 12;
    var rowNumCount = 0;
    var rowNum = 0;
    var basicHeight = 0;
    $("#wrapper").height(dh - params.bbw - 10).width(dw - (params.bbw * 2) - 10);
    $("tr[class*='row-']").each(function () {
        var cn = $(this).attr('class');
        var x2 = cn.length;
        var x3 = 1;
        rowNum += 1;
        if (cn.indexOf('rowspan') > 0) {
            x2 = cn.indexOf('rowspan') - 1;
            x3 = parseInt(cn.substring(cn.indexOf('rowspan') + 7, cn.length));
        }
        var rowHeight = cn.substring(cn.indexOf('row-') + 4, x2);
        rowNumCount += params['row' + rowHeight] * x3;
    });
    basicHeight = (dh - rowNum * params.bbw) / rowNumCount;

    $("tr[class*='row-']").each(function () {
        var cn = $(this).attr('class');
        var x2 = cn.length;
        var x3 = 1;
        rowNum += 1;
        if (cn.indexOf('rowspan') > 0) {
            x2 = cn.indexOf('rowspan') - 1;
            x3 = parseInt(cn.substring(cn.indexOf('rowspan') + 7, cn.length));
        }
        var rowHeight = cn.substring(cn.indexOf('row-') + 4, x2);
        $(this).css({
            "height": basicHeight * params['row' + rowHeight] * x3
        });
    });

    $(".editor_1 > div").each(function () {
        var $this = $(this),
            titleSpan = $this.find('.title-text'),
            msgBox = $this.children('.msg-text'),
            wrapperWidth = $this.outerWidth(),
            titleWidth = titleSpan.outerWidth();
        msgBox.css('line-height', '120%');
        titleSpan.css({
            "line-height": $(this).height() + 'px',
            "padding-left": "5px"
        });
        if (textAreaArr.inArray(titleSpan.attr('id')) >= 0) {
            msgBox.css({
                "width": wrapperWidth - titleWidth - 8,
                "height": $(this).height(),
                //"line-height": $(this).css('fontSize') + 2 + 'px'
            });
        }
        else if (titleSpan.length == 2) {
            msgBox.css({
                "width": wrapperWidth - titleWidth - 8,
                "height": $(this).height(),
                //"line-height": $(this).height() / 2 + 'px'
            });
        }
        else if ($(this).attr('id') == 'yw') {
            msgBox.css({
                "width": wrapperWidth - titleWidth - 5,
                "height": $(this).height(),
                //"line-height": $(this).height() + 'px'
            });
            titleSpan.css({
                "height": $(this).height() / 2 - 2
            })
        }
        else if ($(this).attr('id') == 'day_nurse' || $(this).attr('id') == 'work_nurse') {
            msgBox.css({
                "width": wrapperWidth - titleWidth - 8,
                "height": $(this).height(),
                //"line-height": $(this).height() + 'px'
            });
            $(this).find("li").each(function () {
                msgBox.css({
                    "width": wrapperWidth - titleWidth - 5,
                    "height": $(this).height(),
                    //"line-height": $(this).height() + 'px'
                });
            });
        }
        else if ($(this).attr('class') == 'areaInput') {
            msgBox.css({
                "width": wrapperWidth - titleWidth - 8,
                "height": $(this).height()

            });
        }
        else {
            $(this).children('div').css({
                "width": wrapperWidth - titleWidth - 8,
                "height": $(this).height(),
                //"line-height": $(this).height() + 'px'
            });
        }
    });

    $(".editor_2 > div").each(function () {
        $(this).children('div').height($(this).height() - $(this).children('span').height() - 10);
    });

    getNoteMsg();

    var inputState = false;
    $(".msg-text > span").on('dblclick', function (event) {

        if (inputState) {
            return;
        }

        //是否可编辑
        if ($(this).attr('data-editor') == 'false') {
            return;
        }

        //获取span中的数据
        var str = $(this).attr('data-value') || '';

        //对数据进行处理转成textarea的数据

        str = transformData({start: 'sp', end: 'ta', data: str});

        /*str = str.replace(/[\<br\>]/g,"\n");
         str = str.replace(/\&lt\;/g,"<");
         str = str.replace(/\&gt\;/g,">");
         str = str.replace(/\&nbsp\;/g," ");*/
        /*str = str.split('<br>');
         if(str.length == 0){ str = str.substring(41,str.length-1); }
         for(var i=0;i<length;i++){
         str[i] = str[i].substring(41,str[i].length-1);
         }*/

        if (textAreaArr.inArray($(this).attr('id')) >= 0) {

        }
        else {
            $(this).css('line-height', '160%');
        }

        if ($(this).attr('id') == 'abdomaen' || $(this).attr('id') == 'weight') {
//			var nodeHtml = '<input id="inputBox" class="inputBox" type="text" style="width:'+($(this).parent().width()-5)+'px;height:'+($(this).parent().height()*0.5)+'px;"/>';
            var nodeHtml = '<textarea id="inputBox" class="inputBox" type="text" style="width:' + ($(this).parent().width() - 8) + 'px;height:' + ($(this).parent().height() * 0.5 - 8) + 'px;padding:3px;"></textarea>';
        }
        else if (textAreaArr.inArray($(this).attr('id')) >= 0) {
            var nodeHtml = '<textarea id="inputBox" class="inputBox" type="text" style="width:' + ($(this).parent().width() - 12) + 'px;height:' + ($(this).parent().height() - 12) + 'px;margin:3px 0 0 3px;"></textarea>';
        }
        else {
            var nodeHtml = '<input id="inputBox" class="inputBox" type="text" style="width:' + ($(this).parent().width() - 5) + 'px;height:' + ($(this).parent().height() * 0.98) + 'px;"/>';
        }

        $(this).html(nodeHtml);
        var tempId = $(this).attr('id');
        if (tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient') {
            str = str.split('\u3000')[0];
        }
        $(this).find("input[type='text']").focus().val(str);
        $(this).find("textarea").focus().val(str);
        inputState = true;

        //监听输入事件
        $("#inputBox").keyup(function () {

            var v = $(this).val(),
                total = 0;
            for (var i = 0; i < v.length; i++) {
                if (v.charCodeAt(i) && v.charCodeAt(i) < 128) {
                    total += 1;
                }
                else {
                    total += 2;
                }
            }

            console.log(total);

        });

        if (st) {
            clearTimeout(st);
            st = null;
        }
    });
    $(window).on('keypress', function (event) {
        if (event.keyCode == 13 && inputState) {
            if ($("#inputBox")[0].tagName == 'TEXTAREA') {
                return;
            }
            var v = $("#inputBox").val();
            v = v.replace(/\</g, "&lt;");
            v = v.replace(/\>/g, "&gt;");
            v = v.replace(/\n/g, "<br>");
            v = v.replaceAll(" ", "&nbsp;");
            v = v.replace(/\"|\”/g, "''");
            var id = $("#inputBox").parent().attr('id');
            var recordDay = new Date().format("yyyy-MM-dd");
            whiteBoardRecord = '{"deptId":"' + deptCode + '","itemCode":"' + id + '","itemValue":"' + v + '","showDate":"' + recordDay + '"}';
            $.post(ay.contextPath + '/nur/task/white/add.do', {whiteBoardRecord: whiteBoardRecord}, function (data) {
                try {
                    var tempId = $("#inputBox").parent().attr('id');
                    if (tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient') {
                        v += cacheData[tempId];
                    }
                    $("#inputBox").parent().attr('data-value', decodeURIComponent(v));

                    $("#inputBox").parent().html(v);
                    inputState = false;
                    if (!st) {
                        st = setTimeout(getNoteMsg, t);
                    }
                }
                catch (e) {
                    return;
                }
            });
        }
    });
    $(window).on('click', function (event) {
        var tagId = $(event.target).attr('id');
        if (inputState && tagId != 'inputBox') {
            var v = $("#inputBox").val();

            //v = transformData({start:''});

            v = v.replace(/\</g, "&lt;");
            v = v.replace(/\>/g, "&gt;");
            v = v.replace(/\n/g, "<br>");
            v = v.replaceAll(" ", "&nbsp;");
            v = v.replace(/\"|\”/g, "''");

            v = encodeURIComponent(v);
            var id = $("#inputBox").parent().attr('id');
            var recordDay = new Date().format("yyyy-MM-dd");
            whiteBoardRecord = '{"deptId":"' + deptCode + '","itemCode":"' + id + '","itemValue":"' + v + '","showDate":"' + recordDay + '"}';
            $.post(ay.contextPath + '/nur/task/white/add.do', {whiteBoardRecord: whiteBoardRecord}, function (data) {
                try {
                    var tempId = $("#inputBox").parent().attr('id');
                    if (tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient') {
                        v += cacheData[tempId];
                    }
                    $("#inputBox").parent().attr('data-value', decodeURIComponent(v));
                    $("#inputBox").parent().html(decodeURIComponent(v));
                    inputState = false;
                    if (!st) {
                        st = setTimeout(getNoteMsg, t);
                    }
                }
                catch (e) {
                    return;
                }
            });
        }
    });
}

/*
 *   转换数据
 *   显示，保存与编辑的三种状态数据转换
 *   obj{start,end,data}
 *   span -> textarea
 *   span -> db
 *   textarea -> span
 *   db   -> span
 * */
function transformData(obj) {

    //错误格式退出
    if (!obj || !obj.start || !obj.end || !obj.data) {
        return obj.data;
    }
    var data = obj.data;

    if (obj.start == 'sp' && obj.end == 'ta') {

        data = data.replace(/\<br\>/g, "\n");
        data = data.replace(/\&lt\;/g, "<");
        data = data.replace(/\&gt\;/g, ">");
        data = data.replace(/\&nbsp\;/g, " ");

        return data;
    }

    if (obj.start == 'ta' && obj.end == 'sp') {

    }

    if (obj.start == 'db' && obj.end == 'sp') {

    }

    if (obj.start == 'ta' && obj.end == 'db') {

    }
}

function getNoteMsg() {
    //得到数据
    var currDate = new Date().format("yyyy-MM-dd");
    if (!cacheDate) {
        cacheDate = currDate;
    }
    if (cacheDate != currDate) {
        $(".msg-text span").each(function () {
            if ($(this).attr('id') != 'date') {
                $(this).html('');
            }
        });
        cacheDate = currDate;
    }
    $("#date").html(currDate);

    $.get(ay.contextPath + '/nur/task/white/list.do?deptId=' + deptCode + '&showDate=' + currDate, function (data) {
        try {
            var lst = data.lst;
            for (var i = 0; i < lst.length; i++) {
                $("#" + lst[i].itemCode).html("");
                if (( lst[i].itemCode == 'new_patient' || lst[i].itemCode == 'crisis_patient' || lst[i].itemCode == 'hard_patient' || lst[i].itemCode == 'out_patient'  ) && lst[i].itemValue) {
                    var splitArr = lst[i].itemValue.split('\u3000');
                    cacheData[lst[i].itemCode] = '\u3000' + splitArr[1];
                    $("#" + lst[i].itemCode).attr('data-value', lst[i].itemValue);
                    $("#" + lst[i].itemCode).html(lst[i].itemValue);
                }
                else {
                    $("#" + lst[i].itemCode).attr('data-value', lst[i].itemValue);
                    $("#" + lst[i].itemCode).html(lst[i].itemValue);

                }
            }
            st = setTimeout(getNoteMsg, t);
        }
        catch (e) {
            $.messager.alert('提示', '获取出错' + e);
            if (st) {
                clearTimeout(st);
            }

        }
    });
}

$(function () {

    //

    setSize();

});
