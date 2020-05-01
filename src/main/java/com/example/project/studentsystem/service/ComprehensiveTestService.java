package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IComprehensiveTestServiceImpl;
import com.example.project.studentsystem.dto.ComprehensiveTestResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComprehensiveTestService {

    @Autowired
    private ComprehensiveTestMapper comprehensiveTestMapper;

    @Autowired
    private IComprehensiveTestServiceImpl iComprehensiveTestService;

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private ProfessionMapper professionMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentTranscriptMapper studentTranscriptMapper;

    @Autowired
    private BonusRecordMapper bonusRecordMapper;

    @Autowired
    private DeductionRecordMapper deductionRecordMapper;

    @Autowired
    private CounselorProfessionRelMapper counselorProfessionRelMapper;


    /**
     * 根据辅导员的userId计算某学年某学期其管理学生的综测成绩
     * @param resp
     * @return
     */
    public List<ComprehensiveTestResp> getTotalScoreByCounselorUserId(ComprehensiveTestResp resp){
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(resp.getUserId()));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);
        return this.calculateTheTotalScoreByCounselorId(counselors.get(0).getId(),resp.getYear(),resp.getSemester())
                .stream().sorted(Comparator.comparing(ComprehensiveTestResp::getOverallResult).reversed()).collect(Collectors.toList());
    }


    /**
     * 计算该辅导员下的学生的某个学年某个学期的综测成绩
     * @param counselorId
     * @param year
     * @param semester
     * @return
     */
    private List<ComprehensiveTestResp> calculateTheTotalScoreByCounselorId(Long counselorId,int year,int semester){
        List<ComprehensiveTestResp> resultList = Lists.newArrayList();
        //获取该辅导员管理的专业信息
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("counselor_id",counselorId);
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            //该辅导员有管理专业,根据专业id和入学年份获取其管辖的学生信息
            counselorProfessionRels.forEach(counselorProfessionRel -> {
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("start_year",counselorProfessionRel.getStartYear())
                                    .eq("profession_id",counselorProfessionRel.getProfessionId());
                List<Student> students = studentMapper.selectList(studentQueryWrapper);
                if(CollectionUtil.isNotEmpty(students)){
                    //有管辖的学生
                    students.forEach(student -> {
                        ComprehensiveTestResp resp = new ComprehensiveTestResp();

                        //获取该学生在该学年该学期的加分记录，并计算总的加分分数
                        QueryWrapper<BonusRecord> bonusRecordQueryWrapper = new QueryWrapper<>();
                        bonusRecordQueryWrapper.eq("student_id",student.getId())
                                                .eq("year",year)
                                                .eq("semester",semester);
                        List<BonusRecord> bonusRecords = bonusRecordMapper.selectList(bonusRecordQueryWrapper);
                        int addScores = bonusRecords.stream().mapToInt(BonusRecord::getScore).sum();

                        //获取该学生在该学年该学期的扣分记录，并计算总的扣分分数
                       QueryWrapper<DeductionRecord> deductionRecordQueryWrapper = new QueryWrapper<>();
                       deductionRecordQueryWrapper.eq("student_id",student.getId())
                                .eq("year",year)
                                .eq("semester",semester);
                        List<DeductionRecord> deductionRecords = deductionRecordMapper.selectList(deductionRecordQueryWrapper);
                        int reduceScores = deductionRecords.stream().mapToInt(DeductionRecord::getScore).sum();

                        //获取该学生在该学年该学期的各科总成绩、总学分
                        QueryWrapper<StudentTranscript> studentTranscriptQueryWrapper = new QueryWrapper<>();
                        studentTranscriptQueryWrapper.eq("student_id",student.getId())
                                .eq("year",year)
                                .eq("semester",semester);
                        List<StudentTranscript> studentTranscripts = studentTranscriptMapper.selectList(studentTranscriptQueryWrapper);
                        int scores = studentTranscripts.stream().mapToInt(StudentTranscript::getScore).sum();
                        double credit = studentTranscripts.stream().mapToDouble(StudentTranscript::getCredit).sum();

                        //计算综测成绩
                        double overallResult = (scores / credit) + addScores - reduceScores;

                        resp.setStudentId(student.getId().toString());
                        resp.setOverallResult(overallResult);
                        resp.setAverageScore(scores / credit);
                        resp.setAllReduceScore((double) reduceScores);
                        resp.setAllAddScore((double) addScores);
                        resp.setYear(year);
                        resp.setSemester(semester);
                        resp.setStudentName(student.getName());
                        resp.setProfessionId(student.getProfessionId().toString());
                        resp.setProfessionName(professionMapper.selectById(student.getProfessionId()).getProfessionName());
                        resp.setClassName(student.getClassName());
                        resultList.add(resp);
                    });

                }

            });

        }

        return resultList;
    }


}
