package com.example.project.studentsystem.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.IService.impl.IStudentTranscriptServiceImpl;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.StudentTranscriptReqForExcel;
import com.example.project.studentsystem.entry.Course;
import com.example.project.studentsystem.entry.StudentTranscript;
import com.example.project.studentsystem.mapper.CourseMapper;
import com.example.project.studentsystem.mapper.StudentTranscriptMapper;
import com.example.project.studentsystem.utils.ExcelListener;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/studentTranscript")
public class StudentTranscriptController {


    @Autowired
    private IStudentTranscriptServiceImpl iStudentService;

    @Autowired
    private StudentTranscriptMapper studentTranscriptMapper;

    @Autowired
    private CourseMapper courseMapper;


    @PostMapping("/importByExcel")
    public Result<Object> uploadFile(@RequestParam(value = "file", required=true) MultipartFile file){
        //此处千万不要用InputStream，InputStream会导致无法解析出文件类型
        BufferedInputStream buffer = null;
        try{
            buffer = new BufferedInputStream(file.getInputStream());
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(buffer, ExcelTypeEnum.XLS,listener);
            excelReader.read(new Sheet(1,0, StudentTranscriptReqForExcel.class));
            List<Object> list = listener.getDatas();
            //excel中第一行为栏目，必然是存在一行的
            if(CollectionUtils.isEmpty(list) || list.size() < 2){
                return Results.newFailedResult("文件数据为空");
            }

            List<StudentTranscriptReqForExcel> catagoryList = new ArrayList<StudentTranscriptReqForExcel>();

            //list为excel中解析出的数据,去除第一行的表头
            for (int i = 1; i < list.size(); i++) {
                StudentTranscriptReqForExcel catagory = new StudentTranscriptReqForExcel();
                catagory = (StudentTranscriptReqForExcel) list.get(i);
                catagoryList.add(catagory);
            }


            List<StudentTranscript> studentTranscripts = Lists.newArrayList();
            catagoryList.forEach(info->{
                StudentTranscript studentTranscript = new StudentTranscript();

                studentTranscript.setStudentId(Long.valueOf(info.getStudentId()));
                studentTranscript.setCourseId(Long.valueOf(info.getCourseId()));
                studentTranscript.setScore(Integer.valueOf(info.getScore()));
                studentTranscript.setYear(Integer.valueOf(info.getYear()));
                studentTranscript.setSemester(Integer.valueOf(info.getSemester()));
                //获取该门课程的学分
                Course course = courseMapper.selectById(Long.valueOf(info.getCourseId()));
                if(Integer.valueOf(info.getScore())<60){
                    studentTranscript.setCredit(0.00);
                }else {
                    studentTranscript.setCredit(((course.getCredit() * Double.valueOf(info.getScore()))/100));
                }

                studentTranscripts.add(studentTranscript);
            });

            if(CollectionUtil.isNotEmpty(studentTranscripts)){
                boolean b = iStudentService.saveOrUpdateBatch(studentTranscripts);
                if(b){
                    return Results.newSuccessResult("导入成功");
                }else {
                    return Results.newFailedResult("导入失败");
                }
            }

        } catch(Exception e){
            e.printStackTrace();
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

}
