package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Resume;
import org.springframework.stereotype.Component;

@Component(value = "resumeMapper")
public interface ResumeMapper extends BaseMapper<Resume> {
}
