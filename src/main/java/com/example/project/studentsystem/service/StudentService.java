package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.dto.StudentResp;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.ClassMapper;
import com.example.project.studentsystem.mapper.ProfessionMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private IStudentServiceImpl studentService;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ProfessionMapper professionMapper;


    /**
     * 获取全部学生信息
     * @return
     */
    public List<StudentResp> getAll(){

        List<StudentResp> resultList = Lists.newArrayList();

        List<Student> students = studentMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(students)){
            students.forEach(student -> {
                StudentResp resp = new StudentResp();
                BeanUtils.copyProperties(student,resp);

                resp.setId(student.getId().toString());

                resp.setUserId(student.getUserId().toString());


                //获取班级信息
                if(student.getClassId()!=null){
                    resp.setClassId(student.getClassId().toString());
                    Class aClass = classMapper.selectById(student.getClassId());
                    if(aClass!=null){
                        resp.setClassName(aClass.getClassName());
                    }
                }

                if(student.getProfessionId()!=null){
                    resp.setProfessionId(student.getProfessionId().toString());
                    //获取专业信息
                    Profession profession = professionMapper.selectById(student.getProfessionId());
                    if(profession!=null){
                        resp.setProfessionName(profession.getProfessionName());
                    }
                }

                resultList.add(resp);
            });
        }

        return resultList;

    }

}
