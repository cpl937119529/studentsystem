package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class CounselorProfessionRelResp {

    private String id;

    private String counselorId;

    private String professionId;

    private Integer startYear;

    private Integer endYear;

    private String professionName;

    private String counselorName;

}
