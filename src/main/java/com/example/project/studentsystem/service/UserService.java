package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.entry.User;
import com.example.project.studentsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean login(String userName,String password){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userName);
        wrapper.eq("password",password);
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            return true;
        }else {
            return false;
        }
    }
}
