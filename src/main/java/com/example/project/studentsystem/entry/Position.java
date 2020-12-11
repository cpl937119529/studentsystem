package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class Position {

    private Long id;

    private String positionName;

    private String workexperience;

    private String education;

    private String industryType;

    private String positionType;

    private String workCity;

    private Integer limitSalary;

    private Integer maxSalary;

    private String request;

    private String recruiter;

    private String company;

}
