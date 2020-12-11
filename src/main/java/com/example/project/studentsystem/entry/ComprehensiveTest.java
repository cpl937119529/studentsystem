package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class ComprehensiveTest {
    /**
     * 综合测评表
     */

    private Long id;

    private Long studentId;

    private Double overallResult;

    private Double averageScore;

    private Double allReduceScore;

    private Double allAddScore;

    private Integer year;

    private Integer semester;

    private Integer ranking;


}
