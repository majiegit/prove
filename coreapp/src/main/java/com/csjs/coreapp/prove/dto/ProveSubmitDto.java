package com.csjs.coreapp.prove.dto;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ProveSubmitDto {
    private String userId;
    private String userName;
    private String proveId;
    private String remark;
    private String status;
    private ArrayList<SysProveFieldVo> sysProveFieldExpands;
}
