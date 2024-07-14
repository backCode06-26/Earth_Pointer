package com.earth_pointer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DatabaseUtils {

    private final DataSource dataSource;

    @Autowired
    public DatabaseUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getUserIdByEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            String sql = "SELECT user_id FROM users WHERE email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                throw new SQLException("사용자를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            close(conn, ps, rs);
        }
    }

    public int getFurnitureIdByName(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "select furniture_id from furnitures where name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("furniture_id");
            } else {
                throw new SQLException("가구를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            close(conn, ps, rs);
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    public void close(Connection conn) {
        close(conn, null, null);
    }
}
