package com.example.project.studentsystem.controller;


import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.service.CounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
