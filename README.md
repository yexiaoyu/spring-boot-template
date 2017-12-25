# spring-boot-template
框架环境：
Spring Boot1.5.9.RELEASE
mybatis3.4.5
使用druid数据库连接池
pagehelper分页工具
redis集成
session共享集成
swagger2接口测试和文档

工具：
JDK1.8
mysql5.7.20
maven3.5
Idea2017.3

模块说明
parent定义使用的外部jar包版本
common定义常用常量和工具类
persistence定义基础mapper接口
redis定义按照数据类型，定义常用方法
model定义mapper和model，SQL的provider
service定义service层接口
code-tools基于db生成基础代码
