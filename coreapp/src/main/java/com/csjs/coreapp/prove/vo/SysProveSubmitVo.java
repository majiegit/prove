package com.csjs.coreapp.prove.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class SysProveSubmitVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String userId;
    private String userName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long proveId;
    private String proveName;
    private String remark;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String proveLook;
    private String provePath;
}


