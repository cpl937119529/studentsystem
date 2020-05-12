package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IStudentServiceImpl;
import com.example.project.studentsystem.dto.StudentResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private IStudentServiceImpl studentService;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;

    @Autowired
    private CounselorMapper counselorMapper;


    /**
     * 获取未分配专业的全部学生信息
     * @return
     */
    public List<StudentResp> getAll(){

        List<StudentResp> resultList = Lists.newArrayList();
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.isNull("profession_id");
        List<Student> students = studentMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(students)){
            students.forEach(student -> {
                StudentResp resp = new StudentResp();
                BeanUtils.copyProperties(student,resp);

                resp.setId(student.getId().toString());

                resp.setUserId(student.getUserId().toString());

                if(student.getBirth()!=null){
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate time = student.getBirth();
                    String localTime = df.format(time);
                    resp.setBirth(localTime);
                }


                resultList.add(resp);
            });
        }

        return resultList;

    }


    /**
     * updateInfo
     * @param resp
     * @return
     */
    public boolean updateInfo(StudentResp resp){
        Student student = new Student();
        BeanUtils.copyProperties(resp,student);

        if(resp.getBirth()!=null){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ldt = LocalDate.parse(resp.getBirth(),df);
            student.setBirth(ldt);
        }
        if(resp.getId()!=null){
            student.setId(Long.valueOf(resp.getId()));
        }

        if(resp.getUserId()!=null){
            student.setUserId(Long.valueOf(resp.getUserId()));
        }

        if(resp.getProfessionId()!=null){
            student.setProfessionId(Long.valueOf(resp.getProfessionId()));
        }


       return studentService.saveOrUpdate(student);
    }


    /**
     * 添加学生用户
     * @param resp
     * @return
     */
    public int addStudent(StudentResp resp){

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",resp.getUserName());
        wrapper.eq("pass_word",resp.getPassWord());
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            return -1;
        }

        User user = new User();
        user.setUserType(3);
        user.setUserName(resp.getUserName());
        user.setPassWord(resp.getPassWord());
        userMapper.insert(user);

        List<User> userList = userMapper.selectList(wrapper);
        Long userId = userList.get(0).getId();
        Student student = new Student();
        student.setUserId(userId);
        student.setName(resp.getName());
        student.setSex(resp.getSex());
        student.setStartYear(resp.getStartYear());
        studentMapper.insert(student);
        return 1;

    }


    /**
     * 获取未全部学生信息
     * @return
     */
    public List<StudentResp> getAllInfo(){
        List<StudentResp> resultList = Lists.newArrayList();
        List<Student> students = studentMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(students)){
            students.forEach(student -> {
                StudentResp resp = new StudentResp();
                BeanUtils.copyProperties(student,resp);

                resp.setId(student.getId().toString());

                resp.setUserId(student.getUserId().toString());


                if(student.getProfessionId()!=null){
                    resp.setProfessionId(student.getProfessionId().toString());
                    //获取专业信息
                    Profession profession = professionMapper.selectById(student.getProfessionId());
                    if(profession!=null){
                        resp.setProfessionName(profession.getProfessionName());
                    }
                }

                resultList.add(resp);
            });
        }
        return resultList;

    }


    /**
     * 根据学生姓名、专业名称、入学年份进行查询
     * @param resp
     * @return
     */
    public List<StudentResp> searchByCondition(StudentResp resp){
        //获取所有学生信息
        List<StudentResp> studentLists = this.getStudentListByCounselor(resp.getUserId());

        if(resp.getName()!=null && resp.getProfessionName()==null && resp.getStartYear()==null){
            //根据学生姓名查询
            return  studentLists.stream()
                    .filter(data-> data.getName().equals(resp.getName()))
                    .collect(Collectors.toList());
        }else if(resp.getName()==null && resp.getProfessionName()!=null && resp.getStartYear()==null){
            //根据专业查询
            return  studentLists.stream()
                    .filter(data-> data.getProfessionName()!=null && data.getProfessionName().equals(resp.getProfessionName()))
                    .collect(Collectors.toList());
        }else if(resp.getName()==null && resp.getProfessionName()==null && resp.getStartYear()!=null){
            //根据入学年份查
            return  studentLists.stream()
                    .filter(data-> data.getStartYear().equals(resp.getStartYear()))
                    .collect(Collectors.toList());
        }else if(resp.getName()!=null && resp.getProfessionName()!=null && resp.getStartYear()==null){
            //根据学生姓名,专业查询
            return  studentLists.stream()
                    .filter(data-> data.getName().equals(resp.getName()) && data.getProfessionName()!=null && data.getProfessionName().equals(resp.getProfessionName()))
                    .collect(Collectors.toList());
        }else if(resp.getName()!=null && resp.getProfessionName()==null && resp.getStartYear()!=null){
            //根据学生姓名,入学年份查询
            return  studentLists.stream()
                    .filter(data-> data.getName().equals(resp.getName()) && data.getStartYear().equals(resp.getStartYear()))
                    .collect(Collectors.toList());
        }else if(resp.getName()==null && resp.getProfessionName()!=null && resp.getStartYear()!=null){
            //根据专业,入学年份查询
            return  studentLists.stream()
                    .filter(data-> data.getProfessionName()!=null && data.getProfessionName().equals(resp.getProfessionName()) && data.getStartYear().equals(resp.getStartYear()))
                    .collect(Collectors.toList());
        }else if(resp.getName()!=null && resp.getProfessionName()!=null && resp.getStartYear()!=null){
            //根据姓名,专业,入学年份查询
            return  studentLists.stream()
                    .filter(data-> data.getName().equals(resp.getName()) && data.getProfessionName()!=null && data.getProfessionName().equals(resp.getProfessionName()) && data.getStartYear().equals(resp.getStartYear()))
                    .collect(Collectors.toList());
        }else {
            //查询全部
            return studentLists;
        }

    }

    /**
     *根据入学年份、班级名称、专业名称查询学生信息
     * @param resp
     * @return
     */
    public List<StudentResp> findByCondition(StudentResp resp){
        List<StudentResp> studentLists = this.getStudentListByCounselor(resp.getUserId());

        if(resp.getClassName()!=null && resp.getProfessionName()==null && resp.getStartYear()==null){
            return studentLists.stream().filter(data-> data.getClassName()!=null && data.getClassName().equals(resp.getClassName())).collect(Collectors.toList());
        }else if(resp.getClassName()!=null && resp.getProfessionName()!=null && resp.getStartYear()==null){
            QueryWrapper<Profession> professionQueryWrapper = new QueryWrapper<>();
            professionQueryWrapper.eq("profession_name",resp.getProfessionName());
            List<Profession> professions = professionMapper.selectList(professionQueryWrapper);
            if(CollectionUtil.isNotEmpty(professions)){
                return studentLists.stream().filter(data-> data.getClassName()!=null && data.getClassName().equals(resp.getClassName()) && data.getProfessionId().equals(professions.get(0).getId().toString())).collect(Collectors.toList());
            }else {
                return Lists.newArrayList();
            }
        }else if(resp.getClassName()!=null && resp.getProfessionName()==null && resp.getStartYear()!=null){
            return studentLists.stream().filter(data-> data.getClassName()!=null && data.getClassName().equals(resp.getClassName()) && data.getStartYear().equals(resp.getStartYear())).collect(Collectors.toList());
        }else if(resp.getClassName()==null && resp.getProfessionName()!=null && resp.getStartYear()==null){
            QueryWrapper<Profession> professionQueryWrapper = new QueryWrapper<>();
            professionQueryWrapper.eq("profession_name",resp.getProfessionName());
            List<Profession> professions = professionMapper.selectList(professionQueryWrapper);
            if(CollectionUtil.isNotEmpty(professions)){
                return studentLists.stream().filter(data->data.getProfessionId().equals(professions.get(0).getId().toString())).collect(Collectors.toList());
            }else {
                return Lists.newArrayList();
            }
        }else if(resp.getClassName()==null && resp.getProfessionName()!=null && resp.getStartYear()!=null){
            QueryWrapper<Profession> professionQueryWrapper = new QueryWrapper<>();
            professionQueryWrapper.eq("profession_name",resp.getProfessionName());
            List<Profession> professions = professionMapper.selectList(professionQueryWrapper);
            if(CollectionUtil.isNotEmpty(professions)){
                return studentLists.stream().filter(data->data.getProfessionId().equals(professions.get(0).getId().toString()) && data.getStartYear().equals(resp.getStartYear())).collect(Collectors.toList());
            }else {
                return Lists.newArrayList();
            }
        } else if(resp.getClassName()!=null && resp.getProfessionName()!=null && resp.getStartYear()!=null){
            QueryWrapper<Profession> professionQueryWrapper = new QueryWrapper<>();
            professionQueryWrapper.eq("profession_name",resp.getProfessionName());
            List<Profession> professions = professionMapper.selectList(professionQueryWrapper);
            if(CollectionUtil.isNotEmpty(professions)){
                return studentLists.stream().filter(data-> data.getClassName()!=null && data.getClassName().equals(resp.getClassName()) && data.getProfessionId().equals(professions.get(0).getId().toString()) && data.getStartYear().equals(resp.getStartYear())).collect(Collectors.toList());
            }else {
                return Lists.newArrayList();
            }
        }else if(resp.getClassName()==null && resp.getProfessionName()==null && resp.getStartYear()!=null){
            return studentLists.stream().filter(data->data.getStartYear().equals(resp.getStartYear())).collect(Collectors.toList());
        }else{
            return studentLists;
        }
    }



    /**
     * 根据用户id查找辅导员ID并查询其管理专业下的学生信息
     * @param userId
     * @return
     */
    public List<StudentResp> getStudentListByCounselor(String userId){

        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);


        List<StudentResp> resultList = Lists.newArrayList();

        QueryWrapper<CounselorProfessionRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id",counselors.get(0).getId());
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(counselorProfessionRels)){
            counselorProfessionRels.forEach(counselorProfessionRel -> {
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("profession_id",counselorProfessionRel.getProfessionId())
                                   .eq("start_year",counselorProfessionRel.getStartYear());
                List<Student> students = studentMapper.selectList(studentQueryWrapper);

                if(CollectionUtil.isNotEmpty(students)){
                    students.forEach(student -> {
                        StudentResp resp = new StudentResp();
                        BeanUtils.copyProperties(student,resp);

                        resp.setId(student.getId().toString());
                        resp.setUserId(student.getUserId().toString());
                        if(student.getBirth()!=null){

                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String localTime = df.format(student.getBirth());
                            resp.setBirth(localTime);
                        }
                        resp.setProfessionId(student.getProfessionId().toString());
                        Profession profession = professionMapper.selectById(student.getProfessionId());
                        resp.setProfessionName(profession.getProfessionName());
                        resultList.add(resp);
                    });
                }
            });
        }
        return resultList;
    }


    /**
     * 根据用户ID获取学生信息
     * @param userId
     * @return
     */
    public StudentResp getInfoByUserId(String userId){
        StudentResp resp = new StudentResp();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        Student student = students.get(0);

        BeanUtils.copyProperties(student,resp);
        resp.setId(student.getId().toString());
        resp.setUserId(userId);
        resp.setProfessionId(student.getProfessionId().toString());

        if(student.getBirth()!=null){

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localTime = df.format(student.getBirth());
            resp.setBirth(localTime);
        }

        if(student.getProfessionId()!=null){
            resp.setProfessionId(student.getProfessionId().toString());
            Profession profession = professionMapper.selectById(student.getProfessionId());
            resp.setProfessionName(profession.getProfessionName());

        }

        return resp;
    }

    /**
     * 根据学生id获取学生信息
     * @param id
     * @return
     */
    public StudentResp getInfoById(String id){
        StudentResp resp = new StudentResp();
        Student student = studentMapper.selectById(Long.valueOf(id));
        BeanUtils.copyProperties(student,resp);
        resp.setId(student.getId().toString());
        resp.setProfessionId(student.getProfessionId().toString());

        if(student.getBirth()!=null){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localTime = df.format(student.getBirth());
            resp.setBirth(localTime);
        }

        if(student.getProfessionId()!=null){
            resp.setProfessionId(student.getProfessionId().toString());
            Profession profession = professionMapper.selectById(student.getProfessionId());
            resp.setProfessionName(profession.getProfessionName());
        }
        return resp;
    }

}
