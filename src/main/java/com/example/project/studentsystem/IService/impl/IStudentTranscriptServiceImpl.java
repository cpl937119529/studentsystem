package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IStudentTranscriptService;
import com.example.project.studentsystem.entry.StudentTranscript;
import com.example.project.studentsystem.mapper.StudentTranscriptMapper;
import org.springframework.stereotype.Service;

@Service
public class IStudentTranscriptServiceImpl extends ServiceImpl<StudentTranscriptMapper, StudentTranscript> implements IStudentTranscriptService {
}
