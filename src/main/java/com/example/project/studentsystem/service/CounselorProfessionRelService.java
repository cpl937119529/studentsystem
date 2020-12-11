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
import java.util.stream.Collectors;

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
     * 判断是否划分过
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


    /**
     * 根据专业名或辅导员名称进行搜索
     * @param resp
     * @return
     */
    public List<CounselorProfessionRelResp> searchByCondition(CounselorProfessionRelResp resp){

        List<CounselorProfessionRelResp> respList = Lists.newArrayList();

        if(resp.getCounselorName()!=null && resp.getProfessionName()==null){
            //根据辅导员名称进行查询,先查询辅导员表
            QueryWrapper<Counselor> counselorWrapper = new QueryWrapper<>();
            counselorWrapper.eq("counselor_name",resp.getCounselorName());
            List<Counselor> counselors = counselorMapper.selectList(counselorWrapper);
            if(CollectionUtil.isNotEmpty(counselors)){
                counselors.forEach(counselor -> {
                    QueryWrapper<CounselorProfessionRel> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("counselor_id", counselor.getId());
                    List<CounselorProfessionRel> counselorProfessionRels = mapper.selectList(queryWrapper);
                    if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
                        counselorProfessionRels.forEach(counselorProfessionRel -> {
                            CounselorProfessionRelResp relResp = new CounselorProfessionRelResp();

                            relResp.setCounselorName(counselor.getCounselorName());
                            relResp.setCounselorId(counselor.getId().toString());
                            relResp.setStartYear(counselorProfessionRel.getStartYear());
                            relResp.setEndYear(counselorProfessionRel.getEndYear());
                            relResp.setId(counselorProfessionRel.getId().toString());
                            relResp.setProfessionId(counselorProfessionRel.getProfessionId().toString());

                            Profession profession = professionMapper.selectById(counselorProfessionRel.getProfessionId());
                            relResp.setProfessionName(profession.getProfessionName());
                            respList.add(relResp);
                        });
                    }
                });
            }
            return respList;
        }else if(resp.getCounselorName()==null && resp.getProfessionName()!=null){
            //根据专业名称进行查询，先查询专业表
            QueryWrapper<Profession> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("profession_name",resp.getProfessionName());
            List<Profession> professions = professionMapper.selectList(queryWrapper);
            if(CollectionUtil.isNotEmpty(professions)){
                professions.forEach(profession -> {
                    QueryWrapper<CounselorProfessionRel> counselorProfessionRelWrapper = new QueryWrapper<>();
                    counselorProfessionRelWrapper.eq("profession_id",profession.getId());
                    List<CounselorProfessionRel> counselorProfessionRels = mapper.selectList(counselorProfessionRelWrapper);
                    if(CollectionUtil.isNotEmpty(counselorProfessionRels)){
                        counselorProfessionRels.forEach(counselorProfessionRel -> {
                            CounselorProfessionRelResp relResp = new CounselorProfessionRelResp();

                            relResp.setId(counselorProfessionRel.getId().toString());
                            relResp.setEndYear(counselorProfessionRel.getEndYear());
                            relResp.setStartYear(counselorProfessionRel.getStartYear());
                            relResp.setProfessionId(profession.getId().toString());
                            relResp.setProfessionName(profession.getProfessionName());
                            relResp.setCounselorId(counselorProfessionRel.getCounselorId().toString());

                            Counselor counselor = counselorMapper.selectById(counselorProfessionRel.getCounselorId());
                            relResp.setCounselorName(counselor.getCounselorName());

                            respList.add(relResp);
                        });
                    }
                });
            }
            return respList;
        }else if(resp.getCounselorName()!=null && resp.getProfessionName()!=null){
            //根据辅导员名称和专业名进行查询，先获取所有辅导员与专业的关系，然后再获取辅导员、专业的名称，最后根据条件进行过滤

            List<CounselorProfessionRel> counselorProfessionRels = mapper.selectList(null);
            if(CollectionUtil.isNotEmpty(counselorProfessionRels)){

                counselorProfessionRels.forEach(counselorProfessionRel -> {
                    CounselorProfessionRelResp relResp = new CounselorProfessionRelResp();
                    relResp.setId(counselorProfessionRel.getId().toString());
                    relResp.setCounselorId(counselorProfessionRel.getCounselorId().toString());
                    relResp.setProfessionId(counselorProfessionRel.getProfessionId().toString());
                    relResp.setStartYear(counselorProfessionRel.getStartYear());
                    relResp.setEndYear(counselorProfessionRel.getEndYear());

                    Counselor counselor = counselorMapper.selectById(counselorProfessionRel.getCounselorId());
                    relResp.setCounselorName(counselor.getCounselorName());

                    Profession profession = professionMapper.selectById(counselorProfessionRel.getProfessionId());
                    relResp.setProfessionName(profession.getProfessionName());
                    respList.add(relResp);
                });

                return respList.stream()
                        .filter(data -> data.getProfessionName().equals(resp.getProfessionName()) && data.getCounselorName().equals(resp.getCounselorName()))
                        .collect(Collectors.toList());

            }

            return respList;
        }else {

            return this.getAll();
        }
    }




}
