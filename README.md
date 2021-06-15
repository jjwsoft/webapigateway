# web-api-gateway
本模块主要实现http请求的路由功能，初步解决以下需求：
1. 根据请求body中的一些属性判断是否可以路由此请求到后端的指定服务
2. 能对请求body进行解密

# 原理
web-api-gateway主要使用了spring cloud gateway作为核心，扩展了一个使用groovy表达式作为路由条件的断言器。

# 构建
maven package
cp target/web-dynamic-proxy-1.0.0-SNAPSHOT-jar-with-dependencies.jar $dest

# 配置
建议在部署到生产环境时，生成以下对应的配置文件
1. 路由配置 application-prod.yaml
2. 日志配置 logback.xml

# 运行
java -jar web-api-gateway-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod 

# 开发
1. 解密：可以 RequestEntity.buildBody 中增加对应的判断与解密办法
2. 扩展条件属性：可以 RequestEntity.build 中增加判断属性
3. 扩展断方：可组合使用官方提供的多个断言器 https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories

# 日志解读
最重要的日志在：RequestLogFilter。包含以下信息
1. 请求方法
2. 请求路由
3. 命中路由ID
4. 命中路由目标路径
5. 耗时
6. 结果

如下所示：
```
c.f.w.i.f.RequestLogFilter         :58   | GET	/test	->	default	http://127.0.0.1:8081/aftv1/test	=	72ms	ERROR: Connection refused: /127.0.0.1:8081
c.f.w.i.f.RequestLogFilter         :58   | POST	/test?test=1	->	aftv2	http://127.0.0.1:8081/aftv2/test?test=1	=	3ms	ERROR: Connection refused: /127.0.0.1:8081
c.f.w.i.f.RequestLogFilter         :58   | POST	/test?test=1	->	aftv2	http://127.0.0.1:8081/aftv2/test?test=1	=	384ms	200
c.f.w.i.f.RequestLogFilter         :58   | GET	/test	->	default	http://127.0.0.1:8081tv1/test	=	9ms	200
```