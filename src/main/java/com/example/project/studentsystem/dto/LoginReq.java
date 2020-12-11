package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class LoginReq {

    private String id;

    private String userName;

    private String passWord;

    private Integer userType;

    private String name;

}
