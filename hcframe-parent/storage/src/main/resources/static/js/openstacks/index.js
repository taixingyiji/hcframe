/**
 * 云平台管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		openstacks: [],
    	total_pages:10,
    	showMessage: false,
		title:'',
		type:'',
		query:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		openstack:{
			id:0,
			version:0,
			name:'',
			description:'',
			createTime:0,
			modifiedTime:0
		},
		dialogFormVisible:false,
		textMap: {
	        update: '编辑云平台',
	        create: '新增云平台',
	        query: '查看云平台',
	        delete: '删除云平台',
	      },
	    dialogStatus: '',
	    disabled: false,
	};
  },
  mounted: function(){
	  this.getOpenStackList();
  },
  methods: {
	getOpenStackList() {
		this.$http.get('/openstacks', {params:this.query}).then((response) => {
			this.openstacks = response.body.content;
			this.total_pages = response.body.totalPages;
      	}, (error) => {
    	  	console.log('获取云平台列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取云平台列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQuery(row) {
		this.dialogStatus = 'query';
		this.disabled = true;
		this.openstack = row;
		this.dialogFormVisible = true;
	},
	handleCreate() {
		this.dialogStatus = 'create';
		this.disabled = false;
		this.resetOpenStack();
		this.dialogFormVisible = true;
	},
	handleUpdate(row) {
		this.disabled = false;
		this.dialogStatus = 'update';
		this.openstack = row;
		this.dialogFormVisible = true;
	},
	handleDialog() {
		this.dialogFormVisible = false;
		if(this.dialogStatus == 'update') {
			this.$http.put('/openstacks', this.openstack).then((response) => {
				this.getOpenStackList();
	      	}, (error) => {
	    	  	console.log('更新云平台失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新云平台失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		} else if(this.dialogStatus == 'create') {
			this.$http.post('/openstacks', this.openstack).then(function(response) {
				this.getOpenStackList();
          	}, function(error) {
        	  	console.log('新增云平台失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增云平台失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.dialogStatus == 'delete') {
			this.$http.delete('/openstacks/' + this.openstack.id).then(function(response) {
				this.getOpenStackList();
          	}, function(error) {
        	  	console.log('删除云平台失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除云平台失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
		
	},
	resetOpenStack() {
		this.openstack.id = 0;
		this.openstack.version = 0;
		this.openstack.name = '';
		this.openstack.description = '';
		this.openstack.createTime = 0;
		this.openstack.modifiedTime = 0;
	},
	handleDelete(row) {
		this.dialogStatus = 'delete';
		this.disabled = true;
		this.openstack = row;
		this.dialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	} 
  }
});