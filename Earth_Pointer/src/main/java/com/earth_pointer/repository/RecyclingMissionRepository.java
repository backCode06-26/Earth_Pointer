package com.earth_pointer.repository;

import com.earth_pointer.domain.RecyclingMissions;
import com.earth_pointer.domain.RecyclingQuiz;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
// 수정 필요
public class RecyclingMissionRepository {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public RecyclingMissionRepository(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 미션 추가
    public void addMission(RecyclingMissions mission) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into recycling_missions(mission_id, title, description, mission_type, start_date, end_date)"
                    + "missionSeq.nextVal, ?, ?, ?, ?, SYSDATE";

            ps = conn.prepareStatement(sql);
            ps.setString(1, mission.getTitle());
            ps.setString(2, mission.getDescription());
            ps.setString(3, mission.getMissionType());
            ps.setDate(4, mission.getStartDate());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    // 미션 가져오기 (1개)
    public RecyclingMissions getRecyclingMission(int missionId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RecyclingMissions mission = null;

        try {
            conn = dataSource.getConnection();

            String sql = "select * from recycling_missions where quiz_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, missionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                mission = new RecyclingMissions(
                        rs.getInt("mission_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mission_type"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return mission;
    }

    // 미션 가져오기 (10개)
    public ArrayList<RecyclingMissions> getAllRecyclingMissionWithPagination(int page) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RecyclingMissions> missions = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            int startRow = page*10-9;
            int endRow = 10*page;

            String sql = "select * from ( " +
                    "    select ROWNUM NUM, M.* from (" +
                    "       select * from recycling_missions order by mission_id desc" +
                    "   ) M" +
                    ") where NUM between ? and ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, startRow);
            ps.setInt(2, endRow);

            rs = ps.executeQuery();

            while (rs.next()) {
                RecyclingMissions mission = new RecyclingMissions(
                        rs.getInt("mission_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("mission_type"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                );
                missions.add(mission);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
        return missions;
    }
}
