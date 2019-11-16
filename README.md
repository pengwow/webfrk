# 1. 项目简介

本项目是Spring框架的扩展，核心思想是使用各种注解（Annotation），丰富JavaBean的标注，自动化生成Mysql数据库、API文档的目标和服务化代码。当需求变更时，开发者仅需要修改JavaBean，即可自动化更新相关代码与文档。项目主要针对初级开发团队协同，但期望一定的代码质量和开发效率。

**注意：本项目并不是微服务框架，当前仅仅为服务化框架，主要支持Spring和Mybatis**

## 1.1 特色

- 支持跨域访问
- 完全兼容JSR303规范
- 自动生成Mysql数据库
- 自动生成API文档
- 自动生成Service代码

## 1.2 作者

- wuheng@otcaix.iscas.ac.cn
- xuyuanjia2017@otcaix.iscas.ac.cn
- yoyoyo5006@foxmail.com

## 1.3 使用

### 1.3.1 直接git

则直接基于项目进行二次开发，直接运行com.github.webfrk.ApplicationServer

### 1.3.2 Maven依赖

使用Maven依赖方式（请先自建Maven仓库），推荐模式

```
<dependency>
	<groupId>com.github.webfrk</groupId>
	<artifactId>webfrk</artifactId>
	<version>1.0.0</version>
</dependency>
```

生成ApplicationServer（用于启动Web应用），application.yml（Web应用相关配置）和log4j.proerties（日志输出配置）。
```
SpringBootGenerator sbg = new SpringBootGenerator();
sbg.setPackage("dev.examples"); //需要生成类的包名
List<String> services = new ArrayList<String>();
services.add("dev.examples.services"); //实现com.github.webfrk.core.HttpBodyHandler和标注了com.github.tools.annotations.ServiceDefinition的类目录，可以有多个
sbg.setServices(services);
List<String> mappers = mew ArrayList<String>();
mappers.add("dev.examples.mappers"); //标注了org.apache.ibatis.annotations.Mapper的类目录，可以有多个 
sbg.setMappers(mappers);
sbg.generate();
```

运行com.github.webfrk.ApplicationServer

## 1.4 局限

- [Java版本必须大于1.8，建议使用Oracle JDK，且开启反射参数-parameter](https://blog.csdn.net/sanyuesan0000/article/details/80618913)

# 2. 主要架构

# 3. 相关文档

- 设计文档
  - [请求流程](docs/flow.md)
  - [开发规范](docs/dev.md)
- 示例文档
  - [helloworld](docs/hello.md)
  - [联调mysql](docs/mysql.md)
  - [使用工具类](docs/tools.md)

# 4. 研发计划

- 1.x 基本能力的支撑
  - 1.0.0: 支持跨域访问
  - 1.1.0: 支持JSR303
  - 1.2.0: 支持Sql自动生成
  - 1.3.0: 支持Servcie自动生成
  - 1.4.0: 支持API文档自动生成
  
- 2.x 生产环境的验证
  
