package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class DeductionRecord {

    /**
     * 扣分记录
     */

    private Long id;

    private Long studentId;

    private String type;

    private Integer score;

    private String remark;

    private Long counselorId;

    private Integer year;

    private Integer semester;

}
