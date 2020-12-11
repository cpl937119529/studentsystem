package com.example.project.studentsystem.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.LoginReq;
import com.example.project.studentsystem.dto.UserResp;
import com.example.project.studentsystem.entry.User;
import com.example.project.studentsystem.enums.ErrorCodeEnum;
import com.example.project.studentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param req
     * @return
     */
    @PostMapping("/login")
    public Result<Object> userLogin(@RequestBody LoginReq req){

        LoginReq user = userService.login(req.getUserName(), req.getPassWord());
        if(user.getId()==null){
            return Results.newFailedResult(ErrorCodeEnum.USERNAME_POSSWORD_ERROE);

        }else {
            return Results.newSuccessResult(user);
        }
    }

    /**
     * 获取所有账户
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(userService.getAll());
    }

    /**
     * 添加用户
     * @param req
     * @return
     */
    @PostMapping("/addUser")
    public Result<Object> addUser(@RequestBody LoginReq req){
        int i = userService.addUser(req);
        if(i==-99){
            return Results.newFailedResult("已存在该用户");
        }
        return Results.newSuccessResult(i);
    }


    /**
     * 划分账户
     * @param userResp
     * @return
     */
    @PostMapping("/assignAccount")
    public Result<Object> assignAccount(@RequestBody UserResp userResp){
        User user =new User();
        user.setId(Long.valueOf(userResp.getId()));
        user.setUserName(userResp.getUserName());
        user.setPassWord(userResp.getPassWord());
        user.setUserType(userResp.getUserType());
        return Results.newSuccessResult(userService.assignAccount(user));
    }


    /**
     * 根据条件查找
     * @param userResp
     * @return
     */
    @PostMapping("/findByCondition")
    public Result<Object> findByCondition(@RequestBody UserResp userResp){
        return Results.newSuccessResult(userService.findByCondition(userResp));
    }

    /**
     * 修改密码
     * @param userResp
     * @return
     */
    @PostMapping("/updatePassword")
    public Result<Object> updatePassword(@RequestBody UserResp userResp){
        int i = userService.updatePassword(userResp);
        if(i==-1){
            return Results.newFailedResult("密码错误");
        }
        if(i==0){
            return Results.newFailedResult("修改失败");
        }

        return Results.newSuccessResult("修改成功");
    }

}
