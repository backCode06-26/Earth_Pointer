package com.earth_pointer.repository;

import com.earth_pointer.domain.User;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl {


    private final DataSource dataSource;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findByEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = dataSource.getConnection();

            String sql = "select * from users where email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("email"),
                        rs.getBoolean("is_verified"),
                        rs.getTimestamp("registration_date")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
           DatabaseUtils.close(conn, ps, rs);
        }

        return user;
    }

    public void join(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            // SQL 쿼리에서 컬럼을 명시적으로 지정
            String sql = "insert into users (user_id, username, password_hash, email, is_verified, registration_date) " +
                         "values (userSeq.nextval, ?, ?, ?, 0, SYSTIMESTAMP)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void changePassword(String email, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "update users set password = ? where email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
}