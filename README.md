# spring-boot-template
框架环境：</br>
Spring Boot1.5.9.RELEASE</br>
mybatis3.4.5</br>
使用druid数据库连接池</br>
pagehelper分页工具</br>
redis集成</br>
session共享集成</br>
swagger2接口测试和文档</br>

工具：
JDK1.8</br>
mysql5.7.20</br>
maven3.5</br>
Idea2017.3</br>

模块说明：
parent定义使用的外部jar包版本</br>
common定义常用常量和工具类</br>
persistence定义基础mapper接口</br>
redis定义按照数据类型，定义常用方法</br>
model定义mapper和model，SQL的provider</br>
service定义service层接口</br>
code-tools基于db生成基础代码</br>
