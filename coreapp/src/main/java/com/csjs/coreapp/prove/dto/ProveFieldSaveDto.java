package com.csjs.coreapp.prove.dto;

import com.csjs.coreapp.prove.entity.SysProveField;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ProveFieldSaveDto {
    private ArrayList<SysProveField> sysProveFieldExpands;
    private String proveId;
}
