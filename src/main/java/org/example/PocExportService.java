package org.example;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.Tables;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PocExportService {
    private static String STATUS_POC = "1";
    private static String STATUS_POC_FINISH = "3";
    private static String STATUS_IMP = "5";
    private static String STATUS_ONLINE = "6";
    private static String STATUS_ADAPT = "8";
    private static String STATUS_ADAPT_FINISH = "9";

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
        List<PocRecord> onlineRecords;
        List<PocRecord> adaptRecords;
        List<PocRecord> adaptFinishRecords;
        PocCount newPocCount;
        Long pocCount;
        Long impCount;
        Long finishCount;
        String newFinishDetail;
        Long onlineCount;
        Long adaptCount;
        Long adaptFinishCount;
        try {
            pocRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_POC);
            impRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_IMP);
            finishRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_POC_FINISH);
            onlineRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_ONLINE);
            adaptRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_ADAPT);
            adaptFinishRecords = dbService.queryPocRecords(beginDate, endDate,STATUS_ADAPT_FINISH);

            newPocCount = dbService.queryNewPocDetail(beginDate, endDate);
            pocCount = dbService.queryPocCount(STATUS_POC);
            impCount = dbService.queryPocCount(STATUS_IMP);
            finishCount = dbService.queryPocCount(STATUS_POC_FINISH);
            newFinishDetail = dbService.queryNewFinishDetail(beginDate, endDate);
            onlineCount = dbService.queryPocCount(STATUS_ONLINE);
            adaptCount = dbService.queryPocCount(STATUS_ADAPT);
            adaptFinishCount = dbService.queryPocCount(STATUS_ADAPT_FINISH);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        List<PocRecord> allRecord = Stream.of(pocRecords, impRecords, finishRecords)
//                .flatMap(List::stream)
//                .collect(Collectors.toList());

        // 添加摘要
        data.put("newPocCount", newPocCount.getCount());
        data.put("newPocDetail", newPocCount.getDetail());

        data.put("pocCount", pocCount);

        data.put("impCount", impCount);
        data.put("impDetail", impCount);

        data.put("finishCount", finishCount);
        data.put("newFinishDetail", newFinishDetail);

        data.put("onlineCount", onlineCount);
        data.put("adaptCount", adaptCount);
        data.put("adaptFinishCount", adaptFinishCount);

        // 输出段落
        data.put("pocRecords", pocRecords);
        data.put("impRecords", impRecords);
        data.put("finishRecords", finishRecords);

        // 输出表格
        data.put("onlineTable", onlineRecords);
        data.put("adaptTable", adaptRecords);
        data.put("adaptFinishTable", adaptFinishRecords);

        // 编译模板并渲染
        //XWPFTemplate template = XWPFTemplate.compile(templatePath,config).render(data);
        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(data);

        // 保存文件
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            template.write(out);
        }
        template.close();
    }

}