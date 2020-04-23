package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IStudentService;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class IStudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
}
