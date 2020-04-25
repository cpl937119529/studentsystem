package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.StudentResp;
import com.example.project.studentsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取所有学生信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(studentService.getAll());
    }


    /**
     * updateInfo
     * @return
     */
    @PostMapping("/updateInfo")
    public Result<Object> updateInfo(@RequestBody StudentResp resp){
        return Results.newSuccessResult(studentService.updateInfo(resp));
    }


}
