package com.example.project.studentsystem.entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Student {

    /**
     * 学生
     */

    private Long id;

    private Long userId;

    private String name;

    private Integer sex;

    private Integer startYear;

    private Long professionId;

    private String className;

    private String professionDirection;

    private String politicalStatus;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String nation;

    //身份证号码
    private String personNumber;

    private String hobby;

    private String hometown;

    private String address;

    private String email;

    private String qq;

}
