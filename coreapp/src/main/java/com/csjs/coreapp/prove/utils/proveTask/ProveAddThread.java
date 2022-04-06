package com.csjs.coreapp.prove.utils.proveTask;


import com.csjs.coreapp.prove.entity.SysTp;
import com.csjs.coreapp.prove.service.ISysTpService;
import com.csjs.coreapp.prove.utils.DocmentOperationUtils;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProveAddThread {
    @Autowired
    public DocmentOperationUtils docmentOperationUtils;
    @Autowired
    public FileFtpUtils fileFtpUtils;

    /**
     * 证明模板上传 文档转图片 线程
     */
    @Async
    public void proveTpToImageTask(String tpPath, SysTp sysTp, ISysTpService sysTpService) {
        String base64Src = docmentOperationUtils.wordToImage(tpPath);
        sysTp.setTpLook(base64Src);
        sysTpService.updateById(sysTp);
    }
}
