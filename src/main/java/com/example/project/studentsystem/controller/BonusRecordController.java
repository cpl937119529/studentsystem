package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.service.BonusRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 获取该辅导员下的加分记录
     * @param userId
     * @return
     */
    @GetMapping("/getByUserId")
    public Result<Object> getByUserId(@RequestParam String userId){
        return Results.newSuccessResult(bonusRecordService.getByUserId(userId));
    }


    /**
     * 根据id删除加分记录
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result<Object> deleteById(@RequestParam String id){
        return Results.newSuccessResult(bonusRecordService.deleteById(id));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    @PostMapping("/getListByCondition")
    public Result<Object> getListByCondition(@RequestBody BonusRecordResp resp){
        return Results.newSuccessResult(bonusRecordService.getListByCondition(resp));
    }


}
