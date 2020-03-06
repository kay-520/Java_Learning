/*
 Navicat Premium Data Transfer

 Source Server         : 测试外_101.200.52.215
 Source Server Type    : MySQL
 Source Server Version : 50630
 Source Host           : 101.200.52.215:3306
 Source Schema         : wmh

 Target Server Type    : MySQL
 Target Server Version : 50630
 File Encoding         : 65001

 Date: 06/03/2020 18:13:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway`;
CREATE TABLE `gateway`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` varchar(11) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '路由id',
  `route_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '路名名',
  `route_pattern` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '匹配规则',
  `route_type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '转发规则',
  `route_url` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'uri',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of gateway
-- ----------------------------
INSERT INTO `gateway` VALUES (1, 'wmh-member', 'wmh-member', '/member/**', '0', 'wmh-member');

SET FOREIGN_KEY_CHECKS = 1;
