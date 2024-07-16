package com.earth_pointer.repository;

import com.earth_pointer.domain.Comment;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CommentRepositoryImpl {
    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public CommentRepositoryImpl(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    public void addComment(String email, int postId,  Comment comment) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into comments (comment_id, post_id, user_id, content, created_at) " +
                         "values (commentSeq.nextVal, ?, ?, ?, SYSTIMESTAMP)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            ps.setString(2, databaseUtils.getUserIdByEmail(email));
            ps.setString(3, comment.getComment());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void updateComment(int commentId, String comment) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "update comments set content=? where comment_id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, comment);
            ps.setInt(2, commentId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void deleteComment(int commentId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql  = "delete from comments where comment_id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commentId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
}
