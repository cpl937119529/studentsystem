package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class Counselor {

    /**
     * 辅导员类
     */

    private Long id;

    private Long userId;

    private String counselorName;

    private Integer sex;

    private String department;

}
