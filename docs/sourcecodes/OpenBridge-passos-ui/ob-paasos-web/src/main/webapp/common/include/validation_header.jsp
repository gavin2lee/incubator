<link href="${ WEB_APP_PATH}/assets/css/validation/validation.css"  type='text/css' rel='stylesheet'/>
<script src="${WEB_APP_PATH }/assets/js/validation/jquery.validate.min.js"></script>
<script src="${WEB_APP_PATH }/assets/js/validation/messages_zh.min.js"></script>
<script src="${WEB_APP_PATH }/assets/js/validation/additional-methods.js"></script>

<script>
$.validator.setDefaults({
// 	debug : true,
	errorElement : "em",
	errorPlacement: function(error, element) {  
	    error.appendTo(element.parent());  
	}
});
</script>