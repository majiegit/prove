/*
Navicat Oracle Data Transfer
Oracle Client Version : 10.2.0.5.0

Source Server         : 39.103.194.102_LiHao
Source Server Version : 110200
Source Host           : 39.103.194.102:1521
Source Schema         : LIHAO

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2022-04-01 18:04:05
*/


-- ----------------------------
-- Table structure for SYS_CONFIG
-- ----------------------------
DROP TABLE "SYS_CONFIG";
CREATE TABLE "SYS_CONFIG" (
"ID" NUMBER(20) NOT NULL ,
"KEY_NAME" VARCHAR2(255 BYTE) NULL ,
"VALUE_NAME" CLOB NULL ,
"REMARK" VARCHAR2(255 BYTE) NULL ,
"CREATE_DATE" DATE NULL ,
"UPDATE_DATE" DATE NULL ,
"TYPE" NUMBER(1) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_CONFIG
-- ----------------------------
INSERT INTO "SYS_CONFIG" VALUES ('1506161602989047810', 'appKey', 'ding4z987xpzlf6b4f4r', '钉钉 appKey', TO_DATE('2022-03-22 14:51:23', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506161706517053441', 'appSecret', '-Gqxoq2Udvj9mSE9iXULsmSeDIpJgtIGKD9mm9Kq5P11hdLA4lFJnH4bgH_wT2Pu', '钉钉 appSecret', TO_DATE('2022-03-22 14:51:48', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506161801740337153', 'agentId', '775071201', '钉钉 应用ID', TO_DATE('2022-03-22 14:52:11', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506161901690601474', 'proveCheckUrl', 'http://ehr.qhlihao.net:8085/mobile/#/certificationAuditByDing?query=proveAudit', '钉钉 证明待办审批地址', TO_DATE('2022-03-22 14:52:35', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-04-01 18:02:56', 'YYYY-MM-DD HH24:MI:SS'), null);
INSERT INTO "SYS_CONFIG" VALUES ('1506162306201862145', 'proveApplyUrl', 'http://ehr.qhlihao.net:8085/mobile/#/certificationAuditByDing?query=proveAudit', '证明申请工作通知地址', TO_DATE('2022-03-22 14:54:11', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-04-01 18:03:04', 'YYYY-MM-DD HH24:MI:SS'), null);
INSERT INTO "SYS_CONFIG" VALUES ('1506162599194968065', 'e-host', 'http://125.72.35.211:8035', 'e签宝接口 地址', TO_DATE('2022-03-22 14:55:21', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-03-22 14:55:41', 'YYYY-MM-DD HH24:MI:SS'), null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163221419966466', 'x-timevale-project-id', '1001003', 'e签宝  项目ID', TO_DATE('2022-03-22 14:57:49', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163304337162241', 'x-timevale-signature', '5add874af25d089baff2eedce5f71f83', 'e 签宝 签名', TO_DATE('2022-03-22 14:58:09', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-03-22 14:59:18', 'YYYY-MM-DD HH24:MI:SS'), null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163529445457922', 'accountId', 'f8b1aac4-b371-4000-ae77-020e4274607d', '监印员Id', TO_DATE('2022-03-22 14:59:03', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163735436115969', 'authorizationOrganizeId', 'edc23d8e-a5ac-4512-ab6e-33c515f38577', '机构Id', TO_DATE('2022-03-22 14:59:52', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163814842679297', 'sealId', 'f582e8f4-1322-433a-827f-72ebea52d5eb', '签章ID', TO_DATE('2022-03-22 15:00:11', 'YYYY-MM-DD HH24:MI:SS'), null, null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163814842679296', 'proveTpSql', 'SELECT
 bd_psndoc. NAME NAME,
 bd_psndoc. ID ID,
 org_dept. NAME deptname,
 om_job.jobname jobname,
 org_adminorg. NAME orgname,
 om_post.POSTNAME postname,
 hiorg.JOINSYSDATE joinsysdate
FROM
 hi_psnjob hi_psnjob
LEFT OUTER JOIN bd_psndoc bd_psndoc ON hi_psnjob.pk_psndoc = bd_psndoc.pk_psndoc
LEFT OUTER JOIN org_dept org_dept ON hi_psnjob.pk_dept = org_dept.pk_dept
LEFT OUTER JOIN om_job om_job ON hi_psnjob.pk_job = om_job.pk_job
LEFT OUTER JOIN org_adminorg org_adminorg ON hi_psnjob.pk_org = org_adminorg.pk_adminorg
LEFT OUTER JOIN om_post om_post ON hi_psnjob.pk_post = om_post.pk_post
LEFT OUTER JOIN SM_USER SM_USER ON SM_USER.pk_psndoc = bd_psndoc.pk_psndoc
LEFT OUTER JOIN hi_psnorg hiorg ON hiorg.PK_PSNDOC = bd_psndoc.pk_psndoc and hiorg.PK_PSNORG = hi_psnjob.PK_PSNORG
WHERE
 hi_psnjob.recordnum = 0 and SM_USER.CUSERID = ''用户名''', '证明模板数据 SQL', TO_DATE('2022-03-30 12:06:04', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'), null);
INSERT INTO "SYS_CONFIG" VALUES ('1506163814842679299', 'proveTpSqlReplace', '用户名', '证明模板数据 SQL 用户替换', TO_DATE('2022-03-29 11:47:15', 'YYYY-MM-DD HH24:MI:SS'), null, null);

-- ----------------------------
-- Table structure for SYS_PROVE
-- ----------------------------
DROP TABLE "SYS_PROVE";
CREATE TABLE "SYS_PROVE" (
"ID" NUMBER(20) NOT NULL ,
"NAME" NVARCHAR2(255) NULL ,
"TP_ID" NUMBER(20) NULL ,
"SIGNET_MODE" NVARCHAR2(255) NULL ,
"SIGNET_ID" NUMBER(20) NULL ,
"COORD_X" NUMBER(10) NULL ,
"COORD_Y" NUMBER(10) NULL ,
"CREATE_TIME" DATE NULL ,
"UPDATE_TIME" DATE NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_PROVE
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_CHECK
-- ----------------------------
DROP TABLE "SYS_PROVE_CHECK";
CREATE TABLE "SYS_PROVE_CHECK" (
"ID" NUMBER(20) NOT NULL ,
"PROVE_ID" NUMBER(20) NULL ,
"CHECK_USER_ID" VARCHAR2(255 BYTE) NULL ,
"CHECK_USER_NAME" VARCHAR2(255 BYTE) NULL ,
"TURN" NUMBER(1) NULL ,
"CHECK_MODE" VARCHAR2(255 BYTE) NULL ,
"CREATE_TIME" DATE NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SYS_PROVE_CHECK"."CHECK_MODE" IS '1 会签 2 或签';

-- ----------------------------
-- Records of SYS_PROVE_CHECK
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_FIELD
-- ----------------------------
DROP TABLE "SYS_PROVE_FIELD";
CREATE TABLE "SYS_PROVE_FIELD" (
"ID" NUMBER(20) NOT NULL ,
"PROVE_ID" NUMBER(20) NULL ,
"FIELD_KEY" VARCHAR2(255 BYTE) NULL ,
"FIELD_NAME" VARCHAR2(255 BYTE) NULL ,
"FIELD_TYPE" VARCHAR2(255 BYTE) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_PROVE_FIELD
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_MESSAGE
-- ----------------------------
DROP TABLE "SYS_PROVE_MESSAGE";
CREATE TABLE "SYS_PROVE_MESSAGE" (
"ID" NUMBER(20) NOT NULL ,
"CONTENT" CLOB NULL ,
"CREATE_TIME" DATE NULL ,
"UPDATE_TIME" DATE NULL ,
"IS_READ" VARCHAR2(1 BYTE) NULL ,
"PROVE_SUBMIT_ID" NUMBER(20) NULL ,
"NOTICE_USER" VARCHAR2(255 BYTE) NULL ,
"MESSAGE_TYPE" VARCHAR2(255 BYTE) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_PROVE_MESSAGE
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_SUBMIT
-- ----------------------------
DROP TABLE "SYS_PROVE_SUBMIT";
CREATE TABLE "SYS_PROVE_SUBMIT" (
"ID" NUMBER(20) NOT NULL ,
"USER_ID" NVARCHAR2(255) NULL ,
"USER_NAME" NVARCHAR2(255) NULL ,
"PROVE_ID" NUMBER(20) NULL ,
"REMARK" NVARCHAR2(255) NULL ,
"STATUS" NUMBER(1) NULL ,
"CREATE_TIME" DATE NULL ,
"PROVE_LOOK" CLOB NULL ,
"PROVE_PATH" VARCHAR2(255 BYTE) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SYS_PROVE_SUBMIT"."STATUS" IS '0:审核中 1:审核通过 2：审核不通过 ';

-- ----------------------------
-- Records of SYS_PROVE_SUBMIT
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_SUBMIT_CHECK
-- ----------------------------
DROP TABLE "SYS_PROVE_SUBMIT_CHECK";
CREATE TABLE "SYS_PROVE_SUBMIT_CHECK" (
"ID" NUMBER(20) NOT NULL ,
"PROVE_SUBMIT_ID" NUMBER(20) NOT NULL ,
"PROVE_ID" NUMBER(20) NOT NULL ,
"CHECK_USER_ID" VARCHAR2(255 BYTE) NULL ,
"CHECK_USERNAME" VARCHAR2(255 BYTE) NULL ,
"TURN" NUMBER(1) NULL ,
"CHECK_MODE" VARCHAR2(255 CHAR) NULL ,
"CHECK_OPINION" VARCHAR2(255 CHAR) NULL ,
"STATUS" NUMBER(1) NULL ,
"CREATE_TIME" DATE NULL ,
"UPDATE_TIME" DATE NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON COLUMN "SYS_PROVE_SUBMIT_CHECK"."STATUS" IS '0:审核中 1:审核通过 2：审核不通过 ';

-- ----------------------------
-- Records of SYS_PROVE_SUBMIT_CHECK
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_PROVE_SUBMIT_FIELD
-- ----------------------------
DROP TABLE "SYS_PROVE_SUBMIT_FIELD";
CREATE TABLE "SYS_PROVE_SUBMIT_FIELD" (
"ID" NUMBER(20) NOT NULL ,
"PROVE_SUBMIT_ID" NUMBER(20) NULL ,
"FIELD_ID" NUMBER(20) NULL ,
"FIELD_KEY" VARCHAR2(255 BYTE) NULL ,
"FIELD_NAME" VARCHAR2(255 BYTE) NULL ,
"FIELD_VALUE" VARCHAR2(255 BYTE) NULL ,
"FIELD_TYPE" VARCHAR2(255 BYTE) NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_PROVE_SUBMIT_FIELD
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_SIGNET
-- ----------------------------
DROP TABLE "SYS_SIGNET";
CREATE TABLE "SYS_SIGNET" (
"ID" NUMBER(20) NOT NULL ,
"SIGNET_NAME" VARCHAR2(255 BYTE) NULL ,
"SIGNET_PATH" VARCHAR2(255 BYTE) NULL ,
"CREATE_TIME" DATE NULL ,
"UPDATE_TIME" DATE NULL ,
"SIGNET_LOOK" CLOB NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_SIGNET
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_TP
-- ----------------------------
DROP TABLE "SYS_TP";
CREATE TABLE "SYS_TP" (
"ID" NUMBER(20) NOT NULL ,
"TP_NAME" NVARCHAR2(50) NULL ,
"TP_PATH" NVARCHAR2(150) NULL ,
"CREATE_TIME" DATE NULL ,
"UPDATE_TIME" DATE NULL ,
"TP_LOOK" CLOB NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_TP
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_TP_VAR
-- ----------------------------
DROP TABLE "SYS_TP_VAR";
CREATE TABLE "SYS_TP_VAR" (
"ID" NUMBER(20) NOT NULL ,
"TP_ID" NUMBER(20) NULL ,
"TP_VAR_NAME" NVARCHAR2(255) NULL ,
"UPDATE_TIME" DATE NULL ,
"CREATE_TIME" DATE NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_TP_VAR
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_VAR
-- ----------------------------
DROP TABLE "SYS_VAR";
CREATE TABLE "SYS_VAR" (
"ID" NUMBER(20) NOT NULL ,
"NAME" NVARCHAR2(255) NULL ,
"FIELD" NVARCHAR2(255) NULL ,
"REMARK" NVARCHAR2(255) NULL ,
"UPDATE_TIME" DATE NULL ,
"CREATE_TIME" DATE NULL
)
LOGGING
NOCOMPRESS
NOCACHE

;

-- ----------------------------
-- Records of SYS_VAR
-- ----------------------------
INSERT INTO "SYS_VAR" VALUES ('1509833510154641409', null, 'NAME', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510263693314', null, 'ID', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510334996482', null, 'DEPTNAME', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510460825601', null, 'JOBNAME', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510595043329', null, 'ORGNAME', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510662152194', null, 'POSTNAME', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO "SYS_VAR" VALUES ('1509833510796369921', null, 'JOINSYSDATE', null, null, TO_DATE('2022-04-01 18:02:14', 'YYYY-MM-DD HH24:MI:SS'));

-- ----------------------------
-- Checks structure for table SYS_CONFIG
-- ----------------------------
ALTER TABLE "SYS_CONFIG" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_CONFIG" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Indexes structure for table SYS_PROVE
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE
-- ----------------------------
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE
-- ----------------------------
ALTER TABLE "SYS_PROVE" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_CHECK
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_CHECK
-- ----------------------------
ALTER TABLE "SYS_PROVE_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_CHECK" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_CHECK
-- ----------------------------
ALTER TABLE "SYS_PROVE_CHECK" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_FIELD
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_FIELD
-- ----------------------------
ALTER TABLE "SYS_PROVE_FIELD" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_FIELD" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_FIELD" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_FIELD
-- ----------------------------
ALTER TABLE "SYS_PROVE_FIELD" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_MESSAGE
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_MESSAGE
-- ----------------------------
ALTER TABLE "SYS_PROVE_MESSAGE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_MESSAGE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_MESSAGE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_MESSAGE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_MESSAGE" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_MESSAGE
-- ----------------------------
ALTER TABLE "SYS_PROVE_MESSAGE" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_SUBMIT
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_SUBMIT
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_SUBMIT
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_SUBMIT_CHECK
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_SUBMIT_CHECK
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_SUBMIT_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_SUBMIT_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_SUBMIT_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("PROVE_ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_SUBMIT_CHECK
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT_CHECK" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_PROVE_SUBMIT_FIELD
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_PROVE_SUBMIT_FIELD
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT_FIELD" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_FIELD" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_PROVE_SUBMIT_FIELD" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_PROVE_SUBMIT_FIELD
-- ----------------------------
ALTER TABLE "SYS_PROVE_SUBMIT_FIELD" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_SIGNET
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_SIGNET
-- ----------------------------
ALTER TABLE "SYS_SIGNET" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_SIGNET" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_SIGNET" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_SIGNET" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_SIGNET" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_SIGNET
-- ----------------------------
ALTER TABLE "SYS_SIGNET" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_TP
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_TP
-- ----------------------------
ALTER TABLE "SYS_TP" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_TP
-- ----------------------------
ALTER TABLE "SYS_TP" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_TP_VAR
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_TP_VAR
-- ----------------------------
ALTER TABLE "SYS_TP_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_TP_VAR" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_TP_VAR
-- ----------------------------
ALTER TABLE "SYS_TP_VAR" ADD PRIMARY KEY ("ID");

-- ----------------------------
-- Indexes structure for table SYS_VAR
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_VAR
-- ----------------------------
ALTER TABLE "SYS_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_VAR" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "SYS_VAR" ADD CHECK ("ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_VAR
-- ----------------------------
ALTER TABLE "SYS_VAR" ADD PRIMARY KEY ("ID");
