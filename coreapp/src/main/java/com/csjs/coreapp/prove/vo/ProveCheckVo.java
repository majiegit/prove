package com.csjs.coreapp.prove.vo;


import lombok.Data;

import java.util.ArrayList;


@Data
public class ProveCheckVo {
    private Integer turn;
    private ArrayList<String> checkUserId;
    private ArrayList<String> checkUserName;
    private String checkMode;
    private Boolean editStatus;
}


