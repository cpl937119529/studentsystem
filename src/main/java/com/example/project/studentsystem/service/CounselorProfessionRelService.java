package com.example.project.studentsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.ICounselorProfessionRelServiceImpl;
import com.example.project.studentsystem.dto.CounselorProfessionRelResp;
import com.example.project.studentsystem.entry.CounselorProfessionRel;
import com.example.project.studentsystem.mapper.CounselorProfessionRelMapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounselorProfessionRelService {

    @Autowired
    private CounselorProfessionRelMapper mapper;

    @Autowired
    private ICounselorProfessionRelServiceImpl service;


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



}
