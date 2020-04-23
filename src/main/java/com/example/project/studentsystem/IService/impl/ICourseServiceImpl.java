package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.ICourseService;
import com.example.project.studentsystem.entry.Course;
import com.example.project.studentsystem.mapper.CourseMapper;
import org.springframework.stereotype.Service;

@Service
public class ICourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
}
