package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IPositionService;
import com.example.project.studentsystem.entry.Position;
import com.example.project.studentsystem.mapper.PositionMapper;
import org.springframework.stereotype.Service;

@Service
public class IPositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {
}
