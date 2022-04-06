package com.csjs.coreapp.prove.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ProveCheckDto {
    private ArrayList<String> ids;
    private String status;
    private String checkUserId;
    private String checkUserName;
    private String checkOpinion;
}
