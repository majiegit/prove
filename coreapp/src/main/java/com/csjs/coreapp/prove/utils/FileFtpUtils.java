package com.csjs.coreapp.prove.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class FileFtpUtils {
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.port}")
    private Integer port;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;

    public String upload(String filePath, String fileName, MultipartFile file) {
        String imgLocaltion = filePath + fileName;
        Ftp ftp = new Ftp(host, port, username, password);
        try {
            boolean upload = ftp.upload(filePath, fileName, file.getInputStream());
            ftp.close();
            if (upload) {
                return imgLocaltion;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ftp.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            try {
                ftp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String upload(String filePath, File file) {

        Ftp ftp = new Ftp(host, port, username, password);
        try {
            boolean upload = ftp.upload(filePath, file);
            ftp.close();
            if (upload) {
                return filePath;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ftp.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            try {
                ftp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean download(String filePath) {

        Ftp ftp = new Ftp(host, port, username, password);
        ftp.download(filePath, FileUtil.file(filePath));
        try {
            ftp.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ftp.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return false;
        } finally {
            try {
                ftp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void del(String wordPath) {
        Ftp ftp = new Ftp(host, port, username, password);
        ftp.delFile(wordPath);
        try {
            ftp.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ftp.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                ftp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

  /*  public boolean download(String filePath, String fileName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(filePath);
        boolean b = StrUtil.endWith(filePath, "/");
        if (b) {
            stringBuffer.append(fileName);
        } else {
            stringBuffer.append("/");
            stringBuffer.append(fileName);
        }
        String filePathName = stringBuffer.toString();
        return download(filePathName);
    }*/
}
