package com.example.project.studentsystem.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.service.CounselorProfessionRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counselorProfessionRel")
public class CounselorProfessionRelController {

    @Autowired
    private CounselorProfessionRelService service;

    /**
     * save
     * @param resp
     * @return
     */
    @PostMapping("/save")
    public Result<Object> assignCounselor(@RequestBody CounselorProfessionRelResp resp){
        List<CounselorProfessionRel> counselorProfessionRels = service.findByCondition(resp);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            return Results.newFailedResult("已经分配过，不必重复划分");
        }

        boolean save = service.save(resp);
        if(save){
            return Results.newSuccessResult("分配成功");
    }

        return Results.newFailedResult("分配失败");
    }

    /**
     * 获取所有信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(service.getAll());
    }


    /**
     * 根据专业名或辅导员名称进行搜索
     * @return
     */
    @PostMapping("/searchByCondition")
    public Result<Object> searchByCondition(@RequestBody CounselorProfessionRelResp resp){
        return Results.newSuccessResult(service.searchByCondition(resp));
    }

}
