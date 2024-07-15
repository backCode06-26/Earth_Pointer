package com.earth_pointer.repository;

import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class LikeRepository {
    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public LikeRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
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
            ps.setInt(1, databaseUtils.getUserIdByEmail(email));
            ps.setInt(2, postId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    // 좋아요 수 조회
    public int getLikeCountByPostId(int postId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn  = dataSource.getConnection();

            String sql = "select count(*) as post_per_likes from likes where post_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("post_per_likes");
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void deleteLike(String email, int postId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "delete from likes where post_id=? and user_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setInt(2, databaseUtils.getUserIdByEmail(email));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
}
