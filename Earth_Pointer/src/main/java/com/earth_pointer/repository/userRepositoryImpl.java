package com.earth_pointer.repository;

import com.earth_pointer.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Repository
public class userRepositoryImpl {


    private final DataSource dataSource;

    @Autowired
    public userRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void findByEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
    }

    public void join(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            // SQL 쿼리에서 컬럼을 명시적으로 지정
            String sql = "INSERT INTO users (user_id, username, password_hash, email, is_verified, registration_date) " +
                    "VALUES (userSeq.nextval, ?, ?, ?, 0, SYSTIMESTAMP)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error user", e);
        } finally {
            close(conn, ps, rs);
        }
    }

    public void changePassword(String email) {

    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
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
}
