alter database shop_online character set utf8 collate utf8_general_ci;

use shop_online;

-- 用户
create table if not exists `user` (
    `id` int primary key auto_increment comment 'ID',
    `username` varchar(25) not null comment '用户名',
    `password` varchar(128) not null comment '密码',
    `role` varchar(10) not null comment '角色权限',
    `created_at` datetime not null comment '创建时间'
);

-- 商品
create table if not exists `item` (
    `id` int primary key auto_increment comment 'ID',
    `name` varchar(25) not null comment '商品名',
    `image` varchar(128) not null comment '图片文件名',
    `price` double not null comment '价格',
    `inventory` int not null comment '库存',
    `on_sale` boolean default true comment '是否在售',
    `created_at` datetime not null comment '创建时间',
    `modified_at` datetime not null comment '修改时间',
    `salesman_id` int not null comment '销售人员ID'
);

-- 订单
create table if not exists `order` (
    `id` int primary key auto_increment comment 'ID',
    `customer_id` int not null comment '顾客ID',
    `confirm` boolean default false comment '确认与否',
    `cost` double comment '花费',
    `created_at` datetime not null comment '创建时间'
);

-- 订单详情
create table if not exists `order_detail` (
    `id` int primary key auto_increment comment 'ID',
    `order_id` int not null comment '订单ID',
    `item_id` int not null comment '商品ID',
    `num` int not null comment '购买数量'
);
