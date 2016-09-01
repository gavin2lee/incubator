$.extend($.fn.validatebox.defaults.rules, {    
    number: {    
        validator: function(value,param){
            $.fn.validatebox.defaults.rules.number.message = "输入的数字格式不对";

            //判断格式是否正确
            var numArr = value.split('.');
            if(/\./.test(value) && !numArr[1] ){
                return false;
            }
            if(!/^\d+\.?\d*$/.test(value)){
                return false;
            }
            
            if(param && param[2] ){
                //如果限制了小数点后位数则用split
                precision = param[2];
                if(value.length == 1){
                    $.fn.validatebox.defaults.rules.number.message = '请输入'+param[0]+"~"+param[1]+"之间的数！";
                    var num = parseFloat(value);
                    return num >= param[0] && num <= param[1];
                }
                else{
                    if(numArr[1] && numArr[1].length>precision){
                        $.fn.validatebox.defaults.rules.number.message = "小数点后最多"+precision+"位";
                        return false;
                    }
                }
            }
            else{
                if(param && param[0] && param.length >= 2){
                    $.fn.validatebox.defaults.rules.number.message = '请输入'+param[0]+"~"+param[1]+"之间的数！";
                    var num = parseFloat(value);
                    return num >= param[0] && num <= param[1];
                }
            }

            return true;
	  	},
        message: ''
    },
    bp:{
        validator:function(value,param){
            var itemId = param[2];

            if( value.indexOf('.') >= 0){
                $.fn.validatebox.defaults.rules.bp.message = "不允许输入小数";
                return false;
            }
            if( !/^\d+\/?\d*$/.test(value) ){
                $.fn.validatebox.defaults.rules.bp.message = "数据格式不正确";
                return false;
            }

            var max = 300,
                min = 0;

            if(param && param[0] && param[1]){
                min = param[0];
                max = param[1];
            }
            if(value.indexOf('/') >= 0){
                var numArr = value.split('/');
                numArr[1]  = numArr[1] ? parseInt(numArr[1]) : 0;
                if(numArr[1] ==0){
                    $.fn.validatebox.defaults.rules.bp.message = '请输入'+min+"~"+max+"之间的数！";
                    return false;
                }
                else if(numArr[1]>0){
                    if( numArr[1]>=min && numArr[1]<=max ){
                    }
                    else{
                        $.fn.validatebox.defaults.rules.bp.message = '请输入'+min+"~"+max+"之间的数！";
                        return false;
                    }
                }
            }
            else{
                value = parseInt(value);
                if( value>=min && value<=max ){
                    if(value > 80){
                        $("input[data-name='"+itemId+"']").val(value+'/');
                    }
                }
                else{
                    $.fn.validatebox.defaults.rules.bp.message = '请输入'+min+"~"+max+"之间的数！";
                    return false;
                }
            }
            return true;
        },
        message: '请输入数字正确的血压'
    },
    maxNum:{
    	validator: function(value,param){
    		/*var num = parseFloat(value);
    		if( param 	)
    		if(num>param)*/
    	}
    },
    minNum: {
        validator:function(value,param){
            var itemId = param[2],
                num = Number(value),
                min = 0;

            if (isNaN(num)) {
                return false;
            }

            if(param && param[0]){
                min = param[0];
            }

            if( num <= min){
                $.fn.validatebox.defaults.rules.minNum.message = "请输入大于" + min + "的数字";
                return false;
            }
            return true;
        },
        message: '请输入数字'
    }
}); 
