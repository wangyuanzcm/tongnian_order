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