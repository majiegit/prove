package com.csjs.coreapp.prove.eParam;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Signers {
    private String accountId;
    private String authorizationOrganizeId;
    private Integer accountType;
    private boolean autoSign;
    private ArrayList<SignDocDetail> signDocDetails;
}
