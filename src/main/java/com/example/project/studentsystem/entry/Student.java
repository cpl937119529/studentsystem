package com.example.project.studentsystem.entry;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Student {

    /**
     * 学生
     */

    private Long id;

    private Long userId;

    private Integer sex;

    private Integer startYear;

    private String department;

    private Long professionId;

    private Long classId;

    private String professionDirection;

    //学号
    private String studentNumber;

    private String politicalStatus;

    private LocalDateTime birth;

    private String nation;

    //身份证号码
    private String personNumber;

    private String hobby;

    private String hometown;

    private String address;

    private String email;

    private String qq;

}
