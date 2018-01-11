/*
MySQL Data Transfer
Source Host: localhost
Source Database: memcloud
Target Host: localhost
Target Database: memcloud
Date: 2012-7-1 15:11:32
*/

/*

grant select,delete,update,create on memcloud.* to devacc@"%" identified by "123swl";
grant all privileges on memcloud.* to devacc@"localhost" identified by "123swl";
grant all privileges on memcloud.* to devacc@"127.0.0.1" identified by "123swl";
grant all privileges on memcloud.* to devacc@"CDCS-213057166" identified by "123swl";
flush privileges;

 * */

create database if not exists memcloud CHARACTER SET=utf8  COLLATE=utf8_unicode_ci;
use memcloud;

SET FOREIGN_KEY_CHECKS=0;


DROP TABLE IF EXISTS `app_conf`;
-- ----------------------------
-- Table structure for app_conf
-- ----------------------------
CREATE TABLE `app_conf` (
  `appid` bigint(20) NOT NULL COMMENT 'appid ref to app_desc',
  `shard_num` int(11) NOT NULL,
  `group_text` varchar(256) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `app_conf_audit`;
-- ----------------------------
-- Table structure for app_conf_audit
-- ----------------------------
CREATE TABLE `app_conf_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appid` bigint(20) NOT NULL,
  `shard_num` int(11) NOT NULL,
  `group_text` varchar(256) NOT NULL,
  `version` int(11) NOT NULL,
  `oper_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_audit_appid` (`appid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `app_desc`;
-- ----------------------------
-- Table structure for app_desc
-- ----------------------------
CREATE TABLE `app_desc` (
  `appid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `owner_uid` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `descr` varchar(64) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `passed_time` bigint(20) DEFAULT NULL,
  `notify_emails` varchar(128) DEFAULT NULL,
  `notify_mobiles` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`appid`),
  UNIQUE KEY `uidx_app_name` (`name`),
  KEY `idx_app_uid` (`owner_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `app_mem_group`;
-- ----------------------------
-- Table structure for app_mem_group
-- ----------------------------
CREATE TABLE `app_mem_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appid` bigint(20) NOT NULL,
  `master_ip` varchar(32) NOT NULL,
  `master_port` int(11) NOT NULL,
  `slave_ip` varchar(32) NOT NULL,
  `slave_port` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_group_master` (`master_ip`,`master_port`),
  UNIQUE KEY `uidx_group_slave` (`slave_ip`,`slave_port`),
  KEY `idx_group_app` (`appid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `mem_fault`;
-- ----------------------------
-- Table structure for mem_fault
-- ----------------------------
CREATE TABLE `mem_fault` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0 un-recovered,1 recovered',
  `create_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `mem_host`;
-- ----------------------------
-- Table structure for mem_host
-- ----------------------------
CREATE TABLE `mem_host` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) NOT NULL,
  `ssh_user` varchar(32) DEFAULT NULL,
  `ssh_pwd` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_host_ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `mem_instance`;
-- ----------------------------
-- Table structure for mem_instance
-- ----------------------------
CREATE TABLE `mem_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'memcached instance id',
  `host_id` bigint(20) NOT NULL,
  `host_ip` varchar(32) NOT NULL,
  `port` int(11) NOT NULL,
  `repc_port` int(11) NOT NULL,
  `peer_ip` varchar(32) NOT NULL,
  `status` int(11) NOT NULL,
  `arg_mem` int(11) NOT NULL,
  `arg_conn` int(11) DEFAULT NULL,
  `role_in_peer` tinyint(4) NOT NULL COMMENT '0 no twin, 1 elder , -1 young',
  `mem_cmd` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_inst_ipport` (`host_ip`,`port`),
  UNIQUE KEY `uidx_inst_iprepc` (`host_id`,`repc_port`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `scaleout_appeal`;
-- ----------------------------
-- Table structure for scaleout_appeal
-- ----------------------------
CREATE TABLE `scaleout_appeal` (
  `recid` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `app_name` varchar(16) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `shard_num` int(11) NOT NULL,
  `mem_size` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT 'approval status: 0 waiting for verify; 1 passed; -1 reject',
  `create_time` bigint(20) NOT NULL,
  `passed_time` bigint(20) DEFAULT NULL,
  `passed_shard` int(11) DEFAULT NULL,
  `passed_mem` int(11) DEFAULT NULL,
  PRIMARY KEY (`recid`),
  KEY `scale_idx_appid` (`app_id`),
  KEY `scale_idx_userid` (`user_id`),
  KEY `scale_idx_creattm` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `user`;
-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `pwd` varchar(32) NOT NULL,
  `email` varchar(64) DEFAULT NULL,
  `mobile` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_user_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10002 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records 
-- ----------------------------

-- account:  memcloud    password:   123456

INSERT INTO `user` VALUES ('10001', 'memcloud', 'e10adc3949ba59abbe56e057f20f883e', null, '13812345678');