package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.ISemesterProfessionalCoursesService;
import com.example.project.studentsystem.entry.SemesterProfessionalCourses;
import com.example.project.studentsystem.mapper.SemesterProfessionalCoursesMapper;
import org.springframework.stereotype.Service;

@Service
public class ISemesterProfessionalCoursesServiceImpl extends ServiceImpl<SemesterProfessionalCoursesMapper, SemesterProfessionalCourses> implements ISemesterProfessionalCoursesService {
}
