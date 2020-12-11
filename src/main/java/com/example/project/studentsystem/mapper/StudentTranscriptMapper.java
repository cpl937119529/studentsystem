package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.StudentTranscript;
import org.springframework.stereotype.Component;

@Component(value = "studentTranscriptMapper")
public interface StudentTranscriptMapper extends BaseMapper<StudentTranscript> {
}
