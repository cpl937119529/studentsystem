package com.example.project.studentsystem.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.service.CounselorProfessionRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
