package com.cvconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.cvconnect", "nmquan.commonlib"})
@EnableAsync
@EnableRetry
public class NotifyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyServiceApplication.class, args);
    }

}
