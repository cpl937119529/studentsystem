package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.IUserService;
import com.example.project.studentsystem.IService.impl.IUserServiceImpl;
import com.example.project.studentsystem.dto.LoginReq;
import com.example.project.studentsystem.dto.UserResp;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.entry.User;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.example.project.studentsystem.mapper.UserMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    //去除admin后的所有账户信息
    public List<UserResp> getAll(){
        List<User> users = userMapper.selectList(null);
        List<UserResp> resultList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(users)){

            List<User> collect = users.stream().filter(data -> data.getId() != 1L).collect(Collectors.toList());

            collect.forEach(user -> {
                UserResp userResp = new UserResp();
                userResp.setId(user.getId().toString());
                userResp.setUserName(user.getUserName());
                userResp.setPassWord(user.getPassWord());
                userResp.setUserType(user.getUserType());
                userResp.setIdentity(user.getUserType()==3?"学生":"辅导员");
                //获取名字信息
                if(user.getUserType()==3){
                    QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                    studentQueryWrapper.eq("user_id",user.getId());
                    List<Student> students = studentMapper.selectList(studentQueryWrapper);
                    userResp.setName(students.get(0).getName());
                } else if(user.getUserType()==2){
                    QueryWrapper<Counselor> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id",user.getId());
                    List<Counselor> counselors = counselorMapper.selectList(queryWrapper);
                    userResp.setName(counselors.get(0).getCounselorName());
                }
                resultList.add(userResp);
            });
        }
        return resultList;
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
