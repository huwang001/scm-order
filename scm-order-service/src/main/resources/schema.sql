CREATE TABLE `scm_call_record_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `record_code` varchar(50) DEFAULT NULL COMMENT '单据编号',
  `system_name` varchar(50) NOT NULL COMMENT '调用系统名称',
  `request_service` varchar(100) NOT NULL COMMENT '请求服务名',
  `request_url` varchar(200) NOT NULL COMMENT '请求url',
  `request_content` mediumtext NOT NULL COMMENT '请求内容',
  `response_content` mediumtext NOT NULL COMMENT '响应内容',
  `status` tinyint(4) NOT NULL COMMENT '交互状态 0：失败  1:成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  KEY `idx_record_code` (`record_code`),
  KEY `idx_request_service` (`request_service`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8 COMMENT='调用记录表';

CREATE TABLE `scm_mail_send_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `send_user_mail` varchar(100) NOT NULL COMMENT '发件人邮箱',
  `receive_user_mail` varchar(500) NOT NULL COMMENT '收件人邮箱',
  `mail_title` varchar(100) NOT NULL COMMENT '邮件标题',
  `mail_content` text NOT NULL COMMENT '邮件内容',
  `send_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '邮件状态:0-无需发送,1-待发送,2-已发送',
  `send_times` int(11) NOT NULL DEFAULT '0' COMMENT '发送次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用:0-否,1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除:0-否,1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_mail_title` (`mail_title`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='邮件信息表';

CREATE TABLE `scm_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `parent_order_code` varchar(50) NOT NULL DEFAULT '0' COMMENT '父预约单号',
  `order_code` varchar(50) NOT NULL COMMENT '预约单号',
  `order_type` tinyint(4) NOT NULL COMMENT '单据类型 1:普通订单 2:卡券订单',
  `order_status` tinyint(4) NOT NULL COMMENT '单据状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消',
  `channel_code` varchar(50) NOT NULL COMMENT '渠道code',
  `sale_code` varchar(50) NOT NULL COMMENT '销售单号',
  `custom_name` varchar(50) NOT NULL COMMENT '客户名称',
  `custom_mobile` varchar(30) NOT NULL COMMENT '客户手机号',
  `province_code` varchar(50) NOT NULL COMMENT '省Code',
  `province_name` varchar(50) NOT NULL COMMENT '省名称',
  `city_code` varchar(50) NOT NULL COMMENT '市code',
  `city_name` varchar(50) NOT NULL COMMENT '市名称',
  `county_code` varchar(50) NOT NULL COMMENT '区code',
  `county_name` varchar(50) NOT NULL COMMENT '区名称',
  `custom_address` varchar(100) NOT NULL COMMENT '客户详细地址',
  `need_package` tinyint(4) NOT NULL COMMENT '是否需要包装 0:不需要 1:需要',
  `expect_date` date NOT NULL COMMENT '预计交货日期',
  `factory_code` varchar(20) DEFAULT NULL COMMENT '工厂编号',
  `real_warehouse_code` varchar(50) DEFAULT NULL COMMENT '实仓Code',
  `vm_warehouse_code` varchar(50) DEFAULT NULL COMMENT '虚仓Code',
  `has_trade_audit` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易是否审核通过 0:未通过 1:已通过',
  `has_allot` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否创建调拨 0:未创建 1:已创建',
  `allot_code` varchar(50) DEFAULT NULL COMMENT '调拨单号',
  `allot_factory_code` varchar(50) DEFAULT NULL COMMENT '调入工厂编码',
  `allot_real_warehouse_code` varchar(50) DEFAULT NULL COMMENT '调入实仓code',
  `has_do` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否创建DO 0:未创建 1:已创建',
  `do_code` varchar(50) DEFAULT NULL COMMENT '团购发货单号',
  `do_factory_code` varchar(50) DEFAULT NULL COMMENT 'do发货工厂',
  `do_real_warehouse_code` varchar(50) DEFAULT NULL COMMENT 'do发货仓库编码',
  `is_leaf` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否叶子单, 0:否 1:是',
  `sync_trade_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '同步交易状态 0:无需同步 1:待同步(锁定) 2:已同步(锁定) 10:待同步(DO) 11:已同步(DO)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_code` (`order_code`),
  KEY `idx_parent_order_code` (`parent_order_code`),
  KEY `idx_custom_name` (`custom_name`),
  KEY `uniq_sale_code` (`sale_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='预约单';

CREATE TABLE `scm_order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `order_code` varchar(50) NOT NULL COMMENT '预约单号',
  `sku_code` varchar(50) NOT NULL COMMENT '商品编码',
  `order_qty` decimal(15,3) NOT NULL COMMENT '下单数量',
  `require_qty` decimal(15,3) NOT NULL COMMENT '需求锁定数量（下单数量按发货单位向上取整）',
  `has_lock_qty` decimal(15,3) NOT NULL COMMENT '已锁定数量',
  `delivery_unit_code` varchar(50) NOT NULL COMMENT '发货单位',
  `scale` decimal(15,3) NOT NULL COMMENT '发货与基础转换比例',
  `unit` varchar(50) NOT NULL COMMENT '单位名称',
  `unit_code` varchar(50) NOT NULL COMMENT '单位编码',
  `lock_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '锁定状态 1:部分锁定 2:全部锁定',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  KEY `idx_order_code` (`order_code`),
  KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='预约单明细';

CREATE TABLE `scm_order_return` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `order_code` varchar(50) NOT NULL COMMENT '预约单号',
  `sale_code` varchar(50) NOT NULL COMMENT '销售单号',
  `after_sale_code` varchar(50) NOT NULL COMMENT '售后单号',
  `return_entry_code` varchar(50) DEFAULT NULL COMMENT '团购入库单号',
  `order_status` tinyint(4) NOT NULL COMMENT '单据状态 1:待入库 2:已入库',
  `custom_name` varchar(50) NOT NULL COMMENT '客户名称',
  `custom_mobile` varchar(30) NOT NULL COMMENT '客户手机号',
  `province_code` varchar(50) NOT NULL COMMENT '省Code',
  `province_name` varchar(50) NOT NULL COMMENT '省名称',
  `city_code` varchar(50) NOT NULL COMMENT '市code',
  `city_name` varchar(50) NOT NULL COMMENT '市名称',
  `county_code` varchar(50) NOT NULL COMMENT '区code',
  `county_name` varchar(50) NOT NULL COMMENT '区名称',
  `custom_address` varchar(100) NOT NULL COMMENT '客户详细地址',
  `factory_code` varchar(50) NOT NULL COMMENT '工厂编码',
  `real_warehouse_code` varchar(50) NOT NULL COMMENT '退货实仓code',
  `reason` varchar(100) NOT NULL COMMENT '退货原因',
  `sync_trade_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '通知交易中心状态，0初始，1待通知，2已通知',
  `sync_stock_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '通知库存中心状态，0初始，1待通知，2已通知',
  `express_no` varchar(50) DEFAULT NULL COMMENT '快递单号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_after_sale_code` (`after_sale_code`),
  UNIQUE KEY `uniq_return_entry_code` (`return_entry_code`),
  KEY `idx_order_code` (`order_code`),
  KEY `idx_sale_code` (`sale_code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='团购退货入库单';

CREATE TABLE `scm_order_return_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `after_sale_code` varchar(50) NOT NULL COMMENT '售后单号',
  `sku_code` varchar(50) NOT NULL COMMENT '商品编码',
  `return_qty` decimal(15,3) NOT NULL COMMENT '退货数量',
  `delivery_qty` decimal(15,3) NOT NULL COMMENT '实际发货数量',
  `entry_qty` decimal(15,3) DEFAULT NULL COMMENT '实际收货数量',
  `unit` varchar(50) NOT NULL COMMENT '单位名称',
  `unit_code` varchar(50) NOT NULL COMMENT '单位编码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  KEY `idx_rec_sc` (`after_sale_code`,`sku_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='团购退货入库单明细';

CREATE TABLE `scm_order_status_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `order_code` varchar(50) NOT NULL COMMENT '预约单号',
  `record_status` tinyint(4) NOT NULL COMMENT '单据流转状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_code` (`order_code`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='单据状态流转日志表';

CREATE TABLE `scm_order_vw_move` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `vw_move_code` varchar(50) NOT NULL COMMENT '虚仓调拨单号',
  `order_code` varchar(50) NOT NULL COMMENT '预约单号',
  `factory_code` varchar(50) NOT NULL COMMENT '工厂编号',
  `real_warehouse_code` varchar(50) NOT NULL COMMENT '实仓code',
  `in_vw_warehouse_code` varchar(50) NOT NULL COMMENT '调入虚仓code',
  `out_vw_warehouse_code` varchar(50) NOT NULL COMMENT '调出虚仓code',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_vw_move_code` (`vw_move_code`) USING BTREE,
  KEY `idx_order_code` (`order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预约单虚仓转移记录';

CREATE TABLE `scm_order_vw_move_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `vw_move_code` varchar(50) NOT NULL COMMENT '虚仓调拨单号',
  `sku_code` varchar(50) NOT NULL COMMENT '商品编码',
  `move_qty` decimal(15,3) NOT NULL COMMENT '转移数量',
  `unit` varchar(50) NOT NULL COMMENT '单位名称',
  `unit_code` varchar(50) NOT NULL COMMENT '单位编码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `modifier` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：0-否，1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除：0-否，1-是',
  `version_no` int(11) NOT NULL DEFAULT '0' COMMENT '版本号:默认0,每次更新+1',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(30) DEFAULT NULL COMMENT '业务应用ID',
  PRIMARY KEY (`id`),
  KEY `idx_sku_code` (`sku_code`),
  KEY `idx_vw_move_code` (`vw_move_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预约单虚仓转移记录明细';