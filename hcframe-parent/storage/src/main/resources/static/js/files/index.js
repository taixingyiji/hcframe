/**
 * 文件管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		files:[],
		total_files:10,
		file:{
			id:0,
			version:0,
			name:'',
			type:'',
			tag:'',
			length:0,
			bucketName:0,
		},
    	showMessage: false,
		title:'',
		type:'',
		query_files:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		fileDialogFormVisible:false,
		fileTextMap: {
	        update: '编辑文件',
	        create: '新增文件',
	        query: '查看文件',
	        delete: '删除文件',
	      },
	    fileDialogStatus: '',
	    fileDialogDisabled: false,
	};
  },
  mounted: function(){
	  this.getFileList();
  },
  methods: {
	  getFileList() {
		this.$http.get('/files', {params:this.query_files}).then((response) => {
			this.files = response.body.content;
			this.total_files = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取文件列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取文件列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryFile(row) {
		var url = '/files/view/id/' + row.id;
		window.open(url);
	},
	handleCreateFile() {
		self.location = '/files/upload';
	},
	handleUpdateFile(row) {
		this.fileDialogDisabled = false;
		this.fileDialogStatus = 'update';
		this.file = row;
		this.fileDialogFormVisible = true;
	},
	handleFileDialog() {
		this.fileDialogFormVisible = false;
		if(this.fileDialogStatus == 'create') {
			this.$http.post('/files', this.file).then(function(response) {
				this.getFileList();
          	}, function(error) {
        	  	console.log('新增文件失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增文件失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.fileDialogStatus == 'delete') {
			this.$http.delete('/files/' + this.file.id).then(function(response) {
				this.getFileList();
          	}, function(error) {
        	  	console.log('删除文件失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除文件失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.fileDialogStatus == 'update') {
			this.$http.put('/files', this.file).then((response) => {
				this.getFileList();
	      	}, (error) => {
	    	  	console.log('更新文件失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新文件失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
		
	},
	resetFile() {
		this.file.id = 0;
		this.file.version = 0;
		this.file.name = '';
		this.file.description = '';
		this.file.totalSize = 0;
		this.file.createTime = 0;
		this.file.modifiedTime = 0;
	},
	handleDeleteFile(row) {
		this.fileDialogStatus = 'delete';
		this.fileDialogDisabled = true;
		this.file = row;
		this.fileDialogFormVisible = true;
	},
	handleCurrentChangeFile(page) {
		this.query_files.page = page - 1;
		this.getFileList();
	},
	// 单位换算
	transformUnit(value, unit) {
		var k = 1024;
		var sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
		var start = jQuery.inArray(unit, sizes);
		sizes = sizes.slice(start);
		var i = 0;
		if(value > 0) {
			i = Math.floor(Math.log(value) / Math.log(k));
		}
		return Math.round(value / Math.pow(k, i)) + ' ' + sizes[i];
	},
	formatSize(row, column) {
		return this.transformUnit(row[column.property], 'B');
	},
	handleDownloadFile(row) {
		var url = '/files/download/id/' + row.id;
		window.open(url);
	},
	handleCreateBlockFile() {
		
	}
  }
});