package com.example.project.studentsystem.entry;

import lombok.Data;

@Data
public class User {

    /**
     * 用户类
     */
    private Long id;

    private String userName;

    private String passWord;

    private Integer userType;

}
