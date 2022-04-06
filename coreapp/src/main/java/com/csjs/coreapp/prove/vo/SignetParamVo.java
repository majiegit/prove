package com.csjs.coreapp.prove.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


@Data
public class SignetParamVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long signetId;
    private Integer coordX;
    private Integer coordY;
    private String signetName;
    private String signetPath;
    private String signetLook;
    private String signetMode;
}


