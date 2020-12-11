package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.Position;
import org.springframework.stereotype.Component;

@Component(value = "positionMapper")
public interface PositionMapper extends BaseMapper<Position> {
}
