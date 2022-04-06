package com.csjs.coreapp;


import cn.hutool.core.io.FileUtil;
import com.csjs.coreapp.commen.FinalUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@MapperScan({"com.csjs.coreapp.prove.mapper","com.csjs.coreapp.enquiry.mapper"})
@EnableAsync
public class CoreappApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreappApplication.class, args);
        FileUtil.mkdir(FinalUtils.PROVE_TP_PATH);
        FileUtil.mkdir(FinalUtils.PROVE_PROVE_PATH);
        FileUtil.mkdir(FinalUtils.SIGNET_PATH);
    }
}
