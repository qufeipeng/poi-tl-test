package org.example;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PocExportService {

    public void exportWithTemplate(DatabaseService dbService, String templatePath, String outputPath, String beginDate, String endDate)
            throws IOException {
//        Configure config = Configure.builder()
//                .bind("customer", new BoldRenderPolicy()) // 注册加粗策略
//                .bind("project", new BoldRenderPolicy()) // 注册加粗策略
//                .bind("reportDate", new BoldRenderPolicy()) // 注册加粗策略
//                .build();
        Map<String, Object> data = new HashMap<>();

        // 添加基本数据
        data.put("reportDate", java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        List<PocRecord> pocRecords;
        List<PocRecord> impRecords;
        List<PocRecord> finishRecords;
        try {
            pocRecords = dbService.queryPocRecords(beginDate, endDate,"1");
            impRecords = dbService.queryPocRecords(beginDate, endDate,"5");
            finishRecords = dbService.queryPocRecords(beginDate, endDate,"3");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<PocRecord> allRecord = Stream.of(pocRecords, impRecords, finishRecords)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 添加摘要
        data.put("summary", buildSummaryText(allRecord));

        // 添加记录列表
        data.put("pocRecords", pocRecords);
        data.put("impRecords", impRecords);
        data.put("finishRecords", finishRecords);

        // 编译模板并渲染
        //XWPFTemplate template = XWPFTemplate.compile(templatePath,config).render(data);
        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(data);

        // 保存文件
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            template.write(out);
        }
        template.close();
    }

    private String buildSummaryText(List<PocRecord> records) {
        if (records.isEmpty()) {
            return "暂无记录数据";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("报告摘要：\n");
        summary.append("• 本次共查询到 ").append(records.size()).append(" 条记录\n");

        return summary.toString();
    }
}