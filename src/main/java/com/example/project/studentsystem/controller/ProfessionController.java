package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.service.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profession")
public class ProfessionController {

    @Autowired
    private ProfessionService professionService;

    /**
     * 添加专业
     * @param profession
     * @return
     */
    @PostMapping("/addProfession")
    public Result<Object> addProfession(@RequestBody Profession profession){
        return Results.newSuccessResult(professionService.addProfession(profession));
    }

    /**
     * 获取所有专业信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(professionService.getAll());
    }


    /**
     * 根据id删除专业
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result<Object> deleteById(@RequestParam String id){
        return Results.newSuccessResult(professionService.deleteById(id));
    }

}
