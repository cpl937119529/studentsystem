package com.example.project.studentsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IDeductionRecordServiceImpl;
import com.example.project.studentsystem.dto.DeductionRecordResp;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.DeductionRecord;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.DeductionRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeductionRecordService {

    @Autowired
    private DeductionRecordMapper deductionRecordMapper;

    @Autowired
    private IDeductionRecordServiceImpl iDeductionRecordService;

    @Autowired
    private CounselorMapper counselorMapper;


    /**
     * 添加扣分记录
     * @param resp
     * @return
     */
    public boolean addDeductionRecord(DeductionRecordResp resp){
        DeductionRecord deductionRecord = new DeductionRecord();
        BeanUtils.copyProperties(resp,deductionRecord);

        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(resp.getCounselorUserId()));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);
        deductionRecord.setStudentId(Long.valueOf(resp.getStudentId()));
        deductionRecord.setCounselorId(counselors.get(0).getId());
        return iDeductionRecordService.saveOrUpdate(deductionRecord);
    }

}
