package com.csjs.coreapp.prove.utils.proveTask;

import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.service.ISysProveSubmitService;
import com.csjs.coreapp.prove.utils.DocmentOperationUtils;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProveSubmitThread {
    @Resource
    public DocmentOperationUtils docmentOperationUtils;
    @Autowired
    public FileFtpUtils fileFtpUtils;

    @Async
    public void proveCreateTask(String proveWordPath, String tpPath, ArrayList<HashMap<String, Object>> hashMaps, Map<String, Object> userInfo,
                     SysProveSubmit sysProveSubmit, ISysProveSubmitService sysProveSubmitService,ArrayList<SysProveFieldVo> sysProveFieldVos) {
        /** 1、需下载模板文件*/
        fileFtpUtils.download(tpPath);
        // 替换文本 生成证明文件
        docmentOperationUtils.wordContentReplace(tpPath, proveWordPath, hashMaps, userInfo,sysProveFieldVos,sysProveSubmit.getUserId());
        sysProveSubmit.setProvePath(proveWordPath);
        /** 2、需下载证明文件*/
        fileFtpUtils.download(proveWordPath);
        //  生成提交预览图
        String base64Src = docmentOperationUtils.wordToImage(proveWordPath);
        sysProveSubmit.setProveLook(base64Src);
        sysProveSubmitService.updateById(sysProveSubmit);
    }

}
