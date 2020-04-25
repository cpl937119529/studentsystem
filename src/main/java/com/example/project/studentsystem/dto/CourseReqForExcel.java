package com.example.project.studentsystem.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class CourseReqForExcel extends BaseRowModel {

    @ExcelProperty(value="ID",index=0)
    private String id;

    @ExcelProperty(value="courseName",index=1)
    private String courseName;

    @ExcelProperty(value="professionId",index=3)
    private String professionId;

    @ExcelProperty(value="credit",index=2)
    private String credit;
}
