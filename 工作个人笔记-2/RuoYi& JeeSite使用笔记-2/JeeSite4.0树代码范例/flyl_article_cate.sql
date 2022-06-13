/*
 Navicat Premium Data Transfer

 Source Server         : 39.103.168.144_公司测试库
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 39.103.168.144:5535
 Source Schema         : feilu_sales

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 13/06/2022 13:49:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flyl_article_cate
-- ----------------------------
DROP TABLE IF EXISTS `flyl_article_cate`;
CREATE TABLE `flyl_article_cate`  (
  `cate_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '分类编号',
  `cate_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '上级分类路径',
  `scaling` decimal(10, 3) NULL DEFAULT 0.000 COMMENT '分佣比例(商家给平台的分佣比例，填写0到1的数字)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '0、显示；2、不显示；',
  `cate_img` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类图片',
  `parent_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级编号',
  `parent_codes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有父级编号',
  `tree_sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tree_sorts` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tree_leaf` int(10) NULL DEFAULT NULL,
  `tree_level` int(10) NULL DEFAULT NULL,
  `tree_names` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cate_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flyl_article_cate
-- ----------------------------
INSERT INTO `flyl_article_cate` VALUES ('1526903167825960960', '成功案例', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '0', '0,', '30', '0000000030,', 0, 0, '成功案例');
INSERT INTO `flyl_article_cate` VALUES ('1526903252815142912', '行业资讯', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '0', '0,', '60', '0000000060,', 0, 0, '行业资讯');
INSERT INTO `flyl_article_cate` VALUES ('1526903477030051840', '行业知识', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '0', '0,', '90', '0000000090,', 0, 0, '行业知识');
INSERT INTO `flyl_article_cate` VALUES ('1526903537532887040', '新手指南', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '0', '0,', '120', '0000000120,', 0, 0, '新手指南');
INSERT INTO `flyl_article_cate` VALUES ('1526903593954664448', '直通车', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '0', '0,', '120', '0000000120,', 0, 0, '直通车');
INSERT INTO `flyl_article_cate` VALUES ('1526903736317730816', '医院', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903167825960960', '0,1526903167825960960,', '30', '0000000030,0000000030,', 1, 1, '成功案例/医院');
INSERT INTO `flyl_article_cate` VALUES ('1526903823005605888', '建筑', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903167825960960', '0,1526903167825960960,', '60', '0000000030,0000000060,', 1, 1, '成功案例/建筑');
INSERT INTO `flyl_article_cate` VALUES ('1526903936197287936', '政府', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903167825960960', '0,1526903167825960960,', '90', '0000000030,0000000090,', 1, 1, '成功案例/政府');
INSERT INTO `flyl_article_cate` VALUES ('1526903997220216832', '工地', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903167825960960', '0,1526903167825960960,', '120', '0000000030,0000000120,', 1, 1, '成功案例/工地');
INSERT INTO `flyl_article_cate` VALUES ('1527180793454239744', '医院', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903252815142912', '0,1526903252815142912,', '30', '0000000060,0000000030,', 1, 1, '行业资讯/医院');
INSERT INTO `flyl_article_cate` VALUES ('1527180916947132416', '建筑', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903252815142912', '0,1526903252815142912,', '60', '0000000060,0000000060,', 1, 1, '行业资讯/建筑');
INSERT INTO `flyl_article_cate` VALUES ('1527181011931340800', '政府', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903252815142912', '0,1526903252815142912,', '90', '0000000060,0000000090,', 1, 1, '行业资讯/政府');
INSERT INTO `flyl_article_cate` VALUES ('1527181129480904704', '工地', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903252815142912', '0,1526903252815142912,', '120', '0000000060,0000000120,', 1, 1, '行业资讯/工地');
INSERT INTO `flyl_article_cate` VALUES ('1527184279877165056', '医院', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903477030051840', '0,1526903477030051840,', '30', '0000000090,0000000030,', 1, 1, '行业知识/医院');
INSERT INTO `flyl_article_cate` VALUES ('1527184369144537088', '建筑', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903477030051840', '0,1526903477030051840,', '60', '0000000090,0000000060,', 1, 1, '行业知识/建筑');
INSERT INTO `flyl_article_cate` VALUES ('1527184447913566208', '政府', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903477030051840', '0,1526903477030051840,', '90', '0000000090,0000000090,', 1, 1, '行业知识/政府');
INSERT INTO `flyl_article_cate` VALUES ('1527184536996388864', '工地', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903477030051840', '0,1526903477030051840,', '120', '0000000090,0000000120,', 1, 1, '行业知识/工地');
INSERT INTO `flyl_article_cate` VALUES ('1527184634740449280', '医院', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903537532887040', '0,1526903537532887040,', '30', '0000000120,0000000030,', 1, 1, '新手指南/医院');
INSERT INTO `flyl_article_cate` VALUES ('1527184721117945856', '建筑', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903537532887040', '0,1526903537532887040,', '60', '0000000120,0000000060,', 1, 1, '新手指南/建筑');
INSERT INTO `flyl_article_cate` VALUES ('1527184784552599552', '政府', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903537532887040', '0,1526903537532887040,', '90', '0000000120,0000000090,', 1, 1, '新手指南/政府');
INSERT INTO `flyl_article_cate` VALUES ('1527184839221157888', '工地', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903537532887040', '0,1526903537532887040,', '120', '0000000120,0000000120,', 1, 1, '新手指南/工地');
INSERT INTO `flyl_article_cate` VALUES ('1527184896062365696', '医院', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903593954664448', '0,1526903593954664448,', '30', '0000000120,0000000030,', 1, 1, '直通车/医院');
INSERT INTO `flyl_article_cate` VALUES ('1527185081987473408', '建筑', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903593954664448', '0,1526903593954664448,', '60', '0000000120,0000000060,', 1, 1, '直通车/建筑');
INSERT INTO `flyl_article_cate` VALUES ('1527185167123456000', '政府', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903593954664448', '0,1526903593954664448,', '90', '0000000120,0000000090,', 1, 1, '直通车/政府');
INSERT INTO `flyl_article_cate` VALUES ('1527185855035449344', '工地', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903593954664448', '0,1526903593954664448,', '120', '0000000120,0000000120,', 1, 1, '直通车/工地');
INSERT INTO `flyl_article_cate` VALUES ('1531891908779786240', '帮助', '', 0.000, 'system', 'system', NULL, NULL, NULL, 1, '', '0', '0,', '120', '0000000120,', 1, 0, '帮助');
INSERT INTO `flyl_article_cate` VALUES ('1531892268864978944', '帮助', '', 0.000, 'system', 'system', NULL, NULL, NULL, 0, '', '1526903537532887040', '0,1526903537532887040,', '120', '0000000120,0000000120,', 1, 1, '新手指南/帮助');

SET FOREIGN_KEY_CHECKS = 1;
