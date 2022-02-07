package com.example.graduationlhj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.example.graduationlhj.mapper")
@EnableOpenApi
@EnableAsync
public class GraduationLhjApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationLhjApplication.class, args);
    }

}
