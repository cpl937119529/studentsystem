package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IBonusRecordService;
import com.example.project.studentsystem.entry.BonusRecord;
import com.example.project.studentsystem.mapper.BonusRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class IBonusRecordServiceImpl extends ServiceImpl<BonusRecordMapper, BonusRecord> implements IBonusRecordService {
}
