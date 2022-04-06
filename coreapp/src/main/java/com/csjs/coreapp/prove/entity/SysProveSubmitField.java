package com.csjs.coreapp.prove.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@TableName("sys_prove_submit_field")
public class SysProveSubmitField {
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long proveSubmitId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fieldId;
    private String fieldKey;
    private String fieldValue;
    private String fieldName;
    private String fieldType;
}


