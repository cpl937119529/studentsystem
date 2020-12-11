package com.example.project.studentsystem.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.IService.impl.IStudentTranscriptServiceImpl;
import com.example.project.studentsystem.base.Result;
import com.example.project.studentsystem.base.Results;
import com.example.project.studentsystem.dto.StudentResp;
import com.example.project.studentsystem.dto.StudentTranscriptReqForExcel;
import com.example.project.studentsystem.dto.StudentTranscriptResp;
import com.example.project.studentsystem.entry.Course;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.entry.StudentTranscript;
import com.example.project.studentsystem.mapper.CourseMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.example.project.studentsystem.mapper.StudentTranscriptMapper;
import com.example.project.studentsystem.service.StudentService;
import com.example.project.studentsystem.service.StudentTranscriptService;
import com.example.project.studentsystem.utils.ExcelListener;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/studentTranscript")
public class StudentTranscriptController {


    @Autowired
    private IStudentTranscriptServiceImpl iStudentService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentTranscriptService studentTranscriptService;

    @Autowired
    private StudentService studentService;


    @Autowired
    private StudentMapper studentMapper;



    @PostMapping("/importByExcel")
    public Result<Object> uploadFile(@RequestParam(value = "file", required=true) MultipartFile file,@RequestParam(value = "userId", required=true) String userId){
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

                if(course!=null && course.getCredit()!=null){
                    studentTranscript.setCredit(Double.valueOf(course.getCredit()));
                }else {
                    studentTranscript.setCredit(0.00);
                }

//                if(Integer.valueOf(info.getScore())<60){
//                    studentTranscript.setCredit(0.00);
//                }else {
//                    if(course!=null && course.getCredit()!=null){
//                        studentTranscript.setCredit(Double.valueOf(course.getCredit()));
//                    }else {
//                        studentTranscript.setCredit(0.00);
//                    }
//
//                }

                studentTranscripts.add(studentTranscript);
            });


            List<StudentResp> students = studentService.getStudentListByCounselor(userId);
            if(CollectionUtil.isNotEmpty(students)){
                List<String> studentIds = students.stream().map(StudentResp::getId).collect(Collectors.toList());
                for(int i=0;i<studentTranscripts.size();i++){
                    if(!studentIds.contains(studentTranscripts.get(i).getStudentId().toString())){
                        return Results.newFailedResult("导入失败,学号为："+studentTranscripts.get(i).getStudentId()+"的学生不是您管辖的学生");
                    }
                }

            }else {
                return Results.newFailedResult("导入失败,您没有管辖学生");
            }


            if(CollectionUtil.isNotEmpty(studentTranscripts)){

                List<Long> studentIds = studentTranscripts.stream().map(StudentTranscript::getStudentId).distinct().collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(studentIds)){

                    for (int i=0;i<studentIds.size();i++){
                        Long studentId = studentIds.get(i);

                        //获取Excel中该学生的所有课程ID，并去重
                        List<Long> courseIdOfThisStudent = studentTranscripts.stream().filter(data -> data.getStudentId().equals(studentId))
                                .map(StudentTranscript::getCourseId).distinct()
                                .collect(Collectors.toList());
                        //获取该学生的专业的课程信息
                        Student student = studentMapper.selectById(studentId);
                        if(student!=null && student.getProfessionId()!=null){

                            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("profession_id", student.getProfessionId());
                            List<Course> courseList = courseMapper.selectList(queryWrapper);
                            if(!CollectionUtil.isNotEmpty(courseList)){
                                return Results.newFailedResult("导入失败,学号为："+studentId+"的学生专业未分配课程");
                            }

                            List<Long> collect = courseList.stream().map(Course::getId).distinct().collect(Collectors.toList());
                            for(int j=0;j<courseIdOfThisStudent.size();j++){
                                //该学生专业不包含该Excel中的某个课程，返回导入失败
                                if(!collect.contains(courseIdOfThisStudent.get(j))){
                                    return Results.newFailedResult("导入失败,课程编码为："+courseIdOfThisStudent.get(j)+"的课程不属于该专业");
                                }
                            }

                        }else {
                            return Results.newFailedResult("导入失败,学号为："+studentId+"的学生不存在，或者未分配专业");
                        }
                    }
                }

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


    /**
     * 获取该辅导员下学生的成绩信息
     * @param userId
     * @return
     */
    @GetMapping("/getByCounselorUserId")
    public Result<Object> getByCounselorUserId(@RequestParam String userId){
        return Results.newSuccessResult(studentTranscriptService.getByCounselorUserId(userId));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    @PostMapping("/findByCondition")
    public Result<Object> findByCondition(@RequestBody StudentTranscriptResp resp){
        return Results.newSuccessResult(studentTranscriptService.findByCondition(resp));
    }


    @GetMapping("/findByStudentUserId")
    public Result<Object> findByStudentUserId(@RequestParam String userId){
        return Results.newSuccessResult(studentTranscriptService.findByStudentUserId(userId));
    }


    /**
     * 根据条件查询改学生的成绩
     * @param resp
     * @return
     */
    @PostMapping("/findByConditionWithStudent")
    public Result<Object> findByConditionWithStudent(@RequestBody StudentTranscriptResp resp){
        return Results.newSuccessResult(studentTranscriptService.findByConditionWithStudent(resp));
    }


}
