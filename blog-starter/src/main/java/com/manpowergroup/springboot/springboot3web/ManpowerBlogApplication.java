package com.manpowergroup.springboot.springboot3web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.manpowergroup.springboot.springboot3web")
public class ManpowerBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManpowerBlogApplication.class, args);
    }

}
