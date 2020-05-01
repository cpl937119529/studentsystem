package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.dto.DeductionRecordResp;
import com.example.project.studentsystem.service.DeductionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



    /**
     * 获取该辅导员下的扣分记录
     * @param userId
     * @return
     */
    @GetMapping("/getByUserId")
    public Result<Object> getByUserId(@RequestParam String userId){
        return Results.newSuccessResult(deductionRecordService.getByUserId(userId));
    }


    /**
     * 根据id删除扣分记录
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result<Object> deleteById(@RequestParam String id){
        return Results.newSuccessResult(deductionRecordService.deleteById(id));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    @PostMapping("/getListByCondition")
    public Result<Object> getListByCondition(@RequestBody DeductionRecordResp resp){
        return Results.newSuccessResult(deductionRecordService.getListByCondition(resp));
    }


    /**
     * 获取该学生下的扣分记录
     * @param studentUserId
     * @return
     */
    @GetMapping("/getListByStudentUserId")
    public Result<Object> getListByStudentUserId(@RequestParam String studentUserId){
        return Results.newSuccessResult(deductionRecordService.getListByStudentUserId(studentUserId));
    }



}
