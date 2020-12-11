package com.example.project.studentsystem.controller;


import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorResp;
import com.example.project.studentsystem.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    /**
     * 获取所有辅导员信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(counselorService.getAll());
    }


    /**
     * 添加辅导员信息
     * @param resp
     * @return
     */
    @PostMapping("/addCounselor")
    public Result<Object> addCounselor(@RequestBody CounselorResp resp){
        int i = counselorService.addCounselor(resp);
        if(i==-1){
            return Results.newFailedResult("此用户名已存在");
        }
        return Results.newSuccessResult("添加成功");
    }

}
