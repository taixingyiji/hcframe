/**
 * 面包屑组件
 */
const Foo = { template: '<div>foo</div>' };
const Bar = { template: '<div>bar</div>' };
const routes = [
	  { path: '/foo', component: Foo },
	  { path: '/bar', component: Bar }
	];
const router = new VueRouter({
	  routes // short for `routes: routes`
	})
const app = new Vue({
	  el: '#breadcrumb',
	  router:router,
	  data: function() {
			return {
				levelList: [
					{
						path:'/home',
						redirect:'noredirect',
						meta: { title: '首页' }
					},
					{
						path:'/test',
						meta: { title: '测试' }
					},
					{
						path:'/production',
						redirect:'noredirect',
						meta: { title: '生产' }
					}
				]
			}
	  },
	  methods: {
		  
	  }
});