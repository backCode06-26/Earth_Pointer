package com.earth_pointer.repository;

import com.earth_pointer.domain.recyclingInfos.RecyclingInfo;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class RecyclingInfoRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public RecyclingInfoRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 재활용 정보 등록
    public void addRecyclingInfo(int userId, RecyclingInfo info) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into recycling_info(info_id, title, content, category, author_id, created_at) " +
                    "values(recyclingInfoSeq.nextVal, ?, ?, ?, ?, SYSDATE)";

            ps = conn.prepareStatement(sql);

            ps.setString(1, info.getTitle());
            ps.setString(2, info.getContent());
            ps.setString(3, info.getCategory());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            databaseUtils.close(conn, ps);
        }
    }

    // 정보 수정
    public void updateInfo(int infoId, RecyclingInfo info) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

            String sql = "update recycling_info set title = ?, content = ?, category = ? " +
                    "where info_id = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, info.getTitle());
            ps.setString(2, info.getContent());
            ps.setString(3, info.getCategory());
            ps.setInt(4, infoId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            databaseUtils.close(conn, ps);
        }
    }

    // 정보 삭제
    public void deleteInfo(int infoId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

            String sql = "delete from recycling_info where info_id = ?";

            ps = conn.prepareStatement(sql);

            ps.setInt(1, infoId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            databaseUtils.close(conn, ps);
        }
    }

    // 정보 10개씩 가져오기
    public ArrayList<RecyclingInfo> getRecyclingInfoWithPagination(int page) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RecyclingInfo> infos = new ArrayList<>();

        int startRow = page*10-9;
        int endRow = page*10;

        try {
            conn = dataSource.getConnection();

            String sql = "select * from (" +
                    "select ROWNUM, R.* from (" +
                            "select * from recycling_info order by info_id desc" +
                        ") R" +
                    ") where ROWNUM between ? and ?";

            ps = conn.prepareStatement(sql);

            ps.setInt(1, startRow);
            ps.setInt(2, endRow);
            rs = ps.executeQuery();

            while (rs.next()) {
                RecyclingInfo info = new RecyclingInfo(
                        rs.getInt("info_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getInt("author_id"),
                        rs.getTimestamp("created_at")
                );
                infos.add(info);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return infos;
    }
}
