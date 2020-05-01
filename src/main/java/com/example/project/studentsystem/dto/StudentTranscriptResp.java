package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class StudentTranscriptResp {

    /**
     * 学生成绩
     */

    private String id;

    private String studentId;

    private String courseId;

    private Integer score;

    private Double credit;

    private Integer year;

    private Integer semester;

}
