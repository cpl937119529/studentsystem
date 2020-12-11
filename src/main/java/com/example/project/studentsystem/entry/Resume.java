package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class Resume {

    private Long id;

    private Long studentId;

    private String positionName;

    private String education;

    private String industryType;

    private String positionType;

    private String workCity;

    private Integer limitSalary;

    private Integer maxSalary;

}
