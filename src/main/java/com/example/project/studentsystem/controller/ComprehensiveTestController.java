package com.example.project.studentsystem.controller;


import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.ComprehensiveTestResp;
import com.example.project.studentsystem.service.ComprehensiveTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<ComprehensiveTestResp> result = comprehensiveTestService.getTotalScoreByCounselorUserId(resp);

        boolean isErr=true;

        for(int i=0;i<result.size();i++){
            if(result.get(i).getAverageScore()!=0  && result.get(i).getOverallResult()!=0){
                isErr=false;
            }
        }

        if(isErr){
            return Results.newFailedResult("计算失败，请先录入综测指标");
        }
        return Results.newSuccessResult(result);
    }

    @PostMapping("/sendTotalScore")
    public Result<Object> sendTotalScore(@RequestBody ComprehensiveTestResp resp){
        return Results.newSuccessResult(comprehensiveTestService.sendTotalScore(resp));
    }

    /**
     * 获取该辅导员管辖下的所有已发布的学生的综测成绩
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(@RequestParam String userId){
        return Results.newSuccessResult(comprehensiveTestService.getAll(userId));
    }


    @PostMapping("/findByCondition")
    public Result<Object> findByCondition(@RequestBody ComprehensiveTestResp resp){
        return Results.newSuccessResult(comprehensiveTestService.findByCondition(resp));
    }


    @GetMapping("/getAllByStudentUserId")
    public Result<Object> getAllByStudentUserId(@RequestParam String userId){
        return Results.newSuccessResult(comprehensiveTestService.getAllByStudentUserId(userId));
    }

    @PostMapping("/findByConditionWithStudent")
    public Result<Object> findByConditionWithStudent(@RequestBody ComprehensiveTestResp resp){
        return Results.newSuccessResult(comprehensiveTestService.findByConditionWithStudent(resp));
    }

}
