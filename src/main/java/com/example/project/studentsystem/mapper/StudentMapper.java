package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Student;
import org.springframework.stereotype.Component;

@Component(value = "studentMapper")
public interface StudentMapper extends BaseMapper<Student> {
}
