/**
 * 角色管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		roles: [],
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
		role:{
			id:0,
			version:0,
			name:'',
			description:'',
			disabled:false,
			createTime:0,
			modifiedTime:0
		},
		dialogFormVisible:false,
		textMap: {
	        update: '编辑角色',
	        create: '新增角色',
	        query: '查看角色',
	        delete: '删除角色',
	      },
	    dialogStatus: '',
	    disabled: false,
	};
  },
  mounted: function(){
	  this.getRoleList();
  },
  methods: {
	getRoleList() {
		this.$http.get('/auth/roles', {params:this.query}).then((response) => {
			this.roles = response.body.content;
			this.total_pages = response.body.totalPages;
      	}, (error) => {
    	  	console.log('获取角色列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取角色列表:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQuery(row) {
		this.dialogStatus = 'query';
		this.disabled = true;
		this.role = row;
		this.dialogFormVisible = true;
	},
	handleCreate() {
		this.dialogStatus = 'create';
		this.disabled = false;
		this.resetRole();
		this.dialogFormVisible = true;
	},
	handleUpdate(row) {
		this.disabled = false;
		this.dialogStatus = 'update';
		this.role = row;
		this.dialogFormVisible = true;
	},
	handleDialog() {
		this.dialogFormVisible = false;
		if(this.dialogStatus == 'update') {
			this.$http.put('/auth/roles', this.role).then((response) => {
				this.getRoleList();
	      	}, (error) => {
	    	  	console.log('更新角色失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新角色失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		} else if(this.dialogStatus == 'create') {
			this.$http.post('/auth/roles', this.role).then(function(response) {
				this.getRoleList();
          	}, function(error) {
        	  	console.log('新增角色失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增角色失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.dialogStatus == 'delete') {
			this.$http.delete('/auth/roles/' + this.role.id).then(function(response) {
				this.getRoleList();
          	}, function(error) {
        	  	console.log('删除角色失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除角色失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
		
	},
	resetRole() {
		this.role.id = 0;
		this.role.version = 0;
		this.role.name = '';
		this.role.description = '';
		this.role.disabled = false;
		this.role.createTime = 0;
		this.role.modifiedTime = 0;
	},
	handleDelete(row) {
		this.dialogStatus = 'delete';
		this.disabled = true;
		this.role = row;
		this.dialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	} 
  }
});