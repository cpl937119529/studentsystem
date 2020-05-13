package com.example.project.studentsystem.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.example.project.studentsystem.IService.impl.ICounselorProfessionRelServiceImpl;
import com.example.project.studentsystem.IService.impl.ICourseServiceImpl;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.CourseReqForExcel;
import com.example.project.studentsystem.dto.CourseResp;
import com.example.project.studentsystem.entry.Course;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.mapper.CourseMapper;
import com.example.project.studentsystem.mapper.ProfessionMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.example.project.studentsystem.service.CourseService;
import com.example.project.studentsystem.utils.ExcelListener;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseServiceImpl service;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessionMapper professionMapper;


    @PostMapping("/importByExcel")
    public Result<Object> uploadFile(@RequestParam(value = "file", required=true) MultipartFile file){
        //此处千万不要用InputStream，InputStream会导致无法解析出文件类型
        BufferedInputStream buffer = null;
        try{
            buffer = new BufferedInputStream(file.getInputStream());
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(buffer, ExcelTypeEnum.XLS,listener);
            excelReader.read(new Sheet(1,0, CourseReqForExcel.class));
            List<Object> list = listener.getDatas();
            //excel中第一行为栏目，必然是存在一行的
            if(CollectionUtils.isEmpty(list) || list.size() < 2){
                return Results.newFailedResult("文件数据为空");
            }

            List<CourseReqForExcel> catagoryList = new ArrayList<CourseReqForExcel>();

            //list为excel中解析出的数据,去除第一行的表头
            for (int i = 1; i < list.size(); i++) {
                CourseReqForExcel catagory = new CourseReqForExcel();
                catagory = (CourseReqForExcel) list.get(i);
                catagoryList.add(catagory);
            }
            List<Course> courseList = Lists.newArrayList();
            catagoryList.forEach(info->{
                Course course = new Course();
                course.setId(Long.valueOf(info.getId()));
                course.setCourseName(info.getCourseName());
                course.setCredit(Integer.valueOf(info.getCredit()));
                course.setProfessionId(Long.valueOf(info.getProfessionId()));
                courseList.add(course);
                });



            if(CollectionUtil.isNotEmpty(courseList)){

                for (int i=0;i<courseList.size();i++){
                    Profession profession = professionMapper.selectById(courseList.get(i).getProfessionId());
                    if(profession==null){
                        return Results.newFailedResult("导入失败,不存在专业Id为："+courseList.get(i).getProfessionId()+"的专业");
                    }
                }
            }


            if(CollectionUtil.isNotEmpty(courseList)){
                boolean b = service.saveOrUpdateBatch(courseList);
                if(b){
                    return Results.newSuccessResult("导入成功");
                }else {
                    return Results.newFailedResult("导入失败");
                }
            }

        } catch(Exception e){
            e.printStackTrace();
            return Results.newSuccessResult("导入异常");
        } finally {
            if(buffer != null){
                try{
                    buffer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return Results.newSuccessResult("导入异常");
    }

    /**
     * 获取全部课表信息
     * @return
     */
    @GetMapping("/getAll")
    public Result<Object> getAll(){
        return Results.newSuccessResult(courseService.getAll());
    }

    /**
     * 根据课程名称、专业名称进行查询
     * @return
     */
    @PostMapping("/searchByCondition")
    public Result<Object> searchByCondition(@RequestBody CourseResp resp){
        return Results.newSuccessResult(courseService.searchByCondition(resp));
    }


    /**
     * 根据ID删除课表
     * @param id
     * @return
     */
    @GetMapping("/deleteCourseById")
    public Result<Object> deleteCourseById(@RequestParam String id){
        int i = courseService.deleteCourseById(id);
        if(i==-1){
            return Results.newFailedResult("删除失败，该课程已被使用");
        }
        return Results.newSuccessResult("删除成功");
    }


    /**
     * 获取辅导员管辖专业的课程
     * @param userId
     * @return
     */
    @GetMapping("/getCourseByCourseUserId")
    public Result<Object> getCourseByCourseUserId(@RequestParam String userId){
        return Results.newSuccessResult(courseService.getCourseByCourseUserId(userId));
    }

}
