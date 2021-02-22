/**
 * 逻辑存储区管理主页
 */
new Vue({
  el: '#content',
  data: function() {
	return {
		buckets:[],
		total_buckets:10,
		bucket:{
			id:0,
			version:0,
			name:'',
			description:'',
			totalSize:0,
			createTime:0,
			modifiedTime:0
		},
    	showMessage: false,
		title:'',
		type:'',
		query_buckets:{
			name:'',
			page:0,
			size:10,
			sort:'name,ASC',
		},
		bucketDialogFormVisible:false,
		bucketTextMap: {
	        update: '编辑逻辑存储区',
	        create: '新增逻辑存储区',
	        query: '查看逻辑存储区',
	        delete: '删除逻辑存储区',
	      },
	    bucketDialogStatus: '',
	    bucketDialogDisabled: false,
	};
  },
  mounted: function(){
	  this.getBucketList();
  },
  methods: {
	  getBucketList() {
		this.$http.get('/buckets', {params:this.query_buckets}).then((response) => {
			this.buckets = response.body.content;
			this.total_buckets = response.body.totalElements;
      	}, (error) => {
    	  	console.log('获取逻辑存储区列表失败');
    	  	console.log(error);
    	  	this.showMessage = true;
    	  	this.title = '获取逻辑存储区列表失败:' + error.body.message;
    	  	this.type = 'error';
      	});
	},
	handleQueryBucket(row) {
		this.bucketDialogStatus = 'query';
		this.bucketDialogDisabled = true;
		this.bucket = row;
		this.bucketDialogFormVisible = true;
	},
	handleCreateBucket() {
		this.bucketDialogStatus = 'create';
		this.bucketDialogDisabled = false;
		this.resetBucket();
		this.bucketDialogFormVisible = true;
	},
	handleUpdateBucket(row) {
		this.bucketDialogDisabled = false;
		this.bucketDialogStatus = 'update';
		this.bucket = row;
		this.bucketDialogFormVisible = true;
	},
	handleBucketDialog() {
		this.bucketDialogFormVisible = false;
		if(this.bucketDialogStatus == 'create') {
			this.$http.post('/buckets', this.bucket).then(function(response) {
				this.getBucketList();
          	}, function(error) {
        	  	console.log('新增逻辑存储区失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '新增逻辑存储区失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.bucketDialogStatus == 'delete') {
			this.$http.delete('/buckets/' + this.bucket.id).then(function(response) {
				this.getBucketList();
          	}, function(error) {
        	  	console.log('删除逻辑存储区失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '删除逻辑存储区失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		} else if(this.bucketDialogStatus == 'update') {
			this.$http.put('/buckets', this.bucket).then((response) => {
				this.getBucketList();
	      	}, (error) => {
	    	  	console.log('更新逻辑存储区失败');
	    	  	console.log(error);
	    	  	this.showMessage = true;
	    	  	this.title = '更新逻辑存储区失败:' + error.body.message;
	    	  	this.type = 'error';
	      	});
		}
		
	},
	resetBucket() {
		this.bucket.id = 0;
		this.bucket.version = 0;
		this.bucket.name = '';
		this.bucket.description = '';
		this.bucket.totalSize = 0;
		this.bucket.createTime = 0;
		this.bucket.modifiedTime = 0;
	},
	handleDeleteBucket(row) {
		this.bucketDialogStatus = 'delete';
		this.bucketDialogDisabled = true;
		this.bucket = row;
		this.bucketDialogFormVisible = true;
	},
	handleCurrentChangeBucket(page) {
		this.query_buckets.page = page - 1;
		this.getBucketList();
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