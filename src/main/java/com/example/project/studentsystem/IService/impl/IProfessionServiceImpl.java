package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IProfessionService;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.mapper.ProfessionMapper;
import org.springframework.stereotype.Service;

@Service
public class IProfessionServiceImpl extends ServiceImpl<ProfessionMapper, Profession> implements IProfessionService {
}
