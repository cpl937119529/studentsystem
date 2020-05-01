package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IComprehensiveTestService;
import com.example.project.studentsystem.entry.ComprehensiveTest;
import com.example.project.studentsystem.mapper.ComprehensiveTestMapper;
import org.springframework.stereotype.Service;

@Service
public class IComprehensiveTestServiceImpl extends ServiceImpl<ComprehensiveTestMapper, ComprehensiveTest> implements IComprehensiveTestService {
}
