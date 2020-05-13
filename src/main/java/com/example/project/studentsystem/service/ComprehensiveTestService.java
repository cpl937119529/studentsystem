package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.IComprehensiveTestServiceImpl;
import com.example.project.studentsystem.dto.ComprehensiveTestResp;
import com.example.project.studentsystem.entry.*;
import com.example.project.studentsystem.mapper.*;
import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
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
        if(resp.getProfessionName()==null){
            return this.calculateTheTotalScoreByCounselorId(counselors.get(0).getId(),resp.getYear(),resp.getSemester(),resp.getStudyYear())
                    .stream().sorted(Comparator.comparing(ComprehensiveTestResp::getOverallResult).reversed()).collect(Collectors.toList());
        }else {
            return this.calculateTheTotalScoreByCounselorId(counselors.get(0).getId(),resp.getYear(),resp.getSemester(),resp.getStudyYear())
                    .stream()
                    .filter(data->data.getProfessionName().equals(resp.getProfessionName()))
                    .sorted(Comparator.comparing(ComprehensiveTestResp::getOverallResult).reversed()).collect(Collectors.toList());
        }
    }


    /**
     * 计算该辅导员下的学生的某个学年某个学期的综测成绩
     * @param counselorId
     * @param year
     * @param semester
     * @return
     */
    private List<ComprehensiveTestResp> calculateTheTotalScoreByCounselorId(Long counselorId,int year,int semester,int studyYear){
        List<ComprehensiveTestResp> resultList = Lists.newArrayList();
        //获取该辅导员管理的专业信息
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("counselor_id",counselorId)
                                            .eq("start_year",year);
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            //该辅导员有管理专业,根据专业id和入学年份获取其管辖的学生信息
            counselorProfessionRels.forEach(counselorProfessionRel -> {
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("start_year",year)
                                    .eq("profession_id",counselorProfessionRel.getProfessionId());
                List<Student> students = studentMapper.selectList(studentQueryWrapper);
                if(CollectionUtil.isNotEmpty(students)){
                    //有管辖的学生
                    students.forEach(student -> {
                        ComprehensiveTestResp resp = new ComprehensiveTestResp();

                        //获取该学生在该学年该学期的加分记录，并计算总的加分分数
                        QueryWrapper<BonusRecord> bonusRecordQueryWrapper = new QueryWrapper<>();
                        bonusRecordQueryWrapper.eq("student_id",student.getId())
                                                .eq("year",studyYear)
                                                .eq("semester",semester);
                        List<BonusRecord> bonusRecords = bonusRecordMapper.selectList(bonusRecordQueryWrapper);
                        int addScores = bonusRecords.stream().mapToInt(BonusRecord::getScore).sum();

                        //获取该学生在该学年该学期的扣分记录，并计算总的扣分分数
                       QueryWrapper<DeductionRecord> deductionRecordQueryWrapper = new QueryWrapper<>();
                       deductionRecordQueryWrapper.eq("student_id",student.getId())
                                .eq("year",studyYear)
                                .eq("semester",semester);
                        List<DeductionRecord> deductionRecords = deductionRecordMapper.selectList(deductionRecordQueryWrapper);
                        int reduceScores = deductionRecords.stream().mapToInt(DeductionRecord::getScore).sum();

                        //获取该学生在该学年该学期的各科总成绩、总学分
                        QueryWrapper<StudentTranscript> studentTranscriptQueryWrapper = new QueryWrapper<>();
                        studentTranscriptQueryWrapper.eq("student_id",student.getId())
                                .eq("year",studyYear)
                                .eq("semester",semester);
                        List<StudentTranscript> studentTranscripts = studentTranscriptMapper.selectList(studentTranscriptQueryWrapper);
                        final double[] scores = {0.0};
                        //每一科的成绩*学分之和
                        studentTranscripts.forEach(data->{
                            double v = data.getScore() * data.getCredit();
                            scores[0] = scores[0] +v;
                        });
                        //总学分
                        double credit = studentTranscripts.stream().mapToDouble(StudentTranscript::getCredit).sum();

                        //计算综测成绩
                        double overallResult =credit!=0.0? (scores[0] / credit) + addScores - reduceScores:0.0;

                        resp.setStudentId(student.getId().toString());
                        resp.setOverallResult(Double.parseDouble(String.format("%.2f", overallResult)));
                        resp.setAverageScore(credit!=0.0? Double.parseDouble(String.format("%.2f", scores[0] / credit)) :0.0);
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



    public boolean sendTotalScore(ComprehensiveTestResp resp){
        boolean temp=false;
        List<ComprehensiveTestResp> resultList = this.getTotalScoreByCounselorUserId(resp);
        if(CollectionUtil.isNotEmpty(resultList)){
            for (int i=0;i<resultList.size();i++){
                //先判断该学生在当前学年当前学期是否已经有数据了，有数据则更新，无数据才进行添加
                QueryWrapper<ComprehensiveTest> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("student_id",Long.valueOf(resultList.get(i).getStudentId()))
                        .eq("year",resultList.get(i).getYear())
                        .eq("semester",resultList.get(i).getSemester());
                List<ComprehensiveTest> comprehensiveTests = comprehensiveTestMapper.selectList(queryWrapper);
                ComprehensiveTest comprehensiveTest = new ComprehensiveTest();
                comprehensiveTest.setStudentId(Long.valueOf(resultList.get(i).getStudentId()));
                comprehensiveTest.setOverallResult(resultList.get(i).getOverallResult());
                comprehensiveTest.setAverageScore(resultList.get(i).getAverageScore());
                comprehensiveTest.setAllAddScore(resultList.get(i).getAllAddScore());
                comprehensiveTest.setAllReduceScore(resultList.get(i).getAllReduceScore());
                comprehensiveTest.setYear(resultList.get(i).getYear());
                comprehensiveTest.setSemester(resultList.get(i).getSemester());
                comprehensiveTest.setRanking(i+1);

                if(CollectionUtil.isNotEmpty(comprehensiveTests)){
                    //有数据，更新
                    comprehensiveTest.setId(comprehensiveTests.get(0).getId());
                }
                temp= iComprehensiveTestService.saveOrUpdate(comprehensiveTest);

            }
        }
        return temp;
    }

    /**
     * 获取该辅导员管辖下的所有已发布的学生的综测成绩
     * @param userId
     * @return
     */
    public List<ComprehensiveTestResp> getAll(String userId){
        List<ComprehensiveTestResp> resultList = Lists.newArrayList();
        QueryWrapper<Counselor> counselorQueryWrapper = new QueryWrapper<>();
        counselorQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Counselor> counselors = counselorMapper.selectList(counselorQueryWrapper);

        //获取该辅导员管理的专业信息
        QueryWrapper<CounselorProfessionRel> counselorProfessionRelQueryWrapper = new QueryWrapper<>();
        counselorProfessionRelQueryWrapper.eq("counselor_id",counselors.get(0).getId());
        List<CounselorProfessionRel> counselorProfessionRels = counselorProfessionRelMapper.selectList(counselorProfessionRelQueryWrapper);
        if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
            //有管辖专业，根据专业id和入学年份查找其专业下的学生
            counselorProfessionRels.forEach(rel->{
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("profession_id",rel.getProfessionId())
                            .eq("start_year",rel.getStartYear());
                List<Student> students = studentMapper.selectList(studentQueryWrapper);

                if(CollectionUtil.isNotEmpty(students)){
                    //有学生信息,获取这些学生的综测成绩信息
                    students.forEach(student -> {
                        QueryWrapper<ComprehensiveTest> comprehensiveTestQueryWrapper = new QueryWrapper<>();
                        comprehensiveTestQueryWrapper.eq("student_id",student.getId());
                        List<ComprehensiveTest> comprehensiveTests = comprehensiveTestMapper.selectList(comprehensiveTestQueryWrapper);
                        if(CollectionUtil.isNotEmpty(comprehensiveTests)){
                            //有该学生的综测成绩
                            comprehensiveTests.forEach(comprehensiveTest -> {
                                ComprehensiveTestResp resp = new ComprehensiveTestResp();
                                BeanUtils.copyProperties(comprehensiveTest,resp);
                                resp.setStudyYear(comprehensiveTest.getYear());
                                resp.setId(comprehensiveTest.getId().toString());
                                resp.setStudentId(comprehensiveTest.getStudentId().toString());
                                resp.setProfessionId(rel.getProfessionId().toString());
                                resp.setStudentName(student.getName());
                                resp.setClassName(student.getClassName());
                                resp.setProfessionName(professionMapper.selectById(rel.getProfessionId()).getProfessionName());
                                resultList.add(resp);
                            });
                        }
                    });
                }
            });
        }
        return resultList;
    }


    /**
     * 根据条件查询
     * @param resp
     * @return
     */
    public List<ComprehensiveTestResp> findByCondition(ComprehensiveTestResp resp){
        List<ComprehensiveTestResp> resultList = this.getAll(resp.getUserId()).stream().filter(data->data.getStudyYear().equals(resp.getStudyYear())).collect(Collectors.toList());

        if(resp.getProfessionName()!=null && resp.getYear()==null && resp.getSemester()==null){
            return resultList.stream().filter(data->data.getProfessionName().equals(resp.getProfessionName())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()!=null && resp.getYear()!=null && resp.getSemester()==null){
            return resultList.stream().filter(data->data.getProfessionName().equals(resp.getProfessionName()) && data.getYear().equals(resp.getYear())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()!=null && resp.getYear()!=null && resp.getSemester()!=null){
            return resultList.stream().filter(data->data.getProfessionName().equals(resp.getProfessionName()) && data.getYear().equals(resp.getYear()) && data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()==null && resp.getYear()!=null && resp.getSemester()!=null){
            return resultList.stream().filter(data->data.getYear().equals(resp.getYear()) && data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()==null && resp.getYear()==null && resp.getSemester()!=null){
            return resultList.stream().filter(data-> data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()!=null && resp.getYear()==null && resp.getSemester()!=null){
            return resultList.stream().filter(data->data.getProfessionName().equals(resp.getProfessionName()) && data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        if(resp.getProfessionName()==null && resp.getYear()!=null && resp.getSemester()==null){
            return resultList.stream().filter(data->data.getYear().equals(resp.getYear())).collect(Collectors.toList());
        }

        return resultList;
    }


    /**
     * 获取与当前登录的学生的所有一同入学的综测成绩
     * @param userId
     * @return
     */
    public List<ComprehensiveTestResp> getAllByStudentUserId(String userId){
        List<ComprehensiveTestResp> resultList = Lists.newArrayList();

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("user_id",Long.valueOf(userId));
        List<Student> students = studentMapper.selectList(studentQueryWrapper);

        //获取与当前学期同一年入学同一个专业的学生
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("profession_id",students.get(0).getProfessionId())
                .eq("start_year",students.get(0).getStartYear());
        List<Student> students1 = studentMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(students1)){

            students1.forEach(student -> {
                QueryWrapper<ComprehensiveTest> comprehensiveTestQueryWrapper = new QueryWrapper<>();
                comprehensiveTestQueryWrapper.eq("student_id",student.getId());
                List<ComprehensiveTest> comprehensiveTests = comprehensiveTestMapper.selectList(comprehensiveTestQueryWrapper);
                if(CollectionUtil.isNotEmpty(comprehensiveTests)){
                    //有该学生的综测成绩
                    comprehensiveTests.forEach(comprehensiveTest -> {
                        ComprehensiveTestResp resp = new ComprehensiveTestResp();
                        BeanUtils.copyProperties(comprehensiveTest,resp);

                        resp.setId(comprehensiveTest.getId().toString());
                        resp.setStudentId(comprehensiveTest.getStudentId().toString());
                        resp.setProfessionId(student.getProfessionId().toString());
                        resp.setStudentName(student.getName());
                        resp.setClassName(student.getClassName());
                        resp.setProfessionName(professionMapper.selectById(student.getProfessionId()).getProfessionName());
                        resultList.add(resp);
                    });
                }
            });

        }

        return resultList;
    }


    /**
     * 获取与当前登录的学生的所有一同入学的综测成绩,根据条件查询
     * @param resp
     * @return
     */
    public List<ComprehensiveTestResp>  findByConditionWithStudent(ComprehensiveTestResp resp){
        List<ComprehensiveTestResp> resultList = this.getAllByStudentUserId(resp.getUserId());

        if(resp.getYear()!=null && resp.getSemester()==null){
            return resultList.stream().filter(data->data.getYear().equals(resp.getYear())).collect(Collectors.toList());
        }

        if(resp.getYear()==null && resp.getSemester()!=null){
            return resultList.stream().filter(data->data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        if(resp.getYear()!=null && resp.getSemester()!=null){
            return resultList.stream().filter(data-> data.getYear().equals(resp.getYear()) && data.getSemester().equals(resp.getSemester())).collect(Collectors.toList());
        }

        return resultList;
    }


}
