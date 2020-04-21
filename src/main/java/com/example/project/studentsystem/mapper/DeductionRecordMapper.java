package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.DeductionRecord;
import org.springframework.stereotype.Component;

@Component(value = "deductionRecordMapper")
public interface DeductionRecordMapper extends BaseMapper<DeductionRecord> {
}
