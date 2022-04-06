package com.csjs.coreapp.prove.utils.proveTask;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.service.ISysProveSubmitCheckService;
import com.csjs.coreapp.prove.service.ISysProveSubmitService;
import com.csjs.coreapp.prove.utils.DocmentOperationUtils;
import com.csjs.coreapp.prove.utils.ESignatureTreasureUtils;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProveCheckThread {
    @Autowired
    public DocmentOperationUtils docmentOperationUtils;
    @Autowired
    public FileFtpUtils fileFtpUtils;
    @Autowired
    private ISysProveSubmitCheckService sysProveSubmitCheckService;
    @Autowired
    private ESignatureTreasureUtils eSignatureTreasureUtils;



    /**
     * 本地签章审核 通过
     * @param provePath
     * @param signetPath
     * @param coordX
     * @param coordY
     * @param proveSubmitId
     * @param sysProveSubmitService
     */
    @Async
    public void proveSubmitCheckBySignetLocalhostTask(String provePath, String signetPath, float coordX, float coordY,
                          Long proveSubmitId, ISysProveSubmitService sysProveSubmitService) {
        /** 1、需下载公章文件*/
        fileFtpUtils.download(signetPath);
        /** 2、需下载原来证明文件*/
        fileFtpUtils.download(provePath);
        String wordPath = docmentOperationUtils.wordToSignet(provePath, signetPath, coordX, coordY);
        String imgStr = docmentOperationUtils.wordToImage(wordPath);
        String pdfPath = docmentOperationUtils.wordToPdf(wordPath);
        SysProveSubmit sysProveSubmit = new SysProveSubmit();
        sysProveSubmit.setId(proveSubmitId);
        sysProveSubmit.setProvePath(pdfPath);
        /** 更新状态*/
        sysProveSubmit.setStatus(FinalUtils.PROVE_STATUS_CHEACK_YES);
        sysProveSubmit.setProveLook(imgStr);
        sysProveSubmitService.updateById(sysProveSubmit);

        /**
         * 发送审批通过消息
         */
        sysProveSubmitCheckService.sendMessageApplyOk(proveSubmitId);
    }

    /**
     * e签宝 审核 通过
     *
     * @return
     */
    @Async
    public synchronized void proveSubmitCheckBySignetESignetTreasureTask(SysProveSubmit sysProveSubmit, SysProve sysProve, ISysProveSubmitService sysProveSubmitService) {
        String provePath = sysProveSubmit.getProvePath();
        Integer coordX = sysProve.getCoordX();
        Integer coordY = sysProve.getCoordY();
        String docName = sysProveSubmit.getUserName() + "的" + sysProve.getName();
        // 1、下载待签文件,并转成pdf
        String pdfPath = docmentOperationUtils.wordToPdf(provePath);
        // 2、查询配置参数并赋值
        eSignatureTreasureUtils.setPram();
        // 2、调用e签宝文件上传
        String fileKey = eSignatureTreasureUtils.uploadFile(pdfPath);
        // 3、创建流程、静默签
        if (StrUtil.isNotEmpty(fileKey)) {
            String body = eSignatureTreasureUtils.signetFlow(fileKey, docName, coordX, coordY, docName + "静默签");
            JSONObject response = JSONUtil.parseObj(body);
            Integer errCode = Integer.valueOf((Integer) response.get("errCode"));
            if (errCode == 0) {
                JSONArray objects = JSONUtil.parseArray(JSONUtil.parseObj(response.get("data")).get("signDocs"));
                String fileKeyPdf = JSONUtil.parseObj(objects.get(0)).get("fileKey").toString();
                String docId = JSONUtil.parseObj(objects.get(0)).get("docId").toString();
                // 4、获取流程结束文件路径
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String downloadUrl = eSignatureTreasureUtils.getFile(fileKeyPdf, docId);
                if (StrUtil.isNotEmpty(downloadUrl)) {
                    System.out.println(downloadUrl);
                    // 5、下载文件
                    File file = eSignatureTreasureUtils.downloadFile(downloadUrl, pdfPath);
                    // 6、上传文件到FTP
                    fileFtpUtils.upload(FinalUtils.PROVE_PROVE_PATH, file);
                    String path = file.getPath();
                    if(StrUtil.isNotEmpty(path)){
                        /**
                         * 更新单据审核状态、文档路径、文档预览
                         */
                        sysProveSubmit.setStatus(FinalUtils.PROVE_STATUS_CHEACK_YES);
                        sysProveSubmit.setProvePath(path);
                        sysProveSubmit.setProveLook(docmentOperationUtils.pdfToImage(pdfPath));
                        sysProveSubmitService.updateById(sysProveSubmit);
                        /** 发送通过消息 */
                        sysProveSubmitCheckService.sendMessageApplyOk(sysProveSubmit.getId());
                    }
                }
            }
        }
    }


    /**
     * 发送审批 不通过消息
     * @param proveSubmitId
     */
    @Async
    public void  sendMessageCheckNo( Long proveSubmitId) {
        sysProveSubmitCheckService.sendMessageApplyError(proveSubmitId);
    }
}
