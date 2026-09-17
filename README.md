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

1.  git拉去项目
 
2. 刷新maven依赖

3. 进入目录
```bash 
cd hcframe-parent/ 
```   

4. 执行``mvn install``

5. 刷新maven依赖

#### BaseMapper 原生 SQL

已有 `selectSql`（列表）、`selectOneSql`（单条）和 `selectSqlByPage`（分页）查询方法。
写入和统计可使用下列方法，均提供 `String sql` 和 `String sql, Map<String, Object> params` 两种形式：

| 方法 | 用途 | 返回值 |
| --- | --- | --- |
| `executeSql` / `excuteSql` | 执行单条非查询 SQL（DML、DDL），两种拼写等价 | JDBC 更新计数，DDL 由驱动决定 |
| `insertSql` | 插入 | 受影响行数，不返回生成的主键 |
| `updateSql` | 更新 | 受影响行数，未命中返回 0 |
| `deleteSql` | 删除 | 受影响行数，未命中返回 0 |
| `countSql` | 执行完整的 `SELECT COUNT(*) ...` 查询 | `Long` 计数，不自动包装 SQL |

```java
baseMapper.insertSql("INSERT INTO users (id, name) VALUES (#{id}, #{name})",
        Map.of("id", 1, "name", "张三"));
baseMapper.updateSql("UPDATE users SET name = #{name} WHERE id = #{id}",
        Map.of("id", 1, "name", "李四"));
Long count = baseMapper.countSql("SELECT COUNT(*) FROM users WHERE name = #{name}",
        Map.of("name", "李四"));
baseMapper.deleteSql("DELETE FROM users WHERE id = #{id}", Map.of("id", 1));
```

新增方法使用 `#{参数名}` 绑定值，参数 Map 可为 null 或不可变 Map，调用时不会修改它；`sql` 是内部保留参数名。
SQL 结构由可信代码提供，外部输入通过参数绑定传入。调用沿用配置的 `SqlSessionTemplate`，支持现有动态数据源和 Spring 事务；多次写入需要原子性时，在业务方法上使用 `@Transactional`。DDL 的事务行为取决于数据库。
