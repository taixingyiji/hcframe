/**
 * 文件上传
 */
$(document).ready(function(){
	// 绑定文件上传提交按钮事件
	$("#file_upload").submit(function(event) {
		// 取消事件的默认动作
		event.preventDefault();
		var data = new FormData(jQuery("#file_upload")[0]);
		$.ajax({
			type : 'POST',
			enctype: 'multipart/form-data',
			url : '/files',
			contentType : false,
			data : data,
			processData : false,
			cache : false,
			async : false, //同步传输
			dataType : 'json',
			success : function(result) {
				self.location = '/files/index';
			},
			error : function(XMLHttpRequest, status) {
				if (status == "timeout") {
					alert("文件上传超时，请重试", "错误");
				} else {
					alert("文件上传失败。");
				}
			},
		});
		// 阻止表单提交
		return false;
	});
	
});
