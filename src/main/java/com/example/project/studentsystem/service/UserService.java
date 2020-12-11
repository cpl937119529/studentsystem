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
import com.google.common.collect.Lists;
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

    public LoginReq login(String userName,String password){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",userName);
        wrapper.eq("pass_word",password);
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            LoginReq req = new LoginReq();
            req.setUserName(userName);
            req.setPassWord(password);
            req.setId(users.get(0).getId().toString());
            req.setUserType(users.get(0).getUserType());
            Integer userType = users.get(0).getUserType();
            if(userType==1){
                req.setName("admin");
            }else if(userType==2){
                QueryWrapper<Counselor> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",users.get(0).getId());
                List<Counselor> counselors = counselorMapper.selectList(queryWrapper);
                req.setName(counselors.get(0).getCounselorName());
            }else{
                QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",users.get(0).getId());
                List<Student> students = studentMapper.selectList(queryWrapper);
                req.setName(students.get(0).getName());
            }

            return req;
        }else {
            return new LoginReq();
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


    /**
     * 根据条件查找
     * @param resp
     * @return
     */
    public List<UserResp> findByCondition(UserResp resp){
        List<UserResp> resultList = Lists.newArrayList();

        //如果传过来的姓名不为空
        if(resp.getName()!=null){
            //先在学生表查找，找不到再到辅导员表查找，两者都找不到则返回空数据
            QueryWrapper<Student> studentWrapper = new QueryWrapper<>();
            studentWrapper.eq("name",resp.getName());
            List<Student> students = studentMapper.selectList(studentWrapper);
            if(CollectionUtil.isNotEmpty(students)){
                students.forEach(student -> {
                    UserResp userResp = new UserResp();
                    userResp.setName(student.getName());

                    User user = userMapper.selectById(student.getUserId());
                    userResp.setUserType(3);
                    userResp.setUserName(user.getUserName());
                    userResp.setPassWord(user.getPassWord());
                    userResp.setId(user.getId().toString());
                    resultList.add(userResp);

                });
            }

            QueryWrapper<Counselor> counselorWrapper = new QueryWrapper<>();
            counselorWrapper.eq("counselor_name",resp.getName());
            List<Counselor> counselors = counselorMapper.selectList(counselorWrapper);
            if(CollectionUtil.isNotEmpty(counselors)){
                counselors.forEach(counselor -> {
                    UserResp userResp = new UserResp();
                    userResp.setName(counselor.getCounselorName());

                    User user = userMapper.selectById(counselor.getUserId());
                    userResp.setUserType(3);
                    userResp.setUserName(user.getUserName());
                    userResp.setPassWord(user.getPassWord());
                    userResp.setId(user.getId().toString());
                    resultList.add(userResp);
                });
            }

            //得到两张表的数据后，如果不为空，根据用户名和用户类型进行过滤
            if(CollectionUtil.isNotEmpty(resultList)){
                if(resp.getUserName()!=null && resp.getUserType()==null){

                   return resultList.stream().filter(data -> data.getUserName().equals(resp.getUserName())).collect(Collectors.toList());
                }else if(resp.getUserName()==null && resp.getUserType()!=null){

                    return resultList.stream().filter(data -> data.getUserType().equals(resp.getUserType())).collect(Collectors.toList());
                }else if(resp.getUserName()!=null && resp.getUserType()!=null){

                    return resultList.stream().filter(data -> data.getUserName().equals(resp.getUserName()) && data.getUserType().equals(resp.getUserType())).collect(Collectors.toList());
                }else {

                    return resultList;
                }
            }else{
                //两张表都找不到数据
                return resultList;
            }


        }else {

            QueryWrapper<User> userWrapper = new QueryWrapper<>();
            //传过来的name为空的情况
            if(resp.getUserName()==null && resp.getUserType()!=null){
                userWrapper.eq("user_type",resp.getUserType());

            }else if(resp.getUserName()!=null && resp.getUserType()==null){

                userWrapper.eq("user_name",resp.getUserName());

            }else if(resp.getUserName()!=null && resp.getUserType()!=null){
                userWrapper.eq("user_name",resp.getUserName());
                userWrapper.eq("user_type",resp.getUserType());
            }

            List<User> userLists = userMapper.selectList(userWrapper);
            //去除admin账户
            List<User> users = userLists.stream().filter(data -> !data.getUserName().equals("admin")).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(users)){
                users.forEach(user -> {
                    UserResp userResp = new UserResp();
                    userResp.setId(user.getId().toString());
                    userResp.setUserName(user.getUserName());
                    userResp.setPassWord(user.getPassWord());
                    userResp.setUserType(user.getUserType());
                    if(user.getUserType()==2){
                        //辅导员
                        QueryWrapper<Counselor> counselorWrapper = new QueryWrapper<>();
                        counselorWrapper.eq("user_id", user.getId());
                        List<Counselor> counselors = counselorMapper.selectList(counselorWrapper);
                        userResp.setName(counselors.get(0).getCounselorName());
                    }else {
                        //学生
                        QueryWrapper<Student> studentWrapper = new QueryWrapper<>();
                        studentWrapper.eq("user_id", user.getId());
                        List<Student> students = studentMapper.selectList(studentWrapper);
                        userResp.setName(students.get(0).getName());
                    }
                    resultList.add(userResp);
                });
            }

            return resultList;

        }

    }

    /**
     * 修改密码
     * @param resp
     * @return
     */
    public int updatePassword(UserResp resp){
        User user = userMapper.selectById(Long.valueOf(resp.getId()));

        if(!resp.getPassWord().equals(user.getPassWord())){
            //输入的密码与数据库的密码不一致
            return -1;
        }
        user.setPassWord(resp.getNewPassWord());
        boolean b = iUserService.saveOrUpdate(user);
        if(b){
            return 1;
        }else {
            return 0;
        }

    }

}
