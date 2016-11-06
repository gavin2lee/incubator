/**
 * 
 */
var formValidate = function(){
		this.rules = {
			required : {
				regexpFun : function(v,r){
					var result = {};
					if(v==null || v==''){
						result = {
							'status' : -1,
							'error' : '请输入字符'
						};
					}
					else{
						result =  {
								'status' : 1
						};
					}
					result = $.extend(result,r);
					return result;
				}
			},
			projectName : {
				regexp : '^[a-zA-Z_]+[a-zA-Z0-9_-]+$',
				error : '只能以英文字母或下划线开头,只能包含英文数字和下划线,且至少2个字符'
			},
			packageName : {
				regexp : '^([a-z]+[\\.])+[a-z]+[0-9]*$',
				error : '包名只能包含小写英文和小数点,结尾可以包含数字,如com.company.abc或com.abc123'
			},
			className : {
				regexp : '^[A-Z]{1}[a-zA-Z0-9]+$',
				error : '首字母必须大写,只包含字母和数字'
			},
			number : {
				regexp : '^\\d+$',
				error : '请输入数字'
			},
			phone : {
				regexp : '^((\+)?86|((\+)?86)?)0?1[3458]\d{9}$|^(((0\d2|0\d{2})[- ]?)?\d{8}|((0\d3|0\d{3})[- ]?)?\d{7})(-\d{3})?$',
				error : '请输入正确格式手机号码'		
			}
		};
		this.isArray = Array.isArray || function(obj) {
		    return toString.call(obj) === '[object Array]';
		};
		var validateRule = function(value,r){
			if(r.rule != null){
				r = $.extend(window.rules[r.rule]?window.rules[r.rule]:{},r);
			}
			if(r.regexpFun){
				return r.regexpFun(value,r);
			}
			var regexp =new RegExp(r.regexp,'g');
			if(value.match(regexp)){
				return {
					'status' : 2
				};
			}
			else{
				return {
					'status' : -2,
					'error' : r.error
				};
			}
			
		};
		var validate = function(element,rule){
			if(rule==null){
				rule = $(element).attr('rule');
				rule = eval('('+rule+')');
			}
			var v = $(element).val();
			var r = {};
			if(rule!=null && window.isArray(rule)){
				for(var i=0;i<rule.length;i++){
					r = validateRule(v,rule[i]);
					if(r.status < 0){
						break;
					}
				}
			}
			else{
				r = validateRule(v,rule);
			}
			return r;
		};
		var renderError = function(domEle,msg){
			$(domEle).parent().children('.error,.error_1').remove();
			if(msg.status > 0){
				return ;
			}
			$(domEle).parent().append('<div class="error error_left"><span style="color:red;">'+msg.error+'</span></div>');

		}
		var bindFormValidate = function(domEle){
			var rule = $(domEle).attr('rule');
			rule = eval('('+rule+')');
			if(window.isArray(rule)){
				for(var i=0;i<rule.length;i++){
					if(rule[i].rule!=null && rule[i].rule=='required'){
						if($(domEle).val()==null || $(domEle).val()==''){
							$(domEle).after('<span style="color:red;" class="error_1">*</span>');
						}
						break ;
					}
				}
			}
			else{
				if(rule.rule!=null && rule.rule=='required'){
					if($(domEle).val()==null || $(domEle).val()==''){
						$(domEle).after('<span style="color:red;" class="error_1">*</span>');
					}
				}
			}
			if($(domEle).hasClass('blur-event')){
				$(domEle).bind('blur',function(){
					var r = validate(this,rule);
					renderError(this,r);
				});
			}
			if($(domEle).hasClass('keydown-event')){
				$(domEle).bind('focus',function(){
					var _self = this;
					if($(_self).data('validate')!=null){
						var w = $(_self).data('validate');
						window.clearInterval(w);
						$(_self).removeData('validate');
					}
					var w = window.setInterval(function(){
						var r = validate(_self,rule);
						renderError(_self,r);
					},500);
					$(_self).data('validate',w);
				});
				$(domEle).bind('blur',function(){
					var _self = this;
					validate($(_self).val(),rule);
					if($(_self).data('validate')!=null){
						var w = $(_self).data('validate');
						window.clearInterval(w);
						$(_self).removeData('validate');
					}
				});
			}
		};
		return {
			initBody : function(){
				$('.form-validate').each(function(index, domEle){
					bindFormValidate(domEle);
				});
			},
			initElement : function(domEle){
				bindFormValidate(domEle);
			},
			validate : function(domEle){
				return validate(domEle,null);
			},
			validateForm : function(formEle){
				var isfailure = false;
				$(formEle).find('.form-validate').each(function(index, domEle){
					var msg = validate(domEle,null);
					renderError(domEle,msg);
					if(msg.status < 0 && !isfailure){
						isfailure = true ;
					}
				});
				return isfailure;
			}
		};
	}();
	$(function(){
		formValidate.initBody();
	});