package com.example.project.studentsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IBonusRecordServiceImpl;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.entry.BonusRecord;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.mapper.BonusRecordMapper;
import com.example.project.studentsystem.mapper.CounselorMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusRecordService {

    @Autowired
    private IBonusRecordServiceImpl iBonusRecordService;

    @Autowired
    private BonusRecordMapper bonusRecordMapper;

    @Autowired
    private CounselorMapper counselorMapper;


    /**
     * 添加加分记录
     * @param resp
     * @return
     */
    public boolean addBonusRecord(BonusRecordResp resp){
        BonusRecord bonusRecord = new BonusRecord();
        BeanUtils.copyProperties(resp,bonusRecord);
        //根据counselorUserId获取counselorId
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(resp.getCounselorUserId()));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);

        bonusRecord.setStudentId(Long.valueOf(resp.getStudentId()));
        bonusRecord.setCounselorId(counselors.get(0).getId());

         return iBonusRecordService.saveOrUpdate(bonusRecord);
    }


}
