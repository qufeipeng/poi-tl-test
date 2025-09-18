package org.example;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocExportService {

    public void exportWithTemplate(List<PocRecord> records, String templatePath, String outputPath)
            throws IOException {
        Configure config = Configure.builder()
                .bind("customer", new BoldRenderPolicy()) // 注册加粗策略
                .bind("project", new BoldRenderPolicy()) // 注册加粗策略
                .bind("reportDate", new BoldRenderPolicy()) // 注册加粗策略
                .build();
        Map<String, Object> data = new HashMap<>();

        // 添加基本数据
        data.put("reportDate", java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 添加摘要
        data.put("summary", buildSummaryText(records));

        // 添加记录列表
        data.put("pocRecords", records);

        // 编译模板并渲染
        XWPFTemplate template = XWPFTemplate.compile(templatePath,config).render(data);

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