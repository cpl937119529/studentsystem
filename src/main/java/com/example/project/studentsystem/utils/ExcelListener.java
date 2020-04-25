package com.example.project.studentsystem.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelListener extends AnalysisEventListener {

    private List<Object> datas = new ArrayList<>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        datas.add(o);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //   datas.clear();//解析结束销毁不用的资源
    }


}
