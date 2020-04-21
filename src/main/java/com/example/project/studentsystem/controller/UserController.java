package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.LoginReq;
import com.example.project.studentsystem.enums.ErrorCodeEnum;
import com.example.project.studentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Object> userLogin(@RequestBody LoginReq req){

        boolean login = userService.login(req.getUserName(), req.getPassword());
        if(login){
            return Results.newSuccessResult("登录成功");
        }else {
            return Results.newFailedResult(ErrorCodeEnum.USERNAME_POSSWORD_ERROE);
        }
    }

}
