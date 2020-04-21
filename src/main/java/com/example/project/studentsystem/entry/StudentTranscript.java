package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class StudentTranscript {

    /**
     * 学生成绩
     */

    private Long id;

    private Long studentId;

    private Long courseId;

    private Integer score;

    private Integer credit;

    private Integer year;

    private Integer semester;
}
