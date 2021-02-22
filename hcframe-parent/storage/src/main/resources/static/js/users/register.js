/**
 * 用户注册
 */
new Vue({
  el: '#register-app',
  data: function() {
	return {
		registerForm: {
	        userName: 'admin',
	        password: '123456',
	        email:''
	      },
	    password2: '',
	    showMessage: false,
	    title:'',
	    type:'',
	};
  },
  methods: {
	handleRegister() {
		if(this.registerForm.password != this.password2) {
			this.showMessage = true;
    	  	this.title = '两次输入密码不一致';
    	  	this.type = 'error';
		} else {
			this.$http.post('/auth/users', this.registerForm).then(function(response) {
				self.location = '/auth/users/login';
          	}, function(error) {
        	  	console.log('注册失败');
        	  	console.log(error);
        	  	this.showMessage = true;
        	  	this.title = '注册失败:' + error.body.message;
        	  	this.type = 'error';
          	});
		}
      	
	  }
	}
});