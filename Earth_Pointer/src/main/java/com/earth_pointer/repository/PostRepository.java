package com.earth_pointer.repository;

import com.earth_pointer.domain.Post;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class PostRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public PostRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 게시글 작성하기
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

    // 게시글 수정하기
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

    // 게시글 삭제하기
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

    // 사용자 게시글 가져오기
    public ArrayList<Post> getAllPosts(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Post> posts = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select * from posts where user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, databaseUtils.getUserIdByEmail(email));

            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                );
                posts.add(post);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return posts;
    }

    // 게시글 10개씩 가져오기
    public ArrayList<Post> getAllPostsWithPagination(int page) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Post> posts = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            int startRow = page*10-9;
            int endRow = 10*page;

            String sql = "select * from ( " +
                    "    select ROWNUM NUM, P.* from (" +
                    "       select * from posts order by post_id desc" +
                    "   ) P" +
                    ") where NUM between ? and ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, startRow);
            ps.setInt(2, endRow);

            rs = ps.executeQuery();

            while(rs.next()) {
                Post post = new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                );
                posts.add(post);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return posts;
    }
}
