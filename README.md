# 动图演示&项目结构图

![img](https://gitee.com/zhong_siru/images/raw/master//img/20201006001816440.gif)

**项目结构图**：

![image-20210330002538648](C:\Users\zsr204\AppData\Roaming\Typora\typora-user-images\image-20210330002538648.png)



# 所用技术栈&实现功能

> **技术栈**

+ 后端：`SpringBoot`+`Thymeleaf`
+ 前端：`Bootstrap`
+ 数据库：`MySQL`+`JdbcTemplate`

> **实现功能**

+ 首页登录
+ 页面国际化
+ 登录拦截
+ 员工信息CRUD
+ 404页面定制
+ 注销



# 数据库sql文件

```sql
-- 创建员工系统数据库
CREATE DATABASE IF NOT EXISTS `employee_system`;

-- 用employee_system数据库
USE `employee_system`;

-- 创建员工employee表
CREATE TABLE `employee`(
	`id` INT(4) NOT NULL AUTO_INCREMENT COMMENT '员工编号',
	`lastName` VARCHAR(40) NOT NULL COMMENT '员工名称',
	`email` VARCHAR(40) NOT NULL COMMENT '邮箱',
	`gender` INT(2) NOT NULL COMMENT '性别',
	`departmentName` VARCHAR(20) NOT NULL COMMENT '部门名称',
	`date` DATETIME NOT NULL COMMENT '入职日期',
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 给result表插入数据
INSERT INTO `employee`(`id`,`lastName`,`email`,`gender`,`departmentName`,`date`) 
VALUES (1, "zsr", "1234@qq.com", 1, '技术部', NOW()),
(2, "lyr", "1345@qq.com", 1, '市场部', NOW()),
(3, "gcc", "4324@qq.com", 0, '调研部', NOW()),
(4, "zsr", "8797@qq.com", 1, '后勤部', NOW()),
(5, "zch", "0878@qq.com", 1, '运营部', NOW())
```



# 启动方法

clone项目到本地，用`IDE`打开，下载依赖，修改`application.properties`数据库配置

![image-20210330003130357](https://gitee.com/zhong_siru/images/raw/master//img/image-20210330003130357.png)

然后启动SpringBoot主启动类，访问`8081`端口即可访问

