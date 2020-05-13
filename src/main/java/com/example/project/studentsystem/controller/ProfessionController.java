package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.dto.ProfessionResp;
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
        int result = professionService.addProfession(profession);
        if(result==1){
            return Results.newSuccessResult("成功");
        }else if(result==-1){
            return Results.newFailedResult("已经存在，不必重复添加");
        }else {
            return Results.newFailedResult("添加失败");
        }
    }

    /**
     * 获取该辅导员管理的所有专业信息
     * @return
     */
    @GetMapping("/getAllByCourseUserId")
    public Result<Object> getAllByCourseUserId(@RequestParam String userId){
        return Results.newSuccessResult(professionService.getAllByCourseUserId(userId));
    }


    /**
     * 根据id删除专业
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result<Object> deleteById(@RequestParam String id){
        int i = professionService.deleteById(id);
        if(i==-1){
            return Results.newFailedResult("删除失败，该专业已被使用");
        }
        return Results.newSuccessResult("删除成功");
    }


    /**
     * 根据条件查询
     * @param profession
     * @return
     */
    @PostMapping("/findByCondition")
    public Result<Object> findByCondition(@RequestBody ProfessionResp profession){
        return Results.newSuccessResult(professionService.findByCondition(profession));
    }

}
