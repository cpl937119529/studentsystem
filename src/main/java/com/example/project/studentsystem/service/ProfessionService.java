package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.IProfessionService;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.dto.ProfessionResp;
import com.example.project.studentsystem.entry.Class;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.mapper.ClassMapper;
import com.example.project.studentsystem.mapper.ProfessionMapper;
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


    /**
     * 添加专业
     * @param profession
     * @return
     */
    public int addProfession(Profession profession){

        //判断有无该专业，五则添加
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
      return professionMapper.deleteById(Long.valueOf(id));
    }


}
