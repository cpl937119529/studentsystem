package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.dto.DeductionRecordResp;
import com.example.project.studentsystem.service.DeductionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deductionRecord")
public class DeductionRecordController {

    @Autowired
    private DeductionRecordService deductionRecordService;

    /**
     * 添加扣分记录
     * @param resp
     * @return
     */
    @PostMapping("/addDeductionRecord")
    public Result<Object> addDeductionRecord(@RequestBody DeductionRecordResp resp){
        return Results.newSuccessResult(deductionRecordService.addDeductionRecord(resp));
    }

}
