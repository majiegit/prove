package com.csjs.coreapp.prove.eParam;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SignDocDetail {
    private String docFilekey;
    private ArrayList<SignPo> signPos;
}
