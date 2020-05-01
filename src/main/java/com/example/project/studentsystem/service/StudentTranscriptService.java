package com.example.project.studentsystem.service;

import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.IService.impl.IStudentTranscriptServiceImpl;
import com.example.project.studentsystem.mapper.StudentTranscriptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentTranscriptService {

    @Autowired
    private IStudentTranscriptServiceImpl iStudentTranscriptService;

    @Autowired
    private StudentTranscriptMapper studentTranscriptMapper;

}
