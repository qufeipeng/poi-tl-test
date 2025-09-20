package org.example;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ReportRecord> queryRecords(LocalDateTime startTime,
                                           LocalDateTime endTime,
                                           String recordType) throws SQLException {
        List<ReportRecord> records = new ArrayList<>();
        String sql = "SELECT id, title, content, record_type, author, create_time " +
                "FROM report_records WHERE create_time BETWEEN ? AND ?";

        if (recordType != null && !recordType.equals("all")) {
            sql += " AND record_type = ?";
        }

        sql += " ORDER BY create_time DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, startTime);
            pstmt.setObject(2, endTime);

            if (recordType != null && !recordType.equals("all")) {
                pstmt.setString(3, recordType);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReportRecord record = new ReportRecord(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("record_type"),
                            rs.getString("author"),
                            rs.getObject("create_time", LocalDateTime.class)
                    );
                    records.add(record);
                }
            }
        }
        return records;
    }

    public List<PocRecord> queryPocRecords(String beginDate, String endDate, String status) throws SQLException {
        List<PocRecord> records = new ArrayList<>();
        String sql = "SELECT c.NICKNAME AS OWNER,a.LABEL AS STATUS,CUSTOMER,PROJECT,b.LABEL AS RISK,TODO_RISK,PLAN,EVALUATE, " +
                "SALES,d.NICKNAME AS SA,e.NICKNAME AS POC,PROGRESS,f.WORK_CONTENT,POC_START_DT,POC_END_DT,ONLINE_DT " +
                "FROM T_POC u " +
                "LEFT JOIN T_DICT a ON u.STATUS = a.VALUE AND a.DICT_TYPE = 'status' " +
                "LEFT JOIN T_DICT b ON u.RISK = b.VALUE AND b.DICT_TYPE = 'risk' " +
                "LEFT JOIN SYS_USER c ON u.owner = c.USER_ID " +
                "LEFT JOIN SYS_USER d ON u.SA = d.USER_ID " +
                "LEFT JOIN SYS_USER e ON u.POC = e.USER_ID " +
                "LEFT JOIN T_WORK_TIME f ON u.POC_ID  = f.POC_ID AND u.OWNER = f.USER_ID " +
                "AND f.BEGIN_DATE = ? AND f.END_DATE = ? " +
                "WHERE u.DELETED = 0 AND u.STATUS = ? " +
                "ORDER BY u.CREATE_TIME DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, beginDate);
            pstmt.setObject(2, endDate);
            pstmt.setObject(3, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PocRecord record = new PocRecord(
                            rs.getString("OWNER"),
                            rs.getString("STATUS"),
                            rs.getString("CUSTOMER"),
                            rs.getString("PROJECT"),
                            rs.getString("RISK"),
                            rs.getString("TODO_RISK"),
                            rs.getString("PLAN"),
                            rs.getString("EVALUATE"),
                            rs.getString("SALES"),
                            rs.getString("SA"),
                            rs.getString("POC"),
                            rs.getInt("PROGRESS"),
                            rs.getString("WORK_CONTENT"),
                            rs.getDate("POC_START_DT"),
                            rs.getDate("POC_END_DT"),
                            rs.getDate("ONLINE_DT")
                    );
                    records.add(record);
                }
            }
        }
        return records;
    }

    public PocCount queryNewPocDetail(String beginDate, String endDate) throws SQLException {
        PocCount record = null;
        String sql = "SELECT COUNT(POC_ID) AS NEW_POC_COUNT,LISTAGG(u.CUSTOMER || '（' || u.PROJECT || '）', '、')  AS NEW_POC_DETAIL " +
                "FROM T_POC u " +
                "WHERE TO_CHAR(u.CREATE_TIME,'yyyy-mm-dd') >= ? AND TO_CHAR(u.CREATE_TIME,'yyyy-mm-dd') <= ? " +
                "AND u.DELETED = 0 AND u.STATUS IN (0, 1, 2)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, beginDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    record = new PocCount(
                            rs.getLong("NEW_POC_COUNT"),
                            rs.getString("NEW_POC_DETAIL")
                    );
                }
            }
        }
        return record;
    }

    public String queryNewFinishDetail(String beginDate, String endDate) throws SQLException {
        String record = null;
        String sql = "SELECT LISTAGG(u.CUSTOMER || '（' || u.PROJECT || '）', '、')  AS POC_DETAIL " +
                "FROM T_POC u " +
                "WHERE u.POC_END_DT >= ? AND u.POC_END_DT <= ? " +
                "AND u.DELETED = 0 AND u.STATUS = 3";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, beginDate);
            pstmt.setObject(2, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    record = rs.getString("POC_DETAIL");
                }
            }
        }
        return record;
    }

    public String queryNewImpDetail(String beginDate, String endDate) throws SQLException {
        String record = null;
        String sql = "SELECT LISTAGG(u.CUSTOMER || '（' || u.PROJECT || '）', '、')  AS POC_DETAIL " +
                "FROM T_POC u " +
                "WHERE ((u.POC_END_DT >= ? AND u.POC_END_DT <= ?) " +
                "OR (TO_CHAR(u.CREATE_TIME,'yyyy-mm-dd') >= ? AND TO_CHAR(u.CREATE_TIME,'yyyy-mm-dd') <= ?)) " +
                "AND u.DELETED = 0 AND u.STATUS = 5";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, beginDate);
            pstmt.setObject(2, endDate);
            pstmt.setObject(3, beginDate);
            pstmt.setObject(4, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    record = rs.getString("POC_DETAIL");
                }
            }
        }
        return record;
    }

    public Long queryPocCount(String status) throws SQLException {
        Long count = null;
        String sql = "SELECT COUNT(POC_ID) AS POC_COUNT " +
                "FROM T_POC u " +
                "WHERE u.DELETED = 0 AND u.STATUS = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    count = rs.getLong("POC_COUNT");
                }
            }
        }
        return count;
    }


}