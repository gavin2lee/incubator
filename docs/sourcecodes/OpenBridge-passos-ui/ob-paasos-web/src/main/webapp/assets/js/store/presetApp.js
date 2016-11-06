function deleteApp(id) {
		common.confirm('确定要删除此应用吗？', function(res) {
			if (res) {
				$.post("delete.do", {
					id : id
				}, function(json) {
					if (json.code == 0) {
						common.tips("删除成功！", 1, function() {
							loadItems(pageNo, pageSize);
						},1000);
					} else {
						common.alert("删除失败！" + json.msg, 2);
					}
				});
			}
		});
	}
	function buildApp(id) {
		$.post("build.do", {
			id : id
		}, function(json) {
			if (json.code == 0) {
				loadItems(pageNo, pageSize);
				viewLog(id);
			} else {
				common.alert("构建失败！" + json.msg, 2);
			}
		});
	}
	var __storeDialog;
	var __id,__projectId;//当前操作的预置应用id
	var toDeploy = function(projectId){
		__projectId = projectId;
		window.location.href = WEB_APP_PATH+"/project/env/index.do?projectId="+projectId;
		/*__storeDialog.load(WEB_APP_PATH+"/project/env/index.do",{
			projectId : projectId,
			dialog : true
		});*/
	}
	var toResourceAdd = function(type){//创建资源
		__storeDialog.load(WEB_APP_PATH+"/resource/"+type+"/add.do",{
			dialog : true
		});
	}
	var toDeployView = function(deployId){//部署详情
		__storeDialog.css({
			'height':'86%',
			'overflow':'auto'
		});
		__storeDialog.after('<div id="dialogBottom"><div class="layui-layer-btn"><a class="layui-layer-btn0 deploy-button" attrId="'+deployId+'" href="javascript:void(0);">启动</a></div></div>');
		__storeDialog.load(WEB_APP_PATH+"/project/deploy/view.do",{
			dialog : true,
			deployId : deployId,
			projectId : __projectId
		},function(){
			
		});
	}
	function deployApp(id,name) {
		__id = id;
		var showDialog = function(){
			common.dialogHtml("部署"+name,'<div id="createProject"></div>', 900, $(window).height()*0.9);
			__storeDialog = $("#createProject");
		}
		var toCreateProject = function(id){//展示创建项目页面
// 			__projectId = '7040m3bjajo5siqryf45f7by45r71j9';
// 			toDeployView('7049b05nm5k2hg6aegcygizs6mtqpmc');return false;
			__storeDialog.html($("#createProjectModel").html());
			__storeDialog.find("#businessId").val(id);
		}
		
		$.get(WEB_APP_PATH+"/project/getProjectByBusId.do",{
			businessId : id
		},function(projList){
			if(projList && projList.length>0){
				common.confirm("此预置应用已创建过项目，是否创建新项目？",function(res){
					showDialog();
					if(res){
						toCreateProject(id);
					}else{//展示已有项目列表
						var projCon = "";
						$.each(projList,function(i,v){
							projCon += "<tr>";
							projCon += "<td><a href='"+WEB_APP_PATH+"/project/overview/index.do?projectId="+v.projectId+"'>"+v.projectName+"</a></td>";
							projCon += "<td>"+v.createUserName+"</td>";
							projCon += "<td>"+new Date(v.createDate).format("yyyy-MM-dd HH:mm:ss")+"</td>";
							projCon += "</tr>";
						})
						$("#projectTableModel #projectBody").html(projCon);
    					__storeDialog.html($("#projectTableModel").html());
					}
				});
			}else{
				showDialog();
				toCreateProject(id);
			}
		});

	}
	
	function createProject(){//创建项目提交处理
		var param = __storeDialog.find("#createProjectForm").serializeArray();
    	var data = {};
    	$.each(param,function(i,v){
    		data[v.name] = v.value
    	})
    	var id = __storeDialog.find("#businessId").val();
    	//$("#createProjectBtn").hide();
    	if(!$.trim(__storeDialog.find("#projectName").val())){
			common.tips('项目名称不能为空！');
			return false;
		}
    	var filter= /^[0-9a-z]*$/g;
			var projectCode ;
			$.each($("input[name=projectCode]"),function(index,item){
				if($(item).val()){
					projectCode = $(item).val();
				};
			})	
			if(!projectCode){
				common.tips("项目编码不能为空！");
				return false;
			}else if(projectCode.length >15){
				common.tips("项目编码长度不能超过15！");
				return false;
			}
			
			if(!filter.test(projectCode)){
				common.tips('项目编码只能为数字和小写字母');
				return false;
			}
    	
    	$.post(WEB_APP_PATH+"/project/save.do",data,function(json){
    		if(json.code!=0){
    			common.alert("创建失败！"+json.msg,2);
    			$("#createProjectBtn").show();
    		}else{
    			var projectId = json.data;
    			common.tips("创建成功！",1,function(){
    				toDeploy(projectId);
    			},1000);
    		}
    	});
	}