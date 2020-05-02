package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IResumeServiceImpl;
import com.example.project.studentsystem.dto.PositionResp;
import com.example.project.studentsystem.dto.ResumeResp;
import com.example.project.studentsystem.entry.Position;
import com.example.project.studentsystem.entry.Resume;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.PositionMapper;
import com.example.project.studentsystem.mapper.ResumeMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.example.project.studentsystem.utils.JaccardUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private IResumeServiceImpl iResumeService;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PositionMapper positionMapper;


    /**
     * 更新或保存
     * @param resp
     * @return
     */
    public boolean saveOrUpdate(ResumeResp resp){

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",resp.getUserId());
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        Resume resume = new Resume();
        BeanUtils.copyProperties(resp,resume);
        resume.setStudentId(students.get(0).getId());

        //判断有无该学生的简历信息，有则更新，无则保存
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("student_id",students.get(0).getId());
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);
        if(CollectionUtil.isNotEmpty(resumes)){
            resume.setId(resumes.get(0).getId());
        }
         return iResumeService.saveOrUpdate(resume);
    }


    /**
     * 获取该学生的简历信息
     * @param userId
     * @return
     */
    public ResumeResp getInfo(String userId){
        ResumeResp resp = new ResumeResp();

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("student_id",students.get(0).getId());
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);

        if(CollectionUtil.isNotEmpty(resumes)){
            Resume resume = resumes.get(0);
            BeanUtils.copyProperties(resume,resp);
            resp.setId(resume.getId().toString());
            resp.setStudentId(resume.getStudentId().toString());
        }
        return resp;
    }


    /**
     * 获取推荐职位
     * @param userId
     * @return
     */
    public List<PositionResp> getPosition(String userId){
        List<PositionResp> resultList = Lists.newArrayList();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        //获取该学生的简历信息
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<>();
        resumeQueryWrapper.eq("student_id",students.get(0).getId());
        List<Resume> resumes = resumeMapper.selectList(resumeQueryWrapper);

        if(CollectionUtil.isNotEmpty(resumes)){
            //有该学生的简历信息,将简历信息拼接成一个字符串，用来计算与各个职位的相似度
            //先后顺序为：职位名称、学历、行业类型、职位类型、工作城市、薪资
            String remueStr =resumes.get(0).getPositionName()
                    +resumes.get(0).getEducation()
                    +resumes.get(0).getIndustryType()
                    +resumes.get(0).getPositionType()
                    +resumes.get(0).getWorkCity()
                    +resumes.get(0).getLimitSalary()+"-"
                    +resumes.get(0).getMaxSalary();

            Map<Long,Double> simMap = new HashMap<>();

            //获取所有的职位信息
            List<Position> positions = positionMapper.selectList(null);
            if(CollectionUtil.isNotEmpty(positions)){
                //有职位信息，拼接职位信息的字符串（与简历信息的顺序一致）
                positions.forEach(position -> {
                    String positionStr=position.getPositionName()
                            +position.getEducation()
                            +position.getIndustryType()
                            +position.getPositionType()
                            +position.getWorkCity()
                            +position.getLimitSalary()+"-"
                            +position.getMaxSalary();

                    //计算相似度
                    float simaller = JaccardUtil.doJaccard(remueStr, positionStr);
                    simMap.put(position.getId(), (double) simaller);
                });

                if(simMap.size()>0){
                    List<Map.Entry<Long,Double>>list = simMap.entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry::getValue))
                            .collect(Collectors.toList());

                   for(int i=list.size()-1;i>list.size()-20;i--){
                       PositionResp resp = new PositionResp();
                       Position position = positionMapper.selectById(list.get(i).getKey());
                       BeanUtils.copyProperties(position,resp);
                       resp.setId(position.getId().toString());
                       resultList.add(resp);

                   }
                }
            }
        }
        return resultList;
    }


}
