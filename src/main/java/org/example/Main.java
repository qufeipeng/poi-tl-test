package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // 配置数据库连接池
        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
//        config.setUsername("root");
//        config.setPassword("123456");
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:yasdb://localhost:1706/yashandb");
        config.setUsername("agileboot");
        config.setPassword("yasdb_123");
        config.setDriverClassName("com.yashandb.jdbc.Driver");

        DataSource dataSource = new HikariDataSource(config);
        DatabaseService dbService = new DatabaseService(dataSource);
        //WordExportService exportService = new WordExportService();
        PocExportService exportService = new PocExportService();
        try {
//            // 查询参数
//            LocalDateTime startTime = LocalDateTime.now().minusDays(30);
//            LocalDateTime endTime = LocalDateTime.now();
//            String recordType = "all";
//
//            // 查询数据库
//            List<ReportRecord> records = dbService.queryRecords(startTime, endTime, recordType);
            List<PocRecord> records = dbService.queryPocRecords("1");
            if (records.isEmpty()) {
                System.out.println("没有找到符合条件的记录");
                return;
            }

            // 生成Word文档
            String outputPath = "Database_Report_" +
                    LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".docx";

            ClassLoader classLoader = Main.class.getClassLoader();
            URL resourceUrl = classLoader.getResource("poc_report_template.docx");
            assert resourceUrl != null;
            String filePath = resourceUrl.getPath();
            exportService.exportWithTemplate(records, filePath, outputPath);

            System.out.println("文档生成成功: " + outputPath);
            System.out.println("共导出 " + records.size() + " 条记录");

        } catch (SQLException e) {
            System.err.println("数据库查询错误: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("文件生成错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}