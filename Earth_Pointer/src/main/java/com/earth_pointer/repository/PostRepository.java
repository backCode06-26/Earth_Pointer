package com.earth_pointer.repository;

import com.earth_pointer.domain.Post;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class PostRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public PostRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    public void addPost(String email, Post post) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into posts (post_id, user_id, title, content, created_at) " +
                         "values(postSeq.nextVal, ?, ?, ?, SYSTIMESTAMP)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, databaseUtils.getUserIdByEmail(email));
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContent());

            ps.executeUpdate();


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void updatePost(int postId, String email, Post post) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "update posts set title = ?, content = ? where post_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setInt(3, postId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    public void deletePost(int postId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "delete from posts where post_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }
}
