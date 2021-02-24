# 基础包依赖及框架配置

#### 介绍
基础包，对工具类及框架代码进行封装

#### 功能
1. 封装Shiro权限部分，通过接口继承即可
2. 对SwaggerUI部分封装，可通过yml文件进行配置
3. 对Druid连接池部分进行封装，可通过yml文件进行配置
4. 提供封装VO返回结果
5. 提供PageHelper方法封装
6. 将pageNum,pageSize,order,sort封装成对象，并提供是否包含排序校验，提供sql拼装校验
7. 异常统一封装
8. 通用mapper封装
9. 接口参数日志封装，可通过yml文件进行配置
10. redis工具类封装，可通过yml文件进行配置
11. Spring上下文工具类封装
12. 加密工具类封装
13. 日期工具类封装


#### 安装教程

1.  git拉去目录
 
2.  发布达梦数据库驱动包
```bash
mvn install:install-file -Dfile=lib/Dm7JdbcDriver18.jar -DgroupId=com.dm -DartifactId=Dm7JdbcDriver -Dversion=1.8 -Dpackaging=jar 
``` 
3.  发布oracle数据库驱动包
```bash
mvn install:install-file -Dfile=lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar
``` 
4.  刷新maven依赖
5.  进入基础包目录
```bash 
cd hcframe-parent/hcframe-base/ 
```   
6.  执行``mvn install``
7.  刷新其他项目maven依赖
8.  刷新maven依赖
