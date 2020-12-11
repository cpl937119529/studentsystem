package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IBonusRecordServiceImpl;
import com.example.project.studentsystem.dto.BonusRecordResp;
import com.example.project.studentsystem.entry.BonusRecord;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.Student;
import com.example.project.studentsystem.mapper.BonusRecordMapper;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.StudentMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class BonusRecordService {

    @Autowired
    private IBonusRecordServiceImpl iBonusRecordService;

    @Autowired
    private BonusRecordMapper bonusRecordMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private StudentMapper studentMapper;


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


    /**
     * 获取该辅导员下的加分记录
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
        QueryWrapper<BonusRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id",counselor.getId());
        List<BonusRecord> bonusRecords = bonusRecordMapper.selectList(queryWrapper);
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
     * 根据id删除加分记录
     * @param id
     * @return
     */
    public int deleteById(String id){
        return bonusRecordMapper.deleteById(Long.valueOf(id));
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    public List<BonusRecordResp> getListByCondition(BonusRecordResp resp){
        List<BonusRecordResp> resultList = Lists.newArrayList();
        QueryWrapper<BonusRecord> queryWrapper = new QueryWrapper<>();
        if(resp.getType()!=null){
            queryWrapper.eq("type",resp.getType());
        }
       if(resp.getYear()!=null){
           queryWrapper.eq("year",resp.getYear());
       }
       if(resp.getSemester()!=null){
           queryWrapper.eq("semester",resp.getSemester());
       }

        List<BonusRecord> bonusRecords = bonusRecordMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(bonusRecords)){
            Counselor counselor = counselorMapper.selectById(bonusRecords.get(0).getCounselorId());

            bonusRecords.forEach(bonusRecord -> {
                BonusRecordResp resp1 = new BonusRecordResp();
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
     * 获取该学生下的加分记录
     * @param studentUserId
     * @return
     */
    public List<BonusRecordResp> getListByStudentUserId(String studentUserId){
        List<BonusRecordResp> resultList = Lists.newArrayList();

        //根据studentUserId获取学生信息
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(studentUserId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);
        Student student = students.get(0);

        QueryWrapper<BonusRecord> bonusRecordQueryWrapper = new QueryWrapper<>();
        bonusRecordQueryWrapper.eq("student_id",student.getId());
        List<BonusRecord> bonusRecords = bonusRecordMapper.selectList(bonusRecordQueryWrapper);
        if(CollectionUtil.isNotEmpty(bonusRecords)){

            bonusRecords.forEach(bonusRecord -> {
                BonusRecordResp resp = new BonusRecordResp();
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
