package com.example.project.studentsystem.controller;


import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.ComprehensiveTestResp;
import com.example.project.studentsystem.service.ComprehensiveTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comprehensiveTest")
public class ComprehensiveTestController {

    @Autowired
    private ComprehensiveTestService comprehensiveTestService;

    /**
     * 根据辅导员的userId计算某学年某学期其管理学生的综测成绩
     * @param resp
     * @return
     */
    @PostMapping("/getTotalScoreByCounselorUserId")
    public Result<Object> getTotalScoreByCounselorUserId(@RequestBody ComprehensiveTestResp resp){
        return Results.newSuccessResult(comprehensiveTestService.getTotalScoreByCounselorUserId(resp));
    }

    @PostMapping("/sendTotalScore")
    public Result<Object> sendTotalScore(@RequestBody ComprehensiveTestResp resp){
        return Results.newSuccessResult(comprehensiveTestService.sendTotalScore(resp));
    }

}
