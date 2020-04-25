package com.example.project.studentsystem.dto;

import lombok.Data;

@Data
public class UserResp {

    private String id;

    private String userName;

    private String passWord;

    private Integer userType;

    //姓名
    private String name;
    //身份
    private String identity;

}
