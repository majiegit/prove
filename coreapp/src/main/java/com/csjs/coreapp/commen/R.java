package com.csjs.coreapp.commen;

import lombok.Data;

@Data
public class R<T> {
    /**
     * 统一返回结果集实体类
     *
     * @param <T> 返回数据对象
     */


    //代码
    private Integer code;

    //前端提示信息
    private String message;

    //返回数据
    private T data;

    public R() {
    }

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static final R SUCCESS = new R(200, "操作成功");

    public static final R AUTH_CODE_SUCCESS = new R(200, "验证码正确");

    public static final R SELECT_SUCCESS = new R(200, "查询成功");

    public static final R SEND_SUCCESS = new R(200, "发送成功");

    public static final R INSERT_SUCCESS = new R(200, "新增成功");

    public static final R SAVE_SUCCESS = new R(200, "保存成功");

    public static final R UPDATE_SUCCESS = new R(200, "更新成功");

    public static final R DELETE_SUCCESS = new R(200, "删除成功");

    public static final R UPLOAD_SUCCESS = new R(200, "上传成功");

    public static final R DOWNLOAD_SUCCESS = new R(200, "下载成功");

    public static final R LOGIN_SUCCESS = new R(200, "登陆成功");

    public static final R LOGOUT_SUCCESS = new R(200, "登出成功");

    public static final R LOGIN_ERROR = new R(201, "登陆错误");

    public static final R LOGIN_EXPIRE = new R(202, "登陆过期");

    public static final R ACCESS_LIMITED = new R(301, "访问受限");

    public static final R ARGS_ERROR = new R(501, "参数错误");

    public static final R UNKOWN_ERROR = new R(502, "系统异常");

    public static final R INSERT_ERROR = new R(503, "新增错误");

    public static final R UPDATE_ERROR = new R(504, "更新错误");

    public static final R SAVE_ERROR = new R(505, "保存失败");

    public static final R DELETE_ERROR = new R(506, "删除错误");

    public static final R UPLOAD_ERROR = new R(507, "上传错误");

    public static final R DOWNLOAD_ERROR = new R(508, "下载错误");

    public static final R OTHER_SYSTEM_ERROR = new R(509, "调用系统异常");

    public static final R SELECT_ERROR = new R(510, "查询失败");

    public static final R SELECT_USER_ERROR = new R(511, "用户不存在");

    public static final R SEND_ERROR = new R(512, "发送失败");

    public static final R AUTH_CODE_DISABLED_ERROR = new R(505, "验证码已过期，请重新发送");

    public static final R AUTH_CODE_ERROR = new R(505, "验证码不正确");

    public static final R CHECK_ERROR = new R(512, "审核失败");
    public static final R CHECK_SUCCESS = new R(200, "审核成功");

    /**
     * 配合静态对象直接设置 data 参数
     *
     * @param data
     * @return
     */

    public R data(T data) {
        R r = new R();
        r.setCode(this.code);
        r.setMessage(this.message);
        r.setData(data);
        return r;
    }

    /**
     * 自定义返回信息
     *
     * @param code
     * @param message
     * @return
     */
    public static R data(Integer code, String message) {
        return data(code, message, null);

    }

    public static <T> R data(Integer code, String message, T data) {
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R error(Integer code, String message) {
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static R error(String message) {
        return error(500, message);
    }
}
