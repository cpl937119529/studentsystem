package com.example.project.studentsystem.IService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.studentsystem.IService.IDeductionRecordService;
import com.example.project.studentsystem.entry.DeductionRecord;
import com.example.project.studentsystem.mapper.DeductionRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class IDeductionRecordServiceImpl extends ServiceImpl<DeductionRecordMapper, DeductionRecord> implements IDeductionRecordService {
}
