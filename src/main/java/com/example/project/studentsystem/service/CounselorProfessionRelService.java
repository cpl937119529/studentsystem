package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.ICounselorProfessionRelServiceImpl;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.entry.Profession;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.CounselorProfessionRelMapper;
import com.example.project.studentsystem.mapper.ProfessionMapper;
import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounselorProfessionRelService {

    @Autowired
    private CounselorProfessionRelMapper mapper;

    @Autowired
    private ICounselorProfessionRelServiceImpl service;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private CounselorMapper counselorMapper;


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    public List<CounselorProfessionRel> findByCondition(CounselorProfessionRelResp resp){
        QueryWrapper<CounselorProfessionRel> wrapper = new QueryWrapper<>();
        wrapper.eq("profession_id",Long.valueOf(resp.getProfessionId()));
        wrapper.eq("start_year",resp.getStartYear());
        wrapper.eq("end_year",resp.getEndYear());
        return mapper.selectList(wrapper);
    }

    /**
     * save
     * @param resp
     * @return
     */
    public boolean save(CounselorProfessionRelResp resp){
        CounselorProfessionRel rel = new CounselorProfessionRel();
        rel.setCounselorId(Long.valueOf(resp.getCounselorId()));
        rel.setProfessionId(Long.valueOf(resp.getProfessionId()));
        rel.setEndYear(resp.getEndYear());
        rel.setStartYear(resp.getStartYear());
        return service.save(rel);
    }

    /**
     * 获取所有信息
     * @return
     */
    public List<CounselorProfessionRelResp> getAll(){
        List<CounselorProfessionRel> counselorProfessionRels = mapper.selectList(null);
        List<CounselorProfessionRelResp> resultList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            counselorProfessionRels.forEach(info->{
                CounselorProfessionRelResp relResp = new CounselorProfessionRelResp();
                BeanUtils.copyProperties(info,relResp);
                relResp.setId(info.getId().toString());
                relResp.setCounselorId(info.getCounselorId().toString());
                relResp.setProfessionId(info.getProfessionId().toString());
                //获取专业名称
                Profession profession = professionMapper.selectById(info.getProfessionId());
                relResp.setProfessionName(profession.getProfessionName());
                //获取辅导员名称
                Counselor counselor = counselorMapper.selectById(info.getCounselorId());
                relResp.setCounselorName(counselor.getCounselorName());
                resultList.add(relResp);
            });

        }
        return  resultList;
    }



}
