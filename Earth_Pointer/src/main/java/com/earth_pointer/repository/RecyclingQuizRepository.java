package com.earth_pointer.repository;

import com.earth_pointer.domain.RecyclingQuiz;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class RecyclingQuizRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public RecyclingQuizRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 문제 제공 (1개)
    public RecyclingQuiz getRecyclingQuiz(int quizId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RecyclingQuiz quiz = null;

        try {
            conn = dataSource.getConnection();

            String sql = "select * from recycling_quiz where quiz_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, quizId);
            rs = ps.executeQuery();

            if (rs.next()) {
                quiz = new RecyclingQuiz(
                        rs.getInt("quiz_id"),
                        rs.getString("question"),
                        rs.getString("correct_answer"),
                        rs.getString("explanation"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return quiz;
    }

    // 문제 제공 (10개)
    public ArrayList<RecyclingQuiz> getAllRecyclingQuizWithPagination(int quizId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RecyclingQuiz> quizs = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select * from recycling_quiz where quiz_id between ? and ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, quizId);
            ps.setInt(2, quizId+9);

            rs = ps.executeQuery();

            while (rs.next()) {
                RecyclingQuiz quiz = new RecyclingQuiz(
                        rs.getInt("quiz_id"),
                        rs.getString("question"),
                        rs.getString("correct_answer"),
                        rs.getString("explanation"),
                        rs.getTimestamp("created_at")
                );
                quizs.add(quiz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return quizs;
    }

    // 문제 결과 저장
    public void saveAnswerResult(String email, int quizId, boolean isRight) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into user_answer(answer_id, user_id, post_id, is_right, answer_date)"
                    + "anwser_id.nextVal, ?, ?, ?, SYSTIMESTAMP";

            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseUtils.getUserIdByEmail(email));
            ps.setInt(2, quizId);
            ps.setBoolean(2, isRight);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

}
