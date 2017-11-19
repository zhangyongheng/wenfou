var basepath = $("#baseUrl").attr("href");

$(function() {
	var isSelf = $("#isSelf").attr("data-id");
	var userId = localStorage.userId;
	var peopleId = $("#userId").attr("data-id");
	var followButton = $("#followUserButton");
	var unfollowButton = $("#unfollowUserButton");
	var settingButton = $("#settingButton");
	var deleteAnswerOption = $(".delete");

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
		bindEvent2();
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
	
	function bindEvent2() {
		deleteAnswerOption.on("click", function() {
			if (!confirm("确定删除吗？")) {
				return false;
			}
			var answerId = deleteAnswerOption.attr("data-id");
			var formData = new FormData();
			formData.append("answerId", answerId);
			$.ajax({
				url : basepath + "deleteAnswer",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					if (response.state == 0) {
						window.location.reload();
					} else {
						alert("出现错误了~");
					}
				}
			});
			return false;
		});
	}
	
});
