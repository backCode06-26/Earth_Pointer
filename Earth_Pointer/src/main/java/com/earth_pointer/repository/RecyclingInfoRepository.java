package com.earth_pointer.repository;

import com.earth_pointer.domain.recyclingInfos.RecyclingInfo;
import com.earth_pointer.utils.DatabaseUtils;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecyclingInfoRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

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

    // 정보 하나 가져오기
    
    // 정보 10개 가져오기
    
    // 정보 모두 가져오기
}
