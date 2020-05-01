package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class DeductionRecordResp {

    private String id;

    private String studentId;

    private String type;

    private Integer score;

    private String remark;

    private String counselorId;

    private Integer year;

    private Integer semester;

    private String counselorUserId;

    private String studentName;

    private String counselorName;

}
