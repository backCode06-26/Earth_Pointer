package com.earth_pointer.repository;

import com.earth_pointer.domain.UserPoint;
import com.earth_pointer.enums.ActivityType;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class PointRepositoryImpl {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public PointRepositoryImpl(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 포인트 추가
    public void addPoint(int point, String email, ActivityType type) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            // SQL 쿼리 작성
            String sql = "insert into user_points (point_id, user_id, points_earned, activity_type, activity_date) " +
                    "values (pointSeq.nextVal, ?, ?, ?, SYSTIMESTAMP)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, databaseUtils.getUserIdByEmail(email));
            ps.setInt(2, point);
            ps.setString(3, type.name());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    // 포인트 내역 출력
    public ArrayList<UserPoint> printPoint(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<UserPoint> points = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select u.username, up.points_earned, up.activity_type, up.activity_date " +
                         "from user_points up, users u  " +
                         "where up.user_id = u.user_id " +
                         "and up.user_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, databaseUtils.getUserIdByEmail(email));

            rs = ps.executeQuery();

            while (rs.next()) {
                UserPoint userPoint = new UserPoint(
                        rs.getString("username"),
                        rs.getInt("points_earned"),
                        rs.getString("activity_type"),
                        rs.getTimestamp("activity_date")
                );
                points.add(userPoint);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return points;
    }



}
