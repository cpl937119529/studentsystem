package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.ComprehensiveTest;
import org.springframework.stereotype.Component;

@Component(value = "comprehensiveTestMapper")
public interface ComprehensiveTestMapper extends BaseMapper<ComprehensiveTest> {
}
