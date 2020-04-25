package com.example.project.studentsystem.controller;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.project.studentsystem.dto.CourseReqForExcel;
import com.example.project.studentsystem.utils.ExcelListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ImportByExcelController {

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam(value = "file", required=true) MultipartFile file){
        //此处千万不要用InputStream，InputStream会导致无法解析出文件类型
        BufferedInputStream buffer = null;
        try{
            buffer = new BufferedInputStream(file.getInputStream());
            ExcelListener listener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(buffer, ExcelTypeEnum.XLS,listener);
            excelReader.read(new Sheet(1,0, CourseReqForExcel.class));
            List<Object> list = listener.getDatas();
            //excel中第一行为栏目，必然是存在一行的
            if(CollectionUtils.isEmpty(list) || list.size() < 2){
                return "导入数据为空！";
            }

            List<CourseReqForExcel> catagoryList = new ArrayList<CourseReqForExcel>();

            //进行业务处理，list为excel中解析出的数据
            for (int i = 0; i < list.size(); i++) {
                CourseReqForExcel catagory = new CourseReqForExcel();
                catagory = (CourseReqForExcel) list.get(i);
                catagoryList.add(catagory);
            }

            catagoryList.forEach(info->{
                System.out.println(info.getCourseName());
            });

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(buffer != null){
                try{
                    buffer.close();
                } catch (IOException e){
                   e.printStackTrace();
                }
            }
        }
        return "ok";
    }


    @GetMapping("/test")
    public String test(){
        System.out.println("sss");
        return "test";
    }

}
