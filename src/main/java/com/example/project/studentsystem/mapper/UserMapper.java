package com.example.project.studentsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.project.studentsystem.entry.User;
import org.springframework.stereotype.Component;

@Component(value = "userMapper")
public interface UserMapper extends BaseMapper<User> {
}
