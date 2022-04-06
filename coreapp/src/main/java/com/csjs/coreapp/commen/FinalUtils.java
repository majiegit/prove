package com.csjs.coreapp.commen;

public class FinalUtils {
    /**
     * 证明模板上传路径
     */
    public static final String SIGNET_PATH = "/provefilepath/signet/";
    /**
     * 证明模板上传路径
     */
    public static final String PROVE_TP_PATH = "/provefilepath/tp/";
    /**
     * 生成证明文件
     */
    public static final String PROVE_PROVE_PATH = "/provefilepath/prove/";
    /**
     * 证明提交状态 0 审核中 1 审核通过 2 审核不通过  3 未审核
     */
    public static final Integer PROVE_STATUS_CHECKING = 0;
    public static final Integer PROVE_STATUS_CHEACK_YES = 1;
    public static final Integer PROVE_STATUS_CHEACK_NO = 2;
    public static final Integer PROVE_STATUS_CHEACK_NO_APPROVED = 3;
    public static final String IS_DELETE_Y = "Y";
    public static final String IS_DELETE_N = "N";

    /**
     * 证明字段类型
     */
    public static final String FIELD_TYPE_TEMP = "temp"; // 模板替换字段  &{}
    public static final String FIELD_TYPE_SUBMIT = "submit"; // 单据字段  一般不做 变量替换， 辅助的作用
    public static final String FIELD_TYPE_SYSTEM = "system"; // 系统变量 例如：时间 @{}

    /**
     * 钉钉配置
     */
    public static final String appKey = "appKey";
    public static final String appSecret = "appSecret";
    public static final String agentId = "agentId";
    public static final String proveCheckUrl = "proveCheckUrl";
    public static final String proveApplyUrl = "proveApplyUrl";
    /**
     * sql 配置
     */
    public static final String proveTpSql = "proveTpSql";
    public static final String proveTpSqlReplace = "proveTpSqlReplace";

    /**
     * e签宝配置
     */

    public static final String e_host = "e-host";  // ip + port
    public static final String e_x_timevale_project_id = "x-timevale-project-id"; // 项目Id
    public static final String e_x_timevale_signature = "x-timevale-signature";  // 签名
    public static final String e_accountId = "accountId";  // 监印员Id
    public static final String e_authorizationOrganizeId = "authorizationOrganizeId";  // 机构ID
    public static final String e_sealId = "sealId";  // 签章ID

    /**
     * 证明签章方式  1 e签宝  2 本地签章
     */
    public static final String signetMode_1 = "1";
    public static final String signetMode_2 = "2";
    /**
     * 审批模式 1 会签 2 或签
     */
    public static final String check_Mode_1 = "1";
    public static final String check_Mode_2 = "2";

    /**
     * 消息类型
     */
    public static final String messageTypeCheck = "check";
    public static final String messageTypeApply = "apply";
}
