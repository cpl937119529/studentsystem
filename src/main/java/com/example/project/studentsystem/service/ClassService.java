package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.dto.ClassReq;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.mapper.ClassMapper;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.CounselorProfessionRelMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    /**
     * 根据辅导员Id查询其管辖专业下的班级
     * @param userId
     * @return
     */
    public List<ClassReq> getClassByUserId(String userId){
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);


        List<ClassReq> resultList = Lists.newArrayList();
        QueryWrapper<CounselorProfessionRel> wrapper = new QueryWrapper<>();
        wrapper.eq("counselor_id",counselors.get(0).getId());

        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){

            counselorProfessionRels.forEach(info->{
                QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                classQueryWrapper.eq("profession_id",info.getProfessionId());
                List<Class> classes = classMapper.selectList(classQueryWrapper);
                if(CollectionUtil.isNotEmpty(classes)){
                    classes.forEach(data->{
                        ClassReq req = new ClassReq();
                        BeanUtils.copyProperties(data,req);
                        req.setId(data.getId().toString());
                        req.setProfessionId(data.getProfessionId().toString());
                        resultList.add(req);
                    });
                }
            });
        }
        return resultList;
    }

}
