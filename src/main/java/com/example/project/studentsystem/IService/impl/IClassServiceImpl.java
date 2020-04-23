package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IClassService;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.mapper.ClassMapper;
import org.springframework.stereotype.Service;

@Service
public class IClassServiceImpl extends ServiceImpl<ClassMapper,Class> implements IClassService {
}
