# 电子商务网站ShopOnline

### 项目需求

业务场景包含三种角色：用户、销售人员和管理员。每种角色的业务流程如下：

* 用户：
  * 新用户的自主注册、登录和注销；
  * 已登录用户的购物流程：浏览/查询、添加至购物车、确认订单；
  * 未登录用户浏览商品。
* 销售人员：
  * 添加、删除和修改所负责销售的商品；
  * 监控所负责销售商品的销售业绩。
* 管理员：
  * 添加、删除销售人员账号；
  * 查询和监控销售人员负责商品的销售业绩。

### 开发技术栈

* 数据库：MariaDB 10.3.38
* 后端语言：JDK 17
* 项目构建：Maven
* 后端框架：Spring Boot 3.1.10
* Spring Boot依赖：
  * 模板库：Thymeleaf
  * 安全框架：Spring Security
  * 持久层框架：MyBatis 3.0.3
  * 数据校验：Validation
* 前端库：Bootstrap 4.6.2

### 数据库设计

* USER表：ID、USERNAME、PASSWORD、ROLE、CREATED_AT
* ITEM表：ID、NAME、IMAGE、PRICE、INVENTORY、ON_SALE、CREATED_AT、MODIFIED_AT、SALESMAN_ID
* ORDER表：ID、CUSTOMER_ID、CONFIRM、COST、CREATED_AT
* ORDER_DETAIL表：ID、ORDER_ID、ITEM_ID、NUM

### 程序细节

* application.properties
  * 数据源URL参数createDatabaseIfNotExist设置为true，在连接数据库时若其不存在，将自动建立该数据库；
  * 静态资源位置中首个路径为外部路径，其下的upload目录用于存放上传的图片文件；
* ShopOnlineApplication.java
  * 利用CommandLineRunner类在应用启动时向数据库中插入管理员用户数据。