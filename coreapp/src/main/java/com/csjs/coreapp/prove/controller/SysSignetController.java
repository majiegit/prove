package com.csjs.coreapp.prove.controller;


import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysSignet;
import com.csjs.coreapp.prove.service.ISysProveService;
import com.csjs.coreapp.prove.service.ISysSignetService;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysSignet")
@CrossOrigin
public class SysSignetController {
    @Autowired
    private ISysSignetService sysSignetService;
    @Autowired
    private ISysProveService sysProveService;
    @Autowired
    private FileFtpUtils fileFtpUtils;

    @PostMapping("save")
    public R add(@RequestParam("signetName") String signetName, String id, MultipartFile file) {
        SysSignet sysSignet = new SysSignet();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String tpName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
            String type = fileName.substring(fileName.lastIndexOf("."));
            if (!type.equals(".png")) {
                return R.data(500, "请上传png格式图片");
            }
            String path = fileFtpUtils.upload(FinalUtils.SIGNET_PATH, tpName, file);
            sysSignet.setSignetPath(path);
            try {
                InputStream inputStream = file.getInputStream();
                Image image = ImgUtil.read(inputStream);
                String base64Src = ImgUtil.toBase64DataUri(image, "png");
                sysSignet.setSignetLook(base64Src);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (StrUtil.isNotEmpty(id)) {
            SysSignet byId = sysSignetService.getById(id);
            FileUtil.del(byId.getSignetPath());
            sysSignet.setId(Long.valueOf(id));
            sysSignet.setUpdateTime(new Date());
            sysSignet.setSignetName(signetName);
            sysSignetService.updateById(sysSignet);
        } else {
            sysSignet.setCreateTime(new Date());
            sysSignet.setSignetName(signetName);
            sysSignetService.save(sysSignet);
        }
        return R.SUCCESS;
    }

    @GetMapping("list")
    public R list() {
        return R.SELECT_SUCCESS.data(sysSignetService.list());
    }

    @PostMapping("delete")
    public R delete(@RequestParam("id") String id) {
        SysSignet sysSignet = sysSignetService.getById(id);
        List<SysProve> sysSignetList = sysProveService.listBySignetId(id);
        if (sysSignetList.size() == 0) {
            FileUtil.del(sysSignet.getSignetPath());
            sysSignetService.removeById(Long.valueOf(id));
            return R.DELETE_SUCCESS;
        } else {
            return R.data(506, "已有证明在用该签章，无法删除");
        }
    }
}
