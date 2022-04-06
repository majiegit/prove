package com.csjs.coreapp.prove.eParam;


import lombok.Data;

import java.util.ArrayList;

@Data
public class ActiveMap {

    private String initiatorAccountId;
    private Integer accountType;
    private ArrayList<SignDocs> signDocs;
    private ArrayList<Signers> signers;
    private String subject;
}
