var basepath = $("#baseUrl").attr("href");

$(function() {
	var isSelf = $("#isSelf").attr("data-id");
	var userId = localStorage.userId;
	var peopleId = $("#userId").attr("data-id");
	var followButton = $("#followUserButton");
	var unfollowButton = $("#unfollowUserButton");
	var settingButton = $("#settingButton");

	if (isSelf == 'false') {
		settingButton.hide();
		followButton.show();
		unfollowButton.show();
		init();
		bindEvent();
	} else {
		settingButton.css("display", "block");
		followButton.hide();
		unfollowButton.hide();
	}

	function init() {
		var flag;
		var formData = new FormData();
		formData.append("userId", userId);
		formData.append("peopleId", peopleId);
		$.ajax({
			url : basepath + "judgePeopleFollowUser",
			type : "post",
			data : formData,
			processData : false,
			contentType : false,
			async : false,
			success : function(response) {
				flag = response.data;
			}
		});

		if (flag == true) {
			followButton.hide();
			unfollowButton.show();
		} else {
			unfollowButton.hide();
			followButton.show();
		}
	}

	function bindEvent() {
		// 关注按钮
		followButton.on("click", function() {
			followUser(userId);
			followButton.hide();
			unfollowButton.show();
		});

		// 取消关注按钮
		unfollowButton.on("click", function() {
			unfollowUser(userId);
			unfollowButton.hide();
			followButton.show();
		});
	}

	function followUser(userId) {
		var formData = new FormData();
		formData.append("userId", userId);
		formData.append("peopleId", peopleId);
		$.ajax({
			url : basepath + "followUser",
			type : "post",
			data : formData,
			processData : false,
			contentType : false,
			success : function(response) {
			}
		});
	}

	function unfollowUser(userId) {
		var formData = new FormData();
		formData.append("userId", userId);
		formData.append("peopleId", peopleId);
		$.ajax({
			url : basepath + "unfollowUser",
			type : "post",
			data : formData,
			processData : false,
			contentType : false,
			success : function(response) {
			}
		});
	}

});
