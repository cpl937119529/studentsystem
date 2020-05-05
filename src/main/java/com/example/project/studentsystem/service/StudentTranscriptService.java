package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.IService.impl.IStudentTranscriptServiceImpl;
import com.example.project.studentsystem.dto.StudentTranscriptResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentTranscriptService {

    @Autowired
    private IStudentTranscriptServiceImpl iStudentTranscriptService;

    @Autowired
    private StudentTranscriptMapper studentTranscriptMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ProfessionMapper professionMapper;


    /**
     * 获取该辅导员下学生的成绩信息
     *
     * @param userId
     * @return
     */
    public List<StudentTranscriptResp> getByCounselorUserId(String userId) {
        List<StudentTranscriptResp> resultList = Lists.newArrayList();
        //根据userId获取辅导员信息
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id", Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);
        //根据辅导员信息获取其管理的专业关系
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("counselor_id", counselors.get(0).getId());
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        //辅导员有管理专业
        if (CollectionUtil.isNotEmpty(counselorProfessionRels)) {

            counselorProfessionRels.forEach(counselorProfessionRel -> {

                Profession profession = professionMapper.selectById(counselorProfessionRel.getProfessionId());

                //根据professionId、startYear获取学生信息
                QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("profession_id", counselorProfessionRel.getProfessionId())
                        .eq("start_year", counselorProfessionRel.getStartYear());
                List<Student> students = studentMapper.selectList(queryWrapper);

                //所管理的专业下有学生
                if (CollectionUtil.isNotEmpty(students)) {

                    students.forEach(student -> {
                        //获取该学生下的成绩信息
                        QueryWrapper<StudentTranscript> studentTranscriptQueryWrapper = new QueryWrapper<>();
                        studentTranscriptQueryWrapper.eq("student_id", student.getId());
                        List<StudentTranscript> studentTranscripts = studentTranscriptMapper.selectList(studentTranscriptQueryWrapper);
                        //该学生有成绩
                        if (CollectionUtil.isNotEmpty(studentTranscripts)) {
                            studentTranscripts.forEach(studentTranscript -> {
                                StudentTranscriptResp resp = new StudentTranscriptResp();
                                resp.setId(studentTranscript.getId().toString());
                                resp.setStudentId(studentTranscript.getStudentId().toString());
                                resp.setCourseId(studentTranscript.getCourseId().toString());
                                resp.setCredit(studentTranscript.getCredit());
                                resp.setScore(studentTranscript.getScore());
                                resp.setSemester(studentTranscript.getSemester());
                                resp.setYear(studentTranscript.getYear());
                                resp.setStudentName(student.getName());
                                Course course = courseMapper.selectById(studentTranscript.getCourseId());
                                resp.setCourseName(course.getCourseName());
                                resp.setProfessionName(profession.getProfessionName());
                                resp.setClassName(student.getClassName());
                                resultList.add(resp);
                            });
                        }
                    });
                }
            });
        }
        return resultList;
    }


    /**
     * 根据条件查询
     *
     * @param resp
     * @return
     */
    public List<StudentTranscriptResp> findByCondition(StudentTranscriptResp resp) {
        List<StudentTranscriptResp> resultList = this.getByCounselorUserId(resp.getUserId());

        if (resp.getYear() != null && resp.getClassName() == null && resp.getProfessionName() == null) {

            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear())).collect(Collectors.toList());
        } else if (resp.getYear() != null && resp.getClassName() != null && resp.getProfessionName() == null) {

            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear()) && data.getClassName() != null && data.getClassName().equals(resp.getClassName())).collect(Collectors.toList());
        } else if (resp.getYear() != null && resp.getClassName() != null && resp.getProfessionName() != null) {

            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear()) && data.getClassName() != null && data.getClassName().equals(resp.getClassName()) && data.getProfessionName().equals(resp.getProfessionName())).collect(Collectors.toList());
        } else if (resp.getYear() != null && resp.getClassName() == null && resp.getProfessionName() != null) {

            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear()) && data.getProfessionName().equals(resp.getProfessionName())).collect(Collectors.toList());
        } else if (resp.getYear() == null && resp.getClassName() != null && resp.getProfessionName() == null) {

            return resultList.stream().filter(data -> data.getClassName() != null && data.getClassName().equals(resp.getClassName())).collect(Collectors.toList());
        } else if (resp.getYear() == null && resp.getClassName() != null && resp.getProfessionName() != null) {

            return resultList.stream().filter(data -> data.getClassName() != null && data.getClassName().equals(resp.getClassName()) && data.getProfessionName().equals(resp.getProfessionName())).collect(Collectors.toList());
        } else if (resp.getYear() == null && resp.getClassName() == null && resp.getProfessionName() != null) {

            return resultList.stream().filter(data -> data.getProfessionName().equals(resp.getProfessionName())).collect(Collectors.toList());
        } else {

            return resultList;
        }
    }


    /**
     * 查找该学生的成绩信息
     *
     * @param userId
     * @return
     */
    public List<StudentTranscriptResp> findByStudentUserId(String userId) {
        List<StudentTranscriptResp> resultList = Lists.newArrayList();
        //根据userId获取学生信息
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id", Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        //获取专业信息
        Profession profession = professionMapper.selectById(students.get(0).getProfessionId());
        //获取成绩信息
        QueryWrapper<StudentTranscript> studentTranscriptQueryWrapper = new QueryWrapper<>();
        studentTranscriptQueryWrapper.eq("student_id", students.get(0).getId());
        List<StudentTranscript> studentTranscripts = studentTranscriptMapper.selectList(studentTranscriptQueryWrapper);
        //有该学生的成绩
        if (CollectionUtil.isNotEmpty(studentTranscripts)) {
            studentTranscripts.forEach(studentTranscript -> {
                StudentTranscriptResp resp = new StudentTranscriptResp();
                resp.setId(studentTranscript.getId().toString());
                resp.setStudentId(studentTranscript.getStudentId().toString());
                resp.setCourseId(studentTranscript.getCourseId().toString());
                resp.setCredit(studentTranscript.getCredit());
                resp.setScore(studentTranscript.getScore());
                resp.setSemester(studentTranscript.getSemester());
                resp.setYear(studentTranscript.getYear());
                resp.setStudentName(students.get(0).getName());
                Course course = courseMapper.selectById(studentTranscript.getCourseId());
                resp.setCourseName(course.getCourseName());
                resp.setProfessionName(profession.getProfessionName());
                resp.setClassName(students.get(0).getClassName());
                resultList.add(resp);
            });
        }

        return resultList;
    }


    /**
     * 根据条件查询改学生的成绩
     * @param resp
     * @return
     */
    public List<StudentTranscriptResp> findByConditionWithStudent(StudentTranscriptResp resp) {
        List<StudentTranscriptResp> resultList = this.findByStudentUserId(resp.getUserId());

        if (resp.getYear() != null && resp.getCourseName() == null) {
            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear())).collect(Collectors.toList());
        } else if (resp.getYear() != null && resp.getCourseName() != null) {
            return resultList.stream().filter(data -> data.getYear().equals(resp.getYear()) && data.getCourseName().equals(resp.getCourseName())).collect(Collectors.toList());
        } else if (resp.getYear() == null && resp.getCourseName() != null) {
            return resultList.stream().filter(data -> data.getCourseName().equals(resp.getCourseName())).collect(Collectors.toList());
        } else {
            return resultList;
        }
    }
}
