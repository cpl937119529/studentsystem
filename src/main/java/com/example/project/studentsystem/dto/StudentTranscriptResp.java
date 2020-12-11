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

    private String studentName;

    private String courseName;

    private String professionName;

    private String className;

    private String userId;

    private Integer studyYear;


}
