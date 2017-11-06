$(function() {

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

});
