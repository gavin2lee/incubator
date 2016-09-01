var log = console.log.bind(console);
//不允许输入的字符
/*notAllowSymbol = ['&','*','\''];

 var deptCode;
 var cacheData = {};
 var cacheDate = null;
 var st = null;
 var t  = 20000;
 var textAreaArr = ['24hinout','24hurine','abdomaen','weight','bodysigh','bodysigh','cinnamon','other','important','message'];
 function save(){
 var v = $("#inputBox").val();
 $("#inputBox").parent().html(v);
 }

 function setSize(){
 //获取基础参数
 deptCode = $("#deptCode").val();
 var dw = $(document).width();
 var dh = $(document).height();
 var abbw = params.bbw*12;
 var rowNumCount = 0;
 var rowNum = 0;
 var basicHeight = 0;
 $("#wrapper").height(dh-params.bbw-10).width(dw-(params.bbw*2)-10);
 $("tr[class*='row-']").each(function(){
 var cn = $(this).attr('class');
 var x2 = cn.length;
 var x3 = 1;
 rowNum += 1;
 if(cn.indexOf('rowspan') > 0){
 x2 = cn.indexOf('rowspan') - 1;
 x3 = parseInt(cn.substring(cn.indexOf('rowspan')+7,cn.length));
 }
 var rowHeight = cn.substring(cn.indexOf('row-')+4,x2);
 rowNumCount += params['row'+rowHeight] * x3;
 });
 basicHeight = (dh-rowNum*params.bbw)/rowNumCount;

 $("tr[class*='row-']").each(function(){
 var cn = $(this).attr('class');
 var x2 = cn.length;
 var x3 = 1;
 rowNum += 1;
 if(cn.indexOf('rowspan') > 0){
 x2 = cn.indexOf('rowspan') - 1;
 x3 = parseInt(cn.substring(cn.indexOf('rowspan')+7,cn.length));
 }
 var rowHeight = cn.substring(cn.indexOf('row-')+4,x2);
 $(this).css({
 "height":basicHeight * params['row'+rowHeight] *x3
 });
 });

 $(".editor_1 > div").each(function(){
 $(this).children('span').css({
 "line-height":$(this).height()+'px',
 "padding-left":"5px"
 })
 if( textAreaArr.inArray($(this).children('div').children('span').attr('id')) >= 0 ){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-8,
 "height":$(this).height(),
 "line-height":$(this).css('fontSize')+2+'px'
 });
 }
 else if( $(this).children('div').children('span').length == 2 ){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-8,
 "height":$(this).height(),
 "line-height":$(this).height()/2+'px'
 });
 }
 else if($(this).attr('id') == 'yw'){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-5,
 "height":$(this).height(),
 "line-height":$(this).height()+'px'
 });
 $(this).children('div').children('span').css({
 "height":$(this).height()/2-2
 })
 }
 else if($(this).attr('id') == 'day_nurse' || $(this).attr('id') == 'work_nurse'){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-8,
 "height":$(this).height(),
 "line-height":$(this).height()+'px'
 });
 $(this).find("li").each(function(){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-5,
 "height":$(this).height(),
 "line-height":$(this).height()+'px'
 });
 });
 }
 else if($(this).attr('class') == 'areaInput'){
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-8,
 "height":$(this).height()

 });
 }
 else{
 $(this).children('div').css({
 "width":$(this).width()-$(this).children('span').width()-8,
 "height":$(this).height(),
 "line-height":$(this).height()+'px'
 });
 }
 });

 $(".editor_2 > div").each(function(){
 $(this).children('div').height($(this).height()-$(this).children('span').height()-10);
 });

 getNoteMsg();

 var inputState = false;
 $(".msg-text > span").on('dblclick',function(event){

 if(inputState){
 return;
 }

 //是否可编辑
 if( $(this).attr('data-editor') == 'false' ){
 return ;
 }

 //获取span中的数据
 var str = $(this).attr('data-value') || '';

 //对数据进行处理转成textarea的数据

 str = transformData({start:'sp',end:'ta',data:str});

 /!*str = str.replace(/[\<br\>]/g,"\n");
 str = str.replace(/\&lt\;/g,"<");
 str = str.replace(/\&gt\;/g,">");
 str = str.replace(/\&nbsp\;/g," ");*!/
 /!*str = str.split('<br>');
 if(str.length == 0){ str = str.substring(41,str.length-1); }
 for(var i=0;i<length;i++){
 str[i] = str[i].substring(41,str[i].length-1);
 }*!/

 if( textAreaArr.inArray($(this).attr('id')) >= 0 ){

 }
 else{
 $(this).css('line-height',$(this).height()+'px');
 }

 if( $(this).attr('id') == 'abdomaen' || $(this).attr('id') == 'weight' ){
 //			var nodeHtml = '<input id="inputBox" class="inputBox" type="text" style="width:'+($(this).parent().width()-5)+'px;height:'+($(this).parent().height()*0.5)+'px;"/>';
 var nodeHtml = '<textarea id="inputBox" class="inputBox" type="text" style="width:'+($(this).parent().width()-8)+'px;height:'+($(this).parent().height()*0.5-8)+'px;padding:3px;"></textarea>';
 }
 else if( textAreaArr.inArray($(this).attr('id')) >= 0 ){
 var nodeHtml = '<textarea id="inputBox" class="inputBox" type="text" style="width:'+($(this).parent().width()-12)+'px;height:'+($(this).parent().height()-12)+'px;margin:3px 0 0 3px;"></textarea>';
 }
 else{
 var nodeHtml = '<input id="inputBox" class="inputBox" type="text" style="width:'+($(this).parent().width()-5)+'px;height:'+($(this).parent().height()*0.98)+'px;"/>';
 }

 $(this).html(nodeHtml);
 var tempId = $(this).attr('id');
 if( tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient' ){
 str = str.split('\u3000')[0];
 }
 $(this).find("input[type='text']").focus().val(str);
 $(this).find("textarea").focus().val(str);
 inputState = true;

 //监听输入事件
 $("#inputBox").keyup(function(){

 var v = $(this).val(),
 total = 0;
 for(var i=0;i<v.length;i++){
 if(v.charCodeAt(i) && v.charCodeAt(i)<128){
 total += 1;
 }
 else{
 total += 2;
 }
 }

 console.log(total);

 });

 if(st){
 clearTimeout(st);
 st = null;
 }
 });
 $(window).on('keypress',function(event){
 if(event.keyCode == 13 && inputState){
 if($("#inputBox")[0].tagName == 'TEXTAREA'){
 return ;
 }
 var v = $("#inputBox").val();
 v = v.replace(/\</g,"&lt;");
 v = v.replace(/\>/g,"&gt;");
 v = v.replace(/\n/g,"<br>");
 v = v.replaceAll(" ","&nbsp;");
 v = v.replace(/\"|\”/g,"''");
 var id = $("#inputBox").parent().attr('id');
 var recordDay = new Date().format("yyyy-MM-dd");
 whiteBoardRecord='{"deptId":"'+deptCode+'","itemCode":"'+id+'","itemValue":"'+v+'","showDate":"'+recordDay+'"}';
 $.post(ay.contextPath+'/nur/task/white/add.do',{whiteBoardRecord:whiteBoardRecord},function(data){
 try{
 var tempId = $("#inputBox").parent().attr('id');
 if( tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient' ){
 v += cacheData[tempId];
 }
 $("#inputBox").parent().attr('data-value',decodeURIComponent(v));

 $("#inputBox").parent().html(v);
 inputState = false;
 if(!st){
 st = setTimeout(getNoteMsg,t);
 }
 }
 catch(e){
 return ;
 }
 });
 }
 });
 $(window).on('click',function(event){
 var tagId = $(event.target).attr('id');
 if(inputState && tagId != 'inputBox'){
 var v = $("#inputBox").val();

 //v = transformData({start:''});

 v = v.replace(/\</g,"&lt;");
 v = v.replace(/\>/g,"&gt;");
 v = v.replace(/\n/g,"<br>");
 v = v.replaceAll(" ","&nbsp;");
 v = v.replace(/\"|\”/g,"''");

 v = encodeURIComponent(v);
 var id = $("#inputBox").parent().attr('id');
 var recordDay = new Date().format("yyyy-MM-dd");
 whiteBoardRecord='{"deptId":"'+deptCode+'","itemCode":"'+id+'","itemValue":"'+v+'","showDate":"'+recordDay+'"}';
 $.post(ay.contextPath+'/nur/task/white/add.do',{whiteBoardRecord:whiteBoardRecord},function(data){
 try{
 var tempId = $("#inputBox").parent().attr('id');
 if( tempId == 'new_patient' || tempId == 'crisis_patient' || tempId == 'hard_patient' || tempId == 'out_patient'){
 v += cacheData[tempId];
 }
 $("#inputBox").parent().attr('data-value',decodeURIComponent(v));
 $("#inputBox").parent().html(decodeURIComponent(v));
 inputState = false;
 if(!st){
 st = setTimeout(getNoteMsg,t);
 }
 }
 catch(e){
 return ;
 }
 });
 }
 });
 }

 /!*
 *   转换数据
 *   显示，保存与编辑的三种状态数据转换
 *   obj{start,end,data}
 *   span -> textarea
 *   span -> db
 *   textarea -> span
 *   db   -> span
 * *!/
 function transformData(obj){

 //错误格式退出
 if(!obj || !obj.start || !obj.end || !obj.data ){
 return obj.data;
 }
 var data = obj.data;

 if( obj.start == 'sp' && obj.end == 'ta'){

 data = data.replace(/\<br\>/g,"\n");
 data = data.replace(/\&lt\;/g,"<");
 data = data.replace(/\&gt\;/g,">");
 data = data.replace(/\&nbsp\;/g," ");

 return data;
 }

 if( obj.start == 'ta' && obj.end == 'sp' ){

 }

 if( obj.start == 'db' && obj.end == 'sp' ){

 }

 if( obj.start == 'ta' && obj.end == 'db' ){

 }
 }

 function getNoteMsg(){
 //得到数据
 var currDate = new Date().format("yyyy-MM-dd");
 if(!cacheDate){ cacheDate = currDate; }
 if(cacheDate != currDate){
 $(".msg-text span").each(function(){
 if($(this).attr('id') != 'date'){
 $(this).html('');
 }
 });
 cacheDate = currDate;
 }
 $("#date").html(currDate);

 $.get(ay.contextPath+'/nur/task/white/list.do?deptId='+deptCode + '&showDate=' + currDate ,function(data){
 try{
 var lst = data.lst;
 for(var i=0;i<lst.length;i++){
 $("#"+lst[i].itemCode).html("");
 if( ( lst[i].itemCode == 'new_patient' || lst[i].itemCode == 'crisis_patient' || lst[i].itemCode == 'hard_patient' || lst[i].itemCode == 'out_patient'  ) && lst[i].itemValue ){
 var splitArr = lst[i].itemValue.split('\u3000');
 cacheData[lst[i].itemCode] = '\u3000'+splitArr[1];
 $("#"+lst[i].itemCode).attr('data-value',lst[i].itemValue);
 $("#"+lst[i].itemCode).html(lst[i].itemValue);
 }
 else{
 $("#"+lst[i].itemCode).attr('data-value',lst[i].itemValue);
 $("#"+lst[i].itemCode).html(lst[i].itemValue);

 }
 }
 st = setTimeout(getNoteMsg,t);
 }
 catch(e){
 $.messager.alert('提示','获取出错'+e);
 if(st){
 clearTimeout(st);
 }

 }
 });
 }*/
