/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : studentsystem

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-04-29 23:39:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bonus_record
-- ----------------------------
DROP TABLE IF EXISTS `bonus_record`;
CREATE TABLE `bonus_record` (
  `id` bigint(20) NOT NULL,
  `student_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `counselor_id` bigint(20) DEFAULT NULL,
  `year` int(255) DEFAULT NULL,
  `semester` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bonus_record
-- ----------------------------

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` bigint(20) NOT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `profession_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES ('1253978896370528257', '信息管理与信息系统1班', '1253978896336973825');
INSERT INTO `class` VALUES ('1253978896395694082', '信息管理与信息系统2班', '1253978896336973825');
INSERT INTO `class` VALUES ('1253979323161931778', '计算机1班', '1253979323136765953');
INSERT INTO `class` VALUES ('1253979323170320385', '计算机2班', '1253979323136765953');
INSERT INTO `class` VALUES ('1254433213238038530', '会计1班', '1254433213208678402');
INSERT INTO `class` VALUES ('1254433213259010050', '会计2班', '1254433213208678402');
INSERT INTO `class` VALUES ('1254785260277837825', '三十三1班', '1254785260244283394');
INSERT INTO `class` VALUES ('1254785260298809346', '三十三2班', '1254785260244283394');

-- ----------------------------
-- Table structure for counselor
-- ----------------------------
DROP TABLE IF EXISTS `counselor`;
CREATE TABLE `counselor` (
  `id` bigint(20) NOT NULL COMMENT '辅导员id',
  `user_id` bigint(20) DEFAULT NULL,
  `counselor_name` varchar(255) DEFAULT NULL,
  `sex` int(255) DEFAULT NULL COMMENT '性别  1：男  2：女',
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of counselor
-- ----------------------------
INSERT INTO `counselor` VALUES ('1254042689511411714', '1254042689477857281', '小红', '2', '信息学院');
INSERT INTO `counselor` VALUES ('1254743220064169985', '1254743220026421250', '小陈', '1', '数理学院');
INSERT INTO `counselor` VALUES ('1254743438159589377', '1254743438138617857', '小张', '1', '信息学院');

-- ----------------------------
-- Table structure for counselor_profession_rel
-- ----------------------------
DROP TABLE IF EXISTS `counselor_profession_rel`;
CREATE TABLE `counselor_profession_rel` (
  `id` bigint(20) NOT NULL,
  `profession_id` bigint(20) DEFAULT NULL,
  `counselor_id` bigint(20) DEFAULT NULL,
  `start_year` int(255) DEFAULT NULL,
  `end_year` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of counselor_profession_rel
-- ----------------------------
INSERT INTO `counselor_profession_rel` VALUES ('1254054481021235202', '1253978896336973825', '1254042689511411714', '2015', '2019');
INSERT INTO `counselor_profession_rel` VALUES ('1254743557177159681', '1253979323136765953', '1254743438159589377', '2015', '2019');
INSERT INTO `counselor_profession_rel` VALUES ('1254743631244374017', '1254433213208678402', '1254743220064169985', '2016', '2020');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint(20) NOT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `profession_id` bigint(20) DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('2', '高等数学（2）II', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('3', '宏观与微观经济学', '1253979323136765953', '2');
INSERT INTO `course` VALUES ('4', '管理学', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('5', '创业基础', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('6', '算法与数据结构', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('7', '高级语言课程设计', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('8', '数据结构课程设计', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('9', '体育（2）', '1253979323136765953', '1');
INSERT INTO `course` VALUES ('10', '大学生心理健康教育', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('11', '中国近现代史纲要', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('12', '形势与政策（2）', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('13', '大学英语（1）', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('14', '高等数学（1）I', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('15', '高级语言程序设计', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('16', '信息技术导论', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('17', '体育（1）', '1253979323136765953', '1');
INSERT INTO `course` VALUES ('18', '军事理论', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('19', '军事训练', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('20', '思想道德修养与法律基础', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('21', '形势与政策（1）', '1253978896336973825', '5');
INSERT INTO `course` VALUES ('22', '大学英语（4）', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('23', '概率论与数理统计I', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('24', '管理信息系统', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('25', '网页制作技术', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('26', '面向对象程序设计（JAVA）', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('27', '计算机网络及应用', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('28', '数据库应用课程设计', '1253979323136765953', '2');
INSERT INTO `course` VALUES ('29', '体育（4）', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('30', '形势与政策（4）', '1253979323136765953', '5');
INSERT INTO `course` VALUES ('31', '毛泽东思想和中国特色社会主义理论体系概论（1）', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('32', '大学英语（3）', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('33', '线性代数I', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('34', '运筹学', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('35', '结构化程序综合设计', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('36', '数据库系统原理及应用', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('37', '可视化编程技术（.NET）', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('38', '企业信息管理实习', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('39', '体育（3）', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('40', '马克思主义基本原理', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('41', '形势与政策（3）', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('42', 'Android应用开发基础', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('43', '企业资源规划（ERP）', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('44', '软件测试技术', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('45', '生产与运作管理', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('46', '数据仓库与数据挖掘技术', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('47', '数据库开发技术', '1253979323136765953', '2');
INSERT INTO `course` VALUES ('48', '信息系统架构', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('49', '应用软件开发', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('50', 'ERP软件应用实践', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('51', '基础会计', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('52', 'Web应用程序设计', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('53', '操作系统原理及应用', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('54', '电子商务技术系统', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('55', '多媒体技术与应用', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('56', '信息系统分析与设计', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('57', '应用统计学', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('58', 'Python数据分析', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('59', '管理信息系统课程设计', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('60', '计算机网络工程实践', '1253978896336973825', '1');
INSERT INTO `course` VALUES ('61', '毛泽东思想和中国特色社会主义理论体系概论（2）', '1253979323136765953', '3');
INSERT INTO `course` VALUES ('62', '计算机网络互连技术', '1253978896336973825', '2');
INSERT INTO `course` VALUES ('63', '信息资源组织与管理', '1253978896336973825', '3');
INSERT INTO `course` VALUES ('64', '毕业实习', '1253978896336973825', '4');
INSERT INTO `course` VALUES ('65', '应用项目开发与实施', '1253978896336973825', '6');

-- ----------------------------
-- Table structure for deduction_record
-- ----------------------------
DROP TABLE IF EXISTS `deduction_record`;
CREATE TABLE `deduction_record` (
  `id` bigint(20) NOT NULL,
  `student_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `counselor_id` bigint(20) DEFAULT NULL,
  `year` int(255) DEFAULT NULL,
  `semester` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of deduction_record
-- ----------------------------

-- ----------------------------
-- Table structure for profession
-- ----------------------------
DROP TABLE IF EXISTS `profession`;
CREATE TABLE `profession` (
  `id` bigint(20) NOT NULL,
  `profession_name` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of profession
-- ----------------------------
INSERT INTO `profession` VALUES ('1253978896336973825', '信息管理与信息系统', '信息学院');
INSERT INTO `profession` VALUES ('1253979323136765953', '计算机', '信息学院');
INSERT INTO `profession` VALUES ('1254433213208678402', '会计', '管理学院');

-- ----------------------------
-- Table structure for semester_professional_courses
-- ----------------------------
DROP TABLE IF EXISTS `semester_professional_courses`;
CREATE TABLE `semester_professional_courses` (
  `id` bigint(20) NOT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `profession_id` bigint(20) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `semester` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semester_professional_courses
-- ----------------------------
INSERT INTO `semester_professional_courses` VALUES ('1', '2', '1', '1', '2');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `sex` int(255) DEFAULT NULL COMMENT '性别  1：男 2：女',
  `start_year` int(255) DEFAULT NULL,
  `profession_id` bigint(20) DEFAULT NULL COMMENT '专业id',
  `class_name` varchar(255) DEFAULT NULL,
  `profession_direction` varchar(255) DEFAULT NULL,
  `political_status` varchar(255) DEFAULT NULL COMMENT '政治面貌',
  `birth` datetime DEFAULT NULL,
  `nation` varchar(255) DEFAULT NULL,
  `person_number` varchar(255) DEFAULT NULL,
  `hobby` varchar(255) DEFAULT NULL,
  `hometown` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1254046663488663554', '1254046663438331906', '1', '2015', '1253978896336973825', '信息管理与信息系统2班', '开发', '党员', '2020-04-29 00:00:00', '汉族', '123456789', '游泳', '广东', '2414111', '22222', '11222', '小李', '123555');
INSERT INTO `student` VALUES ('1254056078354788353', '1254056078296068098', '1', '2016', '1253979323136765953', null, null, null, '2020-04-29 17:55:52', null, null, null, null, null, null, null, '小吴', null);
INSERT INTO `student` VALUES ('1255438094837010433', '1255438094774095873', '1', '2015', '1253978896336973825', null, null, null, null, null, null, null, null, null, null, null, '邓执', null);
INSERT INTO `student` VALUES ('1255520734579535874', '1255520734462095361', '1', '2016', null, null, null, null, null, null, null, null, null, null, null, null, '小郑', null);

-- ----------------------------
-- Table structure for student_transcript
-- ----------------------------
DROP TABLE IF EXISTS `student_transcript`;
CREATE TABLE `student_transcript` (
  `id` bigint(20) NOT NULL,
  `student_id` bigint(20) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `semester` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_transcript
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(255) DEFAULT NULL,
  `pass_word` varchar(255) DEFAULT NULL,
  `user_type` int(255) DEFAULT NULL COMMENT '用户类型   1:系统管理员  2：辅导员  3：学生',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123456', '1');
INSERT INTO `user` VALUES ('1254042689477857281', 'xiaohong', '123456', '2');
INSERT INTO `user` VALUES ('1254046663438331906', 'xiaoli', '123456', '3');
INSERT INTO `user` VALUES ('1254056078296068098', 'xiaowu', '123456', '3');
INSERT INTO `user` VALUES ('1254743220026421250', 'xiaochen', '123456', '2');
INSERT INTO `user` VALUES ('1254743438138617857', 'xiaozhang', '123456', '2');
INSERT INTO `user` VALUES ('1255438094774095873', 'dengzhi', '123456', '3');
INSERT INTO `user` VALUES ('1255520734462095361', 'xiaozhen', '123456', '3');
