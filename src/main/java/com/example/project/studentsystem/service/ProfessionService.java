package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.IProfessionService;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.dto.ProfessionResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionService {

    @Autowired
    private IProfessionService professionService;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;


    /**
     * 添加专业
     * @param profession
     * @return
     */
    public int addProfession(Profession profession){

        //判断有无该专业，无则添加
        QueryWrapper<Profession> wrapper = new QueryWrapper<>();
        wrapper.eq("profession_name",profession.getProfessionName());
        List<Profession> professions = professionMapper.selectList(wrapper);

        if(CollectionUtil.isNotEmpty(professions)){
            return -1;
        }

        boolean result = professionService.saveOrUpdate(profession);

        if (result){

            //同时为改专业添加班级信息
            List<Profession> professionList = professionMapper.selectList(wrapper);
            Class classInfo = new Class();
            classInfo.setClassName(profession.getProfessionName()+"1班");
            classInfo.setProfessionId(professionList.get(0).getId());
            classMapper.insert(classInfo);

            Class classInfo1 = new Class();
            classInfo1.setClassName(profession.getProfessionName()+"2班");
            classInfo1.setProfessionId(professionList.get(0).getId());
            classMapper.insert(classInfo1);
            return 1;
        }
        return -2;
    }

    /**
     * 获取所有专业信息
     * @return
     */
    public List<ProfessionResp> getAll(){
        List<ProfessionResp> resultList = Lists.newArrayList();
        List<Profession> professions = professionMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(professions)){

            professions.forEach(profession -> {
                ProfessionResp resp = new ProfessionResp();
                BeanUtils.copyProperties(profession,resp);
                resp.setId(profession.getId().toString());
                resultList.add(resp);
            });

        }

        return resultList;
    }

    /**
     * 根据id删除专业
     * @param id
     * @return
     */
    public int deleteById(String id){

        //判断辅导员-专业关联表
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("profession_id",Long.valueOf(id));
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            return -1;
        }

        //判断学生-专业关联表
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("profession_id",Long.valueOf(id));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        if(CollectionUtil.isNotEmpty(students)){
            return -1;
        }
        //判断课程表
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("profession_id",Long.valueOf(id));
        List<Course> courseList = courseMapper.selectList(courseQueryWrapper);
        if(CollectionUtil.isNotEmpty(courseList)){
            return -1;
        }

        QueryWrapper<Class> classWrapper = new QueryWrapper<>();
        classWrapper.eq("profession_id",Long.valueOf(id));

        classMapper.delete(classWrapper);

        return professionMapper.deleteById(Long.valueOf(id));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    public List<ProfessionResp> findByCondition(ProfessionResp resp){
        List<ProfessionResp> resultList = Lists.newArrayList();
        QueryWrapper<Profession> professionWrapper = new QueryWrapper<>();
        if(resp.getProfessionName()!=null && resp.getDepartment()==null){
            professionWrapper.eq("profession_name",resp.getProfessionName());
        }else if(resp.getProfessionName()==null && resp.getDepartment()!=null){
            professionWrapper.eq("department",resp.getDepartment());
        }else if(resp.getProfessionName()!=null && resp.getDepartment()!=null){
            professionWrapper.eq("profession_name",resp.getProfessionName());
            professionWrapper.eq("department",resp.getDepartment());
        }

        List<Profession> professions = professionMapper.selectList(professionWrapper);
        if(CollectionUtil.isNotEmpty(professions)){
            professions.forEach(profession -> {
                ProfessionResp resp1 = new ProfessionResp();
                BeanUtils.copyProperties(profession,resp1);
                resp1.setId(profession.getId().toString());
                resultList.add(resp1);
            });
        }

        return resultList;
    }

}
