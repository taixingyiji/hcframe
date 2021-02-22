/**
 * 物理存储区管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		regions:[],
		total_regions:10,
		region:{
			id:0,
			version:0,
			name:'',
			description:'',
			path:'',
			createTime:0,
			modifiedTime:0
		},
    	showMessage: false,
		title:'',
		type:'',
		query_regions:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		regionDialogFormVisible:false,
		regionTextMap: {
	        update: '编辑物理存储区',
	        create: '新增物理存储区',
	        query: '查看物理存储区',
	        delete: '删除物理存储区',
	      },
	    regionDialogStatus: '',
	    regionDialogDisabled: false,
	};
  },
  mounted: function(){
	  this.getStorageRegionList();
  },
  methods: {
	  getStorageRegionList() {
		this.$http.get('/regions', {params:this.query_regions}).then((response) => {
			this.regions = response.body.content;
			this.total_regions = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取物理存储区列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取物理存储区列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryStorageRegion(row) {
		this.regionDialogStatus = 'query';
		this.regionDialogDisabled = true;
		this.region = row;
		this.regionDialogFormVisible = true;
	},
	handleCreateStorageRegion() {
		this.regionDialogStatus = 'create';
		this.regionDialogDisabled = false;
		this.resetStorageRegion();
		this.regionDialogFormVisible = true;
	},
	handleUpdateStorageRegion(row) {
		this.regionDialogDisabled = false;
		this.regionDialogStatus = 'update';
		this.region = row;
		this.regionDialogFormVisible = true;
	},
	handleStorageRegionDialog() {
		this.regionDialogFormVisible = false;
		if(this.regionDialogStatus == 'create') {
			this.$http.post('/regions', this.region).then(function(response) {
				this.getStorageRegionList();
          	}, function(error) {
        	  	console.log('新增物理存储区失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增物理存储区失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.regionDialogStatus == 'delete') {
			this.$http.delete('/regions/' + this.region.id).then(function(response) {
				this.getStorageRegionList();
          	}, function(error) {
        	  	console.log('删除物理存储区失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除物理存储区失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.regionDialogStatus == 'update') {
			this.$http.put('/regions', this.region).then((response) => {
				this.getStorageRegionList();
	      	}, (error) => {
	    	  	console.log('更新物理存储区失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新物理存储区失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
		
	},
	resetStorageRegion() {
		this.region.id = 0;
		this.region.version = 0;
		this.region.name = '';
		this.region.description = '';
		this.region.path = '';
		this.region.createTime = 0;
		this.region.modifiedTime = 0;
	},
	handleDeleteStorageRegion(row) {
		this.regionDialogStatus = 'delete';
		this.regionDialogDisabled = true;
		this.region = row;
		this.regionDialogFormVisible = true;
	},
	handleCurrentChangeStorageRegion(page) {
		this.query_regions.page = page - 1;
		this.getStorageRegionList();
	},
	formatHealthy(row, column) {
		return row.healthy ? '正常':'宕机';
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
	}
  }
});