/**
 * 文件上传页面
 */
new Vue({
	el: '#content',
	data: function() {
		return {
			fileList: [],
			bucketName:'',
		};
	},
	mounted: function() {
		
	},
	methods: {
		handleRemove(file, fileList) {
			console.log(file, fileList);
		},
		handlePreview(file) {
			console.log(file);
		},
		handleExceed(files, fileList) {
			this.$message.warning(`当前限制选择10个文件，本次选择了${files.length}个文件，共选择了${files.length + fileList.length}个文件`);
		},
		beforeUpload(file) {
//			var data = new FormData();
//			data.append('bucket_name', this.bucketName);
//			data.append('file', file);
//	        this.$http.post('/files', data).then((response) => {
//	          console.log(response);
//	        }, (error) => {
//	          console.log(error);
//	        });
	        return true;
		},
		handleSuccess(response, file, fileList) {
			this.$notify.success({
		        title: '成功',
		        message: '文件上传成功'
		      });
		},
		handleError(error, file, fileList) {
			this.$notify.error({
		        title: '错误',
		        message: '文件上传失败'
		      });
		}
	}
});