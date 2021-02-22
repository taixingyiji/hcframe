/**
 * 虚拟机硬件配置管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		flavors:[],
		total:10,
		flavor:{
			id:0,
			version:0,
			uuid:'',
			name:'',
			memory:1,
			cores:1,
			disk:1,
			ephemeral:0,
			swap:0,
			shared:false,
			disabled:false,
			rxtxFactor:1.0,
			rxtxQuota:0,
			rxtxCap:0,
			updateTime:0,
			openStackId:null,
		},
		openstacks: [],
    	showMessage: false,
		title:'',
		type:'',
		query_flavors:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		query_openstacks:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		dialogFormVisible:false,
		textMap: {
	        update: '编辑硬件配置',
	        create: '新增硬件配置',
	        query: '查看硬件配置',
	        delete: '删除硬件配置',
	      },
	    dialogStatus: '',
	    disabled: false,
	};
  },
  mounted: function(){
	  this.getOpenStackList();
	  this.getVirtualMachineFlavorList();
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
	getVirtualMachineFlavorList() {
		this.$http.get('/virtual/machine/flavors', {params:this.query_flavors}).then((response) => {
			this.flavors = response.body.content;
			this.total = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取虚拟机硬件配置列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取虚拟机硬件配置列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQuery(row) {
		this.dialogStatus = 'query';
		this.disabled = true;
		this.flavor = row;
		this.dialogFormVisible = true;
	},
	handleCreate() {
		this.dialogStatus = 'create';
		this.disabled = false;
		this.resetFlavor();
		this.dialogFormVisible = true;
	},
	handleUpdate() {
		this.$http.put('/virtual/machine/flavors').then((response) => {
			this.getVirtualMachineFlavorList();
      	}, (error) => {
    	  	console.log('更新虚拟机硬件配置失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '更新虚拟机硬件配置失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleDialog() {
		this.dialogFormVisible = false;
		if(this.dialogStatus == 'create') {
			this.$http.post('/virtual/machine/flavors', this.flavor).then(function(response) {
				this.getVirtualMachineFlavorList();
          	}, function(error) {
        	  	console.log('新增虚拟机硬件配置失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增虚拟机硬件配置失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.dialogStatus == 'delete') {
			this.$http.delete('/virtual/machine/flavors/' + this.flavor.id).then(function(response) {
				this.getVirtualMachineFlavorList();
          	}, function(error) {
        	  	console.log('删除虚拟机硬件配置失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除虚拟机硬件配置失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
		
	},
	resetFlavor() {
		this.flavor.id = 0;
		this.flavor.version = 0;
		this.flavor.uuid = '';
		this.flavor.name = '';
		this.flavor.memory = 1;
		this.flavor.cores = 1;
		this.flavor.disk = 1;
		this.flavor.ephemeral = 0;
		this.flavor.swap = 0;
		this.flavor.shared = false;
		this.flavor.disabled = false;
		this.flavor.rxtxFactor = 1.0;
		this.flavor.rxtxQuota = 0;
		this.flavor.rxtxCap = 0;
		this.flavor.updateTime = 0;
		this.flavor.openStackId = null;
	},
	handleDelete(row) {
		this.dialogStatus = 'delete';
		this.disabled = true;
		this.flavor = row;
		this.dialogFormVisible = true;
	},
	// 日期格式化
	dateFormat(row, column) {
		var datetime = row[column.property];
		return datetime;
	},
	handleCurrentChange(page) {
		this.query_flavors.page = page - 1;
		this.getVirtualMachineFlavorList();
	}
  }
});