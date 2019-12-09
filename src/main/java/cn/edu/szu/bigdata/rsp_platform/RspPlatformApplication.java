package cn.edu.szu.bigdata.rsp_platform;

import cn.edu.szu.bigdata.rsp_platform.oauth.configuration.EnableOAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableOAuthClient
@ServletComponentScan
@SpringBootApplication

@EnableScheduling
public class RspPlatformApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RspPlatformApplication.class);
    }



    public static void main(String[] args) {
        SpringApplication.run(RspPlatformApplication.class, args);
    }
}
