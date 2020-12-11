package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.BonusRecord;
import org.springframework.stereotype.Component;

@Component(value = "bonusRecordMapper")
public interface BonusRecordMapper extends BaseMapper<BonusRecord> {
}
