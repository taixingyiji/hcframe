/**
 * 用户管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		users: [],
    	total:10,
    	showMessage: false,
		title:'',
		type:'',
		query:{
			userName:'',
			page:0,
			size:10,
			sort:'userName,ASC',
		},
		user:{
			id:0,
			version:0,
			userName:'',
			realName:'',
			password:'',
			expired:false,
			disabled:false,
			locked:false,
			credentialsExpired:false,
			email:'',
			phoneNumber:'',
			createTime:0,
			modifiedTime:0
		},
		dialogFormVisible:false,
		textMap: {
	        update: '编辑用户',
	        create: '新增用户',
	        query: '查看用户',
	        delete: '删除用户',
	      },
	    dialogStatus: '',
	    disabled: false,
	    grantFormVisible: false,
	    role:{
	    	user_id:0,
	    	selected:[],
	    	options:[{
	    		value: '选项1',
	    		label: '黄金糕'
	    	},{
	    		value: '选项2',
	    		label: '双皮奶'
	    	}],
	    },
	    queryRoles : {
	    	name:'',
			page:0,
			size:10,
			sort:'name,ASC',
	    },
	};
  },
  mounted: function(){
	  this.getUserList();
  },
  methods: {
	getUserList() {
		this.$http.get('/auth/users', {params:this.query}).then((response) => {
			this.users = response.body.content;
			this.total = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取认证用户列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取认证用户列表:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	QueryUserList() {
		this.query.page = 0;
		this.$http.get('/auth/users', {params:this.query}).then((response) => {
			this.users = response.body.content;
			this.total = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取认证用户列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取认证用户列表:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQuery(row) {
		this.dialogStatus = 'query';
		this.disabled = true;
		this.user = row;
		this.user.password = '';
		this.dialogFormVisible = true;
	},
	handleCreate() {
		this.dialogStatus = 'create';
		this.disabled = false;
		this.resetUser();
		this.dialogFormVisible = true;
	},
	handleUpdate(row) {
		this.disabled = false;
		this.dialogStatus = 'update';
		this.user = row;
		this.user.password = '';
		this.dialogFormVisible = true;
	},
	handleDialog() {
		this.dialogFormVisible = false;
		if(this.dialogStatus == 'update') {
			this.$http.put('/auth/users', this.user).then((response) => {
				this.getUserList();
	      	}, (error) => {
	    	  	console.log('更新认证用户列表失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新认证用户列表:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		} else if(this.dialogStatus == 'create') {
			this.$http.post('/auth/users', this.user).then(function(response) {
				this.getUserList();
          	}, function(error) {
        	  	console.log('新增认证用户失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增认证用户失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.dialogStatus == 'delete') {
			this.$http.delete('/auth/users/' + this.user.id).then(function(response) {
				this.getUserList();
          	}, function(error) {
        	  	console.log('删除认证用户失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除认证用户失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
		
	},
	resetUser() {
		this.user.id = 0;
		this.user.version = 0;
		this.user.userName = '';
		this.user.realName = '';
		this.user.password = '';
		this.user.expired = false;
		this.user.disabled = false;
		this.user.locked = false;
		this.user.credentialsExpired = false;
		this.user.email = '';
		this.user.phoneNumber = '';
		this.user.createTime = 0;
		this.user.modifiedTime = 0;
	},
	handleDelete(row) {
		this.dialogStatus = 'delete';
		this.disabled = true;
		this.user = row;
		this.user.password = '';
		this.dialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	},
	handleGrant(row) {
		// 获取全部角色列表
		this.$http.get('/auth/roles', {params:this.queryRoles}).then((response) => {
			this.role.options = [];
			for(var i=0; i<response.body.content.length; i++) {
				var r = response.body.content[i];
				var option = {};
				option.label = r.description;
				option.value = r.id;
				this.role.options.push(option);
			}
      	}, (error) => {
    	  	console.log('获取角色列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取角色列表:' + error.body.message;
    	  	this.type = 'error';
      	});
		// 根据用户编号获取用户角色列表
		this.$http.get('/auth/roles/users/' + row.id).then((response) => {
			this.role.selected = [];
			if(response.body.length > 0) {
				for(var i=0; i<response.body.length; i++) {
					this.role.selected.push(response.body[i].id);
				}
			}
      	}, (error) => {
    	  	console.log('根据用户编号获取角色列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '根据用户编号获取角色列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
		this.role.user_id = row.id;
		this.grantFormVisible = true;
	},
	handleGrantDialog() {
		this.grantFormVisible = false;
		this.$http.put('/auth/roles/users/' + this.role.user_id, this.role.selected).then((response) => {
			this.getUserList();
      	}, (error) => {
    	  	console.log('为用户授权失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '为用户授权失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleCurrentChange(page) {
		this.query.page = page - 1;
		this.getUserList();
	}
  }
});