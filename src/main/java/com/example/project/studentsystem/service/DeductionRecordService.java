package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IDeductionRecordServiceImpl;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.dto.DeductionRecordResp;
import com.example.project.studentsystem.entry.BonusRecord;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.DeductionRecord;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.DeductionRecordMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.google.common.collect.Lists;
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

    @Autowired
    private StudentMapper studentMapper;


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



    /**
     * 获取该辅导员下的扣分记录
     * @param userId
     * @return
     */
    public List<BonusRecordResp> getByUserId(String userId){
        List<BonusRecordResp> resultList = Lists.newArrayList();
        //根据userId获取辅导员信息
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);

        Counselor counselor = counselors.get(0);

        //根据counselorId获取加分信息
        QueryWrapper<DeductionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id",counselor.getId());
        List<DeductionRecord> bonusRecords = deductionRecordMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(bonusRecords)){
            bonusRecords.forEach(bonusRecord -> {
                BonusRecordResp resp = new BonusRecordResp();
                BeanUtils.copyProperties(bonusRecord,resp);

                resp.setCounselorId(counselor.getId().toString());
                resp.setId(bonusRecord.getId().toString());
                resp.setStudentId(bonusRecord.getStudentId().toString());

                Student student = studentMapper.selectById(bonusRecord.getStudentId());
                resp.setStudentName(student.getName());
                resp.setCounselorName(counselor.getCounselorName());
                resp.setCounselorUserId(counselor.getUserId().toString());

                resultList.add(resp);
            });

        }


        return resultList;
    }

    /**
     * 根据id删除扣分记录
     * @param id
     * @return
     */
    public int deleteById(String id){
        return deductionRecordMapper.deleteById(Long.valueOf(id));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    public List<DeductionRecordResp> getListByCondition(DeductionRecordResp resp){
        List<DeductionRecordResp> resultList = Lists.newArrayList();
        QueryWrapper<DeductionRecord> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(resp.getSemester()!=null,"semester",resp.getSemester())
                    .eq(resp.getType()!=null,"type",resp.getType())
                    .eq(resp.getYear()!=null,"year",resp.getYear());

        List<DeductionRecord> bonusRecords = deductionRecordMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(bonusRecords)){
            Counselor counselor = counselorMapper.selectById(bonusRecords.get(0).getCounselorId());

            bonusRecords.forEach(bonusRecord -> {
                DeductionRecordResp resp1 = new DeductionRecordResp();
                BeanUtils.copyProperties(bonusRecord,resp1);

                resp1.setCounselorId(counselor.getId().toString());
                resp1.setId(bonusRecord.getId().toString());
                resp1.setStudentId(bonusRecord.getStudentId().toString());

                Student student = studentMapper.selectById(bonusRecord.getStudentId());
                resp1.setStudentName(student.getName());
                resp1.setCounselorName(counselor.getCounselorName());
                resp1.setCounselorUserId(counselor.getUserId().toString());

                resultList.add(resp1);
            });
        }

        return resultList;
    }



    /**
     * 获取该学生下的扣分记录
     * @param studentUserId
     * @return
     */
    public List<DeductionRecordResp> getListByStudentUserId(String studentUserId){
        List<DeductionRecordResp> resultList = Lists.newArrayList();

        //根据studentUserId获取学生信息
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(studentUserId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        Student student = students.get(0);

        QueryWrapper<DeductionRecord> bonusRecordQueryWrapper = new QueryWrapper<>();
        bonusRecordQueryWrapper.eq("student_id",student.getId());
        List<DeductionRecord> bonusRecords = deductionRecordMapper.selectList(bonusRecordQueryWrapper);
        if(CollectionUtil.isNotEmpty(bonusRecords)){

            bonusRecords.forEach(bonusRecord -> {
                DeductionRecordResp resp = new DeductionRecordResp();
                BeanUtils.copyProperties(bonusRecord,resp);
                resp.setId(bonusRecord.getId().toString());
                resp.setStudentId(student.getId().toString());
                resp.setStudentName(student.getName());
                resp.setCounselorId(bonusRecord.getCounselorId().toString());
                Counselor counselor = counselorMapper.selectById(bonusRecord.getCounselorId());
                resp.setCounselorName(counselor.getCounselorName());
                resultList.add(resp);
            });

        }

        return  resultList;
    }


}
