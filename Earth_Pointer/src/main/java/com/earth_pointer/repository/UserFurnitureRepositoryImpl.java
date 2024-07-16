package com.earth_pointer.repository;

import com.earth_pointer.domain.UserFurniture;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class UserFurnitureRepositoryImpl {

    private final DataSource dataSource;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public UserFurnitureRepositoryImpl(DataSource dataSource, DatabaseUtils databaseUtils) {
        this.dataSource = dataSource;
        this.databaseUtils = databaseUtils;
    }

    // 가구 구매
    public void buyFurniture(String email, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into user_furnitures (user_furniture_id, user_id, furniture_id) " +
                         "values (userFurnitureId.nextVal,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseUtils.getUserIdByEmail(email));
            ps.setInt(2, databaseUtils.getFurnitureIdByName(name));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }
    }

    // 구매 가구 조회
    public ArrayList<UserFurniture> printUserFurniture(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<UserFurniture> userFurnitures = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select u.username, f.name, f.description, f.price " +
                         "from furnitures f, user_furniture uf, users u " +
                         "where f.furniture_id = uf.furniture_id " +
                         "and uf.user_id = u.user_id " +
                         "and u.user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseUtils.getUserIdByEmail(email));

            rs = ps.executeQuery();

            while(rs.next()) {
                UserFurniture userFurniture = new UserFurniture(
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                );
                userFurnitures.add(userFurniture);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userFurnitures;
    }
}
