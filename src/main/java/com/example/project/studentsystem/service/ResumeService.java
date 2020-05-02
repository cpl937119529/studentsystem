package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IResumeServiceImpl;
import com.example.project.studentsystem.dto.ResumeResp;
import com.example.project.studentsystem.entry.Resume;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.ResumeMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private IResumeServiceImpl iResumeService;

    @Autowired
    private StudentMapper studentMapper;


    /**
     * 更新或保存
     * @param resp
     * @return
     */
    public boolean saveOrUpdate(ResumeResp resp){

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",resp.getUserId());
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        Resume resume = new Resume();
        BeanUtils.copyProperties(resp,resume);
        resume.setStudentId(students.get(0).getId());

        //判断有无该学生的简历信息，有则更新，无则保存
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("student_id",students.get(0).getId());
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);
        if(CollectionUtil.isNotEmpty(resumes)){
            resume.setId(resumes.get(0).getId());
        }
         return iResumeService.saveOrUpdate(resume);
    }


    /**
     * 获取该学生的简历信息
     * @param userId
     * @return
     */
    public ResumeResp getInfo(String userId){
        ResumeResp resp = new ResumeResp();

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("student_id",students.get(0).getId());
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);

        if(CollectionUtil.isNotEmpty(resumes)){
            Resume resume = resumes.get(0);
            BeanUtils.copyProperties(resume,resp);
            resp.setId(resume.getId().toString());
            resp.setStudentId(resume.getStudentId().toString());
        }
        return resp;
    }
}
