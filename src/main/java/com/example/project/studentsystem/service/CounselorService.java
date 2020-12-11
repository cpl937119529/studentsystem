package com.example.project.studentsystem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.studentsystem.IService.impl.ICounselorProfessionRelServiceImpl;
import com.example.project.studentsystem.dto.CounselorResp;
import com.example.project.studentsystem.dto.ProfessionResp;
import com.example.project.studentsystem.entry.Counselor;
import com.example.project.studentsystem.entry.User;
import com.example.project.studentsystem.mapper.CounselorMapper;
import com.example.project.studentsystem.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

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


    /**
     * 创建辅导员账号
     * @param resp
     * @return
     */
    public int addCounselor(CounselorResp resp){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",resp.getUserName());
        List<User> users = userMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(users)){
            return -1;
        }
        User user = new User();
        user.setUserType(2);
        user.setUserName(resp.getUserName());
        user.setPassWord("123456");
        userMapper.insert(user);

        List<User> userList = userMapper.selectList(wrapper);

        Long userId = userList.get(0).getId();

        Counselor counselor = new Counselor();
        counselor.setUserId(userId);
        counselor.setCounselorName(resp.getCounselorName());
        counselor.setDepartment(resp.getDepartment());
        counselor.setSex(resp.getSex());
        counselorMapper.insert(counselor);
        return 1;
    }



}
