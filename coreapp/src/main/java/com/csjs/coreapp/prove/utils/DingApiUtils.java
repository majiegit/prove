package com.csjs.coreapp.prove.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.*;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.service.ISysDataService;
import com.csjs.coreapp.prove.service.ISysConfigService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;


@Component
public class DingApiUtils {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDataService sysDataService;

    //private String appKey = "ding4z987xpzlf6b4f4r";
//    private String appSecret = "-Gqxoq2Udvj9mSE9iXULsmSeDIpJgtIGKD9mm9Kq5P11hdLA4lFJnH4bgH_wT2Pu";
//    private String agentId = "775071201";
    private String appKey = null;
    private String appSecret = null;
    private String agentId = null;

    public void setValue() {

        if (StrUtil.isEmpty(this.appKey)) {
            this.appKey = sysConfigService.getValueByName(FinalUtils.appKey).getValueName();
        }
        if (StrUtil.isEmpty(this.appSecret)) {
            this.appSecret = sysConfigService.getValueByName(FinalUtils.appSecret).getValueName();
        }
        if (StrUtil.isEmpty(this.agentId)) {
            this.agentId = sysConfigService.getValueByName(FinalUtils.agentId).getValueName();
        }

    }

    public void sentDingDingCheckTasks(String userId, String title, String description, String url) {
        setValue();
        try {
            // 根据userId 查询 手机号对接钉钉
            String mobile = sysDataService.getMobileByUserId(userId);
//            mobile  = "15204741491";
            String unionid = null;
            if (StrUtil.isNotEmpty(mobile)) {
                // 第一步 ============================== 根据手机号获取userId
                // 创建者用户Id
                String dingUserId = getUserIdByMobile(mobile);
                // 第二步 =========================== 根据 userId 获取用户信息
                // 创建者
                String userInfoByUserId = getUserInfoByUserId(dingUserId);
                if (ObjectUtil.isNotEmpty(userInfoByUserId)) {
                    unionid = JSONUtil.parseObj(userInfoByUserId).getJSONObject("result").get("unionid").toString();
                }
                if (StrUtil.isNotEmpty(unionid)) {
                    // 发送待办消息
                    System.out.println("发送待办审批消息");
                    addTasks(unionid, title, description);
                    // 发送工作通知消息
                    System.out.println("发送工作通知审批消息");
                    addWorkNotice(dingUserId, title, description, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sentDingDingApplyTasks(String userId, String title, String description, String url) {
        setValue();
        try {
            // 根据userId 查询 手机号对接钉钉
            String mobile = sysDataService.getMobileByUserId(userId);
            String unionid = null;
            if (StrUtil.isNotEmpty(mobile)) {
                // 第一步 ============================== 根据手机号获取userId
                // 创建者用户Id
                String dingUserId = getUserIdByMobile(mobile);
                // 第二步 =========================== 根据 userId 获取用户信息
                // 创建者
                String userInfoByUserId = getUserInfoByUserId(dingUserId);
                if (ObjectUtil.isNotEmpty(userInfoByUserId)) {
                    unionid = JSONUtil.parseObj(userInfoByUserId).getJSONObject("result").get("unionid").toString();
                }
                if (StrUtil.isNotEmpty(unionid)) {
                    // 发送工作通知消息
                    System.out.println("发送审批完成后的工作通知消息");
                    addWorkNotice(dingUserId, title, description, url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送钉钉工作通知
     *
     * @param title
     * @param description
     */
    private void addWorkNotice(String userId, String title, String description, String url) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            request.setAgentId(Long.valueOf(agentId));
            request.setUseridList(userId);
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
            msg.getActionCard().setTitle(title);
            msg.getActionCard().setMarkdown("##### " + title);
//            msg.getActionCard().setMarkdown("##### " + title + "\n[" + description + "]" + "(" + description + ")");
            if (StrUtil.isNotEmpty(url)) {
                msg.getActionCard().setSingleTitle("查看详情");
                msg.getActionCard().setSingleUrl(url);
            }
            msg.setMsgtype("action_card");
            request.setMsg(msg);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, getAccessToken());
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * 新增钉钉待办任务
     *
     * @param unionid     创建者id
     * @param title       待办标题
     * @param description 待办备注
     * @return
     */
    public void addTasks(String unionid, String title, String description) {

        // 第三步 =========================== 待办任务参数的封装
        HashMap<String, Object> params = new HashMap<>();
        // 待办标题
        params.put("subject", title);
        // 创建者的unionid
        params.put("creatorId", unionid);
        // 待办备注描述
        params.put("description", description);
        // 业务系统唯一标识
//        params.put("sourceId","111111111111111111111111");
        // 执行者的unionid    可以是数组或字符串
        params.put("executorIds", Arrays.asList(unionid));
        // 第三方待办任务跳转地址
//        HashMap<String, Object> detailUrl = new HashMap<>();
//        detailUrl.put("appUrl", url);   // 移动端
//        params.put("detailUrl", detailUrl);
        // 参与者的unionid    可以是数组或字符串
        params.put("participantIds", Arrays.asList(unionid));
        // 生成的待办是否仅展示在执行者的待办列表中。 Boolean
        params.put("isOnlyShowExecutor", false);
        // 优先级  10：较低 20：普通 30：紧急 40：非常紧急
        params.put("priority", 20);
        // 待办通知配置 DING通知配置，目前仅支持取值为1，表示应用内DING。
        HashMap<String, Object> dingNotify = new HashMap<>();
        dingNotify.put("dingNotify", "1");
        params.put("notifyConfigs", dingNotify);
        // 第四步 =========================== 发送待办任务 采用HTTP 方式  其他方式请参照官方API
        HttpRequest post = HttpUtil.createPost("https://api.dingtalk.com/v1.0/todo/users/" + unionid +
                "/tasks?operatorId=" + unionid);
        post.header("x-acs-dingtalk-access-token", getAccessToken());
        JSON parse = JSONUtil.parse(params);
        post.body(parse.toString());
        HttpResponse execute = post.execute();
        String body = execute.body();
        System.out.println(body);
    }

    /***
     * 根据用户userId获取用户详情
     * @return
     */
    public String getUserInfoByUserId(String userId) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        try {
            OapiV2UserGetResponse rsp = client.execute(req, getAccessToken());
            return rsp.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 根据手机号获取钉钉用户userID
     * @return
     */
    public String getUserIdByMobile(String mobile) {
        String userId = "";
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
        OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
        req.setMobile(mobile);
        try {
            OapiV2UserGetbymobileResponse rsp = client.execute(req, getAccessToken());
            String body = rsp.getBody();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONObject result = JSONUtil.parseObj(jsonObject.get("result").toString());
            userId = result.get("userid").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }


    /***
     * 获取企业凭证 access_token
     * @return
     */
    public String getAccessToken() {
        GetAccessTokenResponse accessToken = null;
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            com.aliyun.dingtalkoauth2_1_0.Client client = new com.aliyun.dingtalkoauth2_1_0.Client(config);
            GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                    .setAppKey(appKey)
                    .setAppSecret(appSecret);
            accessToken = client.getAccessToken(getAccessTokenRequest);

        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return accessToken.getBody().accessToken;
    }
}
