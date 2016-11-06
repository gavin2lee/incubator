//$.getScript(WEB_APP_PATH + "/assets/js/fileupload/jquery.ui.widget.js");
//$.getScript(WEB_APP_PATH + "/assets/js/fileupload/jquery.iframe-transport.js");
//$.getScript(WEB_APP_PATH + "/assets/js/fileupload/jquery.fileupload.js");

var UPLOAD_URL = WEB_APP_PATH + '/paas/file/upload.do';
function getNameFromPath(filePath) {
	if (filePath && filePath.lastIndexOf("\\") != -1) {
		filePath = filePath.substring(filePath.lastIndexOf("\\") + 1);
	}
	if (filePath && filePath.lastIndexOf("/") != -1) {
		filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
	}
	return filePath;
}

function bindUpload(target, successCb) {
	var getFileNameCon = function(e){
		return $(e.target).parent().parent().find("#fileName");
	}
	target.fileupload({
		dataType : 'json',
		url : UPLOAD_URL,
		progressall : function(e, data) {
			var $fileName = getFileNameCon(e);
			var progress = parseInt(data.loaded / data.total * 100, 10);
			if ($fileName.find('.bar').length == 0) {
				$fileName.append('<span class="bar"></span>');
			}
			$fileName.find('.bar').html(progress + "%");
		},
		add : function(e, data) {
			var $fileName = getFileNameCon(e);
			$fileName.html($('<span/>').text(data.files[0].name));
			data.context = $('<a href="javascript:void(0)"/>').text('上传')
					.appendTo($fileName).click(function() {
						$(this).replaceWith($('<span/>').text('上传中...'));
						data.submit();
					}).click();
		},
		done : function(e, data) {
			var $fileName = getFileNameCon(e);
			$fileName.html($('<span/>').text(data.files[0].name));
			if (data.result.code == 0) {
				successCb(this, data);
			} else {
				common.alert("上传失败！" + (data.result.msg || ''), 2);
			}
		},
		fail: function (e, data) {
			var $fileName = getFileNameCon(e);
			$fileName.find('.bar').html("上传失败").prev().remove();
			common.alert("上传失败！" + (data.errorThrown|| ''), 2);
		} // .bind('fileuploadfail', func);
	});
}
/**
 * 初始化文件显示，有文件时显示文件名
 * @param target
 * @param file
 * @param accept 允许文件类型 数组
 */
function initFile(target, file, __accept,__desc,__showDesc) {
	var acceptStr = "";
	var descStr = '';
	if(typeof __accept == 'undefined' || !__accept){
		__accept = [".txt",".doc",".docx",".xls",".xlsx",".ppt","image/*",'.pptx','.pdf','.zip'];
	}
	if(typeof __desc == 'undefined'){
		__desc = "";
	}
	if(typeof __showDesc == 'undefined'){
		__showDesc = false;
	}
	if(__accept instanceof Array){
		acceptStr = __accept.join(",");
		if(__showDesc===true){
			descStr = __desc ? __desc : "请上传  "+__accept.join("、")+" 类型的文件";
		}
	}
	var id = "fileupload_" + new Date().getTime();
	var con = '<span class="fileinput-button">					 '
			+ '	<span>选择上传</span>						' + '	<input id="' + id
			+ '" class="fileupload" type="file" accept="'+acceptStr+'" name="upfile" multiple>       '
			+ '</span>								 '
			+ '<span id="fileName"/>						   '
			+ '<a href="javascript:void(0);" class="res-cancel">取消</a>	       '
			+ '<span style="padding-left:50px;">'+descStr+'</span>';
	target.html('');
	if (typeof file != 'undefined') {
		target.data("data", file);
	} else {
		file = target.data("data");
	}
	if (typeof file != 'undefined' && file && file.fileName) {
		target.append('<span path="' + file.filePath + '">' + file.fileName
				+ '</span>');
	} else {
		target.append('<span>无</span>');
	}
	target.append('<a href="javascript:void(0);" class="edit">修改</a>');
	target.find("a.edit").bind('click', function() {
		target.html(con);
		target.find("a.res-cancel").bind('click', function() {
			initFile(target, file, __accept,__desc,__showDesc);
		})
		var initActionFileCb = function(_this, data) {
			file = {
				fileName : data.result.data.fileName,
				filePath : data.result.data.filePath,
				fileSize : data.result.data.fileSize
			};
			target.data("data", file);
			initFile(target, file, __accept,__desc,__showDesc);
		}
		bindUpload($('#' + id), initActionFileCb);
	})
}

function initImage(target, filePath) {
	var createImg = function(filePath){
		return '<img src="'+WEB_APP_PATH+'/paas/file/view.do?filePath='+encodeURIComponent(filePath)+'"/>';
	}
	var img = "+";
	if(filePath){
		img = createImg(filePath);
	}
	target.data("data",{
		filePath : filePath
	});
	var id = "fileupload_" + new Date().getTime();
	var con = '<span class="imageinput-button">					 '
			+ '	<div class="img-container">'+img+'</div>						'
			+ '	<input id="'
			+ id
			+ '" accept="image/*" class="fileupload fileupload-img" type="file" name="upfile" multiple>       '
			+ '</span>								 ';
	target.html(con);
	$("#" + id).fileupload({
		dataType : 'json',
		url : UPLOAD_URL,
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$(this).prev().html(progress + "%");
		},
		add : function(e, data) {
			data.submit();
		},
		done : function(e, data) {
			if (data.result.code == 0) {
				$(this).prev().html(createImg(data.result.data.filePath));
				target.data("data",data.result.data);
			} else {
				common.alert("上传失败！" + (data.result.msg || ''), 2);
			}
		}
	});
}