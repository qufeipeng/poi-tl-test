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

    public List<PocRecord> queryPocRecords(String status) throws SQLException {
        List<PocRecord> records = new ArrayList<>();
        String sql = "SELECT a.LABEL AS STATUS,CUSTOMER,PROJECT,b.LABEL AS RISK,DONE,SALES,c.NICKNAME AS SA,d.NICKNAME AS POC,PROGRESS " +
                "FROM T_POC u " +
                "LEFT JOIN T_DICT a ON u.STATUS = a.VALUE AND a.DICT_TYPE = 'status' " +
                "LEFT JOIN T_DICT b ON u.RISK = b.VALUE AND b.DICT_TYPE = 'risk' " +
                "LEFT JOIN SYS_USER c ON u.SA = c.USER_ID " +
                "LEFT JOIN SYS_USER d ON u.POC = d.USER_ID " +
                "WHERE u.DELETED = 0 AND u.STATUS = ? " +
                "ORDER BY u.CREATE_TIME DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PocRecord record = new PocRecord(
                            rs.getString("STATUS"),
                            rs.getString("CUSTOMER"),
                            rs.getString("PROJECT"),
                            rs.getString("RISK"),
                            rs.getString("DONE"),
                            rs.getString("SALES"),
                            rs.getString("SA"),
                            rs.getString("POC"),
                            rs.getInt("PROGRESS")
                    );
                    records.add(record);
                }
            }
        }
        return records;
    }
}