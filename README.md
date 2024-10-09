## 项目简介

此项目通过精简的代码实现了广泛适用的功能，使小型项目能够根据业务需求灵活扩展，快速开发。

## 开发环境

- Java 21
- Tomcat 10.1
- Maven 3.9.9
- MySQL 8.4.2
- Redis 7.4.0

## 项目搭建

1. 使用 `0_MySQL创建数据库.sql` 创建数据库
2. 使用 `1_MySQL创建用户并授权.sql` 创建用户并授权
3. 修改 `database.properties` 和 `redis.properties` 中用于数据库连接的相关配置
4. 启动项目
5. 使用 `2_MySQL数据初始化.sql` 初始化数据

## 其他说明

- `.sql` 文件在 `数据库` 目录下
- `.properties` 文件在 `src/main/resources` 目录下
- 项目使用 `Hibernate` 框架在启动时自动创建数据库表
