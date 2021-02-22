/**
 * 汉堡组件
 */
new Vue({
	  el: '#hamburger',
	  data: function() {
			return {
			      isActive: true
			}
	  },
	  methods: {
		  toggleClick() {
			this.isActive = !this.isActive;
		  }
	  }
});