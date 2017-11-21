$(function() {
	var basepath = $("#baseUrl").attr("href");
	
	var headers = $("section > ul > li");
	var bodys = $("section > div");
	headers.each(function(index, element) {
		$(this).on("click", function() {
			headers.each(function() {
				$(this).removeClass("active-header");
			});
			$(this).addClass("active-header");

			bodys.each(function() {
				$(this).css("display", "none");
			});
			bodys.eq(index).css("display", "block");
		});
	});

	/* 登录 */
	var loginButton = $("#loginButton");
	var loginForm = $("#loginForm");
	var loginInputEmail = $("#loginInputEmail");
	var loginInputPassword = $("#loginInputPassword");

	/* 登录处理 */
	loginButton.on("click", function() {
		if (loginInputEmail.val() == '' || loginInputPassword.val() == '') {
			$("#login-password-error").text('请输入邮箱和密码');
			return;
		}
		var form = new FormData();
		form.append("email", loginInputEmail.val());
		form.append("password", loginInputPassword.val());
		$.ajax({
			url : basepath + 'login',
			type : 'post',
			data : form,
			processData : false,
			contentType : false,
			success : function(response) {
				if (response.state == 0) {
					localStorage.setItem("userId", response.data.userInfo.userId);
					localStorage.setItem("username", response.data.userInfo.username);
					localStorage.setItem("avatarUrl", response.data.userInfo.avatarUrl);
					localStorage.setItem("simpleDesc", response.data.userInfo.simpleDesc);
					window.location.href = basepath + "index";
				} else {
					$("#login-password-error").text(response.message);
				}
			}
		});
	});

	/* 注册 */
	var registerButton = $("#registerButton");
	var registerForm = $("#registerForm");
	var registerInputUsername = $("#registerInputUsername");
	var registerInputEmail = $("#registerInputEmail");
	var registerInputPassword = $("#registerInputPassword");

	/* 注册校验 */
	registerInputUsername.blur(function() {
		checkUsername($(this).val());
	});
	registerInputEmail.blur(function() {
		checkEmail($(this).val());
	});
	registerInputPassword.blur(function() {
		checkPassword($(this).val());
	});

	/* 注册处理 */
	registerButton.on("click", function() {
		if (!checkUsername && checkEmail && checkPassword) {
			return;
		}
		var form = new FormData();
		form.append("username", registerInputUsername.val());
		form.append("email", registerInputEmail.val());
		form.append("password", registerInputPassword.val());
		$.ajax({
			url : basepath + 'register',
			type : "post",
			data : form,
			processData : false,
			contentType : false,
			success : function(response) {
				if (response.state == 0) {
					alert(response.message);
					registerInputUsername.val("");
					registerInputEmail.val("");
					registerInputPassword.val("");
					$("#loginHeader").click();
				} else {
					$("#regi-username-error").text(response.data["regi-username-error"]);
					$("#regi-email-error").text(response.data["regi-email-error"]);
					$("#regi-password-error").text(response.data["regi-passwor-error"]);
				}
			}
		});
	});

	function checkUsername(value) {
		if (value.length < 1 || value.length > 10) {
			$("#regi-username-error").text("用户名长度须在1-10个字符");
			return false;
		} else {
			$("#regi-username-error").text("");
			return true;
		}
	}

	function checkEmail(value) {
		if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
			$("#regi-email-error").text("请输入正确的邮箱");
			return false;
		} else {
			$("#regi-email-error").text("");
			return true;
		}
	}

	function checkPassword(value) {
		if (!/[a-zA-Z0-9]{6,10}/.test(value)) {
			$("#regi-password-error").text("密码长度须在6-20个字符");
			return false;
		} else {
			$("#regi-password-error").text("");
			return true;
		}
	}

});
