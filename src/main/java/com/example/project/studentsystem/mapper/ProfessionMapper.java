package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Profession;
import org.springframework.stereotype.Component;

@Component(value = "professionMapper")
public interface ProfessionMapper extends BaseMapper<Profession> {
}
