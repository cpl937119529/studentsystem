package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.project.studentsystem.IService.impl.ICounselorProfessionRelServiceImpl;
import com.example.project.studentsystem.dto.CounselorResp;
import com.example.project.studentsystem.dto.ProfessionResp;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounselorService {

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ICounselorProfessionRelServiceImpl counselorService;

    /**
     * 获取所有辅导员信息
     * @return
     */
    public List<CounselorResp> getAll(){
        List<CounselorResp> resultList = Lists.newArrayList();

        List<Counselor> counselors = counselorMapper.selectList(null);
        if(CollectionUtil.isNotEmpty(counselors)){
            counselors.forEach(counselor -> {
                CounselorResp resp =new CounselorResp();
                BeanUtils.copyProperties(counselor,resp);
                resp.setId(counselor.getId().toString());
                resp.setUserId(counselor.getUserId().toString());
                resultList.add(resp);
            });
        }

        return resultList;

    }



}
