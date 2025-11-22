# JeecgBoot AI低代码平台模板项目

源码地址：https://github.com/jeecgboot/JeecgBoot

本项目采用jeecg-booot的3.8.3版本进行精简

# 开发环境

Node.js 18.x   
JDK17   
Maven   
Redis3.2+   
Mysql5.7+   

# 启动命令

server启动命令：

```
// 在server文件夹下执行
mvn clean install  
mvn spring-boot:run -pl jeecg-module-system\jeecg-system-start

```

web启动命令

```
pnpm install
pnpm run dev
pnpm run build
```

# 新建后端模块
比如我要新建tongnian模块

第一步：
在jeecg-boot-module目录下执行

```
mvn archetype:generate ^
   -DgroupId=org.jeecgframework.boot3 ^
   -DartifactId=jeecg-module-tongnian ^
   -Dversion=3.8.3 ^
   -DarchetypeGroupId=org.jeecgframework.archetype ^
   -DarchetypeArtifactId=jeecg-boot-archetype ^
   -DarchetypeVersion=3.0
```

多行命令不生效的话，命令会依次提示填写：

- archetype： 默认选择7，直接回车即可
- groupId： 填写org.jeecgframework.boot3
- artifactId： 填写jeecg-module-tongnian
- version： 填写3.8.3
- package： 填写org.jeecgframework.boot3.tongnian

第二步：
修改jeecg-boot-module\pom.xml的配置，在<modules>标签下添加如下配置：

```
<module>jeecg-module-tongnian</module>
```
第三步：
在jeecg-system-start/pom.xml中添加新模块的依赖
```
 <dependency>
    <groupId>org.jeecgframework.boot3</groupId>
    <artifactId>jeecg-module-tongnian</artifactId>
    <version>${jeecgboot.version}</version>
</dependency>
```

最后运行：mvn clean install

# 自动生成代码配置

