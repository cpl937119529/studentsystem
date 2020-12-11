package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IResumeService;
import com.example.project.studentsystem.entry.Resume;
import com.example.project.studentsystem.mapper.ResumeMapper;
import org.springframework.stereotype.Service;

@Service
public class IResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements IResumeService {
}