var start = 0;

(function ($) {
    $.fn.autoScroll = function (options) {
        var defaultOpt = {
            speed: 100
        };
        this.each(function (i, v) {
            var vHeight = $(v).height(),
                sHeight = v.scrollHeight,
                isDown = true;

            if (sHeight <= vHeight) return true;
            setInterval(function () {
                var offsetTop = v.scrollTop;

                if (vHeight + offsetTop === sHeight) {
                    isDown = false;
                }
                if (offsetTop === 0) {
                    isDown = true;
                }
                if (isDown) {
                    $(v).animate({
                        scrollTop: offsetTop + 1
                    }, 0);
                } else {
                    $(v).animate({
                        scrollTop: offsetTop - 1
                    }, 0);
                }

            }, ((options && options.speed) || defaultOpt.speed));
        })
    };

    // 自动轮播
    $.fn.autoSlide = function (options) {
        var that = this,
            defaultOpt = {
                speed: 10000
            },
            opts = options || defaultOpt,
            len = this.length,
            count = 1,
            titleArr = [],
            controllerHtml = '',
            timer;

        function timers() {
            timer = setInterval(function () {
                if (count > len - 1) count = 0;
                that.eq(count).fadeIn(1000);
                that.eq(count).siblings().fadeOut(1000);

                $('.slide-ctr a').eq(count).addClass('active').siblings().removeClass('active');
                count++;
            }, opts.speed);
        }

        this.each(function (i, c) {
            titleArr.push($(c).data('title'));
        });

        controllerHtml = titleArr.map(function (c, i, a) {
            if (i === 0) {
                return ['<div class="slide-ctr">', '<a class="active" href="javascript:;" data-index="' + i + '">', c, '</a>'].join('');
            }

            if (i === a.length - 1) {
                return ['<a href="javascript:;" data-index="' + i + '">', c, '</a>', '</div>'].join('');
            }
            return ['<a href="javascript:;" data-index="' + i + '">', c, '</a>'].join('');
        }).join('');

        log(controllerHtml);

        $(document.body).append(controllerHtml);

        timers();

        $('.slide-ctr').on('click', 'a', function (e) {
            var $this = $(this),
                idx = $this.data('index');

            $this.addClass('active').siblings().removeClass('active');

            that.eq(idx).fadeIn(1000);
            that.eq(idx).siblings().fadeOut(1000);

            clearInterval(timer);
            timers();
        })

    }
})(jQuery);

