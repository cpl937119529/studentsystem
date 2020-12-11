package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.SemesterProfessionalCourses;
import org.springframework.stereotype.Component;

@Component(value = "semesterProfessionalCoursesMapper")
public interface SemesterProfessionalCoursesMapper extends BaseMapper<SemesterProfessionalCourses> {
}
