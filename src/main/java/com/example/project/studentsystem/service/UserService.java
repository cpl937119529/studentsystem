package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.IUserService;
import com.example.project.studentsystem.IService.impl.IUserServiceImpl;
import com.example.project.studentsystem.dto.LoginReq;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.entry.User;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.example.project.studentsystem.mapper.UserMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private IUserServiceImpl iUserService;

    public int login(String userName,String password){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userName);
        wrapper.eq("pass_word",password);
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            return users.get(0).getUserType();
        }else {
            return -1;
        }
    }


    public List<User> getAll(){
        List<User> users = userMapper.selectList(null);
        List<User> result = users.stream().filter(user -> user.getUserType() == null).collect(Collectors.toList());
        return result;
    }

    public int addUser(LoginReq req){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",req.getUserName());
        wrapper.eq("pass_word",req.getPassWord());
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            return -99;
        }

        User user = new User();
        user.setUserName(req.getUserName());
        user.setPassWord(req.getPassWord());
        return userMapper.insert(user);
    }


    public Long assignAccount(User user){
        //更新用户表的用户类型

        iUserService.saveOrUpdate(user);

        //根据Type判断添加的是辅导员还是学生,2:辅导员；3：学生
        if(user.getUserType()==2){
            Counselor counselor = new Counselor();
            counselor.setUserId(user.getId());
            counselorMapper.insert(counselor);
        }else {
            Student student = new Student();
            student.setUserId(user.getId());
            studentMapper.insert(student);
        }

        return user.getId();
    }





}
