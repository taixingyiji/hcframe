/**
 * 微服务管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		microservices:[],
		total_microservices:10,
		microservice:{
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
		query_microservices:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		microserviceDialogFormVisible:false,
		microserviceTextMap: {
	        update: '编辑服务',
	        create: '新增服务',
	        query: '查看服务',
	        delete: '删除服务',
	      },
	    microserviceDialogStatus: '',
	    microserviceDialogDisabled: false,
	};
  },
  mounted: function(){
	  this.getMicroServiceList();
  },
  methods: {
	  getMicroServiceList() {
		this.$http.get('/microservices', {params:this.query_microservices}).then((response) => {
			this.microservices = response.body.content;
			this.total_microservices = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取服务列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取服务列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryMicroService(row) {
		this.microserviceDialogStatus = 'query';
		this.microserviceDialogDisabled = true;
		this.microservice = row;
		this.microserviceDialogFormVisible = true;
	},
	handleCreateMicroService() {
		this.microserviceDialogStatus = 'create';
		this.microserviceDialogDisabled = false;
		this.resetMicroService();
		this.microserviceDialogFormVisible = true;
	},
	handleUpdateMicroService(row) {
		this.microserviceDialogDisabled = false;
		this.microserviceDialogStatus = 'update';
		this.microservice = row;
		this.microserviceDialogFormVisible = true;
	},
	handleMicroServiceDialog() {
		this.microserviceDialogFormVisible = false;
		if(this.microserviceDialogStatus == 'create') {
			this.$http.post('/microservices', this.microservice).then(function(response) {
				this.getMicroServiceList();
          	}, function(error) {
        	  	console.log('新增服务失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增服务失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.microserviceDialogStatus == 'delete') {
			this.$http.delete('/microservices/' + this.microservice.id).then(function(response) {
				this.getMicroServiceList();
          	}, function(error) {
        	  	console.log('删除服务失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除服务失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.microserviceDialogStatus == 'update') {
			this.$http.put('/microservices', this.microservice).then((response) => {
				this.getMicroServiceList();
	      	}, (error) => {
	    	  	console.log('更新服务失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新服务失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
		
	},
	resetMicroService() {
		this.microservice.id = 0;
		this.microservice.version = 0;
		this.microservice.name = '';
		this.microservice.description = '';
		this.microservice.url = '';
		this.microservice.rank = 99;
		this.microservice.createTime = 0;
		this.microservice.modifiedTime = 0;
	},
	handleDeleteMicroService(row) {
		this.microserviceDialogStatus = 'delete';
		this.microserviceDialogDisabled = true;
		this.microservice = row;
		this.microserviceDialogFormVisible = true;
	},
	handleCurrentChangeMicroService(page) {
		this.query_microservices.page = page - 1;
		this.getMicroServiceList();
	},
	handleEnterMicroService(row) {
		window.open(row.url);
	},
  }
});