package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.service.BonusRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bonusRecord")
public class BonusRecordController {

    @Autowired
    private BonusRecordService bonusRecordService;

    /**
     * 添加加分记录
     * @param resp
     * @return
     */
    @PostMapping("/addBonusRecord")
    public Result<Object> addBonusRecord(@RequestBody BonusRecordResp resp){
        return Results.newSuccessResult(bonusRecordService.addBonusRecord(resp));
    }

}
