package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.ICourseServiceImpl;
import com.example.project.studentsystem.dto.CourseResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ICourseServiceImpl iCourseService;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private SemesterProfessionalCoursesMapper semesterProfessionalCoursesMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;

    /**
     * 获取所有课程表信息
     * @return
     */
    public List<CourseResp> getAll(){
        List<CourseResp> respList = Lists.newArrayList();
        List<Course> courseList = courseMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(courseList)){
            courseList.forEach(data->{
                CourseResp resp = new CourseResp();
                BeanUtils.copyProperties(data,resp);
                resp.setId(data.getId().toString());
                resp.setProfessionId(data.getProfessionId().toString());

                Profession profession = professionMapper.selectById(data.getProfessionId());
                resp.setProfessionName(profession.getProfessionName());
                respList.add(resp);
            });
        }
        return respList;
    }

    /**
     * 获取辅导员管辖专业的课程
     * @param userId
     * @return
     */
    public List<CourseResp> getCourseByCourseUserId(String userId){
        List<CourseResp> respList = Lists.newArrayList();
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);
        Counselor counselor = counselors.get(0);
        //获取该辅导员管辖的专业
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("counselor_id",counselor.getId());
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            //有管辖专业
            counselorProfessionRels.forEach(rel->{
                //获取该专业的课程
               QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
               queryWrapper.eq("profession_id",rel.getProfessionId());
                List<Course> courseList = courseMapper.selectList(queryWrapper);
                if(CollectionUtil.isNotEmpty(courseList)){
                    //该专业下有课程信息
                    courseList.forEach(data->{
                        CourseResp resp = new CourseResp();
                        BeanUtils.copyProperties(data,resp);
                        resp.setId(data.getId().toString());
                        resp.setProfessionId(data.getProfessionId().toString());

                        Profession profession = professionMapper.selectById(data.getProfessionId());
                        resp.setProfessionName(profession.getProfessionName());
                        respList.add(resp);
                    });
                }
            });

        }
        return respList.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 根据课程名称、专业名称进行查询
     * @param resp
     * @return
     */
    public List<CourseResp> searchByCondition(CourseResp resp){

        List<CourseResp> courseList = this.getAll();

        if(resp.getCourseName()!=null && resp.getProfessionName()==null){
            //根据课程名称进行查询
            return courseList.stream()
                    .filter(data-> data.getCourseName().equals(resp.getCourseName()))
                    .collect(Collectors.toList());
        }else if(resp.getCourseName()==null && resp.getProfessionName()!=null){
            //根据专业名称进行查询
            return courseList.stream()
                    .filter(data-> data.getProfessionName().equals(resp.getProfessionName()))
                    .collect(Collectors.toList());
        }else if(resp.getCourseName()!=null && resp.getProfessionName()!=null){
            return courseList.stream()
                    .filter(data-> data.getProfessionName().equals(resp.getProfessionName()) && data.getCourseName().equals(resp.getCourseName()))
                    .collect(Collectors.toList());
        }

        return courseList;

    }


    /**
     * 根据ID删除课表
     * @param id
     * @return
     */
    public int deleteCourseById(String id){

        //根据id查找学期专业课程，如有有记录则不让删除，无记录才能删除
        QueryWrapper<SemesterProfessionalCourses> semesterProfessionalCoursesWrapper = new QueryWrapper<>();
        semesterProfessionalCoursesWrapper.eq("course_id",Long.valueOf(id));
        List<SemesterProfessionalCourses> semesterProfessionalCourses = semesterProfessionalCoursesMapper.selectList(semesterProfessionalCoursesWrapper);
        if(CollectionUtil.isNotEmpty(semesterProfessionalCourses)){
            return -1;
        }

        return courseMapper.deleteById(Long.valueOf(id));

    }
}
