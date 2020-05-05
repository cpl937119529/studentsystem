package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class ComprehensiveTestResp {

    private String id;

    private String studentId;

    private Double overallResult;

    private Double averageScore;

    private Double allReduceScore;

    private Double allAddScore;

    private Integer year;

    private Integer semester;

    private Integer ranking;

    private String studentName;

    private String professionName;

    private String professionId;

    private String userId;

    private String className;
}
