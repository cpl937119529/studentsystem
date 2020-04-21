package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class SemesterProfessionalCourses {
    /**
     * 学期专业课程
     */

    private Long id;

    private Long courseId;

    private Long professionId;

    private Integer year;

    private Integer semester;

}
