package org.lxzx.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//这是一个spring boot应用
@SpringBootApplication
@MapperScan("org.lxzx.boot.mapper")
public class MainAplication {
    public static void main(String[] args) {

        SpringApplication.run(MainAplication.class, args);

    }
}
