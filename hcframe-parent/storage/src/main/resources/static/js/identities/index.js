/**
 * 云平台账户管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		openstack_identities:[],
		openstack_identity:{
			id:0,
			version:0,
			userName:'',
			tenantName:'',
			password:'',
			endpoint:'',
			region:'',
			openStackId:null,
			roleName:'',
			createTime:0,
			modifiedTime:0
		},
		openstacks: [],
    	total_pages:10,
    	showMessage: false,
		title:'',
		type:'',
		query_openstack_identities:{
			userName:'',
			page:0,
			size:10,
			sort:'userName,ASC',
		},
		query_openstacks:{
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
	        update: '编辑云平台账户',
	        create: '新增云平台账户',
	        query: '查看云平台账户',
	        delete: '删除云平台账户',
	      },
	    dialogStatus: '',
	    disabled: false,
	    roles:[
	    	{
	    		name:'ROLE_ADMIN',
	    		description:'管理员账户'
	    	},{
	    		name:'ROLE_USER',
	    		description:'普通账户'
	    	}
	    ],
	};
  },
  mounted: function(){
	  this.getOpenStackList();
	  this.getOpenStackIdentityList();
  },
  methods: {
	getOpenStackList() {
		this.$http.get('/openstacks', {params:this.query_openstacks}).then((response) => {
			this.openstacks = response.body.content;
      	}, (error) => {
    	  	console.log('获取云平台列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取云平台列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	getOpenStackIdentityList() {
		this.$http.get('/openstack/identities', {params:this.query_openstack_identities}).then((response) => {
			this.openstack_identities = response.body.content;
			this.total_pages = response.body.totalPages;
      	}, (error) => {
    	  	console.log('获取云平台账户列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取云平台账户列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQuery(row) {
		this.dialogStatus = 'query';
		this.disabled = true;
		this.openstack_identity = row;
		this.dialogFormVisible = true;
	},
	handleCreate() {
		this.dialogStatus = 'create';
		this.disabled = false;
		this.resetOpenStackIdentity();
		this.dialogFormVisible = true;
	},
	handleUpdate(row) {
		this.disabled = false;
		this.dialogStatus = 'update';
		this.openstack_identity = row;
		this.dialogFormVisible = true;
	},
	handleDialog() {
		this.dialogFormVisible = false;
		if(this.dialogStatus == 'update') {
			this.$http.put('/openstack/identities', this.openstack_identity).then((response) => {
				this.getOpenStackIdentityList();
	      	}, (error) => {
	    	  	console.log('更新云平台账户失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新云平台账户失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		} else if(this.dialogStatus == 'create') {
			this.$http.post('/openstack/identities', this.openstack_identity).then(function(response) {
				this.getOpenStackIdentityList();
          	}, function(error) {
        	  	console.log('新增云平台账户失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增云平台账户失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.dialogStatus == 'delete') {
			this.$http.delete('/openstack/identities/' + this.openstack_identity.id).then(function(response) {
				this.getOpenStackIdentityList();
          	}, function(error) {
        	  	console.log('删除云平台账户失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除云平台账户失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
		
	},
	resetOpenStackIdentity() {
		this.openstack_identity.id = 0;
		this.openstack_identity.version = 0;
		this.openstack_identity.userName = '';
		this.openstack_identity.tenantName = '';
		this.openstack_identity.password = '';
		this.openstack_identity.endpoint = '';
		this.openstack_identity.region = '';
		this.openstack_identity.openStackId = null;
		this.openstack_identity.roleName = null;
		this.openstack_identity.createTime = 0;
		this.openstack_identity.modifiedTime = 0;
	},
	handleDelete(row) {
		this.dialogStatus = 'delete';
		this.disabled = true;
		this.openstack_identity = row;
		this.dialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	} 
  }
});