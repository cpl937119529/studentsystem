package com.example.project.studentsystem.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class StudentTranscriptReqForExcel extends BaseRowModel {

    @ExcelProperty(value="studentId",index=0)
    private String studentId;

    @ExcelProperty(value="courseId",index=1)
    private String courseId;

    @ExcelProperty(value="score",index=2)
    private String score;

    @ExcelProperty(value="year",index=3)
    private String year;

    @ExcelProperty(value="semester",index=4)
    private String semester;

}
