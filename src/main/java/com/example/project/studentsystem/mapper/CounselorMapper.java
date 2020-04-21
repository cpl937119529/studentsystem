package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Counselor;
import org.springframework.stereotype.Component;

@Component(value = "counselorMapper")
public interface CounselorMapper extends BaseMapper<Counselor> {
}
