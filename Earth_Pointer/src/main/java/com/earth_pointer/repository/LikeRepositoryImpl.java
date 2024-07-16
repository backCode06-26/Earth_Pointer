package com.earth_pointer.repository;

import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class LikeRepositoryImpl {
    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public LikeRepositoryImpl(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    public void addLike(String email, int postId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into likes(like_id, user_id, post_id, createdAt) " +
                         "values(likeSeq.nextVal, ?, ?, SYSTIMESTAMP)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseUtils.getUserIdByEmail(email));
            ps.setInt(2, postId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

//    public int getLikeCountByPostId(int postId) {
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            conn  = dataSource.getConnection();
//        } catch (Exception e) {
//
//        }
//    }

    public void deleteLike(String email, int postId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "delete from likes where post_id=? and user_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setString(2, databaseUtils.getUserIdByEmail(email));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
}
