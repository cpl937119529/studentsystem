package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class Course {

    /**
     * 课程
     */

    private Long id;

    private String courseName;

    private Long professionId;

    private Integer credit;

}
