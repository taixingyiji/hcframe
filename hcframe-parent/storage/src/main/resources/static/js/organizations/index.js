/**
 * 组织机构管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		// 组织机构树
		organizations:[],
		// 组织机构增删改查
		organization:{
			id:0,
			name:'',
			code:'',
			rank:0,
			description:'',
			parentId:null
		},
		// 组织机构树结构属性
		organization_property: {
			children: 'children',
	        label: 'name'
	    },
	    // 是否显示消息
	    showMessage: false,
	    // 消息标题
		title:'',
		// 消息类型
		type:'',
		// 是否显示组织机构增删改查对话框
		dialogFormVisible:false,
		// 组织机构增删改查对话框标题
		textMap: {
	        update: '编辑组织机构',
	        create: '新增组织机构',
	        query: '查看组织机构',
	        delete: '删除组织机构',
	      },
	    // 组织机构增删改查对话框对话框状态
	    dialogStatus: '',
	    // 组织机构增删改查对话框是否可编辑
	    disabled: false,
	    // 组织机构增删改查对话框检索框
	    filterName:'',
	    // 用户列表
	    users: [],
	    // 用户列表总页数
	    total_pages:10,
	    // 用户列表检索条件
	    query_user:{
	    	userName:'',
	    	organizationId:null,
	    	page:0,
			size:10,
			sort:'userName,ASC',
	    },
	    // 组织机构用户列表
	    organization_users: [],
	    // 组织机构用户列表总页数
	    organization_total_pages:10,
	    // 组织机构用户列表检索条件
	    query_organization_user:{
	    	userName:'',
	    	organizationId:null,
	    	page:0,
			size:10,
			sort:'userName,ASC',
	    },
	    // 组织机构添加用户对话框标题
	    addUserDialogTitle: '添加用户',
	    // 是否显示组织机构添加用户对话框
	    addUserDialogFormVisible : false,
	    // 所选组织机构待添加的用户列表
	    userMultipleSelection: [],
	};
  },
  mounted: function(){
	  this.getOrganizationTree();
  },
  watch: {
	  filterName(val) {
        this.$refs.organization_tree.filter(val);
      }
  },
  methods: {
	  // 获取组织机构树
	  getOrganizationTree(){
		  this.$http.get('/organizations/tree').then((response) => {
				this.organizations = response.body;
	      	}, (error) => {
	    	  	console.log('获取组织机构树失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '获取组织机构树失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
	  },
	  // 处理组织机构树节点点击事件
	  handleQueryUserList(data) {
	        this.setOrganization(data);
	        this.query_organization_user.organizationId = data.id;
	        // 更新组织机构用户列表
	        this.getOrganizationUserList();
	  },
	  // 处理组织机构树中增加按钮事件
	  handleCreate(data) {
		  this.dialogStatus = 'create';
		  this.disabled = false;
		  this.resetOrganization();
		  this.dialogFormVisible = true;
		  // 记录父组织机构编号
		  this.organization.parentId = data.id;
      },
      // 处理新增按钮事件
      handleCreateRoot() {
		  this.dialogStatus = 'create';
		  this.disabled = false;
		  this.resetOrganization();
		  this.dialogFormVisible = true;
      },
      // 处理组织机构树中删除按钮事件
      handleDelete(node, data) {
    	  this.dialogStatus = 'delete';
    	  this.disabled = true;
    	  this.setOrganization(data);
    	  this.dialogFormVisible = true;
      },
      // 处理组织机构树中删除按钮事件
      handleUpdate(node, data) {
    	  this.disabled = false;
    	  this.dialogStatus = 'update';
    	  this.setOrganization(data);
    	  this.dialogFormVisible = true;
      },
      // 处理组织机构树中查看按钮事件
      handleQuery(data) {
    	  this.dialogStatus = 'query';
    	  this.disabled = true;
    	  this.setOrganization(data);
    	  this.dialogFormVisible = true;
      },
      // 处理组织机构树增删改查对话框
      handleDialog() {
  		this.dialogFormVisible = false;
  		if(this.dialogStatus == 'create') {
  			this.$http.post('/organizations', this.organization).then(function(response) {
  				this.getOrganizationTree();
            	}, function(error) {
          	  	console.log('新增组织机构失败');
          	  	console.log(error);
          	  	this.showMessage = true;
          	  	this.title = '新增组织机构失败:' + error.body.message;
          	  	this.type = 'error';
            	});
  		} else if(this.dialogStatus == 'delete') {
  			this.$http.delete('/organizations/' + this.organization.id).then(function(response) {
  				this.getOrganizationTree();
            	}, function(error) {
          	  	console.log('删除组织机构失败');
          	  	console.log(error);
          	  	this.showMessage = true;
          	  	this.title = '删除组织机构失败:' + error.body.message;
          	  	this.type = 'error';
            	});
  		} else if (this.dialogStatus == 'update') {
  			this.$http.put('/organizations', this.organization).then(function(response) {
  				this.getOrganizationTree();
            	}, function(error) {
          	  	console.log('更新组织机构失败');
          	  	console.log(error);
          	  	this.showMessage = true;
          	  	this.title = '更新组织机构失败:' + error.body.message;
          	  	this.type = 'error';
            	});
  		}
  	},
  	// 重置组织机构增删改查对话框变量
  	resetOrganization() {
  		this.organization.id = 0;
  		this.organization.name = '';
  		this.organization.code = '';
  		this.organization.rank = 0;
  		this.organization.description = '';
  		this.organization.parentId = null;
  	},
  	// 设置组织机构增删改查对话框变量
  	setOrganization(data) {
  		this.organization.id = data.id;
  		this.organization.name = data.name;
  		this.organization.code = data.code;
  		this.organization.rank = data.rank;
  		this.organization.description = data.description;
  		this.organization.parentId = null;
  	},
  	// 处理组织机构检索事件
  	filterNode(value, data) {
        if (!value) return true;
        return data.name.indexOf(value) !== -1;
    },
    // 获取用户列表
    getUserList() {
    	this.$http.get('/auth/users', {params:this.query_user}).then((response) => {
			this.users = response.body.content;
			this.total_pages = response.body.totalPages;
      	}, (error) => {
    	  	console.log('获取认证用户列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取认证用户列表:' + error.body.message;
    	  	this.type = 'error';
      	});
    },
    // 获取组织机构用户列表
    getOrganizationUserList() {
    	this.$http.get('/auth/users', {params:this.query_organization_user}).then((response) => {
			this.organization_users = response.body.content;
			this.organization_total_pages = response.body.totalPages;
      	}, (error) => {
    	  	console.log('获取认证用户列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取认证用户列表:' + error.body.message;
    	  	this.type = 'error';
      	});
    },
    // 处理组织机构增加用户复选框事件
    handleSelectionChange(val) {
        this.userMultipleSelection = val;
    },
    // 处理组织机构增加用户对话框
    handleAddUserDialog() {
    	if(this.userMultipleSelection.length > 0) {
    		this.addUserDialogFormVisible = false;
    		for(var i=0; i<this.userMultipleSelection.length; i++) {
    			this.$http.post('/organizations/' + this.organization.id + '/' + this.userMultipleSelection[i].id).then(function(response) {
      				this.getOrganizationUserList();
            	}, function(error) {
	          	  	console.log('添加用户到组织机构失败');
	          	  	console.log(error);
	          	  	this.showMessage = true;
	          	  	this.title = '添加用户到组织机构失败:' + error.body.message;
	          	  	this.type = 'error';
            	});
        	}
    	}
    },
    // 处理组织机构增加用户按钮事件
    handleAdd() {
    	// 获取用户列表
    	this.getUserList();
    	// 显示对话框
    	this.addUserDialogFormVisible = true;
    },
    // 处理组织机构中移除用户事件
    handleRemove(data) {
    	this.$confirm('此操作将组织机构中移除用户, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
        	this.$http.delete('/organizations/' + this.organization.id + '/' + data.id).then(function(response) {
    			this.getOrganizationUserList();
    			this.$message({
	              type: 'success',
	              message: '移除成功!'
	            });
          	}, function(error) {
          	  	console.log('添加用户到组织机构失败');
          	  	console.log(error);
          	  	this.showMessage = true;
          	  	this.title = '添加用户到组织机构失败:' + error.body.message;
          	  	this.type = 'error';
          	});
            
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消移除'
            });
          });
    }
  }
});