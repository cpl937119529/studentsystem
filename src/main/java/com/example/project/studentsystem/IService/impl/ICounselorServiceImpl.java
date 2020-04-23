package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.ICounselorService;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.mapper.CounselorMapper;
import org.springframework.stereotype.Service;

@Service
public class ICounselorServiceImpl extends ServiceImpl<CounselorMapper, Counselor> implements ICounselorService {
}
