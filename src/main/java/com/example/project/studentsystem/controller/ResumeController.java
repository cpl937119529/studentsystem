package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.PositionResp;
import com.example.project.studentsystem.dto.ResumeResp;
import com.example.project.studentsystem.entry.Resume;
import com.example.project.studentsystem.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 获取该学生的个人简历信息
     * @param userId
     * @return
     */
    @GetMapping("/getInfo")
    public Result<Object> getInfo(@RequestParam String userId){
        return Results.newSuccessResult(resumeService.getInfo(userId));
    }

    /**
     * 更新或保存
     * @param resp
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Result<Object> saveOrUpdate(@RequestBody ResumeResp resp){
        return Results.newSuccessResult(resumeService.saveOrUpdate(resp));
    }


    @GetMapping("/getPosition")
    public Result<Object> getPosition(@RequestParam String userId){
        return Results.newSuccessResult(resumeService.getPosition(userId));
    }


}
