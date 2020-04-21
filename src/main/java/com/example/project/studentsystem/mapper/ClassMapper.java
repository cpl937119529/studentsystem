package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Class;
import org.springframework.stereotype.Component;

@Component(value = "classMapper")
public interface ClassMapper extends BaseMapper<Class> {
}
