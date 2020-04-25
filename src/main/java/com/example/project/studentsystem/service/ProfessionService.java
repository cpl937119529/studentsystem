package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.IProfessionService;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.dto.ProfessionResp;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.entry.Profession;
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


    /**
     * 添加专业
     * @param profession
     * @return
     */
    public boolean addProfession(Profession profession){
         return professionService.saveOrUpdate(profession);
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
