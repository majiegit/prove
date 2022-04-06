package com.csjs.coreapp.prove.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class SysProveFieldVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fieldId;
    private String fieldKey;
    private String fieldValue;
    private String fieldName;
    private String fieldType;
}


