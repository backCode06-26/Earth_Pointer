package com.earth_pointer.repository;

import com.earth_pointer.domain.Post;
import com.earth_pointer.domain.RecyclingTips;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class RecyclingTipsRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    public RecyclingTipsRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 팁 작성
    public void addRecyclingTip(RecyclingTips tip) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into recycling_tips(tip_id, title, description, tip_type, created_at)"
                        + "values(tipSeq.nextVal, ?, ?, ?, SYSDATE)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, tip.getTitle());
            ps.setString(2, tip.getDescription());
            ps.setString(3, tip.getTipType());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    // 팁 업데이트
    public void updateRecyclingTip(int tipId, RecyclingTips tip) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "update recycling_tips set title = ?, description = ?, tip_type = ?" +
                     "where tip_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, tip.getTitle());
            ps.setString(2, tip.getDescription());
            ps.setString(3, tip.getTipType());
            ps.setInt(4, tipId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
    
    // 팁 삭제
    public void deleteRecyclingTip(int tipId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "delete from recycling_tips where tip_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, tipId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
    
    // 팁 10개씩 가져오기
    public ArrayList<RecyclingTips> getAllTipsWithPagination(int page) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RecyclingTips> tips = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            int startRow = page*10-9;
            int endRow = 10*page;

            String sql = "select * from ( " +
                    "    select ROWNUM NUM, T.* from (" +
                    "       select * from recycling_tips order by tip_id desc" +
                    "   ) T" +
                    ") where NUM between ? and ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, startRow);
            ps.setInt(2, endRow);

            rs = ps.executeQuery();

            while(rs.next()) {
                RecyclingTips tip = new RecyclingTips(
                        rs.getInt("tip_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("tip_type"),
                        rs.getDate("created_at")
                );
                tips.add(tip);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return tips;
    }
    
    // 팁 1개 가져오기
    public RecyclingTips getRecyclingTip(int tipId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "select * from recycling_tips where tip_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, tipId);

            rs = ps.executeQuery();

            if (rs.next()) {
                // 게시글 객체 생성
                RecyclingTips tip =  new RecyclingTips(
                        rs.getInt("tip_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("tip_type"),
                        rs.getDate("created_at")
                );
                return tip;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
    
    // 팁 종류별로 가져오기
    public ArrayList<RecyclingTips> getAllRecyclingTip(String tipType) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RecyclingTips> tips = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select * from recycling_tips where tip_type = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tipType);

            rs = ps.executeQuery();

            while (rs.next()) {
                RecyclingTips tip = new RecyclingTips(
                        rs.getInt("tip_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("tip_type"),
                        rs.getDate("created_at")
                );
                tips.add(tip);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return tips;
    }
}
