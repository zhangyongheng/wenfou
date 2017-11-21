var basepath = $("#baseUrl").attr("href");

// 等待几秒
function sleep(numberMillis) {
	var now = new Date();
	var exitTime = now.getTime() + numberMillis;
	while (true) {
		now = new Date();
		if (now.getTime() > exitTime)
			return;
	}
}

// long转日期字符串
function longToDateString(num) {
	var dateType = "";
	var date = new Date();
	date.setTime(num);
	dateType = (date.getMonth(date) + 1) + "-" + (date.getDate(date));// yyyy-MM-dd格式日期
	time = " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	return dateType + time;
}

$(function() {

	$(".profile-link").attr("href", basepath + "profile/" + localStorage.userId);
	$(".profile-follow-question-link").attr("href", basepath + "profileFollowQuestion/" + localStorage.userId);
	$(".profile-question-link").attr("href", basepath + "profileQuestion/" + localStorage.userId);
	if (localStorage.avatarUrl && localStorage.avatarUrl != "null") {
		$("#profile-avatar").attr("src", localStorage.avatarUrl);
	} else {
		$("#profile-avatar").attr("src", basepath + "image/avatar.png");
		
	}
	$("#profile-username").text(localStorage.username);

	// 下拉菜单处理
	var profile = $(".nav-profile");
	var downMenu = $(".nav-dropdown");

	profile.mouseover(function() {
		downMenu.show();
	}).mouseout(function() {
		downMenu.hide();
	});

	downMenu.mouseover(function() {
		downMenu.show();
	}).mouseout(function() {
		downMenu.hide();
	});

});