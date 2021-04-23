package com.hcframe.base.module.shiro;

import com.hcframe.base.module.shiro.service.SystemRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    SystemRealm systemRealm;

    /**
     * 不加这个注解不生效，具体不详
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * 将自己的验证方式加入容器
     */
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCachingEnabled(false);
        return new CustomRealm();
    }
    /**
     * @author lhc
     * @description // 自定义subject工厂
     * @date 4:50 下午 2021/4/19
     * @params []
     * @return org.apache.shiro.web.mgt.DefaultWebSubjectFactory
     **/
    @Bean
    public DefaultWebSubjectFactory subjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }
    /**
     * @author lhc
     * @description // 自定义session管理器
     * @date 5:50 下午 2021/4/19
     * @params []
     * @return org.apache.shiro.session.mgt.SessionManager
     **/
    @Bean
    public SessionManager sessionManager() {
        DefaultSessionManager shiroSessionManager = new DefaultSessionManager();
        // 关闭session校验轮询
        shiroSessionManager.setSessionValidationSchedulerEnabled(false);
        return shiroSessionManager;
    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     */
    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 禁用session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // 设置自定义subject工厂
        securityManager.setSubjectFactory(subjectFactory());
        // 设置自定义session管理器
        securityManager.setSessionManager(sessionManager());
        // 设置自定义realm
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }


    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String, Filter> filters = new HashMap<>(1);
        filters.put("auth", new NoStateFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(systemRealm.setShiroUrl());
        return shiroFilterFactoryBean;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


}
