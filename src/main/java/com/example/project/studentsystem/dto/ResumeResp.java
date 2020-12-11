package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class ResumeResp {

    private String id;

    private String studentId;

    private String positionName;

    private String education;

    private String industryType;

    private String positionType;

    private String workCity;

    private Integer limitSalary;

    private Integer maxSalary;

    private String userId;

}
