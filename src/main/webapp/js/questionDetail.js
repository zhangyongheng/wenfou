$(function() {

	var questionId = $("#questionId").attr("data-id");
	var followQuestionButton = $("#followQuestionButton");
	var unfollowQuestionButton = $("#unfollowQuestionButton");
	var expandComment = $(".expandComment");

	bindEvent();

	function bindEvent() {
		// 关注按钮
		followQuestionButton.on("click", function() {
			followQuestion(questionId);
			followQuestionButton.hide();
			unfollowQuestionButton.show();
		});

		// 取消关注按钮
		unfollowQuestionButton.on("click", function() {
			unfollowQuestion(questionId);
			unfollowQuestionButton.hide();
			followQuestionButton.show();
		});

		expandComment.on("click", function() {
			var commentDiv = $(this).parent().siblings(".comment");
			if (commentDiv.is(':hidden')) {
				commentDiv.show();
			} else {
				commentDiv.hide();
			}
			return false;
		});
	}

	function followQuestion(questionId) {
		var formData = new FormData();
		formData.append("questionId", questionId);
		$.ajax({
			url : basepath + "followQuestion",
			type : "post",
			data : formData,
			processData : false,
			contentType : false,
			success : function(response) {
			}
		});
	}

	function unfollowQuestion(questionId) {
		var formData = new FormData();
		formData.append("questionId", questionId);
		$.ajax({
			url : basepath + "unfollowQuestion",
			type : "post",
			data : formData,
			processData : false,
			contentType : false,
			success : function(response) {
			}
		});
	}

	// 点赞按钮处理
	var voteUp = $(".vote-up");

	voteUp.each(function() {
		var likeState = $(this).attr("clicked");
		if (likeState == "true") {
			$(this).removeClass("like-unclicked")
			$(this).find("i").removeClass("like-unclicked-i");
			$(this).addClass("like-clicked")
			$(this).find("i").addClass("like-clicked-i");
		}
	});

	voteUp.click(function() {
		if ($(this).attr("clicked") == "false") {
			$(this).removeClass("like-unclicked")
			$(this).find("i").removeClass("like-unclicked-i");
			$(this).addClass("like-clicked")
			$(this).find("i").addClass("like-clicked-i");

			$(this).attr("clicked", "true");
			// 发送请求
			var formData = new FormData();
			formData.append("answerId", $(this).attr("answer-id"));
			var that = $(this);
			$.ajax({
				url : basepath + "likeAnswer",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					that.html(parseInt(that.text()) + 1 + "");
					that.append("<i class='like-clicked-i'></i>");
				}
			});
			
		} else if ($(this).attr("clicked") == "true"){
			$(this).removeClass("like-clicked")
			$(this).find("i").removeClass("like-clicked-i");
			$(this).addClass("like-unclicked")
			$(this).find("i").addClass("llike-unclicked-i");

			$(this).attr("clicked", "false");
			// 发送请求
			var formData = new FormData();
			formData.append("answerId", $(this).attr("answer-id"));
			var that = $(this);
			$.ajax({
				url : basepath + "unLikeAnswer",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					that.html(parseInt(that.text()) - 1 + "");
					that.append("<i class='like-unclicked-i'></i>");
				}
			});
		}

	}).mouseover(function() {
		if ($(this).attr("clicked") == "false") {
			$(this).removeClass("like-unclicked")
			$(this).find("i").removeClass("like-unclicked-i");
			$(this).addClass("like-clicked")
			$(this).find("i").addClass("like-clicked-i");
		}
	}).mouseout(function() {
		if ($(this).attr("clicked") == "false") {
			$(this).removeClass("like-clicked")
			$(this).find("i").removeClass("like-clicked-i");
			$(this).addClass("like-unclicked")
			$(this).find("i").addClass("like-unclicked-i");
		}
	});

	// 点赞问题下评论
	var likeQuestionComment = $(".like-question-comment");

	likeQuestionComment.each(function() {
		var a = $(this).find("a");
		var span = $(this);
		a.on("click", function(event) {
			event.preventDefault();
			// 发送请求
			var formData = new FormData();
			formData.append("questionCommentId", $(this).attr("question-comment-id"));
			var that = $(this);
			$.ajax({
				url : basepath + "likeQuestionComment",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					that.html("已赞");
					var next = span.next();
					next.html(parseInt(next.text()) + 1 + " 赞")
				}
			});
		});
	});

	// 点赞回答下评论
	var likeAnswerComment = $(".like-answer-comment");

	likeAnswerComment.each(function() {
		var a = $(this).find("a");
		var span = $(this);
		a.on("click", function(event) {
			event.preventDefault();
			// 发送请求
			var formData = new FormData();
			formData.append("answerCommentId", $(this).attr("answer-comment-id"));
			var that = $(this);
			$.ajax({
				url : basepath + "likeAnswerComment",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					that.html("已赞");
					var next = span.next();
					next.html(parseInt(next.text()) + 1 + " 赞")
				}
			});
		});
	});

	/* 富文本编辑器创建 */
	var editor = new wangEditor('answerContent');

	editor.config.menus = [ 'source', '|', 'bold', 'underline', 'italic', 'strikethrough', 'eraser', 'fontsize', '|', 'link', 'table', 'emotion', '|', 'img', 'insertcode', '|', 'undo', 'fullscreen', ];

	// 配置处理图片上传的路径，最好用相对路径
	editor.config.uploadImgUrl = 'uploadImage';
	// 配置图片上传到后台的参数名称
	editor.config.uploadImgFileName = 'paramName';
	editor.create();

	// ------------------------------------------------------------

	/* 回答 */
	var answerButton = $("#answerButton");
	var answerForm = $("#answerForm");
	var answerContentTextarea = $("#answerContent");

	/* 回答处理 */
	answerButton.on("click", function() {
		if (answerContentTextarea.val().trim() == '') {
			return false;
		}
		var form = new FormData();
		form.append("answerContent", answerContentTextarea.val());
		form.append("questionId", questionId);
		$.ajax({
			url : basepath + "answer",
			type : "post",
			data : form,
			processData : false,
			contentType : false,
			success : function(response) {
				if (response.state == 0) {
					window.location.href = basepath + "question/" + questionId + "#answer-" + response.data;
					location.reload();
				} else {
					alert("出现错误了~");
				}
			}
		});
	});

	// 评论回答
	var commentAnswerForm = $(".commentAnswerForm");
	commentAnswerForm.each(function() {
		var that = $(this);
		$(this).find("button").on("click", function() {
			if (that.find("textarea").val().trim() == '') {
				return false;
			}

			event.preventDefault();
			var formData = new FormData();
			formData.append("answerId", that.find("input[name=answerId]").val());
			formData.append("commentContent", that.find("textarea").val());
			$.ajax({
				url : basepath + "commentAnswer",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					if (response.state == 0) {
						var prefix = location.origin + location.pathname;
						var newPath = prefix + "#answer-comment-" + response.data.answerCommentId;
						window.location.href = newPath;
						window.location.reload();
					}
				}
			});
		});
	});

	// 回复答案评论
	var replyAnswerForm = $(".reply-answer-form");
	replyAnswerForm.each(function() {
		var that = $(this);
		$(this).find(".reply-button").on("click", function() {
			var formData = new FormData();
			formData.append("answerId", that.find("input[name=answerId]").val());
			formData.append("atUserId", that.find("input[name=atUserId]").val());
			formData.append("atUserName", that.find("input[name=atUserName]").val());
			formData.append("answerCommentContent", that.find("textarea").val());
			$.ajax({
				url : basepath + "replyAnswerComment",
				type : "post",
				data : formData,
				processData : false,
				contentType : false,
				success : function(response) {
					if (response.state == 0) {
						var prefix = location.origin + location.pathname;
						var newPath = prefix + "#answer-comment-" + response.data.answerCommentId;
						window.location.href = newPath;
						window.location.reload();
					}
				}
			});
		});
	});

});
