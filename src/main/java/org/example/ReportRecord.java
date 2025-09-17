package org.example;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportRecord {
    private Long id;
    private String title;
    private String content;
    private String recordType;
    private String author;
    private LocalDateTime createTime;
    private String formattedTime;
    private String typeChinese;


    // 构造方法
    public ReportRecord() {}

    public ReportRecord(Long id, String title, String content, String recordType,
                        String author, LocalDateTime createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.recordType = recordType;
        this.author = author;
        this.createTime = createTime;
    }

    public ReportRecord(ReportRecord reportRecord) {
    }

    public String getFormattedTime() {
        return createTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getTypeChinese() {
        switch (recordType) {
            case "sales": return "销售";
            case "support": return "支持";
            case "feedback": return "反馈";
            default: return recordType;
        }
    }
}
