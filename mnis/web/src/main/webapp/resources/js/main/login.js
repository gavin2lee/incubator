function submitForm() {
  var form = $("#loginForm");
  var userId = $("#userName").val();
  var password = $("#password").val();
  $(".err").html('');
  if (userId == null || $.trim(userId) == '') {
    //$.messager.alert("用户名不能为空!");
    $(".err").html('用户名不能为空!');
    return;
  }
  /*if (password == null || $.trim(password) == '' ) {
   	//$.messager.alert("密码不能为空!");
   	$(".err").html('密码不能为空!');
  	return;
  }*/

  sessionStorage.setItem('userName', $('#userName').val());
  sessionStorage.setItem('pwd', $('#password').val());

  form.submit();
}

document.onkeydown = function(e) {
  var theEvent = window.event || e;
  var code = theEvent.keyCode || theEvent.which;
  if (code == 13) {
    if (document.getElementById("userName") == document.activeElement) {
      $("#password").focus();
    } else {
      submitForm();
    }
  }
};

function resetInfo(e) {
  e.preventDefault();
  $("#userName").val("").focus();
  $("#password").val("");
  $(".err").html('');
}

//设置默认登陆信息
function setDefaultInfo() {
  $("#userName").val("156");
  $("#password").val("123");
}

function loginTopWindow() {
  if (window.top != window.self) {
    window.top.location.href = ay.contextPath + "/index/login";
  }
}

//define.amd.jQjuery = true;
$(function() {
  $("#userName").focus();
  $("#loginBtn").click(function() {
    submitForm();
  });
});
