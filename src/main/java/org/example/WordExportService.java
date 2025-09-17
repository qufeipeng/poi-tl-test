package org.example;

import com.deepoove.poi.XWPFTemplate;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordExportService {

    public void exportWithTemplate(List<ReportRecord> records, String templatePath, String outputPath)
            throws IOException {

        Map<String, Object> data = new HashMap<>();

        // 添加基本数据
        data.put("title", "数据库记录报告");
        data.put("generateTime", java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("totalCount", records.size());

        // 添加摘要
        data.put("summary", buildSummaryText(records));

        // 为每个记录添加模板需要的数据
        for (int i = 0; i < records.size(); i++) {
            ReportRecord record = records.get(i);
            // 确保内容中的换行符在Word中正确显示
            String formattedContent = record.getContent().replace("\n", "\n    ");
            record.setContent(formattedContent);

        }

        // 添加记录列表
        data.put("records", records);

        // 编译模板并渲染
        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(data);

        // 保存文件
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            template.write(out);
        }
        template.close();
    }

    private String buildSummaryText(List<ReportRecord> records) {
        if (records.isEmpty()) {
            return "暂无记录数据";
        }

        // 统计各类型数量
        Map<String, Integer> typeCount = new HashMap<>();
        for (ReportRecord record : records) {
            String type = record.getTypeChinese();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }

        StringBuilder summary = new StringBuilder();
        summary.append("报告摘要：\n");
        summary.append("• 本次共查询到 ").append(records.size()).append(" 条记录\n");

        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            double percentage = (double) entry.getValue() / records.size() * 100;
            summary.append("• ").append(entry.getKey())
                    .append(": ").append(entry.getValue())
                    .append(" 条 (").append(String.format("%.1f", percentage))
                    .append("%)\n");
        }

        return summary.toString();
    }
}