var note = {
    init: function () {
        this.compile(function () {
            var wrapper1 = $('.wrapper-1'),
                wrapper2 = $('.wrapper-2'),
                wrapper3 = $('.wrapper-3'),
                subTitle = $('.sub-title'),
                bedWrapper = $('.auto-scroll__bedlist'),
                windowHeight = $(window).height(),
                wHeight1 = wrapper1.height(),
                wHeight3 = wrapper3.height(),
                headHeight = $('header').height(),
                h2Height = subTitle.eq(0).height(),
                h2NextHeight = subTitle.next().height(),
                smallScroll = $('.auto-scroll__child'),
                scrollWrap = $('.auto-scroll'),
                lastHeight = windowHeight - wHeight3 - wHeight1 - headHeight - 8;

            log(windowHeight);
            wrapper2.height(lastHeight);
            $('.middle').height(windowHeight - headHeight - $('.bed-wrapper-1').height());
            scrollWrap.height(lastHeight - (h2Height + h2NextHeight));
            $('.wrapper-2--1__body-sign, .wrapper-2--1__blood-sugar').height(lastHeight / 2);
            smallScroll.height((lastHeight / 2) - (h2Height + h2NextHeight));

            smallScroll.autoScroll();
            scrollWrap.autoScroll();
            bedWrapper.autoScroll();
            $('#table2 .content-wrapper').autoScroll();

            $('.note > div').autoSlide({
                speed: 20000
            });
        });
    },
    compile: function (callback) {

        $.get(ay.contextPath + '/nur/nurseWhiteBoard/getNurseWhiteBoardRecords', {
            deptCode: $('#deptCode').val()
        }).done(function (res) {
            log(res);
            var data = {
                    newPatient: [],
                    outPatient: [],
                    stature: [],
                    outIn: [],
                    urine: [],
                    bodySign: [],
                    bloodSugar: [],
                    notes: [],
                    confirms: [],
                    fubanDoctor: '',
                    dailyNurse: [],
                    orderlyDoctor: '',
                    orderlyNurse: [],
                    secondDoctor: '',
                    secondNurse: ''
                },
                headerData = {
                    bw: '',
                    bz: '',
                    total: ''
                };
            if (res['rslt'] === '0') {
                var list = res['data']['list'],
                    freqArr = res['data']['freq'];

                $.each(list, function (i, v) {
                    var code = v['recordCode'],
                        name = v['recordName'],
                        item = v['nurseWhiteBoardRecordFreqInfos'],
                        singleItem = item && item[0]['nurseWhiteBoardRecordFreqPatInfos'];

                    switch (code) {
                        case 'urine':
                            data.urine = singleItem.map(function (c, i) {
                                return c['patInfo'];
                            });
                            break;

                        case 'in-output':
                            data.outIn = singleItem.map(function (c) {
                                return c['patInfo'];
                            });
                            break;

                        case 'bw':
                            headerData.bw = singleItem.map(function (c, i) {
                                return c['patInfo'];
                            });
                            break;

                        case 'bz':
                            headerData.bz = singleItem.map(function (c, i) {
                                return c['patInfo'];
                            });
                            break;

                        case 'ry':
                            data.newPatient = singleItem.map(function (c, i) {
                                return {
                                    patient: c['patInfo']
                                };
                            });
                            break;

                        case 'cy':
                            data.outPatient = singleItem.map(function (c, i) {
                                return {
                                    patient: c['patInfo']
                                };
                            });
                            break;

                        case 'height':
                        case 'abdominalCir':
                        case 'weight':
                            $.each(singleItem, function (i, c) {
                                if ($.inArray(c['patInfo'], data.stature) < 0) {
                                    data.stature.push(c['patInfo']);
                                }
                            });
                            break;

                        case 'bloodGlu':
                            data.bloodSugar = item.map(function (c, i) {
                                var patInfo = c['nurseWhiteBoardRecordFreqPatInfos'];
                                return {
                                    sn: i + 1,
                                    bedCode: patInfo.map(function (crr, i) {
                                        var patStr = crr['patInfo'];
                                        if (patStr && patStr !== '') {
                                            return (patStr.indexOf('-') > -1 ? patStr.split('-')[0] : patStr)
                                        } else {
                                            return ''
                                        }
                                    }).join('、'),
                                    rate: freqArr['bloodGluFreq'][c['performSchedule']],
                                    content: c['recordValue']
                                };
                            });
                            break;

                        case 'dutyDoctor':
                            data.orderlyDoctor = item.map(function (c, i) {
                                return c['recordValue'];
                            }).join('、');
                            break;

                        case 'shiftDoctor':
                            data.fubanDoctor = item.map(function (c, i) {
                                return c['recordValue'];
                            }).join('、');
                            break;

                        case 'secondDoctor':
                            data.secondDoctor = item.map(function (c) {
                                return c['recordValue'];
                            }).join('、');
                            break;

                        case 'secondHeadNurse':
                            data.secondNurse = item.map(function (c, i) {
                                return c['recordValue'];
                            }).join('、');
                            break;

                        case 'bodysign':
                            data.bodySign = item.map(function (c, i) {
                                var itemInfo = c['nurseWhiteBoardRecordFreqItems'],
                                    patInfo = c['nurseWhiteBoardRecordFreqPatInfos'];
                                return {
                                    sn: i + 1,
                                    bedCode: patInfo.map(function (crr, i) {
                                        var patStr = crr['patInfo'];
                                        if (patStr && patStr !== '') {
                                            return (patStr.indexOf('-') > -1 ? patStr.split('-')[0] : patStr)
                                        } else {
                                            return ''
                                        }
                                    }).join('、'),
                                    itemName: itemInfo.map(function (crr, i) {
                                        return crr['recordItemName'];
                                    }).join(','),
                                    rate: c['performSchedule']
                                };
                            });
                            break;

                        case 'importantHint':
                            data.notes = item.map(function (c, i) {
                                var patInfo = c['nurseWhiteBoardRecordFreqPatInfos'];
                                return {
                                    sn: i + 1,
                                    bedCode: patInfo.map(function (crr, i) {
                                        var patStr = crr['patInfo'];
                                        if (patStr && patStr !== '') {
                                            return (patStr.indexOf('-') > -1 ? patStr.split('-')[0] : patStr)
                                        } else {
                                            return ''
                                        }
                                    }).join('、'),
                                    content: c['recordValue']
                                }
                            });
                            break;

                        case 'importantNotice':
                            data.confirms = item.map(function (c, i) {
                                var patInfo = c['nurseWhiteBoardRecordFreqPatInfos'];
                                return {
                                    sn: i + 1,
                                    bedCode: patInfo.map(function (crr, i) {
                                        var patStr = crr['patInfo'];
                                        if (patStr && patStr !== '') {
                                            return (patStr.indexOf('-') > -1 ? patStr.split('-')[0] : patStr)
                                        } else {
                                            return ''
                                        }
                                    }).join('、'),
                                    content: c['recordValue']
                                }
                            });
                            break;

                        case 'pCount':
                            item = item || v['nurseWhiteBoardRecordInfos'];
                            headerData.total = item.map(function (c, i) {
                                return c['recordValue'];
                            });
                            break;

                        case 'a_one_nurse':
                        case 'a_two_nurse':
                        case 'a_three_nurse':
                        case 'a_four_nurse':
                        case 'b_nurse':
                            data.dailyNurse.push(item.map(function (c, i) {
                                return [v['recordName'], c['recordValue']].join('：');
                            }));
                            break;

                        case 'p_nurse':
                        case 'n_nurse':
                        case 'fu_nurse':
                            data.orderlyNurse.push(item.map(function (c, i) {
                                return [v['recordName'], c['recordValue']].join('：');
                            }));
                            break;
                    }
                });

                var todoListTpl = Handlebars.compile($("#todoTpl").html()),
                    headTbody = Handlebars.compile($("#tbody-tpl").html());
                $('#wrapper').empty().html(todoListTpl(data));
                $('#head-tbody').empty().html(headTbody(headerData));

                callback();
                setTimeout(function () {
                    window.location.reload();
                }, 40800);
            }
        });
    }
};

$(function () {
    note.init();
});
