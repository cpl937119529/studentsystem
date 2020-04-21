package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Course;
import org.springframework.stereotype.Component;

@Component(value = "courseMapper")
public interface CourseMapper extends BaseMapper<Course> {
}
