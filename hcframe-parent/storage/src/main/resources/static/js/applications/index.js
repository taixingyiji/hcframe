/**
 * 应用系统管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		applications:[],
		total_applications:10,
		application:{
			id:0,
			version:0,
			name:'',
			description:'',
			url:'',
			rank:99,
			createTime:0,
			modifiedTime:0
		},
    	showMessage: false,
		title:'',
		type:'',
		query_applications:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		applicationDialogFormVisible:false,
		applicationTextMap: {
	        update: '编辑应用系统',
	        create: '新增应用系统',
	        query: '查看应用系统',
	        delete: '删除应用系统',
	      },
	    applicationDialogStatus: '',
	    applicationDialogDisabled: false,
	    modules:[],
		total_modules:10,
		module:{
			id:0,
			version:0,
			name:'',
			description:'',
			url:'',
			rank:99,
			createTime:0,
			modifiedTime:0
		},
		query_modules:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
			applicationId:null
		},
		moduleDialogFormVisible:false,
		moduleTextMap: {
	        update: '编辑模块',
	        create: '新增模块',
	        query: '查看模块',
	        delete: '删除模块',
	      },
	    moduleDialogStatus: '',
	    moduleDialogDisabled: false,
	};
  },
  mounted: function(){
	  this.getApplicationList();
  },
  methods: {
	  getApplicationList() {
		this.$http.get('/applications', {params:this.query_applications}).then((response) => {
			this.applications = response.body.content;
			this.total_applications = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取应用系统列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取应用系统列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryApplication(row) {
		this.applicationDialogStatus = 'query';
		this.applicationDialogDisabled = true;
		this.application = row;
		this.applicationDialogFormVisible = true;
	},
	handleCreateApplication() {
		this.applicationDialogStatus = 'create';
		this.applicationDialogDisabled = false;
		this.resetApplication();
		this.applicationDialogFormVisible = true;
	},
	handleUpdateApplication(row) {
		this.applicationDialogDisabled = false;
		this.applicationDialogStatus = 'update';
		this.application = row;
		this.applicationDialogFormVisible = true;
	},
	handleApplicationDialog() {
		this.applicationDialogFormVisible = false;
		if(this.applicationDialogStatus == 'create') {
			this.$http.post('/applications', this.application).then(function(response) {
				this.getApplicationList();
          	}, function(error) {
        	  	console.log('新增应用系统失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增应用系统失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.applicationDialogStatus == 'delete') {
			this.$http.delete('/applications/' + this.application.id).then(function(response) {
				this.getApplicationList();
          	}, function(error) {
        	  	console.log('删除应用系统失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除应用系统失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.applicationDialogStatus == 'update') {
			this.$http.put('/applications', this.application).then((response) => {
				this.getApplicationList();
	      	}, (error) => {
	    	  	console.log('更新应用系统失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新应用系统失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
		
	},
	resetApplication() {
		this.application.id = 0;
		this.application.version = 0;
		this.application.name = '';
		this.application.description = '';
		this.application.url = '';
		this.application.rank = 99;
		this.application.createTime = 0;
		this.application.modifiedTime = 0;
	},
	handleDeleteApplication(row) {
		this.applicationDialogStatus = 'delete';
		this.applicationDialogDisabled = true;
		this.application = row;
		this.applicationDialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	},
	handleCurrentChangeApplication(page) {
		this.query_applications.page = page - 1;
		this.getApplicationList();
	},
	handleEnterApplication(row) {
		window.open(row.url);
	},
	handleQueryApplicationModule(row) {
		this.application = row;
		this.getModuleList();
	},
	getModuleList() {
		this.query_modules.applicationId = this.application.id;
		this.$http.get('/modules', {params:this.query_modules}).then((response) => {
			this.modules = response.body.content;
			this.total_modules = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取模块列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取模块列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryModule(row) {
		this.moduleDialogStatus = 'query';
		this.moduleDialogDisabled = true;
		this.module = row;
		this.moduleDialogFormVisible = true;
	},
	handleCreateModule() {
		this.moduleDialogStatus = 'create';
		this.moduleDialogDisabled = false;
		this.resetModule();
		this.moduleDialogFormVisible = true;
	},
	handleUpdateModule(row){
		this.moduleDialogDisabled = false;
		this.moduleDialogStatus = 'update';
		this.module = row;
		this.moduleDialogFormVisible = true;
	},
	handleDeleteModule(row){
		this.moduleDialogStatus = 'delete';
		this.moduleDialogDisabled = true;
		this.module = row;
		this.moduleDialogFormVisible = true;
	},
	handleCurrentChangeModule(page) {
		this.query_modules.page = page - 1;
		this.getModuleList();
	},
	resetModule() {
		this.module.id = 0;
		this.module.version = 0;
		this.module.name = '';
		this.module.description = '';
		this.module.url = '';
		this.module.rank = 99;
		this.module.createTime = 0;
		this.module.modifiedTime = 0;
	},
	handleModuleDialog() {
		this.moduleDialogFormVisible = false;
		if(this.moduleDialogStatus == 'create') {
			this.$http.post('/modules/' + this.application.id, this.module).then(function(response) {
				this.getModuleList();
          	}, function(error) {
        	  	console.log('新增模块失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增模块失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.moduleDialogStatus == 'delete') {
			this.$http.delete('/modules/' + this.module.id).then(function(response) {
				this.getModuleList();
          	}, function(error) {
        	  	console.log('删除模块失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除模块失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.moduleDialogStatus == 'update') {
			this.$http.put('/modules', this.module).then((response) => {
				this.getModuleList();
	      	}, (error) => {
	    	  	console.log('更新模块失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新模块失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
	},
	handleEnterModule(row) {
		if(row.url.indexOf('http') == 0) {
			// 模块URL以HTTP开头
			window.open(row.url);
		} else {
			window.open(this.application.url + row.url);
		}
	},
  }
});