package com.example.project.studentsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.project.studentsystem.mapper")
public class StudentsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentsystemApplication.class, args);
    }

}
