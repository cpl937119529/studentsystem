package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class CounselorProfessionRel {

    private Long id;

    private Long counselorId;

    private Long professionId;

    private Integer startYear;

    private Integer endYear;

}
