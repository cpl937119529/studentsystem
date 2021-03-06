package com.example.project.studentsystem.controller;

import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CounselorResp;
import com.example.project.studentsystem.dto.StudentResp;
import com.example.project.studentsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取所有未分配学生信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(studentService.getAll());
    }

    /**
     * 获取所有学生信息
     * @return
     */
    @GetMapping("/getAllInfo")
    public Result<Object> getAllInfo(){
        return Results.newSuccessResult(studentService.getAllInfo());
    }


    /**
     * updateInfo
     * @return
     */
    @PostMapping("/updateInfo")
    public Result<Object> updateInfo(@RequestBody StudentResp resp){
        return Results.newSuccessResult(studentService.updateInfo(resp));
    }


    /**
     * 添加学生用户
     * @param resp
     * @return
     */
    @PostMapping("/addStudent")
    public Result<Object> addCounselor(@RequestBody StudentResp resp){
        int i = studentService.addStudent(resp);
        if(i==-1){
            return Results.newFailedResult("此用户名已存在");
        }
        return Results.newSuccessResult("添加成功");
    }


    /**
     * 根据学生姓名、专业名称、入学年份进行查询
     * @param resp
     * @return
     */
    @PostMapping("/searchByCondition")
    public Result<Object> searchByCondition(@RequestBody StudentResp resp){
        return Results.newSuccessResult(studentService.searchByCondition(resp));
    }


    /**
     * 根据用户id查找辅导员ID并查询其管理专业下的学生信息
     * @return
     */
    @GetMapping("/getStudentListByCounselor")
    public Result<Object> getStudentListByCounselor(@RequestParam String userId){
        return Results.newSuccessResult(studentService.getStudentListByCounselor(userId));
    }


    /**
     * 根据入学年份、班级名称、专业名称查询学生信息
     * @param resp
     * @return
     */
    @PostMapping("/findByCondition")
    public Result<Object> findByCondition(@RequestBody StudentResp resp){
        return Results.newSuccessResult(studentService.findByCondition(resp));
    }

    /**
     * 根据用户ID获取学生信息
     * @return
     */
    @GetMapping("/getInfoByUserId")
    public Result<Object> getInfoByUserId(@RequestParam String userId){
        return Results.newSuccessResult(studentService.getInfoByUserId(userId));
    }

    /**
     * 根据id获取学生信息
     * @param id
     * @return
     */
    @GetMapping("/getInfoById")
    public Result<Object> getInfoById(@RequestParam String id){
        return Results.newSuccessResult(studentService.getInfoById(id));
    }


}
